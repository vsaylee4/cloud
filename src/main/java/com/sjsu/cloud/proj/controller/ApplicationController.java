package com.sjsu.cloud.proj.controller;

import java.util.List;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sjsu.cloud.proj.exceptions.ApplicationException;
import com.sjsu.cloud.proj.model.UpdateFile;
import com.sjsu.cloud.proj.model.User;
import com.sjsu.cloud.proj.request.json.LoginRequest;
import com.sjsu.cloud.proj.request.json.RegisterRequest;
import com.sjsu.cloud.proj.response.RegistrationResponse;
import com.sjsu.cloud.proj.service.AWSS3Services;
import com.sjsu.cloud.proj.service.LoginService;
import com.sjsu.cloud.proj.service.RegisterService;

@RestController
public class ApplicationController {

	@Autowired
	LoginService loginService;

	@Autowired
	RegisterService registerService;

	@Autowired
	AWSS3Services s3Service;

	@RequestMapping(method = RequestMethod.POST, value = "/signup", produces = "application/json")
	public ResponseEntity<RegistrationResponse> signup(@RequestBody RegisterRequest request)
			throws ValidationException, ApplicationException {

		ResponseEntity<RegistrationResponse> responseEntity = null;

		try {
			RegistrationResponse response = registerService.signUp(request);
			responseEntity = new ResponseEntity<RegistrationResponse>(response, HttpStatus.OK);
		} catch (Exception ex) {
			throw new ApplicationException(500, "unable to register, please try again");
		}
		return responseEntity;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/login", produces = "application/json")
	public User login(@RequestBody LoginRequest request) throws ApplicationException {

		User user = null;
		try {
			user = loginService.performLogin(request.getUsername(), request.getPassword());
			if (user == null) {
				throw new ApplicationException(404, "Invalid Username/Password.");
			}
		} catch (Exception ex) {
			throw new ApplicationException(401, "Invalid Username/Password.");
		}
		return user;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/file/upload", produces = "application/json")
	public String uploadFile(@RequestParam("keyname") String keyName,
			@RequestParam("uploadfile") String fileName, @RequestParam("filesize") String fileSize,
			@RequestParam("username") String userName) throws ApplicationException {
		
		keyName = userName.concat("/").concat(keyName);
		
		try {
			s3Service.uploadFile(keyName, fileName, fileSize, userName.trim());
		} catch (Exception ex) {
			throw new ApplicationException(401, "Failed to upload document.");
		}
		return "SUCCESS | Uploaded Document | " + keyName;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/file/download", produces = "application/json")
	public String downloadFile(@RequestParam("keyname") String keyName,
			@RequestParam("downloadfile") String downloadFilePath) throws ApplicationException {
		try { 
			s3Service.downloadFile(keyName, downloadFilePath);
		} catch (Exception e) {
			throw new ApplicationException(401, "Failed to download document.");
		}
		return "SUCCESS | Downloaded Document | " + keyName;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/file/delete", produces = "application/json")
	public String deleteFile(@RequestParam String keyname) {
		try {
			s3Service.deleteFile(keyname);
		} catch (Exception e) {
			return "Cannot Delete File -> Keyname = " + keyname;
		}
		return "SUCCESS | Deleted Document | " + keyname;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/display/files", produces = "application/json")
	public List<UpdateFile> displayUserFiles(@RequestParam("username") String userName) throws ApplicationException {
		
		try {
			return s3Service.getUserFiles(userName);
		} catch (Exception e) {
			throw new ApplicationException(500, "System Error");
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/all/files", produces = "application/json")
	public List<UpdateFile> displayAllFiles() throws ApplicationException {
		
		try {
			return s3Service.getAllFiles();
		} catch (Exception e) {
			throw new ApplicationException(500, "System Error");
		}
	}
}