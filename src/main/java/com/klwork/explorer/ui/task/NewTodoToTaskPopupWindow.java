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
import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.Todo;
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
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

/**
 * Popup window to create a new task
 */
public class NewTodoToTaskPopupWindow extends AbstractFormPoputWindow {

	private static final long serialVersionUID = 1L;

	protected transient TaskService taskService;
	protected transient IdentityService identityService;
	protected TeamService teamService;
	protected I18nManager i18nManager;

	// protected HorizontalLayout layout;
	// protected Form form;
	private Todo relTodo;
	protected TextField nameField;
	protected TextArea descriptionArea;
	protected DateField dueDateField;
	protected PriorityComboBox priorityComboBox;
	ComboBox userGroupComboBox;
	ComboBox userComboBox;
	protected Button createTaskButton;
	
	BusinessComponetHelp help = new BusinessComponetHelp();
	
	public NewTodoToTaskPopupWindow(Todo relTodo) {
		this.relTodo = relTodo;
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		this.i18nManager = ViewToolManager.getI18nManager();
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
		identityService = ProcessEngines.getDefaultProcessEngine()
				.getIdentityService();
		 //addStyleName(ExplorerLayout.THEME);
	}

	@Override
	protected void intPopWindowCustom() {
		setCaption(i18nManager.getMessage(Messages.TASK_NEW));
		setWidth(460, Unit.PIXELS);
		setHeight(380, Unit.PIXELS);
	}

	@Override
	protected void initFormSubComponent() {
		// name
		nameField = new TextField(i18nManager.getMessage(Messages.TASK_NAME));
		nameField.setValue(relTodo.getName());
		nameField.focus();
		nameField.setRequired(true);
		nameField.setRequiredError(i18nManager
				.getMessage(Messages.TASK_NAME_REQUIRED));
		getForm().addComponent(nameField);

		// description
		descriptionArea = new TextArea(
				i18nManager.getMessage(Messages.TASK_DESCRIPTION));
		if(StringTool.judgeBlank(relTodo.getDescription())){
			descriptionArea.setValue(relTodo.getDescription());
		}
		descriptionArea.setColumns(25);
		// form.addField("description", descriptionArea);
		getForm().addComponent(descriptionArea);
		// duedate
		dueDateField = CommonFieldHandler.createDateField(
				i18nManager.getMessage(Messages.TASK_DUEDATE), false);
		if(StringTool.judgeBlank(relTodo.getCompletionDate())){
			dueDateField.setValue(relTodo.getCompletionDate());
		}
		// form.addField("duedate", dueDateField);
		getForm().addComponent(dueDateField);

		// priority
		priorityComboBox = new PriorityComboBox(i18nManager);
		// form.addField("priority", priorityComboBox);
		getForm().addComponent(priorityComboBox);
		//默认选择的用户团队
		String defaultTeam = null;
		if(StringTool.judgeBlank(relTodo.getAssignedTeam())){
			defaultTeam = relTodo.getAssignedTeam();
		}
		userGroupComboBox = help.getUserOfTeamComboBox("用户团队",defaultTeam);
		//userGroupComboBox.
		userGroupComboBox.addValueChangeListener(new com.vaadin.data.Property.ValueChangeListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -3864934875844211279L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				Object o  = event.getProperty().getValue();
				changeUserSelect(o.toString());
			}
			
		});
		getForm().addComponent(userGroupComboBox);
		
		String defaultUser = null;
		//用户
		if(StringTool.judgeBlank(relTodo.getAssignedUser())){
			defaultUser = relTodo.getAssignedUser();
		}
		
		Map<String, String> usersMap = new HashMap();
		usersMap.put("", i18nManager
				.getMessage(Messages.SELECT_DEFAULT));
		userComboBox = CommonFieldHandler.createComBox(i18nManager
				.getMessage(Messages.TEAM_MEMBER_SELECT), usersMap, defaultUser);
		getForm().addComponent(userComboBox);
		
		initCreateTaskButton();
		initEnterKeyListener();
	}


	
	/**
	 * 重新改变用户的选择
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
			//是否是组的任务
			boolean addGroupLink = false;
			Object userGroupId = userGroupComboBox.getValue();
			if(StringTool.judgeBlank(userGroupId)){
				Object userId = userComboBox.getValue();
				if(StringTool.judgeBlank(userId)){//用户不为空
					//taskService .addUserIdentityLink( task.getId(), userId.toString(), role);
					task.setOwner(userId.toString());
				}else {
					addGroupLink = true;
				}
			}else {
				task.setOwner(LoginHandler.getLoggedInUser().getId());
			}
			taskService.saveTask(task);
			
			if(addGroupLink){
				taskService.addCandidateGroup( task.getId(), userGroupId.toString());
			}
			// close popup and navigate to new group
			close();
			ViewToolManager.getMainView().showTasksPage(task.getId());

		} catch (InvalidValueException e) {
			// Do nothing: the Form component will render the errormsgs
			// automatically
			setHeight(350, Unit.PIXELS);
		}
	}

}
