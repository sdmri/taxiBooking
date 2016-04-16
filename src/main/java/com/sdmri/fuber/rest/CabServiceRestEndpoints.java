package com.sdmri.fuber.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sdmri.fuber.cab.services.CabBookingService;
import com.sdmri.fuber.exception.InvalidInputToServiceException;
import com.sdmri.fuber.exception.NoCabsAvailableException;
import com.sdmri.fuber.models.BillDetails;
import com.sdmri.fuber.models.BookingDetails;
import com.sdmri.fuber.models.Cab;
import com.sdmri.fuber.models.CabRequest;
import com.sdmri.fuber.models.Coordinate;

@Component
@Path("/api/")
public class CabServiceRestEndpoints {
	
	Logger LOG = Logger.getLogger(CabServiceRestEndpoints.class);
	
	@Autowired
	CabBookingService cabBookingService;

	/*
	 * Returns a bookedTaxiId which should
	 * be used to continue with booking on confirmation
	 * or cancel
	 */
	@GET
	@Path("/cab/assign/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findCab(
			@PathParam("latitude") String latitude,
			@PathParam("longitude") String longitude,
			@QueryParam("pink") boolean pinkRequested) throws InvalidInputToServiceException, NoCabsAvailableException {
		BookingDetails bookingDetails = cabBookingService.assignCab(getCoordinate(latitude,longitude)
				, pinkRequested);
		LOG.debug("Assigned a cab with id " + bookingDetails.getId());
		Map<String,String> response = new HashMap<>();
		response.put("bookingId", bookingDetails.getId());
		return Response.status(200).entity(response)
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * Returns a trip receipt with amount
	 */
	@GET
	@Path("/trip/end/{bookingId}/{latitude}/{longitude}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response endTrip(
			@PathParam("bookingId") String bookingId,
			@PathParam("latitude") String latitude,
			@PathParam("longitude") String longitude) throws InvalidInputToServiceException {
		BillDetails bill = cabBookingService.endTrip(bookingId, System.currentTimeMillis(), getCoordinate(latitude,longitude));
		LOG.debug(String.format("Completed Trip with Id %s", bookingId));
		return Response.status(200).entity(bill).type(MediaType.APPLICATION_JSON).build();
	}
	
	/**
	 * Converts string values to a wrapper co-ordinate object
	 * 
	 * @param latitude
	 * @param longitude
	 * @return
	 * @throws InvalidInputToServiceException
	 */
	private Coordinate getCoordinate(String latitude, String longitude) throws InvalidInputToServiceException{
		return new Coordinate(convertToDouble(latitude, "Latitude"),
			convertToDouble(longitude, "Longitude"));
	}
	
	/**
	 * @param val
	 * @param type
	 * @return
	 * @throws InvalidInputToServiceException
	 */
	private double convertToDouble(String val, String type) throws InvalidInputToServiceException{
		try{
			return Double.parseDouble(val);
		}catch(Exception e){
			throw new InvalidInputToServiceException(String.format("%s should be valid " +
					"floating numbers. eg: 23.65",type));
		}
	}
	
	@POST
	@Path("/cab/add/{latitude}/{longitude}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewCab(@PathParam("latitude") String latitude,
			@PathParam("longitude") String longitude, CabRequest cabRequest) throws InvalidInputToServiceException {
		cabBookingService.addAvailableCab(new Cab(cabRequest), getCoordinate(latitude,longitude));
		Map<String,String> response = new HashMap<>();
		response.put("status", "Added");
		return Response.status(200).entity(response).
				type(MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/bookings")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchAllBookings(){
		return Response.status(200).entity(cabBookingService.fetchAllBookings())
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/bookings/bills")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchAllBills(){
		return Response.status(200).entity(cabBookingService.fetchAllBills())
				.type(MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/cab/available")
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchAllAvailableCabs(){
		return Response.status(200).entity(cabBookingService.fetchAllAvailableCabs())
				.type(MediaType.APPLICATION_JSON).build();
	}

}