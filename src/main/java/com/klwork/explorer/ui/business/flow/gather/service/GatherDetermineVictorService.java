package com.klwork.explorer.ui.business.flow.gather.service;

import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import com.klwork.explorer.ui.business.service.AbstractTaskFormHandleService;
import com.klwork.explorer.ui.task.TaskForm;

@Service("gather-determineVictor-form")
public class GatherDetermineVictorService extends AbstractTaskFormHandleService {
	@Override
	public TaskForm create(Task task) {
		return new com.klwork.explorer.ui.business.flow.gather.form.GatherDetermineVictorForm(
				task);
	}
}
