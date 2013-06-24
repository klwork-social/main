package com.klwork.explorer.ui.business.flow.act;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.service.ProjectParticipantService;
import com.klwork.explorer.ViewToolManager;

public class CurrentReviewService implements JavaDelegate {
	private static ProjectParticipantService projectParticipantService = ViewToolManager
			.getBean("projectParticipantService");
	
	@Override
	public void execute(DelegateExecution execution) {
		
		//String participants = (String) execution.getVariable("participants");
		String outsourcingProjectId = (String) execution.getVariable(EntityDictionary.OUTSOURCING_PROJECT_ID);
		String upLoadtaskId = (String) execution.getVariable(EntityDictionary.UP_LOADTASK_ID);
		System.out.println("上一个任务：" + upLoadtaskId);
		String participants  = "ww_management,ww";
		//找到任务的审核人进行审核
		String[] participantsArray = participants.split(",");
		List<String> assigneeList = new ArrayList<String>();
		for (String assignee : participantsArray) {
			assigneeList.add(assignee);
		}
		//execution.setVariable("reviewersList", assigneeList);
		execution.setVariable("reviewer", "ww_management");
		//保存到本地一份
		execution.setVariableLocal(EntityDictionary.UP_LOADTASK_ID, upLoadtaskId);
		//提前生成审核人
		projectParticipantService.addProjectParticipantByParam(outsourcingProjectId, "ww_management", EntityDictionary.PARTICIPANTS_TYPE_SCORER,upLoadtaskId);
		//加入审核人
	}
}
