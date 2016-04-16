package com.sdmri.fuber.exception.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.log4j.Logger;

/**
 * Any uncaught exceptions will be caught here and a message returned
 * to user.
 * 
 * @author shiven.dimri
 * 
 */
public class UncaughtExceptionMapper extends Throwable 
	implements ExceptionMapper<Throwable>{
	private static final Logger LOG = Logger.getLogger(UncaughtExceptionMapper.class);
    private static final long serialVersionUID = 1L;
  
    @Override
    public Response toResponse(Throwable exception)
    {
    	LOG.error("Unexpected error while serving request. Needs investigation!! "
    			,exception);
    	return Response.status(500)
			.entity("{\"errors\":\"Unexpected error. Please try again!!\"}")
			.type(MediaType.APPLICATION_JSON).build();
    }
}
