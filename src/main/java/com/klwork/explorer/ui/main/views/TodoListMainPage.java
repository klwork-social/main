package com.klwork.explorer.ui.main.views;

import java.util.HashMap;
import java.util.Map;

import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.project.MyCalendarView;
import com.klwork.explorer.ui.business.project.ProjectList;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.Tab;

public class TodoListMainPage extends AbstractTabViewPage{
	
	
	@Override
	public void initTabData() {
        addTab(new MyCalendarView(),"我的日程");
		addTab(new ProjectList(this),"项目计划");
	}
	
}
