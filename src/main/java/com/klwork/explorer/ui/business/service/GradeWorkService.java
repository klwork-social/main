package com.klwork.explorer.ui.business.service;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.business.domain.service.ProjectParticipantService;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.task.TaskForm;

@Service("crowdsourcing-gradeWork-form")
public class GradeWorkService extends AbstractTaskFormHandleService{
	@Autowired
	ProjectParticipantService projectParticipantService;
	@Autowired
	ProjectManagerService projectManagerService;
	@Autowired
	RuntimeService runtimeService;
	
	@Override
	public TaskForm create(Task task) {
		return new com.klwork.explorer.ui.business.flow.act.GradeWorkForm(task);
	}
	
	@Override
	public void afterClaim(Task task){
		OutsourcingProject outsourcingProject = projectManagerService.getRelateOutSourceingProject(task);
		String userId = LoginHandler.getLoggedInUser().getId();
		Object assessedTaskId = runtimeService.getVariable(task.getExecutionId(), EntityDictionary.UP_LOADTASK_ID);
		
		projectManagerService.createProjectParticipantByParamInclueAssTask(outsourcingProject, userId, EntityDictionary.PARTICIPANTS_TYPE_SCORER,task.getId(),assessedTaskId.toString());
		//当前人，做为上个任务的评分者
		//projectParticipantService.createProjectParticipantByParamInclueAssTask(outsourcingProject, userId, EntityDictionary.PARTICIPANTS_TYPE_SCORER,task.getId(),assessedTaskId.toString());
	}
	
}
