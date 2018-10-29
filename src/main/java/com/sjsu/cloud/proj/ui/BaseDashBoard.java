package com.sjsu.cloud.proj.ui;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sjsu.cloud.proj.LoadProperties;
import com.sjsu.cloud.proj.client.ClientUtils;
import com.sjsu.cloud.proj.model.UpdateFile;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.ItemClick;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.ItemClickListener;


@SpringView(name = "basedashboard")
public class BaseDashBoard extends VerticalLayout implements View{

	private static final long serialVersionUID = -1982699644768541784L;

	public String email;
	public Grid<UpdateFile> grid = new Grid<>(UpdateFile.class);
	public VerticalLayout verti = null;
	
	@Override
	public void enter(ViewChangeEvent event) {

		String args[] = event.getParameters().split("/");
		email = args[0];
	}
	Button uploadFile;
	Button downloadFile;
	Button showAllFiles;
	
	public BaseDashBoard() {
		
		HorizontalLayout layout = new HorizontalLayout();
		uploadFile = new Button("Upload File");
		uploadFile.addStyleName("friendly");
		uploadFile.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = -2846095023225970974L;

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo("uploadfile" + '/' + email);
			}
		});
		
		showAllFiles = new Button("Show All My Files");
		showAllFiles.addStyleName("friendly");
		
		layout.addComponent(uploadFile);
		layout.addComponent(showAllFiles);
		layout.setSpacing(true);
		
		Label label = new Label("NOTE : Double click on the File Row to Download..");
		
		FormLayout formLayout = new FormLayout(layout);		
		formLayout.setMargin(true);
		formLayout.setWidth("420");
		formLayout.setHeight("100");
		
		Panel loginpanel = new Panel("Please select the action you would like to perform?", formLayout);
		loginpanel.setWidth("420");
		loginpanel.setHeight("150");
		addComponent(loginpanel);
		
		showAllFiles.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 6255165223638668220L;

			@Override
			public void buttonClick(ClickEvent event) {
				List<UpdateFile> list = callGetAllFiles(email);
				if (list != null) {
					verti = new VerticalLayout();
					verti.addComponent(grid);
					grid.setItems(list);
					
					//reorder columns
					grid.setColumns("name", "description", "createdTime", "updatedTime", "path", "file_size");
					grid.setColumnOrder("name", "description", "createdTime", "updatedTime", "path", "file_size");
					
					//change headers for the columns.
					grid.getColumn("name").setCaption("File Name");
					grid.getColumn("description").setCaption("Description");
					grid.getColumn("createdTime").setCaption("Created Time");
					grid.getColumn("updatedTime").setCaption("Updated Time");
					grid.getColumn("path").setCaption("Path");
					grid.getColumn("file_size").setCaption("File Size");

					//add remove buttons on the grid.
					grid.addComponentColumn(item -> new NativeButton("Remove", evt -> {
					    ListDataProvider<UpdateFile> dataProvider = (ListDataProvider<UpdateFile>) grid
					            .getDataProvider();
					    Response response = callDeleteFile(item.getPath());
					    String message = response.readEntity(String.class);
					    if (message.contains("SUCCESS")) {
							Notification.show("SUCCESS", item.getPath() + " is deleted successfully",
					                  Notification.Type.HUMANIZED_MESSAGE);
					    }
					    dataProvider.getItems().remove(item);
					    dataProvider.refreshAll();
					})).setCaption("Delete File");
					
					//download file
					grid.addItemClickListener(new ItemClickListener<UpdateFile>() {

						private static final long serialVersionUID = -4181929458499339046L;

						@Override
						public void itemClick(ItemClick<UpdateFile> event) {
							if (event.getMouseEventDetails().isDoubleClick()) {
								String fileName = event.getItem().getPath();
								String location = "//Users//sdorle//Documents//";
								Response response = downloadFile(fileName, location);
								String message = response.readEntity(String.class);
								if (message.contains("SUCCESS")) {
									Notification.show("SUCCESS", event.getItem().getPath() + " is downloaded successfully",
							                  Notification.Type.HUMANIZED_MESSAGE);
								}
							}
						}
					});
					
					//display
					grid.setWidth("100%");
					grid.setHeight("1000px");
					
					addComponent(label);
					addComponent(grid);
				}
			}
		});
	}
	
	public Response callDeleteFile(String key) {
		
		String URL = "http://ec2-54-164-131-43.compute-1.amazonaws.com:8080/" + "file/delete"; 
		ClientUtils client = new ClientUtils();
		Response response = client.deleteFile(URL, key);
		return response;
	}
	
	public List<UpdateFile> callGetAllFiles(String userName) {
		
		String URL = "http://ec2-54-164-131-43.compute-1.amazonaws.com:8080/" + "display/files";
		//String URL = "http://localhost:8080/" + "display/files";
		ClientUtils client = new ClientUtils();
		Response response = client.sendPostRequestForm(URL, userName);
		String massage = response.readEntity(String.class);
		ObjectMapper om = new ObjectMapper();
		List<UpdateFile> myObjects = null;
		try {
			myObjects = om.readValue(massage, new TypeReference<List<UpdateFile>>(){});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myObjects;
	}
	
	public Response downloadFile(String keyName, String downloadLoc) {

		String URL = "http://ec2-54-164-131-43.compute-1.amazonaws.com:8080/" + "file/download";
		//String URL = "http://localhost:8080/" + "file/download";
		ClientUtils client = new ClientUtils();
		Response response = client.downloadFile(URL, keyName, downloadLoc);
		return response;
	}
}