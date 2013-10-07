package com.klwork.explorer.ui.business.service;

import org.activiti.engine.task.Task;

import com.klwork.explorer.ui.task.TaskForm;

public interface TaskFormHandleService {
	
	/**
	 * 创建一个from对象
	 * 
	 * @param task
	 * @return
	 */
	public TaskForm create(Task task);

	/**
	 * 领取任务后的其他操作
	 * 
	 * @param taskId
	 */
	public void afterClaim(Task task);
	
}
