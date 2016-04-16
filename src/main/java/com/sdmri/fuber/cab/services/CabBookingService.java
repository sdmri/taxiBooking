package com.sdmri.fuber.cab.services;

import java.util.Collection;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sdmri.fuber.cab.booking.BookingManagement;
import com.sdmri.fuber.cab.inventory.InventoryManagement;
import com.sdmri.fuber.exception.BookingNotFoundException;
import com.sdmri.fuber.exception.InvalidInputToServiceException;
import com.sdmri.fuber.exception.NoCabsAvailableException;
import com.sdmri.fuber.models.BillDetails;
import com.sdmri.fuber.models.BookingDetails;
import com.sdmri.fuber.models.Cab;
import com.sdmri.fuber.models.CabLocation;
import com.sdmri.fuber.models.Coordinate;

/**
 * Facade for all cab related services. Booking, invoicing, adding new.
 * 
 * @author shiven.dimri
 *
 */
@Service
public class CabBookingService {
	
	private static final Logger LOG = Logger.getLogger(CabBookingService.class);
	
	@Autowired
	private InventoryManagement invMgmt;
	
	@Autowired
	private BookingManagement bookingMgmt;
	
	/**
	 * Checks for availability of a specific cab, if requested, and confirms booking
	 * 
	 * @param coordinate
	 * @param pinkRequested
	 * @return
	 * @throws NoCabsAvailableException
	 */
	public BookingDetails assignCab(Coordinate coordinate, boolean pinkRequested)
		throws NoCabsAvailableException{
		Cab cab = invMgmt.findCab(coordinate, pinkRequested);
		return bookingMgmt.bookACab(cab, coordinate);
	}
	
	/**
	 * Retrieves booking details for the id and bills the trip
	 * 
	 * @param bookingId
	 * @param tripEndEpoch
	 * @param destCoordinate
	 * @return
	 * @throws InvalidInputToServiceException
	 */
	public BillDetails endTrip(String bookingId, long tripEndEpoch, Coordinate destCoordinate) throws InvalidInputToServiceException{
		BillDetails bill;
		try {
			bill = bookingMgmt.billARide(bookingId, tripEndEpoch, destCoordinate);
		} catch (BookingNotFoundException e) {
			throw new InvalidInputToServiceException(ErrorMessages.INVALID_BOOKING_ID);
		}
		Cab cab = bill.getBookingDetails().getInitialCabLocation().getCab();
		LOG.debug(String.format("Billing complete for booking %s. Adding cab %s back to available pool."
				,bookingId, cab.getId()));
		addAvailableCab(cab, destCoordinate);
		return bill;
	}
	
	/**
	 * Adds a newly available cab to inventory
	 * 
	 * @param cab
	 * @param coordinate
	 */
	public void addAvailableCab(Cab cab, Coordinate coordinate){
		invMgmt.addCabToAvailablePool(cab, coordinate);
	}
	
	/**
	 * Not exposed externally. Used for cleanup purposes
	 */
	public void removeAllAvailableCabs(){
		invMgmt.removeAllAvailableCabs();
	}
	
	
	/**
	 * Needed for auditing
	 * 
	 * @return
	 */
	public Collection<BookingDetails> fetchAllBookings(){
		return bookingMgmt.getAllBookingDetails();
	}
	
	/**
	 * Needed for auditing
	 * 
	 * @return
	 */
	public Collection<BillDetails> fetchAllBills(){
		return bookingMgmt.getAllBillDetails();
	}

	/**
	 * 
	 * @return
	 */
	public Set<CabLocation> fetchAllAvailableCabs() {
		return invMgmt.fetchAllAvailableCabs();
	}
}
