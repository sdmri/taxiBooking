package com.sdmri.fuber.cab.booking;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdmri.fuber.cab.utils.BookingUtils;
import com.sdmri.fuber.exception.BookingNotFoundException;
import com.sdmri.fuber.exception.NoCabsAvailableException;
import com.sdmri.fuber.models.BillDetails;
import com.sdmri.fuber.models.BookingDetails;
import com.sdmri.fuber.models.Cab;
import com.sdmri.fuber.models.CabLocation;
import com.sdmri.fuber.models.Coordinate;

/**
 * Responsible for managing bookings. Generates a booking Id
 * to keep track of bookings made. Bills completed rides
 * 
 * @author shiven.dimri
 *
 */
@Service
public class BookingManagement {
	
	private static final Logger LOG = Logger.getLogger(BookingManagement.class);
	
	@Autowired
	private BookingUtils utils;
	
	private static Long bookingId = 1111l; 
	
	private static Long billingID = 2222l; 
	
	/**
	 * A concurrent hashmap is threadsafe and provides locking on buckets in the map.
	 * This is good enough as the access pattern of these map does not warrant locking
	 * the entire map.
	 */
	private Map<String, BookingDetails> mapOfBookings = new ConcurrentHashMap<>();
	
	private Map<String, BillDetails> mapOfBillingDetails = new ConcurrentHashMap<>();
	
	/**
	 * Generates a booking in the system for a cab assigned to a user
	 * 
	 * @param cab
	 * @param coordinate
	 * @return
	 * @throws NoCabsAvailableException
	 */
	public BookingDetails bookACab(Cab cab, Coordinate coordinate)
		throws NoCabsAvailableException{
		String bookingId = generateBookingID();
		// Notify the driver and block while the cab arrives before starting trip timer
		BookingDetails bookingDetails = new BookingDetails(bookingId, System.currentTimeMillis(), 
				new CabLocation(cab, coordinate));
		mapOfBookings.put(bookingId, bookingDetails);
		LOG.debug(String.format("Successfully booked cab(%s) with booking Id : %s",cab.getId(), bookingId));
		return bookingDetails;
	}
	
	/**
	 * Computes bill for a completed trip.
	 * Initial time and coordinates are picked from the booking details
	 * 
	 * @param bookingId
	 * @param tripEndEpoch
	 * @param coordinate
	 * @return
	 * @throws BookingNotFoundException
	 */
	public BillDetails billARide(String bookingId, long tripEndEpoch, Coordinate coordinate) 
			throws BookingNotFoundException{
		BookingDetails bookingDetails = mapOfBookings.get(bookingId);
		if(bookingDetails == null){
			LOG.error("Could not find booking with Id");
			throw new BookingNotFoundException();
		}
		bookingDetails.completeTrip();
		double distanceInKms = utils.calculateDistance(bookingDetails.getInitialCabLocation().getCoordinate(), 
				coordinate);
		double durationInMins = utils.getTripDurationInMins(bookingDetails.getTripStartEpoch(), tripEndEpoch);
		double totalTripCost = utils.calculateTripCost(distanceInKms, durationInMins);
		BillDetails billDetails = new BillDetails(generateBillingId(), totalTripCost, distanceInKms, durationInMins, 
				bookingDetails);
		mapOfBillingDetails.put(billDetails.getInvoiceId(), billDetails);
		return billDetails;
	}
	
	// Generates a unique booking ID for each request
	private synchronized String generateBookingID(){
		return "FUB-" + String.format("%015d", bookingId++);
	}
	
	/**
	 * Generates a unique billing ID
	 * 
	 * @return
	 */
	private synchronized String generateBillingId(){
		return "CB-" + String.format("%015d", billingID++);
	}
	
	public Collection<BookingDetails> getAllBookingDetails(){
		return mapOfBookings.values();
	}
	
	public Collection<BillDetails> getAllBillDetails(){
		return mapOfBillingDetails.values();
	}

}
