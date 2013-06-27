package com.klwork.explorer.ui.business.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;

import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.service.TeamMembershipService;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.business.organization.OrganMemberLeft.MemberType;
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

public class OrganMemberRight extends DetailPanel {

	protected Team team;
	protected TeamService teamService;
	protected transient IdentityService identityService;
	protected transient TeamMembershipService teamMembershipService;
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
	protected OrganizationMemberMainPage mainPage;

	public OrganMemberRight(OrganizationMemberMainPage page,Object leftParameter) {
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
		teamMembershipService = (TeamMembershipService) SpringApplicationContextUtil
				.getContext().getBean("teamMembershipService");
		if(leftParameter != null){
			String userId = LoginHandler.getLoggedInUser().getId();
			MemberType menberType = (MemberType)leftParameter;
			team = teamService.findTeamByUserAndType( userId,menberType.getCode());
			if(team == null){//没有，新增一个
				team = new Team();
				team.setType(menberType.getCode());
				team.setName(menberType.getName());
				team.setOwnUser(userId);
				teamService.createTeam(team);
			}
			team.setName(menberType.getName());
		}
		identityService = ProcessEngines.getDefaultProcessEngine()
				.getIdentityService();
		i18nManager = ViewToolManager.getI18nManager();
		mainPage = page;
	}

	@Override
	public void attach() {
		super.attach();
		init();
	}

	private void init() {
		setSizeFull();
		addStyleName(Reindeer.PANEL_LIGHT);
		// 组名称显示
		initPageTitle();
		// 成员
		initMembers();
	}

	protected void initMembers() {
		HorizontalLayout membersHeader = new HorizontalLayout();
		membersHeader.setSpacing(true);
		membersHeader.setWidth(100, Unit.PERCENTAGE);
		membersHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addDetailComponent(membersHeader);
		// 组的标题
		initMembersTitle(membersHeader);
		// 新增成员按钮
		initAddMembersButton(membersHeader);

		membersLayout = new HorizontalLayout();
		membersLayout.setWidth(100, Unit.PERCENTAGE);
		addDetailComponent(membersLayout);
		
		//menbertable
		initMembersTable();
	}

	protected void initMembersTable() {
		LazyLoadingQuery query = new TeamMemberQuery(team.getId());
		if (query.size() > 0) {
			membersTable = new Table();
			membersTable.setWidth(100, Unit.PERCENTAGE);
			membersTable.setHeight(400, Unit.PIXELS);

			membersTable.setEditable(false);
			membersTable.setSelectable(false);
			membersTable.setSortEnabled(true);

			LazyLoadingContainer container = new LazyLoadingContainer(query, 10);
			membersTable.setContainerDataSource(container);

			membersTable.addContainerProperty("id", String.class, null);
			membersTable.addContainerProperty("firstName", String.class, null);
			membersTable.addContainerProperty("lastName", String.class, null);
			membersTable.addContainerProperty("email", String.class, null);
			membersTable.addGeneratedColumn("actions",
					new MemberActionsColumnGenerator());
			// membersTable.addContainerProperty("actions", Component.class,
			// null);

			membersLayout.addComponent(membersTable);
		} else {
			noMembersTable = new Label(
					i18nManager.getMessage(Messages.GROUP_NO_MEMBERS));
			membersLayout.addComponent(noMembersTable);
		}
	}
	
	/**
	 * 成员table的action
	 * @author ww
	 */
	public class MemberActionsColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			final UserEntity userEntity = BinderHandler.getTableBean(
					source, itemId);
			Embedded deleteIcon = new Embedded(null, Images.DELETE);
			deleteIcon.addStyleName(ExplorerLayout.STYLE_CLICKABLE);
			deleteIcon.addClickListener(new com.vaadin.event.MouseEvents.ClickListener() {
				@Override
				public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
					 ConfirmationDialogPopupWindow confirmationPopup = 
						      new ConfirmationDialogPopupWindow(i18nManager.getMessage(Messages.USER_CONFIRM_DELETE_GROUP, userEntity.getId(),team.getName()));
						    confirmationPopup.addListener(new ConfirmationEventListener() {
						      protected void rejected(ConfirmationEvent event) {
						      }
						      protected void confirmed(ConfirmationEvent event) {
						    	  teamMembershipService.deleteTeamMembership(userEntity.getId(),team.getId());
						    	  notifyMembershipChanged();
						        //membershipChangeListener.notifyMembershipChanged();
						      }
						    });
						    ViewToolManager.showPopupWindow(confirmationPopup);
				}
				
			});
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
		Button addButton = new Button("增加成员");
		// addButton.addStyleName(ExplorerLayout.STYLE_ADD);
		membersHeader.addComponent(addButton);
		membersHeader.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);

		addButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				final SelectUsersPopupWindow selectUsersPopup = new SelectUsersPopupWindow(
						i18nManager.getMessage(Messages.GROUP_SELECT_MEMBERS,
								team.getName()), true, false, getCurrentMembers());
				ViewToolManager.showPopupWindow(selectUsersPopup);

				// Listen to submit events (that contain the selected users)
				selectUsersPopup.addListener(new SubmitEventListener() {
					protected void submitted(SubmitEvent event) {
						Collection<String> userIds = selectUsersPopup
								.getSelectedUserIds();
						if (userIds.size() > 0) {
							for (String userId : userIds) {
								teamMembershipService.createTeamMembership(
										userId, team.getId());
							}
							notifyMembershipChanged();
						}
					}

					protected void cancelled(SubmitEvent event) {
					}
				});
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
				 ConfirmationDialogPopupWindow confirmationPopup = 
					      new ConfirmationDialogPopupWindow(i18nManager.getMessage(Messages.GROUP_CONFIRM_DELETE, team.getName()));
					    confirmationPopup.addListener(new ConfirmationEventListener() {
					      protected void rejected(ConfirmationEvent event) {
					      }
					      protected void confirmed(ConfirmationEvent event) {
					    	  teamService.deleteTeam(team);
					    	  //mainPage.refreshSelectNext();
					      }
					    });
					    ViewToolManager.showPopupWindow(confirmationPopup);
			}
		});
	}



	public void notifyMembershipChanged() {
		membersLayout.removeAllComponents();
		initMembersTable();
	}

	// Hacky - must be put in custom service
	protected List<String> getCurrentMembers() {
		List<User> users = identityService.createUserQuery()
				.memberOfTeam(team.getId()).list();
		List<String> userIds = new ArrayList<String>();
		for (User user : users) {
			userIds.add(user.getId());
		}
		return userIds;
	}
}
