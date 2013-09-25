package com.klwork.explorer.ui.main.views;

import java.util.Iterator;
import java.util.List;

import org.mortbay.log.Log;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.model.ProjectQuery;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.business.domain.service.SocialUserAccountInfoService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.project.MyCalendarView;
import com.klwork.explorer.ui.business.project.ProjectList;
import com.klwork.explorer.ui.business.project.ProjectTreeTable;
import com.klwork.explorer.ui.business.social.WeiboShowPage;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.Tab;

public class TodoListMainPage extends AbstractTabViewPage{
	SocialUserAccountInfoService socialUserAccountInfoService;
	ProjectService projectService;
	
	public TodoListMainPage () {
		projectService = ViewToolManager.getBean("projectService");
		socialUserAccountInfoService = ViewToolManager.getBean("socialUserAccountInfoService");
	}
	
	@Override
	public void initTabData() {
        addTab(new MyCalendarView(),"我的日程");
		addTab(new ProjectList(this),"项目计划");
		//打开默认todolist
		openDefaultTodoList();
	}
	
	//进行默认打开
	public void openDefaultTodoList() {
		ProjectQuery query = new ProjectQuery();
		query.setKey("account_tab_open");
		query.setKeyValue("1");
		query.setOwnuser(LoginHandler. getLoggedInUser().getId());
		List<Project> list = projectService.findByQueryCriteria(query , null);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Project project = (Project) iterator
					.next();
			openProject(project);
		}
	}
	
	public void openProject(Project project) {
		addTabSpecial(new ProjectTreeTable(project.getId(),this), project.getName(),project.getId());
		socialUserAccountInfoService.setEntityInfo(project.getId(), DictDef.dict("todo_project_type"),"account_tab_open", "1");//打开状态
	}
	
	@Override
	public CloseHandler currentCloseHandler() {
		return new CloseHandler() {
			private static final long serialVersionUID = -1764556772862038086L;

			@Override
			public void onTabClose(TabSheet tabsheet, Component tabContent) {
				
				Tab addTab = tabsheet.getTab(tabContent);
				if(tabContent instanceof  WeiboShowPage){
					String projectId = queryTabKey(tabContent);
					socialUserAccountInfoService.setEntityInfo(projectId,DictDef.dict("todo_project_type"), "account_tab_open", "0");//关闭状态
					Log.debug("当前关闭：" + projectId);
				}
				String name = addTab.getCaption();
				if(getTabCache().get(name) != null){
					getTabCache().remove(name);
				}
				tabsheet.removeComponent(tabContent);
			}
		};
	}
}
