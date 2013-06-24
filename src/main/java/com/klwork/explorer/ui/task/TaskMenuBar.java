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

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoggedInUser;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.custom.ToolBar;
import com.klwork.explorer.ui.custom.ToolbarEntry;
import com.klwork.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
import com.klwork.explorer.ui.custom.ToolbarPopupEntry;
import com.klwork.explorer.ui.task.data.ArchivedListQuery;
import com.klwork.explorer.ui.task.data.InboxListQuery;
import com.klwork.explorer.ui.task.data.InvolvedListQuery;
import com.klwork.explorer.ui.task.data.QueuedListQuery;
import com.klwork.explorer.ui.task.data.TasksListQuery;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * The menu bar which is shown when 'Tasks' is selected in the main menu.
 * 
 * @author Joram Barrez
 * @author Frederik Heremans
 */
public class TaskMenuBar extends ToolBar {
  
  private static final long serialVersionUID = 1L;
  
  public static final String ENTRY_TASKS = "tasks";
  public static final String ENTRY_INBOX = "inbox";
  public static final String ENTRY_QUEUED = "queued";
  public static final String ENTRY_INVOLVED = "involved";
  public static final String ENTRY_ARCHIVED = "archived";
  
  protected transient IdentityService identityService;
  protected I18nManager i18nManager;
  
  public TaskMenuBar() {
    this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
    //this.viewManager = ExplorerApp.get().getViewManager();
    this.i18nManager = ViewToolManager.getI18nManager();
    //WW_TODO x代办任务、我的任务,已归档
    initItems();
  //WW_TODO x新任务
    initActions();
  }
  
  protected void initItems() {
    setWidth("100%");

    // TODO: the counts should be done later by eg a Refresher component

    // Inbox 代办任务s
    long inboxCount = new InboxListQuery().size(); 
    ToolbarEntry inboxEntry = addToolbarEntry(ENTRY_INBOX, i18nManager.getMessage(Messages.TASK_MENU_INBOX), new ToolbarCommand() {
      public void toolBarItemSelected() {
    	  ViewToolManager.getMainView().showInboxPage();
      }
    });
    inboxEntry.setCount(inboxCount);
    
    // 我的Tasks
    LoggedInUser user = LoginHandler.getLoggedInUser();
    long tasksCount = new TasksListQuery().size(); 
    ToolbarEntry tasksEntry = addToolbarEntry(ENTRY_TASKS, i18nManager.getMessage(Messages.TASK_MENU_TASKS), new ToolbarCommand() {
      public void toolBarItemSelected() {
    	  ViewToolManager.getMainView().showTasksPage();
      }
    });
    tasksEntry.setCount(tasksCount);
    
    // 队列
    List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
    ToolbarPopupEntry queuedItem = addPopupEntry(ENTRY_QUEUED, (i18nManager.getMessage(Messages.TASK_MENU_QUEUED)));
    long queuedCount = 0;
    for (final Group group : groups) {
      if (group.getType().equals("assignment")) {
        long groupCount = new QueuedListQuery(group.getId()).size(); 
        
        queuedItem.addMenuItem(group.getName() + " (" + groupCount + ")", new ToolbarCommand() {
          public void toolBarItemSelected() {
            //viewManager.showQueuedPage(group.getId());
        	  ViewToolManager.getMainView().showQueuedPage(group.getId());
          }
        });
        
        queuedCount += groupCount;
      }
    }
    queuedItem.setCount(queuedCount);
    
    // Involved 受邀
    long involvedCount = new InvolvedListQuery().size(); 
    ToolbarEntry involvedEntry = addToolbarEntry(ENTRY_INVOLVED, i18nManager.getMessage(Messages.TASK_MENU_INVOLVED), new ToolbarCommand() {
      public void toolBarItemSelected() {
    	  ViewToolManager.getMainView().showInvolvedPage();
        //viewManager.showInvolvedPage();
      }
    });
    involvedEntry.setCount(involvedCount);
    
    // Archived
    //WW_TODO 菜单-已归档
    long archivedCount = new ArchivedListQuery().size(); 
    ToolbarEntry archivedEntry = addToolbarEntry(ENTRY_ARCHIVED, i18nManager.getMessage(Messages.TASK_MENU_ARCHIVED), new ToolbarCommand() {
      public void toolBarItemSelected() {
        //viewManager.showArchivedPage();
    	ViewToolManager.getMainView().showArchivedPage();
      }
    });
    archivedEntry.setCount(archivedCount);
  }
  
  protected void initActions() {
    Button newCaseButton = new Button();
    newCaseButton.setCaption(i18nManager.getMessage(Messages.TASK_NEW));
    //WW_TODO Fix
    newCaseButton.setIcon(Images.TASK_16);
    addButton(newCaseButton);
    
    newCaseButton.addClickListener(new ClickListener() {
      public void buttonClick(ClickEvent event) {
        NewTaskPopupWindow newTaskPopupWindow = new NewTaskPopupWindow();
        ViewToolManager.showPopupWindow(newTaskPopupWindow);
      }
    });
  }
  
}
