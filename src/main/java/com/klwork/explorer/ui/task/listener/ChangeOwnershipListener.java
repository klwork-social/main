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

package com.klwork.explorer.ui.task.listener;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.task.Task;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.custom.SelectUsersPopupWindow;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.task.TaskDetailPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


/**
 * @author Joram Barrez
 */
public class ChangeOwnershipListener implements ClickListener {

  private static final long serialVersionUID = 1L;
  
  protected Task task;
  protected TaskDetailPanel taskDetailPanel;
  protected I18nManager i18nManager;
  
  public ChangeOwnershipListener(Task task, TaskDetailPanel taskDetailPanel) { // changeAssigne == false -> changing owner
    this.task = task;
    this.taskDetailPanel = taskDetailPanel;
    this.i18nManager = ViewToolManager.getI18nManager();
  }
  
  public void buttonClick(ClickEvent event) {
    
    List<String> ignoredIds = null;
    if (task.getOwner() != null) {
      ignoredIds = Arrays.asList(task.getOwner());
    }
    
    final SelectUsersPopupWindow involvePeoplePopupWindow = 
        new SelectUsersPopupWindow(i18nManager.getMessage(Messages.TASK_OWNER_TRANSFER), false, ignoredIds);
    
    involvePeoplePopupWindow.addListener(new SubmitEventListener() {
      private static final long serialVersionUID = 1L;

      protected void submitted(SubmitEvent event) {
        // Update owner
        String selectedUser = involvePeoplePopupWindow.getSelectedUserId();
        task.setOwner(selectedUser);
        ProcessEngines.getDefaultProcessEngine().getTaskService().setOwner(task.getId(), selectedUser);
        
        // Update UI
        taskDetailPanel.notifyOwnerChanged();
      }
      protected void cancelled(SubmitEvent event) {
      }
    });
    
    ViewToolManager.showPopupWindow(involvePeoplePopupWindow);
  }
  
}
