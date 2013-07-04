/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.klwork.explorer.ui.main.views;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewManager;
import com.klwork.explorer.ui.base.AbstractPage;
import com.klwork.explorer.ui.business.organization.OrganizationMainPage;
import com.klwork.explorer.ui.business.organization.OrganizationMemberMainPage;
import com.klwork.explorer.ui.business.outproject.OutProjectManagerMainPage;
import com.klwork.explorer.ui.business.outproject.PublicProjectListPage;
import com.klwork.explorer.ui.business.project.MyCalendarView;
import com.klwork.explorer.ui.business.project.ProjectMain;
import com.klwork.explorer.ui.mainlayout.MainLayout;
import com.klwork.explorer.ui.task.InboxPage;
import com.klwork.explorer.ui.task.InvolvedPage;
import com.klwork.explorer.ui.task.QueuedPage;
import com.klwork.explorer.ui.task.TaskMenuBar;
import com.klwork.explorer.ui.task.TasksPage;
import com.klwork.explorer.web.VaadinView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Panel;

/**
 * The Class MainView.
 */
@Component
@Scope("prototype")
@VaadinView(value = MainView.NAME, cached = true)
public class MainView extends Panel implements View {
	// 为空的实体第一个显示？
	public static final String NAME = "";

	private static final long serialVersionUID = 1L;

	protected I18nManager i18nManager;

	// UI
	protected MainLayout mainLayout;

	@PostConstruct
	public void PostConstruct() {
		System.out.println("***MainLayout***初始化");
		setSizeFull();
		mainLayout = new MainLayout();
		setContent(mainLayout);
		// 显示任务的总数
		showInboxPage();
		// this.
	}

	protected AbstractPage currentPage;

	// Helper

	protected void switchView(AbstractPage page, String mainMenuActive,
			String subMenuActive) {
		currentPage = page;
		// 高亮选择的主菜单
		mainLayout.setMainNavigation(mainMenuActive);
		// 在main中删去所有的元素,把当前的页面加入,最终因为addComponent，触发 init
		mainLayout.setMainContent(page);

		// 高亮子菜单
		if (subMenuActive != null && page.getToolBar() != null) {
			page.getToolBar().setActiveEntry(subMenuActive); // Must be set
																// AFTER adding
																// page to
																// window
																// (toolbar will
																// be created in
																// atach())
		}
	}

	protected void switchMainContent(com.vaadin.ui.Component page) {
		// 在main中删去所有的元素,把当前的页面加入,最终因为addComponent，触发 init
		mainLayout.clearNavigation();
		mainLayout.setMainContent(page);
	}

	public void showInboxPage() {
		switchView(new InboxPage(), ViewManager.MAIN_NAVIGATION_TASK,
				TaskMenuBar.ENTRY_INBOX);
	}
	public void showInboxPage(String taskId) {
		switchView(new InboxPage(taskId), ViewManager.MAIN_NAVIGATION_TASK,
				TaskMenuBar.ENTRY_INBOX);
		
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
		Subject subject = SecurityUtils.getSubject();

		// usernameLabel.setValue((String) subject.getPrincipal());
		// rolesLabel.setValue("");
	}

	// 代办任务
	public void showTasksPage() {
		switchView(new TasksPage(), ViewManager.MAIN_NAVIGATION_TASK,
				TaskMenuBar.ENTRY_TASKS);

	}

	public void showTaskPage(String id) {
		switchView(new TasksPage(id), ViewManager.MAIN_NAVIGATION_TASK,
				TaskMenuBar.ENTRY_TASKS);
	}

	public void showTasksPage(String id) {
		// TODO Auto-generated method stub

	}

	/**
	 * 显示我的项目
	 */
	public void showProjectPage() {
		switchMainContent(new ProjectMain());
	}

	/**
	 * 显示我的日程
	 */
	public void showMySchedulePage() {
		switchMainContent(new MyCalendarView());
	}

	// 受邀任务
	public void showInvolvedPage() {
		switchView(new InvolvedPage(), ViewManager.MAIN_NAVIGATION_TASK,
				TaskMenuBar.ENTRY_INVOLVED);
	}

	// 组任务
	public void showQueuedPage(String groupId) {
		switchView(new QueuedPage(groupId), ViewManager.MAIN_NAVIGATION_TASK,
				TaskMenuBar.ENTRY_QUEUED);
	}

	public void showArchivedPage() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 显示公共项目
	 */
	public void showPublicProject() {
		switchMainContent(new PublicProjectListPage());
	}
	
	//用户成员管理
	public void showOrganizationMain() {
		switchMainContent(new OrganizationMainPage());
		
	}

	public void showOrganMemberMain() {
		switchMainContent(new OrganizationMemberMainPage());
	}

	/**
	 * 显示外部项目管理
	 */
	public void showOutProjectManagerPage() {
		switchMainContent(new OutProjectManagerMainPage());
	}

}
