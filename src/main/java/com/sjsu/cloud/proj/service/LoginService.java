package com.sjsu.cloud.proj.service;

import com.sjsu.cloud.proj.exceptions.ApplicationException;
import com.sjsu.cloud.proj.model.User;

public interface LoginService {
	
	public User performLogin(String username, String password) throws ApplicationException;
}
