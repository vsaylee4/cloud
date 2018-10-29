package com.sjsu.cloud.proj.exceptions;

public class ApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int code = 500;
	
	public ApplicationException(int code, String message) {
		super(message);
		this.code = code;
	}
}