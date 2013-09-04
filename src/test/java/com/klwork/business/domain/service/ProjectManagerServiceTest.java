package com.klwork.business.domain.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.ProjectParticipant;
import com.klwork.business.domain.model.Todo;
import com.klwork.explorer.security.LoggedInUserImpl;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

public class ProjectManagerServiceTest extends BaseTxWebTests {
	@Autowired
	ProjectService projectService;

	@Autowired
	TodoService todoService;
	@Autowired
	RuntimeService runtimeService;

	@Autowired
	ProjectManagerService projectManagerService;

	@Autowired
	IdentityService identityService;

	@Autowired
	TaskService taskService;

	@Test
	public void testAll() {
		String starter = "ww";
		userLogin(starter);
		Todo todo = todoService.newTodo("新任务");
		todoService.createTodo(todo);
		ProcessInstance processInstance = projectManagerService
				.startProcessInstanceRelateTodo(todo.getId(),
						"klwork-crowdsourcing-act");

		// 个人任务
		Task task = taskService.createTaskQuery().taskAssignee(starter)
				.singleResult();
		assertNotNull(task);
		taskService.claim(task.getId(), starter);
		
		
		OutsourcingProject o = queryProjectOfTask(task);
		o.setBounty(333d);
		o.setName("莫名公司外包");
		o.setDescription("需求描叙");
		o.setDeadline(new Date());
		
		// 完成填写需求的任务
		IdentityLinkEntity checker = new IdentityLinkEntity();
		String checkUserId = "ww_management";
		checker.setUserId(checkUserId);
		Map<String, Object> variables = projectManagerService.submitPublishNeed(o,task,checker);
		taskService.complete(task.getId(), variables);
		
		//
		userLogin(checkUserId);
		//进行任务的审核，进行通过操作
		task = taskService.createTaskQuery().taskAssignee(checkUserId).singleResult();
		assertNotNull(task);
		taskService.claim(task.getId(), checkUserId);
		Map<String, Object> formProperties = projectManagerService.submitNeedAudit(o,"true");
		//初审通过
		taskService.complete(task.getId(), formProperties);//默认生成一条上传的任务,留给管理员
				
		userLogin("ww_user2");
		//
		OutsourcingProject project = queryProjectOfTask(task);
		//ww手动进行参与
		projectManagerService.participateProject(project);
		//默认的会生成几个管理任务，如果 1个+手动触发的一个
		List<Task> tasks = listTaskByProcessInsId(processInstance);
		assertEquals(2, tasks.size());
		
		userLogin("ww");
		Task wwtask = taskService.createTaskQuery().taskAssignee("ww").singleResult();
		formProperties = projectManagerService.submitUploadWork(queryProjectOfTask(task), wwtask);
		taskService.complete(wwtask.getId(),formProperties);
		userLogin("ww_user2");
		Task wwtask2 = taskService.createTaskQuery().taskAssignee("ww_user2").singleResult();
		formProperties = projectManagerService.submitUploadWork(queryProjectOfTask(task), wwtask2);
		taskService.complete(wwtask2.getId(),formProperties);
		
		
		//对每个任务进行审核
		userLogin("ww_management");
		List<Task> list = taskService.createTaskQuery().taskAssignee("ww_management").list();
		assertEquals(2, list.size());
		
		Task first = list.get(0);
		ProjectParticipant pt1 = projectManagerService.queryProjectScoreOfTask(first);
		
		Task seconed = list.get(1);
		ProjectParticipant pt2 = projectManagerService.queryProjectScoreOfTask(seconed);
		
		assertFalse(pt1.getAssessedTaskId().equals(pt2.getAssessedTaskId()));
				
	}

	public OutsourcingProject queryProjectOfTask(Task task) {
		OutsourcingProject project = projectManagerService.getRelateOutSourceingProject(task);
		return project;
	}

	public void userLogin(String authenticatedUserId) {
		User user = identityService.createUserQuery()
				.userId(authenticatedUserId).singleResult();
		LoggedInUserImpl loggedInUser = new LoggedInUserImpl(user, "asdsf");
		identityService.setAuthenticatedUserId(authenticatedUserId);
		LoginHandler.setUser(loggedInUser);
	}
	
	private void compleCurrentWork(String cUID) {
		Task wwtaskx = taskService.createTaskQuery().taskAssignee(cUID).singleResult();
		taskService.complete(wwtaskx.getId() );
	}

	private List<Task> listTaskByProcessInsId(ProcessInstance processInstance) {
		List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstance.getId())
                .orderByTaskName()
                .asc()
                .list();
		return tasks;
	}


}
