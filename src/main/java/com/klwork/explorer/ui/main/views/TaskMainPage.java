package com.klwork.explorer.ui.main.views;

import java.util.List;
import java.util.Random;

import com.klwork.business.domain.model.UserDataStatistic;
import com.klwork.business.domain.service.TeamService;
import com.klwork.business.domain.service.UserDataStatisticService;
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

public class TaskMainPage extends AbstractTabViewPage {
	private boolean defaultSign = false;
	
	private Tab todoTab;
	private Tab myTab;
	private Tab teamTab;
	private Tab involvedTab;
	private UserDataStatistic cUserDataStatistic;
	
	private UserDataStatisticService userDataStatisticService;
	
	public TaskMainPage() {
		super();
		userDataStatisticService = ViewToolManager.getBean("userDataStatisticService");
	}
	
	@Override
	public void initTabData() {
		
		String userId = LoginHandler.getLoggedInUser().getId();
		 cUserDataStatistic = userDataStatisticService.queryUserTaskStatistic(userId);
		
		//代办
		InboxPage inboxPage = new InboxPage();
		todoTab = addTab(inboxPage, "inBoxTask", queryTodoTabCaption());
		setDefaultTab(cUserDataStatistic.getTodoTaskTotal(), inboxPage);

		//String myTaskTitle = "我的任务";
		TasksPage taskPage = new TasksPage();
		LoggedInUser user = LoginHandler.getLoggedInUser();
		myTab = addTab(taskPage, "myTask",
				queryMyTabCaption());
		setDefaultTab(cUserDataStatistic.getMyTaskTotal(), taskPage);

		//String teamTitle = "团队任务";
		TeamService teamService = ViewToolManager.getBean("teamService");
		List<String> teams = teamService.queryUserInTeamIds(user.getId());
		TeamTaskPage teamPage = new TeamTaskPage(teams);
		teamTab = addTab(teamPage, "teamTask", queryTeamTabCaption());
		setDefaultTab(cUserDataStatistic.getTeamTaskTotal(), teamPage);
		
		//
		//final String involvedTitle = "参与任务";
		InvolvedPage involvedPage = new InvolvedPage();
		 involvedTab = addTab(involvedPage, "InvolvedTask",
				queryInvolvedTabCaption());
		setDefaultTab(cUserDataStatistic.getInvolvedTaskTotal(), involvedPage);
		
		// 新增任务
		initAddButton();

		// t.setStyleName("tab-myTask");
		// CSSInject css = new CSSInject(UI.getCurrent());

		// bg-number-active.gif
		// t.setIcon(new ThemeResource("img/bg-number-active.gif"));
		// t.setCaption("2");
		// css.setStyles(".v-tabsheet-tabitemcell-tab-myTask .v-captiontext {color: #f00; }");
	}

	public String queryInvolvedTabCaption() {
		String involvedTitle = "参与任务";
		return currentTableTitle(cUserDataStatistic.getInvolvedTaskTotal(), involvedTitle);
	}

	public String queryTeamTabCaption() {
		String teamTitle = "团队任务";
		return currentTableTitle(cUserDataStatistic.getTeamTaskTotal(), teamTitle);
	}

	public String queryMyTabCaption() {
		String myTaskTitle = "我的任务";
		return currentTableTitle(cUserDataStatistic.getMyTaskTotal(), myTaskTitle);
	}

	public String queryTodoTabCaption() {
		String tabTitle = "待办任务";
		return currentTableTitle(cUserDataStatistic.getTodoTaskTotal(), tabTitle);
	}

	private void setDefaultTab(long inboxCount, Component inboxPage) {
		if (inboxCount > 0 && !defaultSign) {
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
	
	//更新
	public void reflashUIByPush() {
		todoTab.setCaption(queryTodoTabCaption());
		myTab.setCaption(queryMyTabCaption());
		teamTab.setCaption(queryTeamTabCaption());
		involvedTab.setCaption(queryInvolvedTabCaption());
	}

	/**
	 * 更新状态
	 * @return
	 */
	public PushDataResult getPushData() {
		String userId = LoginHandler.getLoggedInUser().getId();
		UserDataStatistic newStatis = userDataStatisticService.queryUserTaskStatistic(userId);
		PushDataResult r = new PushDataResult();
		r.setNeedUpdate(false);
		if(!newStatis.equals(cUserDataStatistic)){
			r.setNeedUpdate(true);
			cUserDataStatistic = newStatis;
		}
		return r;
	}

}
