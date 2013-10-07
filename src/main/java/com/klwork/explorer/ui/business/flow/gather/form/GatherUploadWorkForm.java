package com.klwork.explorer.ui.business.flow.gather.form;

import java.util.Map;

import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.service.ProjectParticipantService;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.business.flow.act.MyTaskRelatedContentComponent;
import com.klwork.explorer.ui.business.flow.act.UploadWorkTaskContentComponent;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.form.FormPropertiesEvent;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;

public class GatherUploadWorkForm extends GatherPublishNeedForm {

	

	protected transient ProjectParticipantService projectParticipantService;

	protected ComboBox checkResultField;

	protected UploadWorkTaskContentComponent uploadContent;

	public GatherUploadWorkForm(Task task) {
		super(task);
		this.projectParticipantService = ViewToolManager
				.getBean("projectParticipantService");
	}

	@Override
	public void notifyRelatedContentChanged() {

	}

	@Override
	protected void initPromptTitle() {
		setFormHelp(i18nManager.getMessage(Messages.TASK_FORM_UPLOAD_HELP));
	}

	@Override
	protected void initUpWorkContent() {
		// 作品上传
		uploadContent = new UploadWorkTaskContentComponent(this.getTask().getId(),
				this, false);
		addComponent(uploadContent);
	}

	@Override
	protected void initRelatedContent() {
		relatedContent = new MyTaskRelatedContentComponent(this.getTask(),
				this, true);
		addComponent(relatedContent);
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

	}

	@Override
	protected void initActions() {
		submitFormButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -6091586145870618870L;

			public void buttonClick(ClickEvent event) {
				Map<String, Object> formProperties = null;
				try {
					OutsourcingProject outsourcingProject = BinderHandler
							.getFieldGroupBean(fieldGroup);
					
					formProperties = projectManagerService.submitUploadWork(outsourcingProject,getTask());

					// 通知外边的调用任务,任务相关信息已经保存
					fireEvent(new SubmitEvent(GatherUploadWorkForm.this,
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
				fireEvent(new FormPropertiesEvent(GatherUploadWorkForm.this,
						FormPropertiesEvent.TYPE_CANCEL));
				submitFormButton.setComponentError(null);
			}
		});
	}

	
}
