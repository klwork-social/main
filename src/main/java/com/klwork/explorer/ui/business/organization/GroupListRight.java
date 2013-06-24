package com.klwork.explorer.ui.business.organization;

import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.business.organization.GroupListLeft.MemberCountColumnGenerator;
import com.klwork.explorer.ui.business.query.TeamMemberQuery;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class GroupListRight extends DetailPanel {
	protected Team team;
	protected TeamService teamService;
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

	public GroupListRight(String teamId) {
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
		team = teamService.findTeamById(teamId);
		this.i18nManager = ViewToolManager.getI18nManager();
	}

	@Override
	public void attach() {
		super.attach();
		init();
	}

	private void init() {
		setSizeFull();
		addStyleName(Reindeer.PANEL_LIGHT);

		initPageTitle();
		initGroupDetails();
		initMembers();

		initActions();
	}

	protected void initActions() {
		Button createGroupButton = new Button(
				i18nManager.getMessage(Messages.GROUP_CREATE));
		createGroupButton.setIcon(Images.GROUP_16);

	}

	protected void initMembers() {
		HorizontalLayout membersHeader = new HorizontalLayout();
		membersHeader.setSpacing(true);
		membersHeader.setWidth(100, Unit.PERCENTAGE);
		membersHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addDetailComponent(membersHeader);

		initMembersTitle(membersHeader);
		initAddMembersButton(membersHeader);

		membersLayout = new HorizontalLayout();
		membersLayout.setWidth(100, Unit.PERCENTAGE);
		addDetailComponent(membersLayout);
		initMembersTable();
	}
	
	protected void initMembersTable() {
	    LazyLoadingQuery query = new TeamMemberQuery(team.getId());
	    if (query.size() > 0) {
	      membersTable = new Table();
	      membersTable.setWidth(100, UNITS_PERCENTAGE);
	      membersTable.setHeight(400, UNITS_PIXELS);
	      
	      membersTable.setEditable(false);
	      membersTable.setSelectable(false);
	      membersTable.setSortEnabled(true);
	      
	      LazyLoadingContainer container = new LazyLoadingContainer(query, 10);
	      membersTable.setContainerDataSource(container);
	      
	      membersTable.addContainerProperty("id", String.class, null);
	      membersTable.addContainerProperty("firstName", String.class, null);
	      membersTable.addContainerProperty("lastName", String.class, null);
	      membersTable.addContainerProperty("email", String.class, null);
	      membersTable.addGeneratedColumn("actions", new MemberActionsColumnGenerator());
	     // membersTable.addContainerProperty("actions", Component.class, null);
	      
	      membersLayout.addComponent(membersTable);
	    } else {
	      noMembersTable = new Label(i18nManager.getMessage(Messages.GROUP_NO_MEMBERS));
	      membersLayout.addComponent(noMembersTable);
	    }
	  }
	
	public class MemberActionsColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			Embedded deleteIcon = new Embedded(null, Images.DELETE);
		      deleteIcon.addStyleName(ExplorerLayout.STYLE_CLICKABLE);
			return deleteIcon;
		}

	}
	
	protected void initMembersTitle(HorizontalLayout membersHeader) {
		Label usersHeader = new Label(
				i18nManager.getMessage(Messages.GROUP_HEADER_USERS));
		usersHeader.addStyleName(ExplorerLayout.STYLE_H3);
		membersHeader.addComponent(usersHeader);
	}

	protected void initAddMembersButton(HorizontalLayout membersHeader) {
		Button addButton = new Button();
		addButton.addStyleName(ExplorerLayout.STYLE_ADD);
		membersHeader.addComponent(addButton);
		membersHeader.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);

		addButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				
			}
		});
	}

	protected void initPageTitle() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(false, false, true, false));
		addDetailComponent(layout);

		Embedded groupImage = new Embedded(null, Images.GROUP_50);
		layout.addComponent(groupImage);

		Label groupName = new Label(team.getName());
		groupName.setSizeUndefined();
		groupName.addStyleName(Reindeer.LABEL_H2);
		layout.addComponent(groupName);
		layout.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(groupName, 1.0f);
	}

	protected void initGroupDetails() {
		Label groupDetailsHeader = new Label(
				i18nManager.getMessage(Messages.GROUP_HEADER_DETAILS));
		groupDetailsHeader.addStyleName(ExplorerLayout.STYLE_H3);
		groupDetailsHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);

		addDetailComponent(groupDetailsHeader);

		detailLayout = new HorizontalLayout();
		detailLayout.setSpacing(true);
		detailLayout.setMargin(new MarginInfo(true, false, true, false));
		addDetailComponent(detailLayout);

		populateGroupDetails();
	}

	protected void populateGroupDetails() {
		initGroupProperties();
		initGroupActions();
	}

	protected void initGroupProperties() {
		detailsGrid = new GridLayout(2, 2);
		detailsGrid.setSpacing(true);
		detailLayout.setMargin(new MarginInfo(true, true, true, false));
		detailLayout.addComponent(detailsGrid);

		// id
		Label idLabel = new Label(i18nManager.getMessage(Messages.GROUP_ID)
				+ ": ");
		idLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
		detailsGrid.addComponent(idLabel);
		Label idValueLabel = new Label(team.getId());
		detailsGrid.addComponent(idValueLabel);

		// name
		Label nameLabel = new Label(i18nManager.getMessage(Messages.GROUP_NAME)
				+ ": ");
		nameLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
		detailsGrid.addComponent(nameLabel);
		if (!editingDetails) {
			Label nameValueLabel = new Label(team.getName());
			detailsGrid.addComponent(nameValueLabel);
		} else {
			nameTextField = new TextField(null, team.getName());
			detailsGrid.addComponent(nameTextField);
		}
	}

	protected void initGroupActions() {
		VerticalLayout actionsLayout = new VerticalLayout();
		actionsLayout.setSpacing(true);
		actionsLayout.setMargin(new MarginInfo(false, false, false, true));
		detailLayout.addComponent(actionsLayout);
		if (editingDetails) {
			initSaveButton(actionsLayout);
		} else {
			initEditButton(actionsLayout);
			initDeleteButton(actionsLayout);
		}
	}

	protected void initSaveButton(VerticalLayout actionsLayout) {
		Button saveButton = new Button(
				i18nManager.getMessage(Messages.USER_SAVE));
		saveButton.addStyleName(Reindeer.BUTTON_SMALL);
		actionsLayout.addComponent(saveButton);

		saveButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {

			}
		});
	}

	protected void initDeleteButton(VerticalLayout actionsLayout) {
		Button deleteButton = new Button(
				i18nManager.getMessage(Messages.GROUP_DELETE));
		deleteButton.addStyleName(Reindeer.BUTTON_SMALL);
		actionsLayout.addComponent(deleteButton);

		deleteButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
			}
		});
	}

	protected void initEditButton(VerticalLayout actionsLayout) {
		Button editButton = new Button(
				i18nManager.getMessage(Messages.USER_EDIT));
		editButton.addStyleName(Reindeer.BUTTON_SMALL);
		actionsLayout.addComponent(editButton);

		editButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				editingDetails = true;
				detailLayout.removeAllComponents();
				populateGroupDetails();
			}
		});
	}
}
