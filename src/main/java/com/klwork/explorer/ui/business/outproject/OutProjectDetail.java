package com.klwork.explorer.ui.business.outproject;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class OutProjectDetail extends DetailPanel {

	protected transient IdentityService identityService;
	protected transient TaskService taskService;
	protected transient HistoryService historyService;
	protected I18nManager i18nManager;
	
	//实体对象
	protected HistoricTaskInstance historicTask;
	
	//UI
	protected VerticalLayout panelLayout;
	protected boolean editingDetails;
	protected HorizontalLayout detailLayout;
	protected GridLayout detailsGrid;
	protected TextField nameTextField;
	protected ComboBox typeCombobox;
	protected HorizontalLayout membersLayout;
	protected Table membersTable;
	protected Label noMembersTable;
	protected VerticalLayout centralLayout;
	
	
	public OutProjectDetail(HistoricTaskInstance historicTaskInstance) {
		this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
		this.historyService = ProcessEngines.getDefaultProcessEngine().getHistoryService();
		this.historicTask = historicTaskInstance;
	}

	@Override
	protected void initUI() {
		setSizeFull();
		//addStyleName(Reindeer.PANEL_LIGHT);
		addStyleName("social");
		addStyleName(Reindeer.LAYOUT_WHITE);
		centralLayout = new VerticalLayout();
	    centralLayout.setMargin(true);
	    setDetailContainer(centralLayout);
	    initForm();
	}

	private void initForm() {
		OutProjectNeedForm c = new OutProjectNeedForm(historicTask);
		centralLayout.addComponent(c);
	}
}
