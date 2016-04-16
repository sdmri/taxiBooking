package com.sdmri.fuber.cab.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.sdmri.fuber.cab.utils.BookingUtils;
import com.sdmri.fuber.models.Coordinate;

@RunWith(MockitoJUnitRunner.class)
public class BookingUtilsTests {
	
	@InjectMocks
	private BookingUtils utils = new BookingUtils();
	
	@Before
	public void setup() {
		ReflectionTestUtils.setField(utils,"perKmPrice", 2.0);
		ReflectionTestUtils.setField(utils,"perMinPrice", 1.0);
	}
	
	@Test
	public void testDistanceCalculation(){
		Assert.assertEquals("Some problem with distance calculation", 5.0, 
				utils.calculateDistance(new Coordinate(10,7), new Coordinate(6,4)), 0.0);
	}
	
	@Test
	public void testTimeCalculation(){
		Assert.assertEquals("Some problem with duration calculation", 4.0,
				utils.getTripDurationInMins(1460720400000l, 1460720640000l), 0.0);
	}
	
	@Test
	public void testCostCalculation(){
		Assert.assertEquals("Some problem with trip cost calculation",58.9, 
				utils.calculateTripCost(20.3, 18.3),0.1);
	}
}