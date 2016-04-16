package com.sdmri.fuber.cab.services;

import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sdmri.fuber.cab.booking.BookingManagement;
import com.sdmri.fuber.cab.inventory.InventoryManagement;
import com.sdmri.fuber.exception.NoCabsAvailableException;
import com.sdmri.fuber.models.BookingDetails;
import com.sdmri.fuber.models.Cab;
import com.sdmri.fuber.models.CabLocation;
import com.sdmri.fuber.models.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class CabBookingServiceTests {
	@InjectMocks
	CabBookingService cabBookingService = new CabBookingService();
	
	@Mock
	InventoryManagement invMgmt = new InventoryManagement();
	
	@Mock
	BookingManagement bookMgmt = new BookingManagement();
	
	private static final Coordinate COORDINATE = new Coordinate(23.5, 3.26);
	
	private static final String BOOKING_ID = "b001";
	
	@Test
	public void testCabAssignment(){
		
		Cab cab = new Cab("cab001", "XVZ099", false);
		BookingDetails bDetails = new BookingDetails(BOOKING_ID, System.currentTimeMillis(), 
				new CabLocation(cab, COORDINATE));
		try {
			when(invMgmt.findCab(COORDINATE, false)).thenReturn(cab);
			when(bookMgmt.bookACab(cab, COORDINATE)).thenReturn(bDetails);
		} catch (NoCabsAvailableException e1) {
			Assert.fail();
		}
		
		try {
			BookingDetails booking = cabBookingService.assignCab(COORDINATE, false);
			Assert.assertEquals("Incorrect booking Id returned.",BOOKING_ID, booking.getId());
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
	}

}
