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
package com.klwork.explorer.ui.task.data;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.task.TaskQuery;

import com.klwork.business.domain.service.TeamService;
import com.klwork.explorer.ViewToolManager;

/**
 * The Class TeamTaskListQuery.
 */
public class TeamTaskListQuery extends AbstractTaskListQuery {
	protected transient TeamService teamService;
	private List<String> teams;
	
	public TeamTaskListQuery(List<String> teams) {
		if(teams == null || teams.isEmpty() ){
			teams = new ArrayList<String>();
			teams.add("-99");
		}
		this.teams = teams;
		 teamService =ViewToolManager. getBean("teamService");
	}

	@Override
	protected TaskQuery getQuery() {
		//act_ru_identitylink中的组当成team来使用
		return taskService.createTaskQuery()
				.taskCandidateGroupIn(teams).taskUnassigned().orderByTaskId().asc();
	}

}
