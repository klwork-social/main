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
import org.activiti.engine.task.Event;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;

/**
 * @author ww
 */
public class ActBaseTest extends AbstractKlworkTestCase {
	// 测试主要流程
	@Deployment(resources = { "act-crowdsourcing-test.bpmn20.xml" })
	public void testMainFlow() {
		List<Event> taskEvents = taskService.getAllEvents(0, 10);
	}

	
	

}
