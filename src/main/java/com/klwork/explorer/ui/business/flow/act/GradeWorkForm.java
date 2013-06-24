package com.klwork.explorer.ui.business.flow.act;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.ProjectParticipant;
import com.klwork.business.domain.service.ProjectParticipantService;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.form.FormPropertiesEvent;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

public class GradeWorkForm extends PublishNeedForm {

	protected  ProjectParticipantService projectParticipantService;
	protected  RuntimeService runtimeService;
	
	protected ComboBox checkResultField;

	protected UploadWorkTaskContentComponent uploadContent;
	
	private FieldGroup gradefieldGroup = new FieldGroup();
	private ProjectParticipant projectScoreParticipant;
	
	public GradeWorkForm(Task task) {
		super(task);
		this.projectParticipantService = ViewToolManager
				.getBean("projectParticipantService");
		this.runtimeService = ProcessEngines.getDefaultProcessEngine()
				.getRuntimeService();
	}


	@Override
	public void notifyRelatedContentChanged() {

	}

	@Override
	protected void initPromptTitle() {
		setFormHelp(i18nManager.getMessage(Messages.TASK_FORM_GRADE_HELP));
	}

	@Override
	protected void initUpWorkContent() {
		uploadContent = new UploadWorkTaskContentComponent(projectScoreParticipant.getAssessedTaskId(),
				this, true);
		addComponent(uploadContent);
	}

	@Override
	protected void initRelatedContent() {

	}

	@Override
	protected void initFormPropertiesComponent() {
		// 绑定数据和初始grid布局
		initFormLayoutAndData();
		// 项目名称
		initProjectName(true);

		// 截止时间
		initDeadline(true);

		// 悬赏金额
		initBounty(true);

		// 需求类型
		initNeedType(true);

		// 需求描叙
		initDescription(true);
		
		initGrade();

	}

	private void initGrade() {
		addComponent(CommonFieldHandler.getSpacer());
		
		GridLayout gradeLayout = new GridLayout(4, 3);
		gradeLayout.setWidth(100, Unit.PERCENTAGE);
		gradeLayout.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
		gradeLayout.setSpacing(true);
		addComponent(gradeLayout);
		
		projectScoreParticipant = projectManagerService.queryProjectScoreOfTask(getTask());
		BeanItem<ProjectParticipant> item = new BeanItem<ProjectParticipant>(
				projectScoreParticipant );
		gradefieldGroup.setItemDataSource(item);
		
		
		Label nameLabel = new Label("作品上传者:");
		gradeLayout.addComponent(nameLabel, 0, 0, 0, 0);
		initLabelOfGrid(gradeLayout, nameLabel);
		TextField nameField = CommonFieldHandler.createTextField("");
		gradeLayout.addComponent(nameField, 1, 0);
		gradefieldGroup.bind(nameField, "scoreUserId");
		nameField.setReadOnly(true);
		
		
		Label uploadDateLabel = new Label("上传时间:");
		gradeLayout.addComponent(uploadDateLabel, 2, 0);
		initLabelOfGrid(gradeLayout, uploadDateLabel);

		DateField uploadDateField = CommonFieldHandler.createDateField("", false);
		gradeLayout.addComponent(uploadDateField, 3, 0);
		uploadDateField.setReadOnly(true);
		
		Label scoreLabel = new Label("作品得分:");
		gradeLayout.addComponent(scoreLabel, 0, 1);
		initLabelOfGrid(gradeLayout, scoreLabel);

		TextField scoreField = CommonFieldHandler.createTextField("");
		gradeLayout.addComponent(scoreField, 1, 1);
		gradefieldGroup.bind(scoreField, "score");
		
		Label descriptionLabel = new Label("作品评价:");
		gradeLayout.addComponent(descriptionLabel, 0, 2);
		initLabelOfGrid(gradeLayout, descriptionLabel);

		TextArea descriptionField = CommonFieldHandler.createTextArea("");
		descriptionField.setSizeFull();
		descriptionField.setHeight("80px");
		// descriptionField.setH
		gradeLayout.addComponent(descriptionField, 1, 2);
		gradefieldGroup.bind(descriptionField, "workComment");
	}



	@Override
	protected void initActions() {
		submitFormButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6091586145870618870L;

			public void buttonClick(ClickEvent event) {
				Map<String, Object> formProperties = new HashMap<String, Object>();
				try {
					BinderHandler.commit(gradefieldGroup);
					ProjectParticipant p = BinderHandler
							.getFieldGroupBean(gradefieldGroup);
					
					formProperties = projectManagerService.submitGradeWork(p,getTask());
					/*formProperties.put("outsourcingProjectId",
							outsourcingProject.getId());
					// 把任务id传给下一个流程
					formProperties.put("taskId", getTask().getId());*/
					

					// 通知外边的调用任务,任务相关信息已经保存
					fireEvent(new SubmitEvent(GradeWorkForm.this,
							SubmitEvent.SUBMITTED, formProperties));
					submitFormButton.setComponentError(null);
				} catch (InvalidValueException ive) {
					// Error is presented to user by the form component
					ive.printStackTrace();
				}
			}
		});

		cancelFormButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = -8980500491522472381L;

			public void buttonClick(ClickEvent event) {
				fireEvent(new FormPropertiesEvent(GradeWorkForm.this,
						FormPropertiesEvent.TYPE_CANCEL));
				submitFormButton.setComponentError(null);
			}
		});
	}




	
}
