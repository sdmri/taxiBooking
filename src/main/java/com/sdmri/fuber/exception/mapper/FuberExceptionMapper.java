package com.sdmri.fuber.exception.mapper;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Extend this abstract class to access methods that create http responses
 * 
 * @author shiven.dimri
 * 
 */
public abstract class FuberExceptionMapper {

	/**
	 * Return a response in json format
	 * 
	 * @param status
	 * @param message
	 * @return
	 */
	public Response createJsonResponse(Status status, String message) {
		Map<String,String> response = new HashMap<>();
		response.put("errors", message);
		return Response.status(status)
				.entity(response)
				.type(MediaType.APPLICATION_JSON).build();
	}
}
