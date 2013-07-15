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
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.StringTool;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.NotificationManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.custom.PrettyTimeLabel;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.form.FormPropertiesEvent;
import com.klwork.explorer.ui.form.FormPropertiesEventListener;
import com.klwork.explorer.ui.form.FormPropertiesForm;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.listener.ClaimTaskClickListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;


/**
 * The central panel on the task page, showing all the details of a task.
 * 
 * @author Joram Barrez
 */
public class TaskDetailPanel extends DetailPanel {
  
  private static final long serialVersionUID = 1L;

  protected Task task;
  
  //protected TaskEntity taskEntity;
  TaskDefinition taskDefinition;
  
  // Services
  protected transient TaskService taskService;
  protected transient FormService formService;
  protected transient RepositoryService repositoryService;
  protected transient TeamService teamService;
  protected I18nManager i18nManager;
  protected NotificationManager notificationManager;
  
  // UI
  protected TaskPage taskPage;
  protected VerticalLayout centralLayout;
  protected FormPropertiesForm taskForm;
  protected TaskInvolvedPeopleComponent involvedPeople;
  protected SubTaskComponent subTaskComponent;
  protected TaskRelatedContentComponent relatedContent;
  protected Button completeButton;
  protected Button claimButton;
  private boolean showEvent = false;
  
  public TaskDetailPanel(Task task, TaskPage taskPage) {
	showEvent = taskPage.isShowEvents();
    this.task = task;
    this.taskPage = taskPage;
    this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
    this.formService = ProcessEngines.getDefaultProcessEngine().getFormService();
    this.repositoryService = ProcessEngines.getDefaultProcessEngine().getRepositoryService();
    teamService = (TeamService) SpringApplicationContextUtil.getContext()
			.getBean("teamService");
    this.i18nManager = ViewToolManager.getI18nManager();
    this.notificationManager = ViewToolManager.getNotificationManager();
    if( task != null && task instanceof TaskEntity && task.getProcessDefinitionId() != null){
    	taskDefinition = taskService.queryTaskDefinition(task);
    }
  }
  
  @Override
  public void attach() {
    super.attach();
    init();
  }
  
  protected void init() {
    setSizeFull();
    addStyleName("social");
    addStyleName(Reindeer.LAYOUT_WHITE);
    
    // Central panel: all task data
    this.centralLayout = new VerticalLayout();
    centralLayout.setMargin(true);
    setDetailContainer(centralLayout);
    
    initHeader();
    if(judgeInnerTask()){
	    //时间描写和优先级描叙
	    initDescriptionAndClaimButton();
	    //任务属于流程
	    initProcessLink();
    }
    if(judgeInnerTask()){//非外部任务
	    //当前任务为某个任务的子任务
	    initParentTaskLink();
	    //任务参与者
	    initPeopleDetails();
	    //子任务
	    initSubTasks();
    
    //关联的任务的内容
    initRelatedContent();
    }
    //任务的orm
    initTaskForm();
    
  }

public boolean judgeInnerTask() {
	if(taskDefinition == null)
		return true;
	return taskDefinition !=null && (!"out".equals(taskDefinition.getInType()));
}
  
  protected void initHeader() {
    GridLayout taskDetails = new GridLayout(2, 2);
    taskDetails.setWidth(100, Unit.PERCENTAGE);
    taskDetails.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
    taskDetails.setSpacing(true);
    taskDetails.setMargin(new MarginInfo(false, false, true, false));
    taskDetails.setColumnExpandRatio(1, 1.0f);
    centralLayout.addComponent(taskDetails);
    
    // Add image
    Embedded image = new Embedded(null, Images.TASK_50);
    //占据两行
    taskDetails.addComponent(image, 0, 0, 0, 1);
    
    // Add task name
    Label nameLabel = new Label(task.getName());
    nameLabel.addStyleName(Reindeer.LABEL_H2);
    taskDetails.addComponent(nameLabel, 1, 0);
    
    /*Label nameLabel2 = new Label(task.getId());
    nameLabel2.addStyleName(Reindeer.LABEL_H2);
    taskDetails.addComponent(nameLabel2, 2, 0);
    taskDetails.setComponentAlignment(nameLabel2, Alignment.MIDDLE_RIGHT);*/

    // Properties
    HorizontalLayout propertiesLayout = new HorizontalLayout();//满了，往下排
    propertiesLayout.setSpacing(true);
    taskDetails.addComponent(propertiesLayout);
    
    propertiesLayout.addComponent(new DueDateComponent(task, i18nManager, taskService));
    propertiesLayout.addComponent(new PriorityComponent(task, i18nManager, taskService));
    //创建于多少之前
    initCreateTime(propertiesLayout);
    initTaskNo(propertiesLayout);
    initShowEvent(propertiesLayout);
  }
  
