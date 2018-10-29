package com.sjsu.cloud.proj.service.impl;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sjsu.cloud.proj.model.User;
import com.sjsu.cloud.proj.repository.UserRepository;
import com.sjsu.cloud.proj.repository.impl.NativeDBRepositoryImpl;
import com.sjsu.cloud.proj.request.json.RegisterRequest;
import com.sjsu.cloud.proj.response.RegistrationResponse;
import com.sjsu.cloud.proj.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {
	
	@Autowired
	public UserRepository userRepository;

	@Autowired
	NativeDBRepositoryImpl dbRepository;
	
	@Override
	public RegistrationResponse signUp(RegisterRequest request) throws ValidationException {
		
		//check if the user already exists in the application.
		User existingUser = dbRepository.getUserInfo(request.getEmail());
		if (existingUser != null) {
			return new RegistrationResponse("User with this email id already registered.");
		}
		
		User user = new User();
		user.setFirst_name(request.getFirstname());
		user.setLast_name(request.getLastname());
		user.setEmail_id(request.getEmail());
		user.setPassword(request.getPassword());
		user.setIs_admin("false");
		userRepository.save(user);
		
		return new RegistrationResponse("Successful Registration...");
	}
}
	