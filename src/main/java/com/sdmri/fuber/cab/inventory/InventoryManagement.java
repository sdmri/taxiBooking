package com.sdmri.fuber.cab.inventory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdmri.fuber.cab.services.ErrorMessages;
import com.sdmri.fuber.cab.utils.BookingUtils;
import com.sdmri.fuber.exception.NoCabsAvailableException;
import com.sdmri.fuber.models.Cab;
import com.sdmri.fuber.models.CabLocation;
import com.sdmri.fuber.models.Coordinate;

/**
 * Takes care of the cab inventory. Maintaining a pool
 * of available cabs. Assigning from the pool and returning when
 * the cab is available again
 * 
 * @author shiven.dimri
 *
 */
@Service
public class InventoryManagement {
	
	@Autowired
	private BookingUtils utils;
	
	private final Map<String,CabLocation> availableCabs = new HashMap<>();
	
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	
	private final Lock readLock = readWriteLock.readLock();
	
	private final Lock writeLock = readWriteLock.writeLock();
	
	private final int MAX_BOOKING_ATTEMPTS = 10;
	
	/**
	 * Books a cab reliably. Searches for the nearest cab and then tries to book it.
	 * Tries a bounded number of times if the booking was not successful and cabs are available.
	 * 
	 * @param coordinate
	 * @param pinkRequested
	 * @return
	 * @throws NoCabsAvailableException
	 */
	public Cab findCab(Coordinate coordinate, boolean pinkRequested)
		throws NoCabsAvailableException{
		Cab cab = null;
		boolean bookedCab = false;
		int attempts = 1;
		while(!bookedCab && attempts <= MAX_BOOKING_ATTEMPTS){
			cab = findNearestCab(coordinate, pinkRequested);
			// Need to book this cab now
			bookedCab = bookCab(cab);
			attempts++;
		}
		if(cab == null){
			throw new NoCabsAvailableException(ErrorMessages.BOOKING_TIMED_OUT);
		}
		return cab;
	}
	
	/**
	 * Finds the nearest cab available at a particular time. Does not assign it nor removes it from the pool.
	 * 
	 * @param coordinate
	 * @param pinkRequested
	 * @return
	 * @throws NoCabsAvailableException
	 */
	private Cab findNearestCab(Coordinate coordinate, boolean pinkRequested) throws NoCabsAvailableException{
		Cab cab = null;
		Set<CabLocation> tempCabSet = null;
		// Fetch current set of available cabs
		readLock.lock();
		try{
			tempCabSet = new HashSet<>(availableCabs.values());
		}finally{
			readLock.unlock();
		}
		if(tempCabSet.size() == 0){
			throw new NoCabsAvailableException(ErrorMessages.ALL_CABS_BUSY);
		}
		Double minDistance = null;
		// Rudimentary O(n) approach where n is the number of available cabs
		for(CabLocation cl : tempCabSet){
			Cab currCab = cl.getCab();
			double cDistance = utils.calculateDistance(coordinate, cl.getCoordinate());
			if((minDistance == null || cDistance<minDistance) && (currCab.isColorApplicable(pinkRequested))){
				// Assign this cab if it is nearest so far and is as per user's color preference
				minDistance = cDistance;
				cab = cl.getCab();
			}
		}
		if(cab == null){
			throw new NoCabsAvailableException(ErrorMessages.ALL_CABS_BUSY);
		}
		return cab;
	}
	
	/**
	 * Removes the cab, if available, from the pool
	 * 
	 * @param cab
	 * @return
	 */
	private boolean bookCab(Cab cab){
		// Take a write lock so no other thread can read this cab in the map
		writeLock.lock();
		try{
			if(!availableCabs.containsKey(cab.getId())){
				return false;
			}else{
				availableCabs.remove(cab.getId());
				return true;
			}
		}finally{
			writeLock.unlock();
		}
	}
	
	/**
	 * Returns a cab, previously assigned, to the pool at the end of the trip
	 * 
	 * @param cab
	 * @param coordinate
	 */
	public void addCabToAvailablePool(Cab cab, Coordinate coordinate){
		// Take a write lock so no other thread can read this cab in the map
		writeLock.lock();
		try{
			availableCabs.put(cab.getId(), new CabLocation(cab, coordinate));
		}finally{
			writeLock.unlock();
		}
	}

	/**
	 * Clears all inventory of available cabs
	 */
	public void removeAllAvailableCabs() {
		writeLock.lock();
		try{
			availableCabs.clear();
		}finally{
			writeLock.unlock();
		}
	}
}
