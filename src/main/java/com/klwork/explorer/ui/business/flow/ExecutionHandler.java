package com.klwork.explorer.ui.business.flow;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;

public class ExecutionHandler {
	public static <T> T getLoopVariable(ActivityExecution execution,
			String variableName) {
		Object value = execution.getVariableLocal(variableName);
		ActivityExecution parent = execution.getParent();
		while (value == null && parent != null) {
			value = parent.getVariableLocal(variableName);
			parent = parent.getParent();
		}
		return (T) value;
	}
	
	public static String getVar(DelegateTask delegateTask,String variableName) {
		return getLoopVariable((ExecutionEntity)delegateTask.getExecution(),variableName);
	}
}
