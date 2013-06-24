package com.klwork.explorer.ui.business.flow.act;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.klwork.business.domain.service.OutsourcingProjectService;

public class WorkCheckService implements JavaDelegate {
	protected transient OutsourcingProjectService outsourcingProjectService;
	
	@Override
	public void execute(DelegateExecution execution) {
		String taskId = (String) execution.getVariable("taskId");
		String outsourcingProjectId = (String) execution.getVariable("outsourcingProjectId");
		//outsourcingProjectService.findOutsourcingProjectById(outsourcingProjectId);
		//检查通过
		execution.setVariable("workChecked",true);
		System.out.println("检查通过");
		
	}
}
