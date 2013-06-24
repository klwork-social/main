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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.custom.SelectUsersPopupWindow;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;


/**
 * @author Joram Barrez
 */
public class ChangeCheckerListener implements ClickListener {
  
  private static final long serialVersionUID = 1L;
  protected IdentityLink identityLink;
  protected Task task;
  protected PublishNeedForm publishNeedForm;
  
  protected I18nManager i18nManager;
  
  protected transient IdentityService identityService;
  protected transient TaskService taskService;
  
  public ChangeCheckerListener(IdentityLink identityLink, Task task, PublishNeedForm publishNeedForm) {
    this.identityLink = identityLink;
    this.task = task;
    this.publishNeedForm = publishNeedForm;
    this.i18nManager = ViewToolManager.getI18nManager();
    this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
    this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
  }
  
  public void buttonClick(ClickEvent event) {
	  List<String> ignoredIds = null;
	    if (identityLink != null) {
	      ignoredIds = Arrays.asList(identityLink.getUserId());
	    }
	    
	    final SelectUsersPopupWindow involvePeoplePopupWindow = 
	        new SelectUsersPopupWindow(i18nManager.getMessage(Messages.TASK_OWNER_TRANSFER), false, ignoredIds);
	    
	    involvePeoplePopupWindow.addListener(new SubmitEventListener() {
	      private static final long serialVersionUID = 1L;

	      protected void submitted(SubmitEvent event) {
	    	List<String> userIds = new ArrayList<String>();
	        // Update owner
	        String selectedUser = involvePeoplePopupWindow.getSelectedUserId();
	        if (identityLink != null) {
	        	ProcessEngines.getDefaultProcessEngine().getRuntimeService().deleteIdentityLink((IdentityLinkEntity) identityLink);
	        }
	        
	        if(selectedUser != null){
	        	userIds.add(selectedUser);
	        
	        ProcessEngines.getDefaultProcessEngine().getRuntimeService().addUserIdentityLink(task.getProcessInstanceId(), selectedUser, EntityDictionary.IDENTITY_LINK_TYPE_AUDITOR);
	        }
	        // Update UI
	        publishNeedForm.notifyCheckerChanged();
	      }
	      protected void cancelled(SubmitEvent event) {
	      }
	    });
	    
	    ViewToolManager.showPopupWindow(involvePeoplePopupWindow);
  }
  
}
