package com.sdmri.fuber.exception;

public class InvalidInputToServiceException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4231003063936295859L;
	
	public InvalidInputToServiceException() {
		super();
	}

	public InvalidInputToServiceException(Exception e){
		super(e);
	}
	
	public InvalidInputToServiceException(String message){
		super(message);
	}
	
}
