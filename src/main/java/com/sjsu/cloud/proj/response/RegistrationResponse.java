package com.sjsu.cloud.proj.response;

import java.io.Serializable;

public class RegistrationResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;

	public RegistrationResponse() {
	}

	public RegistrationResponse(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "GenericFileResponse [message=" + message + "]";
	}

}
