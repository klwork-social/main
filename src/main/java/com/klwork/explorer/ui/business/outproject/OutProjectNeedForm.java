package com.klwork.explorer.ui.business.outproject;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.explorer.ui.business.flow.act.MyTaskRelatedContentComponent;
import com.klwork.explorer.ui.business.flow.act.NeedAuditForm;
import com.klwork.explorer.ui.business.flow.act.PublishNeedForm;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.form.FormPropertiesEvent;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;

public class OutProjectNeedForm extends PublishNeedForm {
	public ComboBox checkResultField;
	public OutProjectNeedForm(Task task) {
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

	}
	
	@Override
	protected void initButtons() {
		
	}
	
	
	@Override
	protected void initActions() {
	}


}
