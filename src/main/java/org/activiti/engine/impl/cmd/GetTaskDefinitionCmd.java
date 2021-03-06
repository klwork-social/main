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

package org.activiti.engine.impl.cmd;

import java.io.Serializable;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

/**
 * @author Tom Baeyens
 */
public class GetTaskDefinitionCmd implements Command<TaskDefinition>,
		Serializable {

	private static final long serialVersionUID = 1L;
	protected Task task;

	public GetTaskDefinitionCmd(Task task) {
		this.task = task;
	}

	public TaskDefinition execute(CommandContext commandContext) {
		String processDefinitionId = task.getProcessDefinitionId();
		ProcessDefinitionEntity processDefinition = Context
				.getProcessEngineConfiguration().getDeploymentManager()
				.findDeployedProcessDefinitionById(processDefinitionId);
		if (processDefinition == null) {
			throw new ActivitiObjectNotFoundException(
					"No process definition found for id '"
							+ processDefinitionId + "'",
					ProcessDefinition.class);
		}
	
		TaskDefinition taskDefinition = processDefinition.getTaskDefinitions()
				.get(task.getTaskDefinitionKey());

		return taskDefinition;
	}
}
