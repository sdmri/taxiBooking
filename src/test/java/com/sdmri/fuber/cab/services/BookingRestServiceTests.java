package com.sdmri.fuber.cab.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.sdmri.fuber.exception.InvalidInputToServiceException;
import com.sdmri.fuber.exception.NoCabsAvailableException;
import com.sdmri.fuber.rest.CabServiceRestEndpoints;

@RunWith(MockitoJUnitRunner.class)
public class BookingRestServiceTests {
	
	@InjectMocks
	CabServiceRestEndpoints restService = new CabServiceRestEndpoints();
	
	@Mock
	CabBookingService cabBookingService = new CabBookingService();
	
	@Test
	public void testInvalidLatLong(){
		try {
			restService.findCab("abc", "123.5", false);
			Assert.fail();
		} catch (InvalidInputToServiceException e) {
			// Expected
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
		
		try {
			restService.findCab("123.5", "abc", false);
			Assert.fail();
		} catch (InvalidInputToServiceException e) {
			// Expected
		} catch (NoCabsAvailableException e) {
			Assert.fail();
		}
	}
}
