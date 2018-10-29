package com.sjsu.cloud.proj.repository;

import java.util.List;

import com.sjsu.cloud.proj.model.UpdateFile;
import com.sjsu.cloud.proj.model.User;

public interface NativeDBRepository {
	
	public User getUserInfo(String username, String password);
	
	public User getUserInfo(String username);
	
	public List<UpdateFile> getUserFiles(int userid);
	
	public List<UpdateFile> getAllUserFiles();
	
	public int deleteFile(String key);
}
