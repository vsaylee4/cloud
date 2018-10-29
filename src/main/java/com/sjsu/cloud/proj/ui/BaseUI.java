package com.sjsu.cloud.proj.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;

@SpringUI
public class BaseUI extends UI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest request) {
	
		Navigator navigator = new Navigator(this, this);
		
		navigator.setErrorView(new LoginUI());
		
		navigator.addView("registration", new RegistrationUI());
		
		navigator.addView("registration", new RegistrationUI());
		
		navigator.addView("basedashboard", new BaseDashBoard());
		
		navigator.addView("uploadfile", new UploadFileUI());	
		
		navigator.addView("adminview", new AdminUI());	
	}
}
