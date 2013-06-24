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
package com.klwork.explorer.ui.business.flow.act;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.service.OutsourcingProjectService;
import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.business.flow.ExecutionHandler;

/**
 * @author Frederik Heremans
 */
public class ActTaskAssignmentListener implements TaskListener {

	private static final long serialVersionUID = 1L;
	private static OutsourcingProjectService outsourcingProjectService = ViewToolManager
			.getBean("outsourcingProjectService");
	private static ProjectManagerService projectManagerService = ViewToolManager
			.getBean("projectManagerService");
	
	public void notify(DelegateTask delegateTask) {
		delegateTask.setDescription("TaskAssignmentListener is listening: "
				+ delegateTask.getAssignee());
		String userId = ExecutionHandler.getVar(delegateTask,EntityDictionary.CLAIM_USER_ID);
		String outsourcingProjectId = ExecutionHandler.getVar(delegateTask,EntityDictionary.OUTSOURCING_PROJECT_ID);
		
		if (userId != null) {
			System.out.println("外部claimUserId:" + userId);
			delegateTask.setAssignee((String) userId);
		} else {
			OutsourcingProject p = outsourcingProjectService.findOutsourcingProjectById(outsourcingProjectId);
			if(p != null){
				System.out.println("项目参与人:" + p.getOwnUser());
				delegateTask.setAssignee(p.getOwnUser());
				userId = p.getOwnUser();
			}
		}
		projectManagerService.addNewParticipate(outsourcingProjectId, userId);
		saveAuthToVariable(delegateTask, userId.toString());
	}


	private void saveAuthToVariable(DelegateTask delegateTask,
			String authenticatedUserId) {
		delegateTask.setVariableLocal(EntityDictionary.CLAIM_USER_ID,
				authenticatedUserId);
		delegateTask.getExecution().setVariableLocal(EntityDictionary.CLAIM_USER_ID,
				authenticatedUserId);
	}
	
	
}
