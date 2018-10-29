package com.sjsu.cloud.proj.ui;

import javax.annotation.PostConstruct;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView
public class ErrorView extends VerticalLayout implements View {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    @PostConstruct
    void init() {
        addComponent(new Label("This is the Error View!"));
    }
}