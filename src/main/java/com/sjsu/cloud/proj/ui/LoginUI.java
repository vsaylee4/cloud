package com.sjsu.cloud.proj.ui;

import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjsu.cloud.proj.client.ClientUtils;
import com.sjsu.cloud.proj.model.User;
import com.sjsu.cloud.proj.request.json.LoginRequest;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = "login")
public class LoginUI extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	
	TextField username;
	PasswordField password;
	Button login;
	Button signup;
	Button loginUsingGoogle;
	User user;
	InputStream input = null;
	
	public LoginUI() {
		username = new TextField("Username");
		password = new PasswordField("Password");
		addComponent(username);
		addComponent(password);
		
		//layout for buttons
		HorizontalLayout layout = new HorizontalLayout();
		login = new Button("Login");
		login.addStyleName("friendly");
		login.addClickListener(new Button.ClickListener() {

			private static final long serialVersionUID = 8212469862002913054L;

			@Override
			public void buttonClick(ClickEvent event) {
				LoginRequest request = new LoginRequest();
				request.setUsername(username.getValue());
				request.setPassword(password.getValue());
				ObjectMapper om = new ObjectMapper();
				String payload = null;
				try {
					payload = om.writeValueAsString(request);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
				ClientUtils client = new ClientUtils();
				//String URL = "http://ec2-54-164-131-43.compute-1.amazonaws.com:8080" + "/login";
				String URL = "http://localhost:8080" + "/login";
				Response response = client.sendPostRequest(URL, payload);
				String message = response.readEntity(String.class);
				
				try {
					user = om.readValue(message, User.class);
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if (response.getStatus() == 200) {
					if (user.getIs_admin().equalsIgnoreCase("true")) {
						getUI().getNavigator().navigateTo("adminview");
					} else {
						getUI().getNavigator().navigateTo("basedashboard" + '/' + user.getEmail_id());
					}
				} else {
					Notification.show("FAILURE", "Incorrect Username/Password",
			                  Notification.Type.HUMANIZED_MESSAGE);
				}
			}			
		});
		
		signup = new Button("SignUp");
		signup.addStyleName("primary");
		signup.addClickListener(new Button.ClickListener() {		

			private static final long serialVersionUID = -2678841346183774840L;

			@Override
			public void buttonClick(ClickEvent event) {
				getUI().getNavigator().navigateTo("registration");
			}
		});
		
		//google related
		loginUsingGoogle = new Button("Google");
		loginUsingGoogle.addStyleName("primary");
		loginUsingGoogle.addClickListener(new Button.ClickListener() {		

			private static final long serialVersionUID = 2979067697057276017L;

			@Override
			public void buttonClick(ClickEvent event) {
				ClientUtils client = new ClientUtils();
				String URL = "http://ec2-54-164-131-43.compute-1.amazonaws.com:8080/"  + "gmail/login"; 
				Response response = client.sendGetRequest(URL);
				if (response.getStatus() == 200) {
					getUI().getNavigator().navigateTo("dashboard");
				} 
			}
		});

		layout.addComponent(login);
		layout.addComponent(signup);
		layout.addComponent(loginUsingGoogle);
		layout.setSpacing(true);
		
		//Form
		FormLayout formLayout = new FormLayout(username, password, layout);		
		formLayout.setMargin(true);
		
		Panel loginpanel = new Panel("File Uploader", formLayout);
		loginpanel.setWidth("600");
		loginpanel.setHeight("250");
		
		addComponent(loginpanel);
		setComponentAlignment(loginpanel, Alignment.MIDDLE_CENTER);
		setHeight("100%");
	}
}
