package com.sdmri.fuber.exception;

public class NoCabsAvailableException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4231003063936295859L;
	
	public NoCabsAvailableException() {
		super();
	}

	public NoCabsAvailableException(Exception e){
		super(e);
	}
	
	public NoCabsAvailableException(String message){
		super(message);
	}
	
}
