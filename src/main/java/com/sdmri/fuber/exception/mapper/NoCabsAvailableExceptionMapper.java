package com.sdmri.fuber.exception.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.sdmri.fuber.exception.NoCabsAvailableException;

/**
 * @author shiven.dimri
 * 
 */
@Provider
public class NoCabsAvailableExceptionMapper extends FuberExceptionMapper implements
		ExceptionMapper<NoCabsAvailableException> {

	@Override
	public Response toResponse(NoCabsAvailableException e) {
		return createJsonResponse(Status.SERVICE_UNAVAILABLE, e.getMessage());
	}

}
