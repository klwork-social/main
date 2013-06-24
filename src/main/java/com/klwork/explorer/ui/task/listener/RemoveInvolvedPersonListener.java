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

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.custom.ConfirmationDialogPopupWindow;
import com.klwork.explorer.ui.event.ConfirmationEvent;
import com.klwork.explorer.ui.event.ConfirmationEventListener;
import com.klwork.explorer.ui.task.TaskDetailPanel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


/**
 * @author Joram Barrez
 */
public class RemoveInvolvedPersonListener implements ClickListener {
  
  private static final long serialVersionUID = 1L;
  protected IdentityLink identityLink;
  protected Task task;
  protected TaskDetailPanel taskDetailPanel;
  
  protected I18nManager i18nManager;
  
  protected transient IdentityService identityService;
  protected transient TaskService taskService;
  
  public RemoveInvolvedPersonListener(IdentityLink identityLink, Task task, TaskDetailPanel taskDetailPanel) {
    this.identityLink = identityLink;
    this.task = task;
    this.taskDetailPanel = taskDetailPanel;
    this.i18nManager = ViewToolManager.getI18nManager();
    this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
    this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
  }
  
  public void buttonClick(ClickEvent event) {
    User user = identityService.createUserQuery().userId(identityLink.getUserId()).singleResult();
    String name = user.getFirstName() + " " + user.getLastName();
    
    ConfirmationDialogPopupWindow confirmationPopup = new ConfirmationDialogPopupWindow(
        i18nManager.getMessage(Messages.TASK_INVOLVED_REMOVE_CONFIRMATION_TITLE, name),
        i18nManager.getMessage(Messages.TASK_INVOLVED_REMOVE_CONFIRMATION_DESCRIPTION, name, task.getName()));
    
    confirmationPopup.addListener(new ConfirmationEventListener() {
      private static final long serialVersionUID = 1L;
      protected void rejected(ConfirmationEvent event) {
      }
      protected void confirmed(ConfirmationEvent event) {
        taskService.deleteUserIdentityLink(identityLink.getTaskId(), identityLink.getUserId(), identityLink.getType());
        taskDetailPanel.notifyPeopleInvolvedChanged();
      }
    });
    ViewToolManager.showPopupWindow(confirmationPopup);
  }
  
}
