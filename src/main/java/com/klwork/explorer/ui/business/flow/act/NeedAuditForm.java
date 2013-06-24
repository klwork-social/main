package com.klwork.explorer.ui.business.flow.act;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.form.FormPropertiesEvent;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;


public class NeedAuditForm extends PublishNeedForm {
	public ComboBox checkResultField;
	public NeedAuditForm(Task task) {
		super(task);
	}

	@Override
	public void notifyRelatedContentChanged() {

	}
	@Override
	protected void initRelatedContent() {
		relatedContent = new MyTaskRelatedContentComponent(
				this.getTask(), this,true);
		addComponent(relatedContent);
	}
	@Override
	protected void initFormPropertiesComponent() {
		//绑定数据和初始grid布局
		initFormLayoutAndData();
		//项目名称
		initProjectName(true);
		
		//截止时间
		initDeadline(true);
		
		//悬赏金额
		initBounty(true);
		
		//需求类型
		initNeedType(true);
		
		//需求描叙
		initDescription(true);

		//同意还是不同意
		initCheckResult();
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
						
					formProperties = projectManagerService.submitNeedAudit(outsourcingProject,checkResultField.getValue());
					// 通知外边的调用任务,任务相关信息已经保存
					fireEvent(new SubmitEvent(NeedAuditForm.this,
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
				fireEvent(new FormPropertiesEvent(NeedAuditForm.this,
						FormPropertiesEvent.TYPE_CANCEL));
				submitFormButton.setComponentError(null);
			}
		});
	}

	
	private void initCheckResult() {
		Label checkResultLabel = new Label("你同意这次请求吗?");
		taskDetails.addComponent(checkResultLabel,0, 9);
		initLabelOfGrid(taskDetails, checkResultLabel);
		Map<String, String> data = new HashMap();
		data.put("1", "通过");
		data.put("0", "不通过");
		checkResultField = CommonFieldHandler.createComBox(null,
				data, "1");
		checkResultField.setRequired(true);
		taskDetails.addComponent(checkResultField, 1, 9);
		//fieldGroup.bind(reqTypeLabelField, "type");
	}
}
