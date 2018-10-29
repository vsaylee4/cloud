package com.sjsu.cloud.proj.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.ws.rs.core.Response;
import com.sjsu.cloud.proj.client.ClientUtils;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Upload.FinishedEvent;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

@SpringView(name = "uploadfile")
public class UploadFileUI extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	public String email;
	Label label;
	Upload upload;
	String fileName;

	@Override
	public void enter(ViewChangeEvent event) {

		String args[] = event.getParameters().split("/");
		email = args[0];
	}

	public UploadFileUI() {	
		label = new Label("Dashboard : Welcome to File Upload, Please select a file to upload!");
		addComponent(label);
		Upload upload = new Upload("Upload File", new Upload.Receiver() {

			private static final long serialVersionUID = -1182884405175192159L;

			// exmaple with hard coded path
			@Override
			public OutputStream receiveUpload(String filename, String mimeType) {
				OutputStream out = null;
				fileName = filename;
				//*******************
				File file = new File("/Users/sdorle/Desktop/test/" + filename);
				try {
					out = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return out;
			}
		});
	
		upload.addSucceededListener(new SucceededListener() {

			private static final long serialVersionUID = -418344585124543137L;

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				Notification.show("SUCCESS",
						event.getFilename() + " is uploaded successfully",
		                  Notification.Type.HUMANIZED_MESSAGE);
				getUI().getNavigator().navigateTo("basedashboard" + '/' + email);
			}
		});

		final TextField name = new TextField();
		upload.addFinishedListener(new Upload.FinishedListener() {

			private static final long serialVersionUID = -3689821149167395175L;

			@Override
			public void uploadFinished(FinishedEvent event) {
				
				//*******************
				File file = new File("/Users/sdorle/Desktop/test/" + fileName);
				long size = file.length();
				ClientUtils client = new ClientUtils();
				String URL = "http://ec2-54-164-131-43.compute-1.amazonaws.com:8080" + "/file/upload";
				//String URL = "http://localhost:8080" + "/file/upload";
				Response response = client.sendFileUploadRequest(URL, file.getAbsolutePath(), fileName, Long.toString(size), email);
				name.setValue(event.getFilename());
			}
		});
		addComponent(name);
		addComponent(upload);
	}
}
