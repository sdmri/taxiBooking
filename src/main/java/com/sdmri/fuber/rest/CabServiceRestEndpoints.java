package com.sdmri.fuber.rest;

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
	public Response findCab(
			@PathParam("latitude") String latitude,
			@PathParam("longitude") String longitude,
			@QueryParam("pink") boolean pinkRequested) throws InvalidInputToServiceException, NoCabsAvailableException {
		BookingDetails bookingDetails = cabBookingService.assignCab(getCoordinate(latitude,longitude)
				, pinkRequested);
		LOG.debug("Assigned a cab with id " + bookingDetails.getId());
		return Response.status(200).entity("{\"id\" : \"" +bookingDetails.getId() + "\"").type(MediaType.APPLICATION_JSON).build();
	}
	
	/*
	 * Returns a trip receipt with amount
	 */
	@GET
	@Path("/trip/end/{bookingId}/{latitude}/{longitude}")
	@Produces("application/json")
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
	public Response addNewCab(@PathParam("latitude") String latitude,
			@PathParam("longitude") String longitude, Cab cab) throws InvalidInputToServiceException {
		cabBookingService.addAvailableCab(cab, getCoordinate(latitude,longitude));
		return Response.status(200).entity("{\"status\":\"Added\"}").build();
	}

}