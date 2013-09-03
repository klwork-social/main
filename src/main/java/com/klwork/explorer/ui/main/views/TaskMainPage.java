package com.klwork.explorer.ui.main.views;

import java.util.List;

import com.klwork.business.domain.service.TeamService;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoggedInUser;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.task.InboxPage;
import com.klwork.explorer.ui.task.InvolvedPage;
import com.klwork.explorer.ui.task.NewTaskPopupWindow;
import com.klwork.explorer.ui.task.TasksPage;
import com.klwork.explorer.ui.task.TeamTaskPage;
import com.klwork.explorer.ui.task.data.InboxListQuery;
import com.klwork.explorer.ui.task.data.InvolvedListQuery;
import com.klwork.explorer.ui.task.data.TasksListQuery;
import com.klwork.explorer.ui.task.data.TeamTaskListQuery;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;

public class TaskMainPage extends AbstractTabViewPage{
	private boolean defaultSign = false;
	@Override
	public void initTabData() {
		long inboxCount = new InboxListQuery().size();
        String tabTitle = "待办任务";
		InboxPage inboxPage = new InboxPage();
		addTab(inboxPage,"inBoxTask",currentTableTitle(inboxCount, tabTitle));
		setDefaultTab(inboxCount, inboxPage);
		
		String myTaskTitle = "我的任务";
		TasksPage taskPage = new TasksPage();
		LoggedInUser user = LoginHandler.getLoggedInUser();
	    long tasksCount = new TasksListQuery().size();
		Tab t =addTab(taskPage,"myTask",currentTableTitle(tasksCount, myTaskTitle));
		setDefaultTab(tasksCount, taskPage);
		
		TeamService teamService = ViewToolManager. getBean("teamService");
		List<String> teams = teamService.queryUserInTeamIds(user.getId());
	    long queuedCount = 0;
	    if(!teams.isEmpty()){
	    	queuedCount = new TeamTaskListQuery(teams).size();
	    }
        String teamTitle = "团队任务";
		TeamTaskPage teamPage = new TeamTaskPage(teams);
		addTab(teamPage,"teamTask",currentTableTitle(queuedCount, teamTitle));
		setDefaultTab(queuedCount, teamPage);
        //
        long involvedCount = new InvolvedListQuery().size();
        String involvedTitle = "参与任务";
		InvolvedPage involvedPage = new InvolvedPage();
		addTab(involvedPage,"InvolvedTask",currentTableTitle(involvedCount, involvedTitle));
		setDefaultTab(involvedCount, involvedPage);
		
		//新增任务
        initAddButton();
        
        
    	//t.setStyleName("tab-myTask");
		//CSSInject css = new CSSInject(UI.getCurrent());

		//bg-number-active.gif
		//t.setIcon(new ThemeResource("img/bg-number-active.gif"));
		//t.setCaption("2");
        //css.setStyles(".v-tabsheet-tabitemcell-tab-myTask .v-captiontext {color: #f00; }");
	}

	private void setDefaultTab(long inboxCount, Component inboxPage) {
		if(inboxCount > 0 && !defaultSign){
			setSelectedTab(inboxPage);
			defaultSign = true;
		}
	}

	private String currentTableTitle(long inboxCount, String tabTitle) {
		return tabTitle + "(" + inboxCount + ")";
	}
	
	protected void initAddButton() {
	    Button newCaseButton = new Button();
	    newCaseButton.setStyleName("myTabButton");
	    newCaseButton.setCaption(i18nManager.getMessage(Messages.TASK_NEW));
	    newCaseButton.setIcon(Images.TASK_16);
	    getMainLayout().addComponent(newCaseButton);
	    
	    newCaseButton.addClickListener(new ClickListener() {
	      public void buttonClick(ClickEvent event) {
	        NewTaskPopupWindow newTaskPopupWindow = new NewTaskPopupWindow();
	        ViewToolManager.showPopupWindow(newTaskPopupWindow);
	      }
	    });
	  }
	
	public VerticalLayout createTaskCompon(String title) {
		VerticalLayout center = new VerticalLayout();
        center.setSizeFull();
        center.setCaption(title);
		return center;
	}
}
