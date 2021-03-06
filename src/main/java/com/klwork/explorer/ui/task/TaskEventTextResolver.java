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

import java.io.Serializable;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;

import com.klwork.common.utils.StringTool;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;


/**
 * Helper class that resolves a task {@link Event} to a Label
 * that contains all the information in the event.
 * 
 * @author Joram Barrez
 * @author Tom Baeyens
 */
public class TaskEventTextResolver implements Serializable {
  
  private static final long serialVersionUID = -1241011503689621172L;
  protected I18nManager i18nManager;
  
  public TaskEventTextResolver() {
	  this.i18nManager = ViewToolManager.getI18nManager();
  }
  
  public Label resolveText(Event event) {
	  IdentityService identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
	  if(!StringTool.judgeBlank(event.getUserId())){//用户id没有那里来的
		  String s = i18nManager.getMessage(Messages.EVENT_DEFAULT, "未知", event.getMessage());
		 return new Label(s, ContentMode.HTML);
	  }
	  User user = identityService.createUserQuery().userId(event.getUserId()).singleResult();
    
   // User user = LoginHandler.findUser(event.getUserId());
    String eventAuthor = "<span class='" + ExplorerLayout.STYLE_TASK_EVENT_AUTHOR + "'>" 
          + user.getFirstName() + " " + user.getLastName() + "</span> ";
    
    String text = null;
  if (Event.ACTION_ADD_USER_LINK.equals(event.getAction())) {
	  User involvedUser = identityService.createUserQuery().userId(event.getMessageParts().get(0)).singleResult();
      text = i18nManager.getMessage(Messages.EVENT_ADD_USER_LINK, 
              eventAuthor, 
              involvedUser.getFirstName() + " " + involvedUser.getLastName(),
              event.getMessageParts().get(1)); // second msg part = role
    } else if (Event.ACTION_DELETE_USER_LINK.equals(event.getAction())) {
    	User involvedUser = identityService.createUserQuery().userId(event.getMessageParts().get(0)).singleResult();
      text = i18nManager.getMessage(Messages.EVENT_DELETE_USER_LINK, 
              eventAuthor, 
              involvedUser.getFirstName() + " " + involvedUser.getLastName(),
              event.getMessageParts().get(1));
    } else if (Event.ACTION_ADD_GROUP_LINK.equals(event.getAction())) {
      text = i18nManager.getMessage(Messages.EVENT_ADD_GROUP_LINK, 
              eventAuthor, 
              event.getMessageParts().get(0),
              event.getMessageParts().get(1)); // second msg part = role
    } else if (Event.ACTION_DELETE_GROUP_LINK.equals(event.getAction())) {
        text = i18nManager.getMessage(Messages.EVENT_DELETE_GROUP_LINK, 
                eventAuthor, 
                event.getMessageParts().get(0),
                event.getMessageParts().get(1)); // second msg part = role
    } else if (Event.ACTION_ADD_ATTACHMENT.equals(event.getAction())) {
      text = i18nManager.getMessage(Messages.EVENT_ADD_ATTACHMENT, eventAuthor, event.getMessage());
    } else if (Event.ACTION_DELETE_ATTACHMENT.equals(event.getAction())) {
      text = i18nManager.getMessage(Messages.EVENT_DELETE_ATTACHMENT, eventAuthor, event.getMessage());
    } else if (Event.ACTION_ADD_COMMENT.equals(event.getAction())) {
      text = i18nManager.getMessage(Messages.EVENT_COMMENT, eventAuthor, event.getMessage());
    } else if (Event.ACTION_COMPLETE_TASK.equals(event.getAction())) {//任务完成
        text = i18nManager.getMessage(Messages.EVENT_COMPLETE_TASK, eventAuthor);
      } else { // default: just show the message
      text += i18nManager.getMessage(Messages.EVENT_DEFAULT, eventAuthor, event.getMessage());
    }
    //text = "hello,baidu";
    return new Label(text, ContentMode.HTML);
  }

public Label resolveText(Comment event) {
	IdentityService identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
    User user = identityService.createUserQuery().userId(event.getUserId()).singleResult();
    String eventAuthor = "<span class='" + ExplorerLayout.STYLE_TASK_EVENT_AUTHOR + "'>" 
          + user.getFirstName() + " " + user.getLastName() + "</span> ";
    
    String text = null;
    text = i18nManager.getMessage(Messages.EVENT_COMMENT, eventAuthor, event.getFullMessage());
	return new Label(text, ContentMode.HTML);
}

}
