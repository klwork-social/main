package com.klwork.explorer.ui.business.flow.act;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.service.OutsourcingProjectService;
import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.form.FormPropertiesComponent;
import com.klwork.explorer.ui.form.FormPropertiesEvent;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.UserDetailsComponent;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class PublishNeedForm extends com.klwork.explorer.ui.task.TaskForm {

	private static final long serialVersionUID = -3197331726904715949L;
	
	// Services
	protected transient FormService formService;
	protected transient RuntimeService runtimeService;
	protected transient TaskService taskService;
	public transient OutsourcingProjectService outsourcingProjectService;
	protected transient ProjectManagerService projectManagerService;
	protected I18nManager i18nManager;

	// UI
	protected Label formTitle;
	protected Button submitFormButton;
	protected Button cancelFormButton;
	protected FormPropertiesComponent formPropertiesComponent;
	protected GridLayout taskDetails;
	protected MyTaskRelatedContentComponent relatedContent;
	protected OutsourcingProject relateOutSourceingProject;
	//需求审核人列表
	public IdentityLink identityLinkChecker;

	FieldGroup fieldGroup = new FieldGroup();

	public PublishNeedForm(Task task) {
		setTask(task);
		formService = ProcessEngines.getDefaultProcessEngine().getFormService();
		runtimeService = ProcessEngines.getDefaultProcessEngine()
				.getRuntimeService();
		taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		
		i18nManager = ViewToolManager.getI18nManager();
		outsourcingProjectService = ViewToolManager
				.getBean("outsourcingProjectService");
		projectManagerService = ViewToolManager
				.getBean("projectManagerService");
	}
	
	@Override
	protected void initUi() {
		addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addStyleName(ExplorerLayout.STYLE_FORM_PROPERTIES);
		//标题
		initTitle();
		//表单数据
		initFormPropertiesComponent();
		
		//相关附件
		initRelatedContent();
		
		//作品上传
		initUpWorkContent();
	
		//button
		initButtons();
		//button的行为
		initActions();
	}

	
	protected void initUpWorkContent() {
		
	}

	protected void initActions() {
		submitFormButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6091586145870618870L;

			public void buttonClick(ClickEvent event) {
				try {
					Map<String, Object> formProperties = null;
					try {
						fieldGroup.commit();
						OutsourcingProject outsourcingProject = BinderHandler
								.getFieldGroupBean(fieldGroup);
						formProperties = projectManagerService.submitPublishNeed(outsourcingProject,getTask(),identityLinkChecker);
					} catch (CommitException e) {
						e.printStackTrace();
					}

					// 通知外边的调用任务,任务相关信息已经保存
					fireEvent(new SubmitEvent(PublishNeedForm.this,
							SubmitEvent.SUBMITTED, formProperties));
					submitFormButton.setComponentError(null);
				} catch (InvalidValueException ive) {
					// Error is presented to user by the form component
				}
			}
		});

		cancelFormButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -8980500491522472381L;

			public void buttonClick(ClickEvent event) {
				fireEvent(new FormPropertiesEvent(PublishNeedForm.this,
						FormPropertiesEvent.TYPE_CANCEL));
				submitFormButton.setComponentError(null);
			}
		});
	}

	protected void initRelatedContent() {
		relatedContent = new MyTaskRelatedContentComponent(
				this.getTask(), this,false);
		addComponent(relatedContent);
	}

	public void setFormProperties(List<FormProperty> formProperties) {
		// Component will refresh it's components based on the passed properties
		formPropertiesComponent.setFormProperties(formProperties);
	}

	public void setSubmitButtonCaption(String caption) {
		submitFormButton.setCaption(caption);
	}

	public void setCancelButtonCaption(String caption) {
		cancelFormButton.setCaption(caption);
	}

	public void setFormHelp(String caption) {
		formTitle.setValue(caption);
		formTitle.setVisible(caption != null);
	}

	/**
	 * Clear all (writable) values in the form.
	 */
	public void clear() {
		formPropertiesComponent.setFormProperties(formPropertiesComponent
				.getFormProperties());
	}

	protected void initTitle() {
		formTitle = new Label();
		formTitle.addStyleName(ExplorerLayout.STYLE_H4);
		formTitle.setVisible(false);
		addComponent(formTitle);

		initPromptTitle();
	}

	protected void initPromptTitle() {
		setFormHelp(i18nManager.getMessage(Messages.TASK_FORM_HELP));
	}

	protected void initButtons() {
		submitFormButton = new Button();
		cancelFormButton = new Button();

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.setSpacing(true);
		buttons.setWidth(100, Unit.PERCENTAGE);
		buttons.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		buttons.addComponent(submitFormButton);
		buttons.setComponentAlignment(submitFormButton, Alignment.BOTTOM_RIGHT);

		buttons.addComponent(cancelFormButton);
		buttons.setComponentAlignment(cancelFormButton, Alignment.BOTTOM_RIGHT);

		Label buttonSpacer = new Label();
		buttons.addComponent(buttonSpacer);
		buttons.setExpandRatio(buttonSpacer, 1.0f);
		addComponent(buttons);

		setSubmitButtonCaption(i18nManager.getMessage(Messages.TASK_COMPLETE));
		setCancelButtonCaption(i18nManager.getMessage(Messages.TASK_RESET_FORM));
	}

	protected void initFormPropertiesComponent() {
		//绑定数据和初始grid布局
		initFormLayoutAndData();
		//项目名称
		initProjectName(false);
		
		//截止时间
		initDeadline(false);
		
		//悬赏金额
		initBounty(false);
		
		//需求类型
		initNeedType(false);
		
		//需求描叙
		initDescription(false);

		//审核人
		initChecker();

	}

	protected void initFormLayoutAndData() {
		taskDetails = new GridLayout(4, 10);
		taskDetails.setWidth(100, Unit.PERCENTAGE);
		taskDetails.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
		taskDetails.setSpacing(true);
		addComponent(taskDetails);
		// 查询关联的project
		relateOutSourceingProject = projectManagerService.getRelateOutSourceingProject(getTask());
		BeanItem<OutsourcingProject> item = new BeanItem<OutsourcingProject>(
				relateOutSourceingProject);
		fieldGroup.setItemDataSource(item);
	}

	protected void initChecker() {
		Label checkerLabel = new Label("需求审核人:");
		taskDetails.addComponent(checkerLabel, 0, 9);
		initLabelOfGrid(taskDetails, checkerLabel);
		
		//需求审核人
		UserDetailsComponent u = createCheckerComponent();
		taskDetails.addComponent(u, 1, 9);
	}

	protected void initDescription(boolean readOnly) {
		Label descriptionLabel = new Label("需求描述及要求:");
		taskDetails.addComponent(descriptionLabel, 0, 3, 0, 3);
		initLabelOfGrid(taskDetails, descriptionLabel);

		TextArea descriptionField = CommonFieldHandler.createTextArea("");
		descriptionField.setSizeFull();
		descriptionField.setHeight("80px");
		// descriptionField.setH
		taskDetails.addComponent(descriptionField, 1, 3, 3, 8);
		fieldGroup.bind(descriptionField, "description");
		descriptionField.setReadOnly(readOnly);
	}

	protected void initNeedType(boolean readOnly) {
		Label reqTypeLabel = new Label("需求类型:");
		taskDetails.addComponent(reqTypeLabel, 2, 1, 2, 1);
		initLabelOfGrid(taskDetails, reqTypeLabel);
		Map<String, String> data = new HashMap();
		data.put("1", "视频");
		data.put("2", "创意");
		ComboBox reqTypeLabelField = CommonFieldHandler.createComBox(null,
				data, "1");
		taskDetails.addComponent(reqTypeLabelField, 3, 1, 3, 1);
		fieldGroup.bind(reqTypeLabelField, "type");
		reqTypeLabelField.setReadOnly(readOnly);
	}

	protected void initBounty(boolean readOnly) {
		Label amountLabel = new Label("悬赏金额:");
		taskDetails.addComponent(amountLabel, 0, 1, 0, 1);
		initLabelOfGrid(taskDetails, amountLabel);

		TextField amountField = CommonFieldHandler.createTextField("");
		taskDetails.addComponent(amountField, 1, 1, 1, 1);
		fieldGroup.bind(amountField, "bounty");
		fieldGroup.setReadOnly(readOnly);
	}

	protected void initDeadline(boolean readOnly) {
		Label endDateLabel = new Label("作品上传截止时间:");
		taskDetails.addComponent(endDateLabel, 2, 0, 2, 0);
		initLabelOfGrid(taskDetails, endDateLabel);

		DateField endDateField = CommonFieldHandler.createDateField("", false);
		taskDetails.addComponent(endDateField, 3, 0, 3, 0);
		fieldGroup.bind(endDateField, "deadline");
		endDateField.setReadOnly(readOnly);
	}

	protected void initProjectName(boolean readOnly) {
		Label nameLabel = new Label("项目名称:");
		taskDetails.addComponent(nameLabel, 0, 0, 0, 0);
		initLabelOfGrid(taskDetails, nameLabel);

		TextField nameField = CommonFieldHandler.createTextField("");
		taskDetails.addComponent(nameField, 1, 0, 1, 0);
		fieldGroup.bind(nameField, "name");
		nameField.setReadOnly(readOnly);
	
	}

	
	private IdentityLink queryAuditorOfInstance() {
		List<IdentityLink> identityLinks = runtimeService
				.getIdentityLinksForProcessInstance(getTask()
						.getProcessInstanceId());
		for (final IdentityLink identityLink : identityLinks) {// 审核类型的用户
			if (EntityDictionary.IDENTITY_LINK_TYPE_AUDITOR.equals(identityLink
					.getType())) {
				return identityLink;
			}
		}
		return null;
	}
	
	public void notifyCheckerChanged() {
		taskDetails.removeComponent(1, 9);
		taskDetails.addComponent(createCheckerComponent(), 1, 9);
	}

	protected UserDetailsComponent createCheckerComponent() {
		boolean hasCheck = false;
		identityLinkChecker = queryAuditorOfInstance();
		// 无所属人
		String roleMessage = (identityLinkChecker != null) ? "审核人" : "无审核人";
		String userId = (identityLinkChecker != null) ? identityLinkChecker.getUserId(): null;
		String buttonCaption = (identityLinkChecker != null) ? "重新指定":"指定";
		UserDetailsComponent involvedDetails = new UserDetailsComponent(
				userId,
				roleMessage, buttonCaption,
				new ChangeCheckerListener(identityLinkChecker, getTask(), this));
		
		return involvedDetails;
	}

	public void initLabelOfGrid(GridLayout taskDetails, Label nameLabel) {
		nameLabel.addStyleName(ExplorerLayout.STYLE_PROFILE_FIELD);
		nameLabel.setSizeUndefined();
		taskDetails.setComponentAlignment(nameLabel, Alignment.MIDDLE_RIGHT);
	}

	public void hideCancelButton() {
		cancelFormButton.setVisible(false);
	}

	protected void addEmptySpace(ComponentContainer container) {
		Label emptySpace = new Label("&nbsp;", Label.CONTENT_XHTML);
		emptySpace.setSizeUndefined();
		container.addComponent(emptySpace);
	}

	@Override
	public void notifyRelatedContentChanged() {
		relatedContent.refreshTaskAttachments();
	}
}