package com.sdmri.fuber.cab.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.sdmri.fuber.cab.booking.BookingManagement;
import com.sdmri.fuber.cab.inventory.InventoryManagement;
import com.sdmri.fuber.cab.utils.BookingUtils;
import com.sdmri.fuber.exception.InvalidInputToServiceException;
import com.sdmri.fuber.exception.NoCabsAvailableException;
import com.sdmri.fuber.models.BillDetails;
import com.sdmri.fuber.models.BookingDetails;
import com.sdmri.fuber.models.Cab;
import com.sdmri.fuber.models.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class TripBookingTests {
	@InjectMocks
	private CabBookingService cabBookingService = new CabBookingService();
	
	@Spy
	private InventoryManagement invMgmt = new InventoryManagement();
	
	@Spy
	private BookingManagement bookMgmt = new BookingManagement();
	
	@Spy
	private BookingUtils utils = new BookingUtils();
	
	private static final double PER_KM_RATE = 2.0;
	
	private static final double PER_MIN_RATE = 1.0;
	
	private static final double CONVERSION_FACTOR = 1.0;
	
	private static final Cab CAB_M = new Cab("M", "ABC001", false);
	
	private static final Cab CAB_N = new Cab("N", "ABC002", false);
	
	private static final Cab CAB_O = new Cab("O", "ABC003", true);
	
	private static final Cab CAB_P = new Cab("P", "ABC004", true);
	
	@Before
	public void setup(){
		ReflectionTestUtils.setField(utils,"perKmPrice", PER_KM_RATE);
		ReflectionTestUtils.setField(utils,"perMinPrice", PER_MIN_RATE);
		ReflectionTestUtils.setField(utils,"conversionFactor", CONVERSION_FACTOR);
		ReflectionTestUtils.setField(invMgmt,"utils", utils);
		ReflectionTestUtils.setField(bookMgmt,"utils", utils);
		
		cabBookingService.addAvailableCab(CAB_M, new Coordinate(2.0, 3.0));
		cabBookingService.addAvailableCab(CAB_N, new Coordinate(10.5, 6.8));
		cabBookingService.addAvailableCab(CAB_O, new Coordinate(100.04, 92.3));
	}
	
	
	@Test
	public void testCabAssignment(){
		//Request normal cab
		BookingDetails firstBooking = null;
		try {
			firstBooking = cabBookingService.assignCab(new Coordinate(2.5, 3.5), false);
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		
		Assert.assertEquals("Incorrect cab booked", CAB_M, firstBooking.getInitialCabLocation().getCab());
		
		// Request a pink cab
		BookingDetails secondBooking = null;
		try {
			secondBooking = cabBookingService.assignCab(new Coordinate(11.1, 19.3), true);
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		Assert.assertEquals("Incorrect cab booked", CAB_O, secondBooking.getInitialCabLocation().getCab());
		
		// Another pink cab cannot be assigned
		try {
			cabBookingService.assignCab(new Coordinate(10.1, 12.3), true);
			Assert.fail();
		} catch (NoCabsAvailableException e) {
			// Expected
		}
	}
	
	@Test
	public void testCabBilling(){
		// Book a cab
		BookingDetails firstBooking = null;
		try {
			firstBooking = cabBookingService.assignCab(new Coordinate(2.5, 3.5), false);
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		
		Assert.assertEquals("Incorrect cab booked", CAB_M, firstBooking.getInitialCabLocation().getCab());
		
		
		long stopEpoch = System.currentTimeMillis();
		
		double expDurationInMins = ((double)(stopEpoch - firstBooking.getTripStartEpoch()))/(60.0*1000.0);
		double expDistKms = 13.124;
		double expectedAmount = (expDurationInMins * PER_MIN_RATE) + (expDistKms * PER_KM_RATE);
		
		// End trip
		try {
			BillDetails bill = cabBookingService.endTrip(firstBooking.getId(), stopEpoch , new Coordinate(10.1,14.2));
			Assert.assertEquals(expDistKms, bill.getDistanceInKms(), 0.1);
			Assert.assertEquals(expDurationInMins, bill.getDurationInMinutes(), 0.1);
			Assert.assertEquals(expectedAmount, bill.getAmount(), 0.1);
		} catch (InvalidInputToServiceException e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testCabsExhaustedThenAvailable(){
		//Request normal cab
		BookingDetails firstBooking = null;
		try {
			firstBooking = cabBookingService.assignCab(new Coordinate(2.5, 3.5), false);
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		
		Assert.assertEquals("Incorrect cab booked", CAB_M, firstBooking.getInitialCabLocation().getCab());
		
		// Request a pink cab
		BookingDetails secondBooking = null;
		try {
			secondBooking = cabBookingService.assignCab(new Coordinate(11.1, 19.3), true);
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		Assert.assertEquals("Incorrect cab booked", CAB_O, secondBooking.getInitialCabLocation().getCab());
		
		// Another plain cab assigned
		try {
			BookingDetails thirdBooking = cabBookingService.assignCab(new Coordinate(10.1, 12.3), false);
			Assert.assertEquals("Incorrect cab booked", CAB_N, thirdBooking.getInitialCabLocation().getCab());
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		
		// Another pink cab cannot be assigned
		try {
			cabBookingService.assignCab(new Coordinate(12.3, 89.4), true);
			Assert.fail();
		} catch (NoCabsAvailableException e) {
			// Expected
		}
		
		// Add a pink cab to inventory. Booking should be accepted now for pink cab!
		cabBookingService.addAvailableCab(CAB_P, new Coordinate(2.0,3.0));
		
		try {
			BookingDetails secondPink = cabBookingService.assignCab(new Coordinate(10.1, 12.3), true);
			Assert.assertEquals("Incorrect cab booked", CAB_P,
					secondPink.getInitialCabLocation().getCab());
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		
		// Complete a trip
		try {
			cabBookingService.endTrip(secondBooking.getId(), System.currentTimeMillis(), new Coordinate(10.1,14.2));
		} catch (InvalidInputToServiceException e) {
			Assert.fail();
		}
		
		// A cab is available again. Booking should now be assigned to this available cab only
		try {
			BookingDetails reAssignCab = cabBookingService.assignCab(new Coordinate(10.1, 12.3), false);
			Assert.assertEquals("Incorrect cab booked", secondBooking.getInitialCabLocation().getCab(),
					reAssignCab.getInitialCabLocation().getCab());
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		
		// All cabs exhausted now
		try {
			cabBookingService.assignCab(new Coordinate(12.3, 89.4), true);
			Assert.fail();
		} catch (NoCabsAvailableException e) {
			// Expected
		}
		
	}
	
	@After
	public void destroy(){
		cabBookingService.removeAllAvailableCabs();
	}

}
