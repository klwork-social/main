package com.klwork.explorer.ui.main.views;

import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.task.InboxPage;
import com.klwork.explorer.ui.task.InvolvedPage;
import com.klwork.explorer.ui.task.TasksPage;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class TaskMainPage extends AbstractTabViewPage{
	@Override
	public void initTabData() {
        addTab(new InboxPage(),"代办任务");
       /* VerticalLayout createTaskCompon = createTaskCompon("我的任务");
        //createTaskCompon.setHtmlContentAllowed(true);
		Tab t = addTab(createTaskCompon);
		t.setCaption("2dd");
		t.setIcon(Images.USER_ADD);*/
		//t.
		addTab(new TasksPage(),"我的任务");
        addTab(new InboxPage(),"团队任务");
        addTab(new InvolvedPage(),"参与任务");
	}

	public VerticalLayout createTaskCompon(String title) {
		VerticalLayout center = new VerticalLayout();
        center.setSizeFull();
        center.setCaption(title);
		return center;
	}
	
	public HorizontalLayout createMyTask() {
		 HorizontalLayout editor = new HorizontalLayout();
	        editor.setSizeFull();
	        editor.addStyleName("editor");
	        editor.addStyleName("no-horizontal-drag-hints");
	        return editor;
	}
}
