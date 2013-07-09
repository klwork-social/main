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

package com.klwork.explorer.ui.mainlayout;

import java.util.HashMap;
import java.util.Map;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoggedInUser;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.user.ChangePasswordPopupWindow;
import com.klwork.explorer.ui.user.ProfilePopupWindow;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Joram Barrez
 * @author Frederik Heremans
 */
@SuppressWarnings("serial")
public class MainMenuBar extends HorizontalLayout {

	private static final long serialVersionUID = 1L;

	// protected ViewManager viewManager;
	protected I18nManager i18nManager;
	protected Map<String, Button> menuItemButtons;
	protected String currentMainNavigation;

	public MainMenuBar() {
		this.i18nManager = ViewToolManager.getI18nManager();

		menuItemButtons = new HashMap<String, Button>();
		init();
	}

	/**
	 * Highlights the given main navigation in the menubar.
	 */
	public synchronized void setMainNavigation(String navigation) {
		if (currentMainNavigation != null) {
			menuItemButtons.get(currentMainNavigation).removeStyleName(
					ExplorerLayout.STYLE_ACTIVE);
		}
		currentMainNavigation = navigation;

		Button current = menuItemButtons.get(navigation);
		if (current != null) {
			current.addStyleName(ExplorerLayout.STYLE_ACTIVE);
		}
	}

	public synchronized void clearNavigation() {
		menuItemButtons.get(currentMainNavigation).removeStyleName(
				ExplorerLayout.STYLE_ACTIVE);
	}

	protected void init() {
		setHeight(54, Unit.PIXELS);
		setWidth(100, Unit.PERCENTAGE);
		setMargin(new MarginInfo(false, true, false, false));

		initTitle();// 1.0
		// WW_TODO 首页大菜单(任务流程、报表、管理)
		initButtons();
		
		//通知按钮
		//initNotificationButton();
		
		initProjectButton();
		// WW_TODO 用户修改密码/注销等
		initProfileButton();// 1.0
	}

	protected void initButtons() {
		// TODO: fixed widths based on i18n strings?
		Button taskButton = addMenuButton(ViewManager.MAIN_NAVIGATION_TASK,
				i18nManager.getMessage(Messages.MAIN_MENU_TASKS),
				Images.MAIN_MENU_TASKS, false, 80);
		taskButton.addClickListener(new ShowTasksClickListener());
		menuItemButtons.put(ViewManager.MAIN_NAVIGATION_TASK, taskButton);
		
		//公共任务
		Button processButton = addMenuButton(
				ViewManager.MAIN_NAVIGATION_PROCESS,
				i18nManager.getMessage(Messages.MAIN_MENU_PUBLIC_TASK),
				Images.MAIN_MENU_PROCESS, false, 80);
		processButton
				.addClickListener(new ShowProcessDefinitionsClickListener());
		menuItemButtons.put(ViewManager.MAIN_NAVIGATION_PROCESS, processButton);
		
		
		
		//我的代办事项
		Button todoListButton = addMenuButton(
				ViewManager.MAIN_NAVIGATION_REPORT,
				i18nManager.getMessage(Messages.MAIN_MENU_TODOLIST),
				Images.MAIN_MENU_REPORTS, false, 80);
		todoListButton.addClickListener(new ShowTodoListClickListener());
		menuItemButtons
				.put(ViewManager.MAIN_NAVIGATION_REPORT, todoListButton);
		
		//外部项目管理
				Button outProjectManagerButton = addMenuButton(
						ViewManager.MAIN_NAVIGATION_PROCESS,
						i18nManager.getMessage(Messages.MAIN_MENU_MY_PROJECT),
						Images.MAIN_MENU_PROCESS, false, 80);
				outProjectManagerButton
						.addClickListener(new ShowOutProjectManagerClickListener());
				menuItemButtons.put(ViewManager.MAIN_NAVIGATION_PROCESS, outProjectManagerButton);
		/*
		 * if (xx.getLoggedInUser().isAdmin()) { Button manageButton =
		 * addMenuButton(ViewManager.MAIN_NAVIGATION_MANAGE,
		 * i18nManager.getMessage(Messages.MAIN_MENU_MANAGEMENT),
		 * Images.MAIN_MENU_MANAGE, false, 90); manageButton.addListener(new
		 * ShowManagementClickListener());
		 * menuItemButtons.put(ViewManager.MAIN_NAVIGATION_MANAGE,
		 * manageButton); }
		 */
	}

	protected void initTitle() {
		Label title = new Label();
		title.addStyleName(Reindeer.LABEL_H1);
		title.addStyleName(ExplorerLayout.STYLE_APPLICATION_LOGO);
		/*
		 * if (xx.getEnvironment().equals(Environments.ALFRESCO)) {
		 * title.addStyleName(ExplorerLayout.STYLE_WORKFLOW_CONSOLE_LOGO); }
		 * else { title.addStyleName(ExplorerLayout.STYLE_APPLICATION_LOGO); }
		 */

		addComponent(title);

		setExpandRatio(title, 1.0f);
	}

	protected Button addMenuButton(String type, String label, Resource icon,
			boolean active, float width) {
		Button button = new Button(label);
		button.addStyleName(type);
		button.addStyleName(ExplorerLayout.STYLE_MAIN_MENU_BUTTON);
		button.addStyleName(Reindeer.BUTTON_LINK);
		button.setHeight(54, Unit.PIXELS);
		button.setIcon(icon);
		button.setWidth(width, Unit.PIXELS);

		addComponent(button);
		setComponentAlignment(button, Alignment.TOP_CENTER);

		return button;
	}