  private void initTaskNo(HorizontalLayout propertiesLayout) {
	  Label nameLabel2 = new Label("任务编号:" + task.getId());
	  //nameLabel2.addStyleName(Reindeer.LABEL_SMALL);
	  propertiesLayout.addComponent(nameLabel2);
  }

private String getShowEventButtonTitle() {
	  if(showEvent)
		  return "隐藏任务留言";
	  return "显示任务留言";
  }
  private void initShowEvent(HorizontalLayout propertiesLayout) {
	final Button updateSave = new Button(getShowEventButtonTitle());
		updateSave.addClickListener(new ClickListener() {
			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) {
				showEvent = !showEvent;
				taskPage.setEventHiden(showEvent);
				updateSave.setCaption(getShowEventButtonTitle());
				
			}
		}
			);
	  propertiesLayout.addComponent(updateSave);
	  
  }

protected void initCreateTime(HorizontalLayout propertiesLayout) {
    PrettyTimeLabel createLabel = new PrettyTimeLabel(
            i18nManager.getMessage(Messages.TASK_CREATED_SHORT), task.getCreateTime(), "", true);
    createLabel.addStyleName(ExplorerLayout.STYLE_TASK_HEADER_CREATE_TIME);
    propertiesLayout.addComponent(createLabel);
  }
  
  protected void initDescriptionAndClaimButton() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
    layout.setWidth(100, Unit.PERCENTAGE);
    layout.setSpacing(true);
    centralLayout.addComponent(layout);
    
    initClaimButton(layout);
    initDescription(layout);
  }

  protected void initClaimButton(HorizontalLayout layout) {
	  //任务的指定人不是当前人，candidateUser为当前人
    if(!isCurrentUserAssignee() && canUserClaimTask()) {
      claimButton = new Button(i18nManager.getMessage(Messages.TASK_CLAIM));
      claimButton.addClickListener(new ClaimTaskClickListener(task.getId(), taskService));
      layout.addComponent(claimButton);
      layout.setComponentAlignment(claimButton, Alignment.MIDDLE_LEFT);
      
    }
  }

  protected void initDescription(HorizontalLayout layout) {
    final CssLayout descriptionLayout = new CssLayout();
    descriptionLayout.setWidth(100, Unit.PERCENTAGE);
    layout.addComponent(descriptionLayout);
    layout.setExpandRatio(descriptionLayout, 1.0f);
    layout.setComponentAlignment(descriptionLayout, Alignment.MIDDLE_LEFT);

    String descriptionText = null;
    if (task.getDescription() != null && !"".equals(task.getDescription())) {
      descriptionText = task.getDescription();
    } else {//此任务没有描述的
      descriptionText = i18nManager.getMessage(Messages.TASK_NO_DESCRIPTION);
    }
    final Label descriptionLabel = new Label(descriptionText);
    descriptionLabel.addStyleName(ExplorerLayout.STYLE_CLICKABLE);
    descriptionLayout.addComponent(descriptionLabel);
    
    descriptionLayout.addLayoutClickListener(new LayoutClickListener() {
      public void layoutClick(LayoutClickEvent event) {
        if (event.getClickedComponent() != null && event.getClickedComponent().equals(descriptionLabel)) {
          // layout for textarea + ok button
          final VerticalLayout editLayout = new VerticalLayout();
          editLayout.setSpacing(true);
          
          // textarea
          final TextArea descriptionTextArea = new TextArea();
          //设置为空时的显示类容
          descriptionTextArea.setNullRepresentation("");
          descriptionTextArea.setWidth(100, Unit.PERCENTAGE);
          descriptionTextArea.setValue(task.getDescription());
          editLayout.addComponent(descriptionTextArea);
          
          // ok button
          Button okButton = new Button(i18nManager.getMessage(Messages.BUTTON_OK));
          editLayout.addComponent(okButton);
          editLayout.setComponentAlignment(okButton, Alignment.BOTTOM_RIGHT);
          
          // replace
          descriptionLayout.replaceComponent(descriptionLabel, editLayout);
          
          // When OK is clicked -> update task data + ui
          okButton.addClickListener(new ClickListener() {
            public void buttonClick(ClickEvent event) {
              // Update data
              task.setDescription(descriptionTextArea.getValue().toString());
              taskService.saveTask(task);
              
              // Update UI
              descriptionLabel.setValue(task.getDescription());
              descriptionLayout.replaceComponent(editLayout, descriptionLabel);
            }
          });
        }
      }
    });
  }

  protected void initProcessLink() {
    if(task.getProcessInstanceId() != null) {
      ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
        .processDefinitionId(task.getProcessDefinitionId())
        .singleResult();
      
      Button showProcessInstanceButton = new Button(i18nManager.getMessage(
        Messages.TASK_PART_OF_PROCESS, getProcessDisplayName(processDefinition)));
      showProcessInstanceButton.addStyleName(Reindeer.BUTTON_LINK);
      showProcessInstanceButton.addClickListener(new ClickListener() {
        public void buttonClick(ClickEvent event) {
          //viewManager.showMyProcessInstancesPage(task.getProcessInstanceId());
        }
      });
     
      centralLayout.addComponent(showProcessInstanceButton);
      addEmptySpace(centralLayout);
    }
  }
  
  protected String getProcessDisplayName(ProcessDefinition processDefinition) {
    if(processDefinition.getName() != null) {
      return processDefinition.getName();
    } else {
      return processDefinition.getKey();
    }
  }
  
  protected void initParentTaskLink() {
    if (task.getParentTaskId() != null) {
      final Task parentTask = taskService.createTaskQuery()
        .taskId(task.getParentTaskId()).singleResult();
      
      Button showParentTaskButton = new Button(i18nManager.getMessage(
              Messages.TASK_SUBTASK_OF_PARENT_TASK, parentTask.getName()));
      showParentTaskButton.addStyleName(Reindeer.BUTTON_LINK);
      showParentTaskButton.addClickListener(new ClickListener() {
        public void buttonClick(ClickEvent event) {
          //viewManager.showTaskPage(parentTask.getId());
        }
      });
      
      centralLayout.addComponent(showParentTaskButton);
      addEmptySpace(centralLayout);
    }
  }
  
  protected void initPeopleDetails() {
   involvedPeople = new TaskInvolvedPeopleComponent(task, this);
    centralLayout.addComponent(involvedPeople);
  }
  
  
  protected void initSubTasks() {
   subTaskComponent = new SubTaskComponent(task);
    centralLayout.addComponent(subTaskComponent);
  }
  
  protected void initRelatedContent() {
    relatedContent = new TaskRelatedContentComponent(task, this);
    centralLayout.addComponent(relatedContent);
  }
  
  protected void initTaskForm() {
    // Check if task requires a form
	  //task.getT
    TaskFormData formData = formService.getTaskFormData(task.getId());
    
    if(formData != null && StringTool.judgeBlank(formData.getFormKey())){
    	TaskForm c = TaskFormFactory.create(formData.getFormKey(),task);
    	centralLayout.addComponent(c);
    	c.addListener(new SubmitEventListener() {
            private static final long serialVersionUID = -3893467157397686736L;
            
            @Override
			protected void submitted(SubmitEvent event) {
              //流程变量，也提交来了
              Map<String, Object> properties = (Map<String, Object>)event.getData();
              System.out.println(properties);
              taskService.complete(task.getId(), properties);
              notificationManager.showInformationNotification(Messages.TASK_COMPLETED, task.getName());
              taskPage.refreshSelectNext();
            }
          

			@Override
			protected void cancelled(SubmitEvent event) {
				 taskForm.clear();
			}
          });
    	
    	return;
    }
    //formService.getS
    if(formData != null && formData.getFormProperties() != null && formData.getFormProperties().size() > 0) {
      taskForm = new FormPropertiesForm();
      taskForm.setSubmitButtonCaption(i18nManager.getMessage(Messages.TASK_COMPLETE));
      taskForm.setCancelButtonCaption(i18nManager.getMessage(Messages.TASK_RESET_FORM));
      taskForm.setFormHelp(i18nManager.getMessage(Messages.TASK_FORM_HELP));
      taskForm.setFormProperties(formData.getFormProperties());
      //WW_TODO 任务form监听器
      taskForm.addListener(new FormPropertiesEventListener() {
        private static final long serialVersionUID = -3893467157397686736L;
        
        @Override
        protected void handleFormSubmit(FormPropertiesEvent event) {
          //流程变量，也提交来了
          Map<String, String> properties = event.getFormProperties();
          formService.submitTaskFormData(task.getId(), properties);
          notificationManager.showInformationNotification(Messages.TASK_COMPLETED, task.getName());
          taskPage.refreshSelectNext();
        }
        
        @Override
        protected void handleFormCancel(FormPropertiesEvent event) {
          // Clear the form values 
          taskForm.clear();
        }
      });
      // Only if current user is task's assignee
      taskForm.setEnabled(isCurrentUserAssignee());
      
      // Add component to page
      centralLayout.addComponent(taskForm);
    } else {
      // Just add a button to complete the task
      // TODO: perhaps move to a better place
      
      CssLayout buttonLayout = new CssLayout();
      buttonLayout.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
      buttonLayout.setWidth(100, Unit.PERCENTAGE);
      centralLayout.addComponent(buttonLayout);
      
      completeButton = new Button(i18nManager.getMessage(Messages.TASK_COMPLETE));
      
      completeButton.addClickListener(new ClickListener() {
        
        private static final long serialVersionUID = 1L;

        public void buttonClick(ClickEvent event) {
          // If no owner, make assignee owner (will go into archived then)
          if (task.getOwner() == null) {
            task.setOwner(task.getAssignee());
            taskService.setOwner(task.getId(), task.getAssignee());
          }
          
          taskService.complete(task.getId());     
          notificationManager.showInformationNotification(Messages.TASK_COMPLETED, task.getName());
          taskPage.refreshSelectNext();
        }
      });
      
      completeButton.setEnabled(isCurrentUserAssignee() || isCurrentUserOwner());
      buttonLayout.addComponent(completeButton);
    }
  }

  protected boolean isCurrentUserAssignee() {
    String currentUser = LoginHandler.getLoggedInUser().getId();
    return currentUser.equals(task.getAssignee());
  }
  
  protected boolean isCurrentUserOwner() {
    String currentUser = LoginHandler.getLoggedInUser().getId();
    return currentUser.equals(task.getOwner());
  }
  
  protected boolean canUserClaimTask() {
   String userId = LoginHandler.getLoggedInUser().getId();
   boolean userCandidate =  taskService.createTaskQuery()
     .taskCandidateUser(userId)
     .taskId(task.getId())
     .count() == 1; 
   	List<String> groups = teamService.queryTeamsOfUser(userId);
    boolean groupCandidate =  taskService.createTaskQuery()
    		.taskCandidateGroupIn(groups)
    	     .taskId(task.getId())
    	     .count() == 1; 
    return userCandidate || groupCandidate;
  }
  
  protected void addEmptySpace(ComponentContainer container) {
    Label emptySpace = new Label("&nbsp;", ContentMode.HTML);
    emptySpace.setSizeUndefined();
    container.addComponent(emptySpace);
  }
  
  public void notifyPeopleInvolvedChanged() {
    involvedPeople.refreshPeopleGrid();
    taskPage.getTaskEventPanel().refreshTaskEvents();
  }
  
  public void notifyAssigneeChanged() {
    if (LoginHandler.getLoggedInUser().getId().equals(task.getAssignee())) { // switch view to inbox if assignee is current user
      //viewManager.showInboxPage(task.getId());
    } else {
     involvedPeople.refreshAssignee();
      taskPage.getTaskEventPanel().refreshTaskEvents();
    }
  }
  
  public void notifyOwnerChanged() {
    if (LoginHandler.getLoggedInUser().getId().equals(task.getOwner())) { // switch view to tasks if owner is current user
      //viewManager.showTasksPage(task.getId());
    } else {
     involvedPeople.refreshOwner();
      taskPage.getTaskEventPanel().refreshTaskEvents();
    }
  }
  
  public void notifyRelatedContentChanged() {
    relatedContent.refreshTaskAttachments();
    taskPage.getTaskEventPanel().refreshTaskEvents();
  }
  
}
