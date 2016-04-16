package com.sdmri.fuber.cab.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sdmri.fuber.models.Coordinate;

@Service
public class BookingUtils {
	
	@Value("${per.km.price:2.0}")
	private Double perKmPrice;
	
	@Value("${per.min.price:1.0}")
	private Double perMinPrice;
	
	/**
	 * This is used to convert distance in latitude longitude to kms
	 * We assume each coordinate to be 1 km by default
	 */
	@Value("${distance.conversion.factor:1.0}")
	private Double conversionFactor;

	/**
	 * Calculates distance between 2 points in 2d space assuming all 
	 * the points lie in the 2nd quandrant (+x,+y)
	 * 
	 * @param c1
	 * @param c2
	 *  
	 * @return
	 */
	public double calculateDistance(Coordinate c1, Coordinate c2){
		// Pythagoras theorem
		return Math.pow((Math.pow(c2.x-c1.x, 2) + Math.pow(c2.y-c1.y, 2)),.5);
	}
	
	public double getTripDistanceInKms(Coordinate c1, Coordinate c2){
		return (calculateDistance(c1, c2) * conversionFactor);
	}
	
	public double getTripDurationInMins(long startEpoch, long stopEpoch){
		return (stopEpoch - startEpoch)/(60.0 * 1000.0);
	}
	
	/**
	 * Calculates cost of trip from the distance and duration
	 * 
	 * @param distanceInKms
	 * @param durationInMins
	 * @return
	 */
	public double calculateTripCost(double distanceInKms, double durationInMins){
		return (distanceInKms * perKmPrice) 
				+ (perMinPrice * durationInMins);
	}
	
}
