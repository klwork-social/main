package com.klwork.explorer.ui.task;

import org.activiti.engine.history.HistoricTaskInstance;

import com.vaadin.ui.VerticalLayout;

public abstract class TaskHistoryForm extends VerticalLayout {
	private HistoricTaskInstance  currentTask;

	public void setTask(HistoricTaskInstance task) {
		currentTask = task;
	}

	public HistoricTaskInstance getTask() {
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
