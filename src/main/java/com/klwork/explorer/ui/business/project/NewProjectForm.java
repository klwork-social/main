package com.klwork.explorer.ui.business.project;

import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

public class NewProjectForm extends CustomComponent {
	protected I18nManager i18nManager;

	@PropertyId("name")
	TextField nameField = null;
	@PropertyId("description")
	TextArea descriptionArea = null;
	FormLayout layout = null;

	ProjectService projectService;

	boolean edit = false;

	public NewProjectForm(final Window opener,
			final BeanItem<Project> projectItem) {
		projectService = ViewToolManager.getBean("projectService");
		this.i18nManager = ViewToolManager.getI18nManager();
		layout = new FormLayout();

		nameField = new TextField(i18nManager.getMessage(Messages.TASK_NAME));
		nameField.setValue("");
		nameField.focus();
		nameField.setRequired(true);
		nameField.setRequiredError(i18nManager
				.getMessage(Messages.TASK_NAME_REQUIRED));

		// description
		descriptionArea = new TextArea(
				i18nManager.getMessage(Messages.TASK_DESCRIPTION));
		descriptionArea.setColumns(25);

		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(100, Unit.PERCENTAGE);
		if (projectItem.getBean().getId() != null) {
			edit = true;
		}
		if (edit) {
			Button deleteButton = new Button(
					i18nManager.getMessage(Messages.BUTTON_DELETE));
			buttonLayout.addComponent(deleteButton);
			buttonLayout.setComponentAlignment(deleteButton,
					Alignment.BOTTOM_RIGHT);
			deleteButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					handleDelete(projectItem);
					opener.close();

				}
			});
		}

		Button createButton = new Button(
				i18nManager.getMessage(Messages.BUTTON_CREATE));
		buttonLayout.addComponent(createButton);
		buttonLayout
				.setComponentAlignment(createButton, Alignment.BOTTOM_RIGHT);

		layout.addComponent(nameField);
		layout.addComponent(descriptionArea);
		layout.addComponent(buttonLayout);
		final FieldGroup binder = new FieldGroup(projectItem);
		binder.bindMemberFields(this);
		setCompositionRoot(layout);

		createButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				try {
					binder.commit();
					handleFormSubmit(projectItem);
					opener.close();
					ViewToolManager.getMainView().showProjectPage();
				} catch (CommitException e) {
					e.printStackTrace();
				}
				// System.out.println("提交完成" + projectItem);
			}
		});

	}

	/**
	 * 保存到数据库
	 * 
	 * @param projectItem
	 */
	private void handleFormSubmit(BeanItem<Project> projectItem) {
		if (projectItem != null) {
			Project bean = projectItem.getBean();
			if (bean.getId() == null) {
				projectService.createProject(bean);
			} else {
				projectService.updateProject(bean);
			}
		}
	};

	/**
	 * 保存到数据库
	 * 
	 * @param projectItem
	 */
	private void handleDelete(BeanItem<Project> projectItem) {
		if (projectItem != null) {
			projectService.deleteProject(projectItem.getBean());
		}
	};

	public void addComponentToMain(Component buttonLayout) {
		layout.addComponent(buttonLayout);
	}
}
