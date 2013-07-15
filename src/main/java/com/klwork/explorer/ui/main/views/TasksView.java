/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.klwork.explorer.ui.main.views;

import org.springframework.context.annotation.Scope;

import com.klwork.explorer.web.VaadinView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(value = TasksView.NAME, cached = false)
public class TasksView extends VerticalLayout implements View {
	public static final String NAME = "tasks";
    
	@Override
	public void attach() {
		super.attach();
		initUi();
	}
	
    public void initUi() {
    	setSizeFull();
        addStyleName("reports");
        addComponent(new TaskMainPage());
    }

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("sdfdf");
	}

}
