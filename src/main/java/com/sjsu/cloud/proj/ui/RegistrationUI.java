package com.sjsu.cloud.proj.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sjsu.cloud.proj.LoadProperties;
import com.sjsu.cloud.proj.client.ClientUtils;
import com.sjsu.cloud.proj.request.json.RegisterRequest;
import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SpringView(name = "registration")
public class RegistrationUI extends VerticalLayout implements View {

	private static final long serialVersionUID = 1L;
	
	Label title;
	Label header;
	TextField firstName;
	TextField lastName;
	TextField email;
	TextField password;
	Button submit;
	Button cancel;
	
	public RegistrationUI() {
		setSpacing(true);
		setMargin(true);
		
		title = new Label("Registration Form");
		title.addStyleName("h1");
		addComponent(title);
		
		FormLayout form = new FormLayout();
		form.setMargin(false);
		form.setWidth("700");
		form.setStyleName("light");
		addComponent(title);
		
		header = new Label("Personal Information");
		header.setStyleName("h2");
		header.setStyleName("colored");
		form.addComponent(header);
		
		firstName = new TextField("FirstName");
		form.addComponent(firstName);
		
		lastName = new TextField("LastName");
		form.addComponent(lastName);
		
		email = new TextField("Email");
		form.addComponent(email);
		
		password = new TextField("Password");
		form.addComponent(password);
		addComponent(form);
		
		HorizontalLayout layout = new HorizontalLayout();
		submit = new Button("Submit");
		submit.addStyleName("friendly");
		submit.addClickListener(new Button.ClickListener() {
			
			private static final long serialVersionUID = 3626183860460840816L;

			@Override
			public void buttonClick(ClickEvent event) {
				RegisterRequest registration = new RegisterRequest();
				registration.setFirstname(firstName.getValue());
				registration.setLastname(lastName.getValue());
				registration.setEmail(email.getValue());
				registration.setPassword(password.getValue());
				
				ObjectMapper om = new ObjectMapper();
				String payload = null;
				try {
					payload = om.writeValueAsString(registration);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ClientUtils client = new ClientUtils();
				String URL = "http://ec2-54-164-131-43.compute-1.amazonaws.com:8080/" + "signup";
				client.sendPostRequest(URL, payload);
				getUI().getNavigator().navigateTo("dashboard");
			}
		});
		
		layout.addComponent(submit);
		cancel = new Button("Cancel");
		cancel.addStyleName("friendly");
		layout.addComponent(cancel);
		addComponent(layout);
	}
}