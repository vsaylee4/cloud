package com.sjsu.cloud.proj.service;

import javax.xml.bind.ValidationException;

import com.sjsu.cloud.proj.request.json.RegisterRequest;
import com.sjsu.cloud.proj.response.RegistrationResponse;

public interface RegisterService {
	
	RegistrationResponse signUp(RegisterRequest request) throws ValidationException;
}
