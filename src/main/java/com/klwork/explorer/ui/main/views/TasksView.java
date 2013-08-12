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

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.task.NewTaskPopupWindow;
import com.klwork.explorer.web.VaadinView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(value = TasksView.NAME, cached = false)
public class TasksView extends VerticalLayout implements View {
	public static final String NAME = "tasks";
	protected I18nManager i18nManager;
	
	public TasksView() {
		this.i18nManager = ViewToolManager.getI18nManager();
	}
	
	@Override
	public void attach() {
		super.attach();
		initUi();
	}
	
	protected void initAddButton() {
	    Button newCaseButton = new Button();
	    newCaseButton.setStyleName("myTabButton");
	    newCaseButton.setCaption(i18nManager.getMessage(Messages.TASK_NEW));
	    //WW_TODO Fix
	    newCaseButton.setIcon(Images.TASK_16);
	    addComponent(newCaseButton);
	    
	    newCaseButton.addClickListener(new ClickListener() {
	      public void buttonClick(ClickEvent event) {
	        NewTaskPopupWindow newTaskPopupWindow = new NewTaskPopupWindow();
	        ViewToolManager.showPopupWindow(newTaskPopupWindow);
	      }
	    });
	  }
	
    public void initUi() {
    	setSizeFull();
        addStyleName("reports");
        initAddButton();
        TaskMainPage c = new TaskMainPage();
		addComponent(c);
		setExpandRatio(c, 1.0f);
       
    }

	@Override
	public void enter(ViewChangeEvent event) {
		System.out.println("sdfdf");
	}

}
