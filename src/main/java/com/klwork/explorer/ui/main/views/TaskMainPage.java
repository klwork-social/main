package com.klwork.explorer.ui.main.views;

import org.vaadin.cssinject.CSSInject;

import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.task.InboxPage;
import com.klwork.explorer.ui.task.InvolvedPage;
import com.klwork.explorer.ui.task.TasksPage;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

public class TaskMainPage extends AbstractTabViewPage{
	@Override
	public void initTabData() {
        addTab(new InboxPage(),"代办任务");
       VerticalLayout createTaskCompon = createTaskCompon("我的任务");
      
		//t.
		Tab t =addTab(new TasksPage(),"我的任务(6)");
		//t.setStyleName("tab-myTask");
		CSSInject css = new CSSInject(UI.getCurrent());

		//bg-number-active.gif
		//t.setIcon(new ThemeResource("img/bg-number-active.gif"));
		//t.setCaption("2");
        addTab(new InboxPage(),"团队任务");
        addTab(new InvolvedPage(),"参与任务");
        
        //css.setStyles(".v-tabsheet-tabitemcell-tab-myTask .v-captiontext {color: #f00; }");
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
