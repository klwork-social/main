package com.klwork.explorer.ui.business.outproject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.model.TeamMembership;
import com.klwork.business.domain.service.TeamMembershipService;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.business.query.TeamMemberQuery;
import com.klwork.explorer.ui.custom.ConfirmationDialogPopupWindow;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.custom.SelectUsersPopupWindow;
import com.klwork.explorer.ui.event.ConfirmationEvent;
import com.klwork.explorer.ui.event.ConfirmationEventListener;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.TaskForm;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class OutProjectDetail extends DetailPanel {

	
	protected transient IdentityService identityService;
	protected transient TaskService taskService;
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
	protected VerticalLayout centralLayout;
	public OutProjectDetail() {
		this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
	}

	@Override
	public void attach() {
		super.attach();
		init();
	}

	private void init() {
		setSizeFull();
		//addStyleName(Reindeer.PANEL_LIGHT);
		addStyleName(Reindeer.LAYOUT_WHITE);
		centralLayout = new VerticalLayout();
	    centralLayout.setMargin(true);
	    setDetailContainer(centralLayout);
	    initForm();
	}

	private void initForm() {
		Task task = taskService.createTaskQuery().taskId("424").singleResult();
		TaskForm c = new OutProjectNeedForm(task);
		centralLayout.addComponent(c);
	}
}
