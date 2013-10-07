package com.klwork.explorer.ui.business.outproject;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricTaskInstance;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.service.OutsourcingProjectService;
import com.klwork.business.domain.service.TeamMembershipService;
import com.klwork.business.domain.service.TeamService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTabPage;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.TaskEventsPanel;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ProjectManagerAddInDetail extends AbstractTabPage {

	protected Team team;
	protected TeamService teamService;
	protected transient IdentityService identityService;
	protected transient TeamMembershipService teamMembershipService;
	protected transient  HistoryService historyService;
	protected transient OutsourcingProjectService outsourcingProjectService;
	
	protected I18nManager i18nManager;
	protected VerticalLayout panelLayout;
	protected boolean editingDetails;
	protected HorizontalLayout detailLayout;
	protected GridLayout detailsGrid;
	protected TextField nameTextField;
	protected ComboBox typeCombobox;
	protected HorizontalLayout membersLayout;
	protected Table membersTable;
	protected Label noMembersTable;
	HistoricTaskInstance historicTaskInstance;
	private OutsourcingProject relateOutSourceingProject;
	
	
	
	public ProjectManagerAddInDetail(String id) {
		identityService = ProcessEngines.getDefaultProcessEngine()
				.getIdentityService();
		historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
		
		outsourcingProjectService = ViewToolManager
				.getBean("outsourcingProjectService");
		i18nManager = ViewToolManager.getI18nManager();
		if(id != null){
			relateOutSourceingProject = outsourcingProjectService.findOutsourcingProjectById(id);
			historicTaskInstance = historyService.createHistoricTaskInstanceQuery().processInstanceId(relateOutSourceingProject.getProcInstId()).taskDefinitionKey("publishNeed").singleResult();
		}
	}
	

	@Override
	public void initTabData() {
		OutProjectDetail detial = new OutProjectDetail(historicTaskInstance);
		addTab(detial, i18nManager.getMessage(Messages.OUTPROJECT_PAGE_DETAIL));
		TaskEventsPanel taskEventPanel = new TaskEventsPanel();
		// 只查询流程的留言
		//taskEventPanel.setProcessInstanceId(historicTaskInstance.getProcessInstanceId());
		//addTab(taskEventPanel, i18nManager.getMessage(Messages.OUTPROJECT_PAGE_COMMENT));
		//
		addTab(new WinnersListPage(relateOutSourceingProject), i18nManager.getMessage(Messages.OUTPROJECT_PAGE_WINNERLIST));
		//addTab(new OutProjectAnalysisPage(), i18nManager.getMessage(Messages.OUTPROJECT_PAGE_STATUS));
		
	}


	protected void initPageTitle() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(false, false, true, false));
		//addDetailComponent(layout);

		Embedded groupImage = new Embedded(null, Images.GROUP_50);
		layout.addComponent(groupImage);

		Label groupName = new Label(team.getName());
		groupName.setSizeUndefined();
		groupName.addStyleName(Reindeer.LABEL_H2);
		layout.addComponent(groupName);
		layout.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(groupName, 1.0f);
	}

	
}
