package com.sjsu.cloud.proj.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sjsu.cloud.proj.exceptions.ApplicationException;
import com.sjsu.cloud.proj.model.User;
import com.sjsu.cloud.proj.repository.impl.NativeDBRepositoryImpl;
import com.sjsu.cloud.proj.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	NativeDBRepositoryImpl dbRepository;
	
	@Override
	public User performLogin(String username, String password) throws ApplicationException {

		User user = dbRepository.getUserInfo(username, password);
		return user;
	}
}