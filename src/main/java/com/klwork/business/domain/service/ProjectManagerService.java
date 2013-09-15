package com.klwork.business.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.OutsourcingProjectQuery;
import com.klwork.business.domain.model.ProjectParticipant;
import com.klwork.business.domain.model.ProjectParticipantQuery;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.business.flow.act.PublishNeedForm;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class ProjectManagerService {
	@Autowired
	ProjectParticipantService projectParticipantService;

	@Autowired
	OutsourcingProjectService outsourcingProjectService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	/**
	 * 查询任务的审核人
	 * 
	 * @return
	 */
	public IdentityLink queryAuditorOfInstance(String processInstanceId) {
		List<IdentityLink> identityLinks = runtimeService
				.getIdentityLinksForProcessInstance(processInstanceId);
		for (final IdentityLink identityLink : identityLinks) {// 审核类型的用户
			if (EntityDictionary.IDENTITY_LINK_TYPE_AUDITOR.equals(identityLink
					.getType())) {
				return identityLink;
			}
		}
		return null;
	}

	/**
	 * 提交需求
	 * 
	 * @param outsourcingProject
	 * @param task
	 * @return
	 */
	public Map<String, Object> submitPublishNeed(
			OutsourcingProject outsourcingProject, Task task,
			IdentityLink identityLinkChecker) {
		String processInstanceId = task.getProcessInstanceId();
		Map<String, Object> formProperties;
		// IdentityLink identityLinkChecker =
		// queryAuditorOfInstance(processInstanceId);
		formProperties = new HashMap<String, Object>();
		if (identityLinkChecker == null) {// 没有审核人进行直接发布
			outsourcingProject
					.setPrgStatus(EntityDictionary.OUTSOURCING_STATUS_PUBLISHED);
		} else {
			outsourcingProject
					.setPrgStatus(EntityDictionary.OUTSOURCING_STATUS_PUBLISHING);
		}

		outsourcingProject.setProcInstId(processInstanceId);
		outsourcingProject.setOwnUser(task.getAssignee());
		outsourcingProjectService.updateOutsourcingProject(outsourcingProject);

		// 设置审核人
		String checker = (identityLinkChecker != null) ? identityLinkChecker
				.getUserId() : "";
		formProperties.put(EntityDictionary.CHECKER_USER_ID, checker);
		formProperties.put(EntityDictionary.OUTSOURCING_PROJECT_ID,
				outsourcingProject.getId());
		return formProperties;
	}

	/**
	 * 开始一个流程,并关联todoId
	 * 
	 * @param todoId
	 * @param processKey
	 * @return
	 */
	public ProcessInstance startProcessInstanceRelateTodo(String todoId,
			String processKey) {
		/*
		 * processDefinition = repositoryService
		 * .getProcessDefinition("klwork-crowdsourcing-act:1:71609");
		 * ProcessInstance processInstance = runtimeService
		 * .startProcessInstanceById(processDefinition.getId());
		 */
		ProcessInstance processInstance = runtimeService
				.startProcessInstanceByKey(processKey);
		// 新建一个业务对象
		OutsourcingProject outsourcingProject = new OutsourcingProject();
		outsourcingProject.setProcInstId(processInstance.getId());
		outsourcingProject.setOwnUser(LoginHandler.getLoggedInUser().getId());
		// 关联的todo对象
		outsourcingProject.setRelatedTodo(todoId);
		outsourcingProjectService.createOutsourcingProject(outsourcingProject);
		return processInstance;
	}

	/**
	 * 提交审核
	 * 
	 * @param outsourcingProject
	 * @param checkResultStr
	 * @return
	 */
	public Map<String, Object> submitNeedAudit(
			OutsourcingProject outsourcingProject, Object checkResultStr) {
		Map<String, Object> formProperties = new HashMap<String, Object>();
		// 新创建一个，为了修改
		OutsourcingProject newOut = new OutsourcingProject();
		newOut.setId(outsourcingProject.getId());
		boolean needPreChecked = StringTool.parseBoolean("" + checkResultStr);
		if (needPreChecked) {// 审核通过进行直接发布
			newOut.setPrgStatus(EntityDictionary.OUTSOURCING_STATUS_PUBLISHED);
		}
		outsourcingProjectService.updateOutsourcingProject(newOut);
		formProperties.put(EntityDictionary.NEED_PRE_CHECKED, needPreChecked
				+ "");
		formProperties.put(EntityDictionary.OUTSOURCING_PROJECT_ID,
				outsourcingProject.getId() + "");

		return formProperties;
	}

	/**
	 * 增加一个新的参与人
	 * 
	 * @param outsourcingProjectId
	 * @param userId
	 */
	public void addNewParticipate(String outsourcingProjectId, String userId) {
		String participantsTypeUser = EntityDictionary.PARTICIPANTS_TYPE_USER;
		projectParticipantService.addProjectParticipantByParam(
				outsourcingProjectId, userId, participantsTypeUser, null);
	}

	/**
	 * 参与项目
	 * 
	 * @param project
	 */
	public void participateProject(OutsourcingProject project) {
		// WW_TODO 参与到本项目1
		// 当前用户
		String userId = LoginHandler.getLoggedInUser().getId();

		// 开始一个新的子流程
		Map<String, String> formProperties = new HashMap<String, String>();
		/*
		 * formProperties.put(EntityDictionary.OUTSOURCING_PROJECT_ID,
		 * project.getId());
		 */
		formProperties.put(EntityDictionary.CLAIM_USER_ID, userId);
		taskService.currentNewInstanceByKey(project.getProcInstId(),
				"uploadWork", formProperties);
	}

	/**
	 * 提交作品
	 * 
	 * @param outsourcingProject
	 * @param task
	 * @return
	 */
	public Map<String, Object> submitUploadWork(
			OutsourcingProject outsourcingProject, Task task) {
		Map<String, Object> formProperties;
		formProperties = new HashMap<String, Object>();
		//
		formProperties.put(EntityDictionary.OUTSOURCING_PROJECT_ID,
				outsourcingProject.getId());
		// 把任务id传给下一个流程
		String cTaskId = task.getId();
		formProperties.put(EntityDictionary.UP_LOADTASK_ID, cTaskId);
		// 保存附件
		updateParticipantSubmitStatus(outsourcingProject, task);
		return formProperties;
	}

	private void updateParticipantSubmitStatus(
			OutsourcingProject outsourcingProject, Task task) {
		String userId = LoginHandler.getLoggedInUser().getId();
		String participantsType = EntityDictionary.PARTICIPANTS_TYPE_USER;
		ProjectParticipantQuery query = new ProjectParticipantQuery();
		// 每个用户，在普通用户,只有一个(没个用户只能参与一次)
		query.setOutPrgId(outsourcingProject.getId()).setUserId(userId)
				.setParticipantsType(participantsType)
				.setProcInstId(task.getProcessInstanceId());
		ProjectParticipant old = projectParticipantService
				.findOneByQuery(query);

		ProjectParticipant n = new ProjectParticipant();
		n.setId(old.getId());
		n.setCurrentTaskId(task.getId());
		n.setHandleStatus(EntityDictionary.PARTICIPANTS_STATUS_UPLOADED);
		projectParticipantService.updateProjectParticipant(n);
	}

	/**
	 * 提交作品打分
	 * 
	 * @param p
	 * @return
	 */
	public Map<String, Object> submitGradeWork(ProjectParticipant p, Task task) {
		Map<String, Object> formProperties = new HashMap<String, Object>();
		p.setHandleStatus(EntityDictionary.PARTICIPANTS_STATUS_SCORED);
		projectParticipantService.updateProjectParticipant(p);
		return formProperties;
	}

	/**
	 * 查询任务相关的项目对象
	 * 
	 * @param task
	 * @return
	 */
	public OutsourcingProject getRelateOutSourceingProject(Task task) {
		String processInstanceId = task.getProcessInstanceId();
		return queryOutProjectByProInsId(processInstanceId);
	}
	
	/**
	 * 查询流程相关的实体
	 * @param processInstanceId
	 * @return
	 */
	public OutsourcingProject queryOutProjectByProInsId(String processInstanceId) {
		OutsourcingProjectQuery query = new OutsourcingProjectQuery();
		query.setProcInstId(processInstanceId);
		OutsourcingProject ret = outsourcingProjectService
				.findOneEntityByQuery(query);
		if (ret != null)
			return ret;
		return new OutsourcingProject();
	}
	
	//查询任务评分者
	public ProjectParticipant queryProjectScoreOfTask(Task task) {
		OutsourcingProject outsourcingProject = getRelateOutSourceingProject(task);
		String userId = LoginHandler.getLoggedInUser().getId();
		String participantsType = EntityDictionary.PARTICIPANTS_TYPE_SCORER;
		//WW_TODO 怎么从流程变量得到值
		Object assessedTaskId = runtimeService.getVariable(task.getExecutionId(), EntityDictionary.UP_LOADTASK_ID);
		//System.out.println("查询出的任务 id: " + assessedTaskId);
		if(assessedTaskId == null){
			return null;
		}
		ProjectParticipantQuery query = new ProjectParticipantQuery();
		query.setOutPrgId(outsourcingProject.getId()).setUserId(userId)
				.setParticipantsType(participantsType).setAssessedTaskId(assessedTaskId.toString()).setProcInstId(task.getProcessInstanceId());
		ProjectParticipant r = projectParticipantService.findOneByQuery(query);
		return r;
	}
}