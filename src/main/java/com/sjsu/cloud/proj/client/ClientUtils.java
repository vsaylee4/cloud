package com.sjsu.cloud.proj.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class ClientUtils {

	public Response sendPostRequest(String URL,String jsonPayload) {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		return target.request().post(Entity.entity(jsonPayload, MediaType.APPLICATION_JSON));
	}
	
	public Response sendGetRequest(String URL) {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		return target.request().get();
	}
	
	public Response sendPostRequestForm(String URL, String userName) {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		Form form = new Form();
		form.param("username", userName);
		return target.request().post(Entity.form(form), Response.class);
	}
	
	public Response sendFileUploadRequest(String URL, String uploadFile, String keyName, String fileSize, String userName) {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		Form form = new Form();
		form.param("uploadfile", uploadFile);
		form.param("keyname", keyName);
		form.param("filesize", fileSize);
		form.param("username", userName);
		return target.request().post(Entity.form(form), Response.class);
	}
	
	public Response downloadFile(String URL, String keyName, String DownloadPath) {
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		Form form = new Form();
		form.param("keyname", keyName);
		form.param("downloadfile", DownloadPath);
		return target.request().post(Entity.form(form), Response.class);
	}
	
	public Response deleteFile(String URL, String keyname) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(URL);
		Form form = new Form();
		form.param("keyname", keyname);
		return target.request().post(Entity.form(form), Response.class);
	}
}
