package com.sdmri.fuber.exception;

public class BookingNotFoundException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4231003063936295859L;
	
	public BookingNotFoundException() {
		super();
	}

	public BookingNotFoundException(Exception e){
		super(e);
	}
	
	public BookingNotFoundException(String message){
		super(message);
	}
	
}
