package com.sjsu.cloud.proj.service;

import java.util.List;

import com.sjsu.cloud.proj.exceptions.ApplicationException;
import com.sjsu.cloud.proj.model.UpdateFile;

public interface AWSS3Services {

	public void uploadFile(String key, String fileName, String fileSize, String userName) throws ApplicationException;

	public void downloadFile(String key, String downloadPath);

	public void deleteFile(String key);

	public List<UpdateFile> getUserFiles(String userName) throws ApplicationException;
	
	public List<UpdateFile> getAllFiles();
}
