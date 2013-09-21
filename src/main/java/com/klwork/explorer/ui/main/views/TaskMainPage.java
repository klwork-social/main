package com.klwork.explorer.ui.main.views;

import java.util.List;

import com.klwork.business.domain.model.UserDataStatistic;
import com.klwork.business.domain.service.TeamService;
import com.klwork.business.domain.service.UserDataStatisticService;
import com.klwork.common.utils.StringTool;
import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoggedInUser;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.task.InboxPage;
import com.klwork.explorer.ui.task.InvolvedPage;
import com.klwork.explorer.ui.task.NewTaskPopupWindow;
import com.klwork.explorer.ui.task.TaskPage;
import com.klwork.explorer.ui.task.TasksPage;
import com.klwork.explorer.ui.task.TeamTaskPage;
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
	private String parameter;
	private UserDataStatisticService userDataStatisticService;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	public TaskMainPage(String parameter) {
		super();
		this.parameter = parameter;
		userDataStatisticService = ViewToolManager.getBean("userDataStatisticService");
	}
	
	
	public String getParameter() {
		return parameter;
	}


	public void setParameter(String parameter) {
		this.parameter = parameter;
	}


	@Override
	public void initTabData() {
		
		String userId = LoginHandler.getLoggedInUser().getId();
		 cUserDataStatistic = userDataStatisticService.queryUserTaskStatistic(userId);
		
		//代办
		InboxPage inboxPage = new InboxPage(queryTaskIdFromParam());
		todoTab = addTab(inboxPage, "todoTask", queryTodoTabCaption());
		setDefaultTab("todoTask",cUserDataStatistic.getTodoTaskTotal(), inboxPage);

		//String myTaskTitle = "我的任务";
		TasksPage taskPage = new TasksPage(queryTaskIdFromParam());
		LoggedInUser user = LoginHandler.getLoggedInUser();
		myTab = addTab(taskPage, "myTask",
				queryMyTabCaption());
		setDefaultTab("myTask",cUserDataStatistic.getMyTaskTotal(), taskPage);

		//String teamTitle = "团队任务";
		TeamService teamService = ViewToolManager.getBean("teamService");
		List<String> teams = teamService.queryUserInTeamIds(user.getId());
		TeamTaskPage teamPage = new TeamTaskPage(teams);
		teamTab = addTab(teamPage, "teamTask", queryTeamTabCaption());
		setDefaultTab("teamTask",cUserDataStatistic.getTeamTaskTotal(), teamPage);
		
		//
		//final String involvedTitle = "参与任务";
		InvolvedPage involvedPage = new InvolvedPage();
		 involvedTab = addTab(involvedPage, "involvedTask",
				queryInvolvedTabCaption());
		setDefaultTab("involvedTask",cUserDataStatistic.getInvolvedTaskTotal(), involvedPage);
		
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

	private void setDefaultTab(String sign, long inboxCount, Component inboxPage) {
		if(paramMatching(sign)){//传入了参数，和tab进行匹配，则显示这个tab
			setSelectedTab(inboxPage);
			defaultSign = true;
			return;
		}
		if (inboxCount > 0 && !defaultSign) {
			setSelectedTab(inboxPage);
			defaultSign = true;
		}
	}

	public boolean paramMatching(String sign) {
		return StringTool.judgeBlank(parameter) && (parameter.indexOf(sign) == 0);
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
		updateTodoTab();
		updateMyTab();
		updateTeamTab();
		updateInvolvedTab();
	}


	public void updateInvolvedTab() {
		involvedTab.setCaption(queryInvolvedTabCaption());
		TaskPage taskpage = (TaskPage)getTabCache().get("involvedTab");
		taskpage.refreshTableContent();
	}


	public void updateTeamTab() {
		teamTab.setCaption(queryTeamTabCaption());
		TaskPage taskpage = (TaskPage)getTabCache().get("teamTask");
		taskpage.refreshTableContent();
	}


	public void updateMyTab() {
		myTab.setCaption(queryMyTabCaption());
		TaskPage taskpage = (TaskPage)getTabCache().get("myTask");
		taskpage.refreshTableContent();
	}


	public void updateTodoTab() {
		todoTab.setCaption(queryTodoTabCaption());
		TaskPage taskpage = (TaskPage)getTabCache().get("todoTask");
		taskpage.refreshTableContent();
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
			logger.debug("PushData进行数据更新");
			r.setNeedUpdate(true);
			
		}else {
			logger.debug("PushData数据已经是最新的");
		}
		cUserDataStatistic = newStatis;
		return r;
	}
	
	/**
	 * 从参数中取任务id
	 * @return
	 */
	public String queryTaskIdFromParam() {
		String retTaskId = null;
		if(StringTool.judgeBlank(parameter) ){
			 String[] tokenArray = parameter.split("\\?");
			 if (tokenArray.length < 2) {
		            return null;
		        }else {
		        	 String userIdStr = tokenArray[1];
					if (userIdStr.startsWith("taskid=")){
		        		 retTaskId = userIdStr.substring(userIdStr.indexOf('=')+1, userIdStr.length());
		             }
		        }
		}
		return retTaskId;
	}
}
