package com.klwork.business.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.OutsourcingProject;
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
	TaskService taskService;
	
	@Before
	public void initBefer() {
		intTestUser();
	}
	
	/**
	 * 测试一个流程
	 */
	@Test
	public void testBaseFlow() {
		String starter = TEST_USER1;
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
		o.setBounty(5000d);
		o.setName("一个外包5000元的外包项目");
		o.setDescription("需求描叙");
		//最后期限
		o.setDeadline(new Date());
		
		// 完成填写需求的任务
		IdentityLinkEntity checker = new IdentityLinkEntity();
		String checkUserId = TEST_USER2;
		checker.setUserId(checkUserId);
		//sales来进行评分
		Map<String, Object> variables = projectManagerService.submitPublishNeed(o,task,checker,"develop");
		taskService.complete(task.getId(), variables);
		
		//
		userLogin(checkUserId);
		//进行任务的审核，进行通过操作
		task = taskService.createTaskQuery().taskAssignee(checkUserId).singleResult();
		assertNotNull(task);
		taskService.claim(task.getId(), checkUserId);
		Map<String, Object> formProperties = projectManagerService.submitNeedAudit(o,"true");
		//初审通过
		taskService.complete(task.getId(), formProperties);
		
		
		userLogin(TEST_USER5);//接项目
		//
		OutsourcingProject project = queryProjectOfTask(task);
		//手动进行参与
		projectManagerService.participateProject(project);
		//默认的会生成几个管理任务，如果 1个+手动触发的一个
		List<Task> tasks = listTaskByProcessInsId(processInstance);
		assertEquals(2, tasks.size());
		
       
		userLogin(TEST_USER1);
		Task wwtask = taskService.createTaskQuery().taskAssignee(TEST_USER1).singleResult();
		formProperties = projectManagerService.submitUploadWork(queryProjectOfTask(task), wwtask);
		taskService.complete(wwtask.getId(),formProperties);
		
		userLogin(TEST_USER5);
		Task wwtask2 = taskService.createTaskQuery().taskAssignee(TEST_USER5).singleResult();
		formProperties = projectManagerService.submitUploadWork(queryProjectOfTask(task), wwtask2);
		taskService.complete(wwtask2.getId(),formProperties);
		
		
		//对每个任务进行审核,审核的任务会流转到开发组
		userLogin("TEST_USER2");
		taskService.createTaskQuery();
		List<String> teams = new ArrayList();
		teams.add("develop");
		List<Task> list = taskService.createTaskQuery().taskCandidateGroupIn(teams).taskUnassigned().orderByTaskId().asc().list();
		//List<Task> list = taskService.createTaskQuery().taskAssignee("develop").list();
		assertEquals(2, list.size());
		
		/*Task first = list.get(0);
		ProjectParticipant pt1 = projectManagerService.queryProjectScoreOfTask(first);
		
		Task seconed = list.get(1);
		ProjectParticipant pt2 = projectManagerService.queryProjectScoreOfTask(seconed);
		
		assertFalse(pt1.getAssessedTaskId().equals(pt2.getAssessedTaskId()));*/
				
	}
	
	/**
	 * 测试普通一个流程
	 */
	@Test
	public void testGatherFlow() {
		String starter = TEST_USER1;
		userLogin(starter);
		ProcessInstance processInstance = projectManagerService
				.startProcessInstance(
						"klwork-gather-act");

		// 个人任务
		Task task = taskService.createTaskQuery().taskAssignee(starter)
				.singleResult();
		assertNotNull(task);
		taskService.claim(task.getId(), starter);
		
		
		OutsourcingProject o = queryProjectOfTask(task);
		o.setBounty(5000d);
		o.setName("一个外包5000元的外包项目");
		o.setDescription("需求描叙");
		//最后期限
		o.setDeadline(new Date());
		
		// 完成填写需求的任务
	
		//sales来进行评分
		Map<String, Object> variables = projectManagerService.submitPublishNeed(o,task,null,"develop");
		taskService.complete(task.getId(), variables);
		
		//

		
		
		userLogin(TEST_USER5);//接项目
		//
		OutsourcingProject project = queryProjectOfTask(task);
		//手动进行参与
		projectManagerService.participateProject(project);
		//默认的会生成几个管理任务，如果 1个+手动触发的一个
		List<Task> tasks = listTaskByProcessInsId(processInstance);
		assertEquals(2, tasks.size());
		
       
		userLogin(TEST_USER1);
		Task wwtask = taskService.createTaskQuery().taskAssignee(TEST_USER1).singleResult();
		Map<String, Object> formProperties = projectManagerService.submitUploadWork(queryProjectOfTask(task), wwtask);
		taskService.complete(wwtask.getId(),formProperties);
		
		userLogin(TEST_USER5);
		Task wwtask2 = taskService.createTaskQuery().taskAssignee(TEST_USER5).singleResult();
		formProperties = projectManagerService.submitUploadWork(queryProjectOfTask(task), wwtask2);
		taskService.complete(wwtask2.getId(),formProperties);
		
		
		//对每个任务进行审核,审核的任务会流转到开发组
		userLogin("TEST_USER2");
		taskService.createTaskQuery();
		List<String> teams = new ArrayList();
		teams.add("develop");
		List<Task> list = taskService.createTaskQuery().taskCandidateGroupIn(teams).taskUnassigned().orderByTaskId().asc().list();
		//List<Task> list = taskService.createTaskQuery().taskAssignee("develop").list();
		assertEquals(2, list.size());
				
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
