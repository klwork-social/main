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
package com.klwork.explorer.ui.task;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;

import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.task.data.ArchivedListQuery;
import com.vaadin.ui.Component;

/**
 * The page displaying all archived cases for the logged in user.
 * 
 * @author Joram Barrez
 */
public class ArchivedPage extends TaskPage {
  
  private static final long serialVersionUID = 1L;
  
  public ArchivedPage() {
  }
  
  /**
   * Constructor called when page is accessed straight through the url, eg. /task/123
   */
  public ArchivedPage(String taskId) {
    super(taskId);
  }
  
  @Override
  protected LazyLoadingQuery createLazyLoadingQuery() {
    return new ArchivedListQuery();
  }
  
  @Override
  protected Component createDetailComponent(String id) {
    HistoryService historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
    HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(id).singleResult();
    taskEventPanel.setTaskId(historicTaskInstance.getId());
    return new HistoricTaskDetailPanel(historicTaskInstance, this);
  }

  
}
