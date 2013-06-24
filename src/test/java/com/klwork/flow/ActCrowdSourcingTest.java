/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.klwork.flow;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;

/**
 * @author ww
 */
public class ActCrowdSourcingTest extends AbstractKlworkTestCase {
	// 测试主要流程
	@Deployment(resources = { "act-crowdsourcing-test.bpmn20.xml" })
	public void testMaiFlow() {
		ProjectManagerService  projectManagerService = ViewToolManager
				.getBean("projectManagerService");
		// 测试部署的文件
		String deploymentId = repositoryService.createDeploymentQuery()
				.singleResult().getId();
		List<String> deploymentResources = repositoryService
				.getDeploymentResourceNames(deploymentId);
		// verify bpmn file name sdfsdf
		// com/klwork/flow/act-test.bpmn20.xml,
		// com/klwork/flow/act-test.process.png
		//assertEquals(1, deploymentResources.size());

		
		// ww 启动流程
		String[] assignmentGroups = new String[] {"entrepreneur","user","management"};//企业主
	    for (String groupId : assignmentGroups) {
	      createGroup(groupId, "assignment");
	    }
	    
		createUser("ww", "ww", "The Frog", "ww", "ww@activiti.org", 
	            null,
	            Arrays.asList( "entrepreneur", "user"),
	            Arrays.asList("birthDate", "10-10-1955", "jobTitle", "Muppet", "location", "Hollywoord",
	                          "phone", "+123456789", "twitterName", "alfresco", "skype", "activiti_kermit_frog"));
		createUser("ww_management", "xx", "yy", "ww_management", "ww_management@activiti.org", 
	            null,
	            Arrays.asList( "management", "user"),
	            Arrays.asList("birthDate", "10-10-1955", "jobTitle", "Muppet", "location", "Hollywoord",
	                          "phone", "+123456789", "twitterName", "alfresco", "skype", "activiti_kermit_frog"));
		
		
		createUser("ww_user", "xx", "yy", "ww_user", "ww_user@activiti.org", 
	            null,
	            Arrays.asList("user"),
	            Arrays.asList("birthDate", "10-10-1955", "jobTitle", "Muppet", "location", "Hollywoord",
	                          "phone", "+123456789", "twitterName", "alfresco", "skype", "activiti_kermit_frog"));
		createUser("ww_user2", "xx2", "yy2", "ww_user2", "ww_user2@activiti.org", 
	            null,
	            Arrays.asList("user"),
	            Arrays.asList("birthDate", "10-10-1955", "jobTitle", "Muppe2t", "location", "Hollywoord",
	                          "phone", "+123456789", "twitterName", "alfr2esco", "skype", "activiti_kermit_frog"));
		
		//谁开始出现化流程和流程实例,原来是一个线程变量,ww开始流程
		identityService.setAuthenticatedUserId("ww");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("klwork-crowdsourcing-act");
		Map<String, Object> variableMap = new HashMap<String, Object>();
		//个人任务
		Task task = taskService.createTaskQuery().taskAssignee("ww").singleResult();
		assertNotNull(task);
		taskService.claim(task.getId(), "ww");
		//UserTaskXMLConverter
		
		//完成填写需求的任务
		Map<String, Object> variables = new HashMap<String, Object>();
		String projectId = "klwork.com";
		variables.put("outsourcingProjectId", projectId);
		//流程变量
		variables.put("checker", "ww_user2");
		taskService.complete(task.getId(), variables );
		
		
		//以ww_user2登录
		identityService.setAuthenticatedUserId("ww_user2");
		//管理员进行任务的审核，进行通过操作
		task = taskService.createTaskQuery().taskAssignee("ww_user2").singleResult();
		
		assertNotNull(task);
		taskService.claim(task.getId(), "ww_user2");
		Map<String, String> formProperties = new HashMap<String, String>();
		//初审通过
		formProperties.put("needPreChecked", "true");
		//完成一个任务
		formService.submitTaskFormData(task.getId(), formProperties);
		
		
		identityService.setAuthenticatedUserId("ww");
		formProperties = new HashMap<String, String>();
		formProperties.put("outsourcingProjectId",
				projectId);
		formProperties.put("claimUserId",
				 "ww");
		taskService.currentNewInstanceByKey(processInstance.getProcessInstanceId(),"uploadWork",formProperties);
		//默认的会生成几个管理任务，如果 1个+手动触发的一个
		List<Task> tasks = listTaskByProcessInsId(processInstance);
		assertEquals(2, tasks.size());
		
		
		
		//ww进行提交,上传资料
		Task wwtask = taskService.createTaskQuery().taskAssignee("ww").singleResult();
		taskService.complete(wwtask.getId() );
		
		Task wwtask2 = taskService.createTaskQuery().taskAssignee("ww_user2").singleResult();
		taskService.complete(wwtask2.getId() );
		
		
		
		/*//管理员公布需求
		task = taskService.createTaskQuery().taskCandidateGroup("management").singleResult();
		assertNotNull(task);
		taskService.claim(task.getId(), "ww_management");
		//一群人进行任务的提交,时间过期则进行销毁操作,只要是用户组的都可以进行领取
		taskService.complete(task.getId() );
		
		
		//两个评审任务
		tasks = listTaskByProcessInsId(processInstance);
		assertEquals(2,tasks.size());
		//task = taskService.createTaskQuery().taskAssignee("ww_user2").singleResult();
		//assertNotNull(task);
		compleCurrentWork("ww_management");
		compleCurrentWork("ww");
		//公布获奖名单
		task = taskService.createTaskQuery().taskCandidateGroup("management").singleResult();
		assertNotNull(task);
		taskService.claim(task.getId(), "ww_management");
		//一群人进行任务的提交,时间过期则进行销毁操作,只要是用户组的都可以进行领取
		taskService.complete(task.getId() );
		
		//检查流程是否完成
		assertProcessEnded(processInstance.getId());*/
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
