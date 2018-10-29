package com.sjsu.cloud.proj.request.json;

import java.io.Serializable;

public class LoginRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String username;
	public String password;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