	private void initNotificationButton() {
		
		 Button notify = new Button("2");
	        notify.setDescription("未读通知(2)");
	        // notify.addStyleName("borderless");
	        //notify.addStyleName(ExplorerLayout.STYLE_MAIN_MENU_BUTTON);
	        //notify.addStyleName(Reindeer.BUTTON_LINK);
	        CssLayout panel = new CssLayout();
	        panel.addStyleName("dashboard");
	        panel.setSizeFull();
	        notify.addStyleName("notifications");
	        notify.addStyleName("unread");
	        notify.addStyleName("icon-only");
	        notify.addStyleName("icon-bell");
	        notify.addClickListener(new ClickListener() {
	            @Override
	            public void buttonClick(ClickEvent event) {
	                //((DashboardUI) getUI()).clearDashboardButtonBadge();
	                event.getButton().removeStyleName("unread");
	                event.getButton().setDescription("Notifications");
	            }
	        });
	        
	        panel.addComponent(notify);
	        //notify.setSizeFull();
	        addComponent(panel);   
		setComponentAlignment(panel, Alignment.BOTTOM_CENTER);
		setExpandRatio(panel, 1.0f);
	}
	
	private void initProjectButton() {
		MenuBar helpMenu = new MenuBar();
		helpMenu.addStyleName(ExplorerLayout.STYLE_HEADER_PROFILE_BOX);

		MenuItem rootItem = helpMenu.addItem("项目管理", null);
		rootItem.setStyleName(ExplorerLayout.STYLE_HEADER_PROFILE_MENU);
		//项目计划
		rootItem.addItem(i18nManager.getMessage(Messages.MAIN_MENU_TODOLIST), new Command() {
			public void menuSelected(MenuItem selectedItem) {
				ViewToolManager.getMainView().showProjectPage();
			}
		});
		//我的日程
		rootItem.addItem(i18nManager.getMessage(Messages.EXPAND_MENU_MY_SCHEDULE), new Command() {
			public void menuSelected(MenuItem selectedItem) {
				ViewToolManager.getMainView().showMySchedulePage();
			}
		});


		addComponent(helpMenu);
		setComponentAlignment(helpMenu, Alignment.TOP_RIGHT);
		setExpandRatio(helpMenu, 1.0f);
	}

	protected void initProfileButton() {
		final LoggedInUser user = LoginHandler.getLoggedInUser();
		// LoggedInUser user = new LoggedInUser();
		// User name + link to profile
		MenuBar profileMenu = new MenuBar();
		profileMenu.addStyleName(ExplorerLayout.STYLE_HEADER_PROFILE_BOX);
		MenuItem rootItem = profileMenu.addItem(user.getFirstName() + " "
				+ user.getLastName(), null);
		rootItem.setStyleName(ExplorerLayout.STYLE_HEADER_PROFILE_MENU);

		if (useProfile()) {
			// Show profile//参看资料
			rootItem.addItem(i18nManager.getMessage(Messages.PROFILE_SHOW),
					new Command() {
						public void menuSelected(MenuItem selectedItem) {
							ViewToolManager
									.showPopupWindow(new ProfilePopupWindow(
											user.getId()));
						}
					});

			// Edit profile
			rootItem.addItem("账户管理", new Command() {

				public void menuSelected(MenuItem selectedItem) {
					ViewToolManager.showPopupWindow(new ProfilePopupWindow(user
							.getId()));
				}
			});

			// Change password
			rootItem.addItem(i18nManager.getMessage(Messages.PASSWORD_CHANGE),
					new Command() {
						public void menuSelected(MenuItem selectedItem) {
							ViewToolManager
									.showPopupWindow(new ChangePasswordPopupWindow());
						}
					});
		}

		// Logout
		rootItem.addItem(i18nManager.getMessage(Messages.HEADER_LOGOUT),
				new Command() {
					public void menuSelected(MenuItem selectedItem) {
						// xx.close();
						ViewToolManager.logout();
					}
				});
		rootItem.addSeparator();
		// 邀请好友
		rootItem.addItem(i18nManager.getMessage(Messages.EXPAND_MENU_INVITE), new Command() {
			public void menuSelected(MenuItem selectedItem) {
				// xx.close();
				// ViewToolManager.logout();
			}
		});

		// 创建团队
		rootItem.addItem("创建团队", new Command() {
			public void menuSelected(MenuItem selectedItem) {
				// xx.close();
				// ViewToolManager.logout();
				ViewToolManager.getMainView().showOrganizationMain();
				
			}
		});

		addComponent(profileMenu);
		setComponentAlignment(profileMenu, Alignment.TOP_RIGHT);
		// setExpandRatio(profileMenu, 1.0f);
	}

	protected boolean useProfile() {
		return true;
	}

	// Listener classes
	private class ShowTasksClickListener implements ClickListener {
		public void buttonClick(ClickEvent event) {
			ViewToolManager.getMainView().showInboxPage();
		}
	}

	private class ShowProcessDefinitionsClickListener implements ClickListener {
		public void buttonClick(ClickEvent event) {
			ViewToolManager.getMainView().showPublicProject();
		}
	}
	
	private class ShowOutProjectManagerClickListener implements ClickListener {
		public void buttonClick(ClickEvent event) {
			ViewToolManager.getMainView().showOutProjectManagerPage();
		}
	}
	
	private class ShowTodoListClickListener implements ClickListener {
		public void buttonClick(ClickEvent event) {
			ViewToolManager.getMainView().showProjectPage();
		}
	}

	private class ShowReportsClickListener implements ClickListener {
		public void buttonClick(ClickEvent event) {
		}
	}

	private class ShowManagementClickListener implements ClickListener {
		public void buttonClick(ClickEvent event) {
		}
	}
}
