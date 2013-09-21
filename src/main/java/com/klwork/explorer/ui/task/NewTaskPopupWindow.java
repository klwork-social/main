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
package com.klwork.explorer.ui.task;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.StringTool;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.base.AbstractFormPoputWindow;
import com.klwork.explorer.ui.handler.BusinessComponetHelp;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * Popup window to create a new task
 */
public class NewTaskPopupWindow extends AbstractFormPoputWindow {

	private static final long serialVersionUID = 1L;

	protected transient TaskService taskService;
	protected transient IdentityService identityService;
	protected TeamService teamService;
	protected I18nManager i18nManager;

	// protected HorizontalLayout layout;
	// protected Form form;
	protected TextField nameField;
	protected TextArea descriptionArea;
	protected DateField dueDateField;
	protected PriorityComboBox priorityComboBox;
	ComboBox userGroupComboBox;
	ComboBox userComboBox;
	protected Button createTaskButton;

	BusinessComponetHelp help = new BusinessComponetHelp();
	// 我的team数据
	private Map<String, String> teamsOfMyMap;
	// 我参与的team数据
	private Map<String, String> teamsMyInMap;

	public NewTaskPopupWindow() {
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		this.i18nManager = ViewToolManager.getI18nManager();
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
		identityService = ProcessEngines.getDefaultProcessEngine()
				.getIdentityService();
		// addStyleName(ExplorerLayout.THEME);
		String userId = LoginHandler.getLoggedInUser().getId();
		teamsOfMyMap = help.getUserOfMyMap();
		teamsMyInMap = help.getUserOfMyInMap();

	}

	@Override
	protected void intPopWindowCustom() {
		setCaption(i18nManager.getMessage(Messages.TASK_NEW));
		setWidth(560, Unit.PIXELS);
		setHeight(480, Unit.PIXELS);
	}

