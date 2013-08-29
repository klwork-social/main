package com.klwork.explorer.ui.business.social;

import com.klwork.common.utils.StringTool;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.business.query.WeiboSendQuery;
import com.klwork.explorer.ui.business.social.WeiboSendLeft.MemberType;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
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

public class WeiboSendRight extends DetailPanel {

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
	protected WeiboSendMainPage mainPage;
	
	protected MemberType menberType;

	public WeiboSendRight(WeiboSendMainPage page,Object leftParameter) {
		if(leftParameter != null){
			String userId = LoginHandler.getLoggedInUser().getId();
			menberType = (MemberType)leftParameter;
		}
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
		//membersHeader.setSpacing(true);
		membersHeader.setWidth(100, Unit.PERCENTAGE);
		membersHeader.setMargin(true);
		membersHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addDetailComponent(membersHeader);
		// 组的标题
		//initMembersTitle(membersHeader);

		membersLayout = new HorizontalLayout();
		membersLayout.setWidth(100, Unit.PERCENTAGE);
		addDetailComponent(membersLayout);
		
		//menbertable
		initMembersTable();
	}

	protected void initMembersTable() {
		WeiboSendQuery query = new WeiboSendQuery();
		query.setType(StringTool.parseInt(menberType.getCode()));
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
			membersTable.addContainerProperty("userAccountId", String.class, null);
			membersTable.addContainerProperty("shortText", String.class, null);
			membersTable.addGeneratedColumn("actions",
					new MemberActionsColumnGenerator());
			// membersTable.addContainerProperty("actions", Component.class,
			// null);

			membersLayout.addComponent(membersTable);
		} else {
			noMembersTable = new Label("没有任何操作记录");
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
			return null;
		}
	}

	protected void initMembersTitle(HorizontalLayout membersHeader) {
		Label usersHeader = new Label(
				i18nManager.getMessage(Messages.GROUP_HEADER_USERS));
		usersHeader.addStyleName(ExplorerLayout.STYLE_H3);
		membersHeader.addComponent(usersHeader);
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

		Label groupName = new Label(menberType.getName());
		groupName.setSizeUndefined();
		groupName.addStyleName(Reindeer.LABEL_H2);
		layout.addComponent(groupName);
		layout.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(groupName, 1.0f);
	}
}
