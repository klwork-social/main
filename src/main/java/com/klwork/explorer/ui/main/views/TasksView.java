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

import java.util.Map;

import org.springframework.context.annotation.Scope;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.web.VaadinView;
import com.klwork.explorer.web.dashboard.DashboardUI;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(value = TasksView.NAME, cached = false)
public class TasksView extends VerticalLayout implements View,PushUpdateInterface {
	public static final String NAME = "tasks";
	protected I18nManager i18nManager;
	TaskMainPage mainPage = null;
	private String parameter;
	
	public TasksView() {
		this.i18nManager = ViewToolManager.getI18nManager();
	}
	
	@Override
	public void attach() {
		super.attach();
		//initUi();
	}
	

	
    public void initUi() {
    	setSizeFull();
        addStyleName("reports");
        mainPage = new TaskMainPage(parameter);
		addComponent(mainPage);
		setExpandRatio(mainPage, 1.0f);
       
    }

	@Override
	public void enter(ViewChangeEvent event) {
		View v = event.getNewView();
		parameter = event.getParameters();
		((DashboardUI)UI.getCurrent()).setCurrentView(v);
		initUi();
	}

	@Override
	public PushDataResult getPushData() {
		return mainPage.getPushData();
	}

	@Override
	public void reflashUIByPush() {
		mainPage.reflashUIByPush();
	}

	@Override
	public String getViewName() {
		return NAME;
	}

}
