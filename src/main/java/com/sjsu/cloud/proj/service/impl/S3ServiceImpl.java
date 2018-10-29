package com.sjsu.cloud.proj.service.impl;

import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.sjsu.cloud.proj.exceptions.ApplicationException;
import com.sjsu.cloud.proj.model.UpdateFile;
import com.sjsu.cloud.proj.model.User;
import com.sjsu.cloud.proj.repository.UserRepository;
import com.sjsu.cloud.proj.repository.impl.NativeDBRepositoryImpl;
import com.sjsu.cloud.proj.service.AWSS3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.Upload;

@Service
public class S3ServiceImpl implements AWSS3Services {

	@Autowired
	protected TransferManager tm;
	
	@Autowired
	private AmazonS3 client;

	@Value("${jsa.s3.bucket}")
	protected String bucketName;
	
	@Autowired
	NativeDBRepositoryImpl dbRepository;
	
	@Autowired
	public UserRepository userRepository;
	
	@Override
	public void uploadFile(String key, String fileName, String fileSize, String userName) throws ApplicationException {
		
		User userInfo = dbRepository.getUserInfo(userName);
		if (userInfo == null) {
			throw new ApplicationException(403, "User not found");
		}
		
		File file = new File(fileName);
		System.out.println(file.length());
		final PutObjectRequest request = new PutObjectRequest(bucketName, key, new File(fileName));
		request.setGeneralProgressListener(new ProgressListener() {
			@Override
			public void progressChanged(ProgressEvent progressEvent) {
				long bytes = progressEvent.getBytesTransferred();
			}
		});

		Upload upload = tm.upload(request);
		
		String[] name = fileName.split("\\/");
		String fname = name[name.length-1];
		UpdateFile fileInfo = new UpdateFile();
		fileInfo.setName(fname);
		fileInfo.setCreatedTime(new Timestamp(System.currentTimeMillis()));
		fileInfo.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
		fileInfo.setDescription("upload:" + fname);
		if (fileSize == null)
			fileInfo.setFile_size(null); 
		else 
			fileInfo.setFile_size(fileSize); 
		
		fileInfo.setPath(key);
		fileInfo.setUserid(userInfo.getId());
		
		try {
			upload.waitForCompletion(); 
		} catch (AmazonServiceException e) {
				e.getMessage();
		} catch (AmazonClientException e) {
			e.getMessage();
		} catch (InterruptedException e) {
			e.getMessage();
		} 
		userRepository.save(fileInfo);
	}

	@Override
	public void downloadFile(String keyName, String downloadFilePath) {
		final GetObjectRequest request = new GetObjectRequest(bucketName, keyName);

		request.setGeneralProgressListener(new ProgressListener() {
			@Override
			public void progressChanged(ProgressEvent progressEvent) {
				long bytes = progressEvent.getBytesTransferred();
			}
		});
		
        File downloadedFile = new File(downloadFilePath + keyName + System.currentTimeMillis());
		Download download = tm.download(request, downloadedFile);
		try {
			download.waitForCompletion();
		} catch (AmazonServiceException e) {
			e.getMessage();
		} catch (AmazonClientException e) {
			e.getMessage();
		} catch (InterruptedException e) {
			e.getMessage();
		}
	}
	
	@Override
	public void deleteFile(String keyName) {
        try {
        		client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
        		dbRepository.deleteFile(keyName);
        } catch(AmazonServiceException ex) {
        		ex.getMessage();
        }
	}
	
	@Override
	public List<UpdateFile> getUserFiles(String userName) throws ApplicationException {
		
		User userInfo = dbRepository.getUserInfo(userName);
		if (userInfo == null) {
			throw new ApplicationException(403, "User not found");
		}
		
		List<UpdateFile> files = dbRepository.getUserFiles(userInfo.getId());
		return files;
	}

	@Override
	public List<UpdateFile> getAllFiles() {
		
		List<UpdateFile> files = dbRepository.getAllUserFiles();
		return files;
	}
}
