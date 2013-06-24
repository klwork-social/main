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

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Picture;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.util.time.HumanTime;
import com.vaadin.event.Action;
import com.vaadin.event.Action.Handler;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.StreamResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * Component containing all events for a given task.
 * 
 * @author Joram Barrez
 */
public class TaskEventsPanel extends Panel {
  
  private static final long serialVersionUID = 1L;
  
  protected transient IdentityService identityService;
  protected transient TaskService taskService; 
  protected I18nManager i18nManager;
  protected ViewManager viewManager;
  protected TaskEventTextResolver taskEventTextResolver;
  
  protected VerticalLayout pMainContent;
  protected String taskId;
  protected List<org.activiti.engine.task.Event> taskEvents;
  protected TextField commentInputField;
  protected GridLayout eventGrid;

  public TaskEventsPanel() {
	pMainContent = new VerticalLayout();
	this.setContent(pMainContent);
    this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
    this.identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
    this.i18nManager = ViewToolManager.getI18nManager();
   
    this.taskEventTextResolver = new TaskEventTextResolver();
    
    pMainContent.setSpacing(true);
    pMainContent.setMargin(true);
    setHeight(100, Unit.PERCENTAGE);
    pMainContent.setHeight(100, Unit.PERCENTAGE);
    addStyleName(ExplorerLayout.STYLE_TASK_EVENT_PANEL);
    
    addTitle();
    addInputField();
    initEventGrid();
    addTaskEvents();
  }
  

  public void refreshTaskEvents() {
    eventGrid.removeAllComponents();
    addTaskEvents();
  }
  
  /**
   * Set the task this component is showing the events for. Triggers
   * an update of the UI.
   */
  public void setTaskId(String taskId) {
    this.taskId = taskId;
    refreshTaskEvents();
  }
  
  protected void addTitle() {
    Label eventTitle = new Label(i18nManager.getMessage(Messages.EVENT_TITLE));
    eventTitle.addStyleName(Reindeer.LABEL_H2);
    pMainContent.addComponent(eventTitle);
  }
  
  protected void initEventGrid() {
    eventGrid = new GridLayout();
    eventGrid.setColumns(2);
    eventGrid.setSpacing(true);
    eventGrid.setMargin(new MarginInfo(true, false, false, false));
    eventGrid.setWidth("100%");
    eventGrid.setColumnExpandRatio(1, 1.0f);
    eventGrid.addStyleName(ExplorerLayout.STYLE_TASK_EVENT_GRID);
    
    pMainContent.addComponent(eventGrid);
    pMainContent.setExpandRatio(eventGrid, 1.0f);
  }
  
  protected void addTaskEvents() {
    if(taskId != null) {
      //WW_TODO 查询任务的实践
      taskEvents = taskService.getTaskEvents(taskId);
      for (final org.activiti.engine.task.Event event : taskEvents) {
        addTaskEventPicture(event, eventGrid);
        addTaskEventText(event, eventGrid);
      }
    }
  }

  protected void addTaskEventPicture(final org.activiti.engine.task.Event taskEvent, GridLayout eventGrid) {
    final Picture userPicture = identityService.getUserPicture(taskEvent.getUserId());
    Embedded authorPicture = null;
    
    if (userPicture != null) {
      /*StreamResource imageresource = new StreamResource(new StreamSource() {
        private static final long serialVersionUID = 1L;
        public InputStream getStream() {
          return userPicture.getInputStream();
        }
      }, "event_" + taskEvent.getUserId() + "." + Constants.MIMETYPE_EXTENSION_MAPPING.get(userPicture.getMimeType()), ExplorerApp.get());
      authorPicture = new Embedded(null, imageresource);*/
    } else {
      authorPicture = new Embedded(null, Images.USER_50);
    }
    
    authorPicture.setType(Embedded.TYPE_IMAGE);
    authorPicture.setHeight("48px");
    authorPicture.setWidth("48px");
    authorPicture.addStyleName(ExplorerLayout.STYLE_TASK_EVENT_PICTURE);
    eventGrid.addComponent(authorPicture);
  }
  
  protected void addTaskEventText(final org.activiti.engine.task.Event taskEvent, final GridLayout eventGrid) {
    VerticalLayout layout = new VerticalLayout();
    layout.addStyleName(ExplorerLayout.STYLE_TASK_EVENT);
    layout.setWidth("100%");
    eventGrid.addComponent(layout);
    
    // Actual text
    Label text = taskEventTextResolver.resolveText(taskEvent);
    text.setWidth("100%");
    layout.addComponent(text);
    
    // Time
    Label time = new Label(new HumanTime(i18nManager).format(taskEvent.getTime()));
    time.setSizeUndefined();
    time.addStyleName(ExplorerLayout.STYLE_TASK_EVENT_TIME);
    layout.addComponent(time);
    
  }
  
  protected void addInputField() {
    HorizontalLayout hLayout = new HorizontalLayout();
    hLayout.setSpacing(true);
    hLayout.setWidth(100, Unit.PERCENTAGE);
    pMainContent.addComponent(hLayout);
    //加入事件源
    initAddEventInput(hLayout);
    
    Button addCommentButton = new Button(i18nManager.getMessage(Messages.TASK_ADD_COMMENT));
    hLayout.addComponent(addCommentButton);
    hLayout.setComponentAlignment(addCommentButton, Alignment.MIDDLE_LEFT);
    addCommentButton.addListener(new ClickListener() {
      public void buttonClick(ClickEvent event) {
        addNewComment(commentInputField.getValue().toString());
      }
    });
  }


public void initAddEventInput(HorizontalLayout hLayout) {
	Panel textFieldPanel = new Panel(); // Hack: actionHandlers can only be attached to panels or windows
    textFieldPanel.addStyleName(Reindeer.PANEL_LIGHT);
    VerticalLayout textFieldPanelLayout = new VerticalLayout();
	textFieldPanel.setContent(textFieldPanelLayout);
	
	
    textFieldPanel.setWidth(100, Unit.PERCENTAGE);
    hLayout.addComponent(textFieldPanel);
    hLayout.setExpandRatio(textFieldPanel, 1.0f);
    
    commentInputField = new TextField();
    commentInputField.setWidth(100, Unit.PERCENTAGE);
    textFieldPanelLayout.addComponent(commentInputField);
    
    // Hack to catch keyboard 'enter'
    textFieldPanel.addActionHandler(new Handler() {
      public void handleAction(Action action, Object sender, Object target) {
        addNewComment(commentInputField.getValue().toString());
      }
      public Action[] getActions(Object target, Object sender) {
        return new Action[] {new ShortcutAction("enter", ShortcutAction.KeyCode.ENTER, null)};
      }
    });
}
  
  protected void addNewComment(String text) {
    taskService.addComment(taskId, null, text);
    refreshTaskEvents();
    commentInputField.setValue("");
    commentInputField.focus();
  }

}
