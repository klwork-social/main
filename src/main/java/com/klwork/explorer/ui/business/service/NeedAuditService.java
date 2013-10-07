package com.klwork.explorer.ui.business.service;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import com.klwork.explorer.ui.task.TaskForm;

@Service("crowdsourcing-needCheck-form")
public class NeedAuditService extends AbstractTaskFormHandleService{

	@Override
	public TaskForm create(Task task) {
		return new com.klwork.explorer.ui.business.flow.act.NeedAuditForm(task);
	}
	
}
