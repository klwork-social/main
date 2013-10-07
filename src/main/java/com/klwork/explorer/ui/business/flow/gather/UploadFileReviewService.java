package com.klwork.explorer.ui.business.flow.gather;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.service.ProjectParticipantService;
import com.klwork.explorer.ViewToolManager;

public class UploadFileReviewService implements JavaDelegate {
	private static ProjectParticipantService projectParticipantService = ViewToolManager
			.getBean("projectParticipantService");
	
	@Override
	public void execute(DelegateExecution execution) {
		String outsourcingProjectId = (String) execution.getVariable(EntityDictionary.OUTSOURCING_PROJECT_ID);
		String upLoadtaskId = (String) execution.getVariable(EntityDictionary.UP_LOADTASK_ID);
		String teamId = (String) execution.getVariable(EntityDictionary.GRADE_TEAM);
		execution.setVariable("gradeTeam", teamId);
		//保存到本地一份
		execution.setVariableLocal(EntityDictionary.UP_LOADTASK_ID, upLoadtaskId);
	}
}
