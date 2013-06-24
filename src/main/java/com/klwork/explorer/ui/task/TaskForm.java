package com.klwork.explorer.ui.task;

import org.activiti.engine.task.Task;

import com.vaadin.ui.VerticalLayout;

public abstract class TaskForm extends VerticalLayout {
	private Task currentTask;

	public void setTask(Task task) {
		currentTask = task;
	}

	public Task getTask() {
		return currentTask;
	}

	public abstract void notifyRelatedContentChanged();

	@Override
	public void attach() {
		initUi();
	}

	protected void initUi() {

	}
}