	@Override
	protected void initFormSubComponent() {
		// name
		nameField = new TextField(i18nManager.getMessage(Messages.TASK_NAME));
		nameField.focus();
		nameField.setRequired(true);
		nameField.setRequiredError(i18nManager
				.getMessage(Messages.TASK_NAME_REQUIRED));
		getForm().addComponent(nameField);

		// description
		descriptionArea = new TextArea(
				i18nManager.getMessage(Messages.TASK_DESCRIPTION));
		descriptionArea.setColumns(25);
		// form.addField("description", descriptionArea);
		getForm().addComponent(descriptionArea);
		// duedate
		dueDateField = CommonFieldHandler.createDateField(
				i18nManager.getMessage(Messages.TASK_DUEDATE), false);
		// form.addField("duedate", dueDateField);
		getForm().addComponent(dueDateField);

		// priority
		priorityComboBox = new PriorityComboBox(i18nManager);
		priorityComboBox.setCaption("优先级");
		// form.addField("priority", priorityComboBox);
		getForm().addComponent(priorityComboBox);

		List<DictDef> list = DictDef.queryDictsByType(DictDef
				.dict("user_team_type"));
		OptionGroup g = CommonFieldHandler.createCheckBoxs(list, "团队类型", false,
				"0");
		g.setImmediate(true);
		g.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				Object value = event.getProperty().getValue();
				if ("0".equals(value)) {
					CommonFieldHandler.updateComBoxData(userGroupComboBox,
							i18nManager.getMessage(Messages.TEAM_SELECT),
							teamsOfMyMap, "");
				} else {
					CommonFieldHandler.updateComBoxData(userGroupComboBox,
							i18nManager.getMessage(Messages.TEAM_SELECT),
							teamsMyInMap, "");
				}
				// System.out.println(value);
			}

		});
		getForm().addComponent(g);

		// 用户组
		userGroupComboBox = help.getUserOfTeamComboBox();
		// userGroupComboBox.
		userGroupComboBox
				.addValueChangeListener(new com.vaadin.data.Property.ValueChangeListener() {
					/**
			 * 
			 */
					private static final long serialVersionUID = -3864934875844211279L;

					@Override
					public void valueChange(ValueChangeEvent event) {
						Object o = event.getProperty().getValue();
						if (StringTool.judgeBlank(o)) {
							changeUserSelect(o.toString());
						}
					}

				});
		getForm().addComponent(userGroupComboBox);

		// 用户
		Map<String, String> usersMap = new HashMap();
		usersMap.put("", i18nManager.getMessage(Messages.SELECT_DEFAULT));
		userComboBox = CommonFieldHandler.createComBox(
				i18nManager.getMessage(Messages.TEAM_MEMBER_SELECT), usersMap,
				"");
		getForm().addComponent(userComboBox);

		initCreateTaskButton();
		initEnterKeyListener();
	}

	/**
	 * 重新改变用户的选择
	 * 
	 * @param groupId
	 */
	protected void changeUserSelect(String groupId) {
		Map<String, String> usersMap = new HashMap<String, String>();
		List<User> users = identityService.createUserQuery()
				.memberOfTeam(groupId).list();
		userComboBox.removeAllItems();
		for (User user : users) {
			Item i = userComboBox.addItem(user.getId());
			userComboBox.setItemCaption(user.getId(), user.getId());
		}
	}

	protected void initCreateTaskButton() {
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setMargin(true);
		buttonLayout.setWidth(100, Unit.PERCENTAGE);
		form.addComponent(buttonLayout);

		Button createButton = new Button(
				i18nManager.getMessage(Messages.BUTTON_CREATE));
		buttonLayout.addComponent(createButton);
		buttonLayout
				.setComponentAlignment(createButton, Alignment.BOTTOM_RIGHT);

		createButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				handleFormSubmit();
			}
		});
	}

	protected void initEnterKeyListener() {
		addActionHandler(new Handler() {
			public void handleAction(Action action, Object sender, Object target) {
				handleFormSubmit();
			}

			public Action[] getActions(Object target, Object sender) {
				return new Action[] { new ShortcutAction("enter",
						ShortcutAction.KeyCode.ENTER, null) };
			}
		});
	}

	protected void handleFormSubmit() {
		try {
			// Check for errors

			// Create task
			Task task = taskService.newTask();
			task.setName(nameField.getValue().toString());
			task.setDescription(descriptionArea.getValue().toString());
			task.setDueDate((Date) dueDateField.getValue());
			task.setPriority(priorityComboBox.getPriority());
			task.setOwner(LoginHandler.getLoggedInUser().getId());
			// 是否是组的任务
			boolean addGroupLink = false;
			boolean addUserTaskLink = false;// 直接到人的任务

			Object userGroupId = userGroupComboBox.getValue();
			if (StringTool.judgeBlank(userGroupId)) {// 用户组，进行了选择
				Object userId = userComboBox.getValue();
				if (StringTool.judgeBlank(userId)) {// 用户不为空
					addUserTaskLink = true;
				} else {
					addGroupLink = true;
				}
			} else {// 什么都没有进行选择
				task.setOwner(LoginHandler.getLoggedInUser().getId());
			}
			taskService.saveTask(task);

			if (addGroupLink) {
				taskService.addCandidateGroup(task.getId(),
						userGroupId.toString());
			}
			if (addUserTaskLink) {//指定任务的办理人
				String factUserId = userComboBox
						.getValue().toString();
				taskService.addUserIdentityLink(task.getId(), factUserId, IdentityLinkType.ASSIGNEE);
			}
			//
			Notification.show("任务创建成功!", Notification.Type.HUMANIZED_MESSAGE);
			close();
			ViewToolManager.showTasksPage(task.getId());
			

		} catch (InvalidValueException e) {
			// Do nothing: the Form component will render the errormsgs
			// automatically
			setHeight(350, Unit.PIXELS);
		}
	}

}
