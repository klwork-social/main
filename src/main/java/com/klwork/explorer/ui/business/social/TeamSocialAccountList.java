package com.klwork.explorer.ui.business.social;

import java.util.HashMap;
import java.util.Map;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.query.SocialListQuery;
import com.klwork.explorer.ui.business.query.TeamSocialListQuery;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableFieldCache;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.themes.Reindeer;

public class TeamSocialAccountList extends DetailPanel {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;

	final HashMap<Object, String> projectNames = new HashMap<Object, String>();

	private TableFieldCache fieldCache = new TableFieldCache();
	// 当前table id
	private String currentProjectId;
	private Object currentItemId;

	AbstractTabViewPage  mainPage;
	ProjectService projectService;
	protected HorizontalLayout projectsLayout;

	public TeamSocialAccountList(AbstractTabViewPage tabPage) {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = tabPage;
		projectService = ViewToolManager.getBean("projectService");
	}

	/*
	 * public ProjectList(ProjectMain projectMain, String projectId) {
	 * this(projectMain); this.projectId = projectId; }
	 */
	@Override
	public void attach() {
		super.attach();
		initPageTitle();
		// 项目table
		initProjectList();
	}

	private void initProjectList() {
		HorizontalLayout projectsHeader = new HorizontalLayout();
		//projectsHeader.setSpacing(true);
		projectsHeader.setMargin(true);
		projectsHeader.setWidth(100, Unit.PERCENTAGE);
		projectsHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addDetailComponent(projectsHeader);

		// 组的标题
		initProjectsTitle(projectsHeader);
		// 新增成员按钮
		initAddAccountButton(projectsHeader);

		projectsLayout = new HorizontalLayout();
		projectsLayout.setWidth(100, Unit.PERCENTAGE);
		addDetailComponent(projectsLayout);

		initProjectsTable();
	}

	private void initProjectsTitle(HorizontalLayout membersHeader) {
		Label usersHeader = new Label("帐号列表");
		usersHeader.addStyleName(ExplorerLayout.STYLE_H3);
		membersHeader.addComponent(usersHeader);

	}

	public void initProjectsTable() {
		final Table listTable = new Table();
		/*listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);*/
		
		listTable.setWidth(100, Unit.PERCENTAGE);
		listTable.setHeight(400, Unit.PIXELS);

		listTable.setEditable(false);
		listTable.setSelectable(false);
		listTable.setSortEnabled(true);
		projectsLayout.addComponent(listTable);
		listTable.addValueChangeListener(getListSelectionListener(listTable));
		LazyLoadingQuery lazyLoadingQuery = new TeamSocialListQuery(this);
		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, 10);
		listTable.setContainerDataSource(listContainer);
		
		//listTable.addContainerProperty("id", String.class, null);
		listTable.addContainerProperty("userScreenName", Button.class, "");
		listTable.addContainerProperty("name", String.class, "");
		listTable.addContainerProperty("url", String.class, "");
		listTable.addContainerProperty("type", String.class, "");
		listTable.addContainerProperty("actions", Component.class, "");
		//listTable.addGeneratedColumn("action", new SocialActionColumnGenerator());
	}

	public class SocialActionColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			HorizontalLayout buttonLayout = new HorizontalLayout();
			buttonLayout.setSpacing(true);
			buttonLayout.addStyleName("social");
			final SocialUserAccount socialUserAccount = BinderHandler.getTableBean(
					source, itemId);
			
			Button editButton  = new Button("微博");
			editButton.addStyleName(Reindeer.BUTTON_LINK);
			editButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					// WW_TODO 后台进行修改
				/*	Item item = source.getItem(itemId);
					String id = (String) item.getItemProperty("id").getValue();
					String name = (String) item.getItemProperty("name").getValue();*/
					selectedHandle(socialUserAccount);
				}
			});
			buttonLayout.addComponent(editButton);
			return buttonLayout;
		}

	}

	public void selectElement(Table table, int index) {
		if (table.getContainerDataSource().size() > index) {
			table.select(index);
			table.setCurrentPageFirstItemId(index);
		}
	}

	private ValueChangeListener getListSelectionListener(final Table listTable) {
		return new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				Object itemId = event.getProperty().getValue();
				Item item = listTable.getItem(itemId);

				if (item != null) {
					currentItemId = itemId;
					String id = (String) item.getItemProperty("id").getValue();
					// 保存当前项目id
					currentProjectId = id;
					String name = (String) item.getItemProperty("name")
							.getValue();
					System.out.println(id);
					if (id != null) {
						Map<String, String> parameter = new HashMap<String, String>();
						parameter.put("id", id);
						parameter.put("name", name);
						/*mainPage.setLeftParameter(parameter);
						mainPage.initRightComponent();*/
						//selectedHandle(id, name);
						// projectMain.initRightContent(id,name);
					}
				} else {
					System.out.println("erro....null?");
				}
			}
		};
	}

	private void initPageTitle() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100, Unit.PERCENTAGE);
		layout.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(false, false, true, false));
		addDetailComponent(layout);

		Embedded groupImage = new Embedded(null, Images.GROUP_50);
		layout.addComponent(groupImage);

		Label groupName = new Label("账号管理");
		groupName.setSizeUndefined();
		groupName.addStyleName(Reindeer.LABEL_H2);
		layout.addComponent(groupName);
		layout.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(groupName, 1.0f);
	}

	protected void initAddAccountButton(HorizontalLayout headerLayout) {
		
		Button addButton = new Button();
		addButton.setCaption("新增账号");
		// addButton.addStyleName(ExplorerLayout.STYLE_ADD);
		headerLayout.addComponent(addButton);
		headerLayout.setComponentAlignment(addButton, Alignment.MIDDLE_RIGHT);

		addButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8050449471041932066L;

			public void buttonClick(ClickEvent event) {
				/*final NewProjectWindow newProjectWindow = new NewProjectWindow(
						null);
				ViewToolManager.showPopupWindow(newProjectWindow);*/
			}
		});
	}


	public void selectedHandle(SocialUserAccount socialUserAccount) {
		if(socialUserAccount.getType() == 0){//新浪微博
			mainPage.addTabSpecial(new SinaWeiboShowPage(socialUserAccount,mainPage), "新浪_" + socialUserAccount.getUserScreenName());
		}else {
			mainPage.addTabSpecial(new QQWeiboShowPage(socialUserAccount,mainPage), "腾讯_" +  socialUserAccount.getUserScreenName());
		}
	}

	public void openAuthorityWindow(SocialUserAccount sc) {
		AccountAuthorityPopupWindow t = new AccountAuthorityPopupWindow(sc);
		ViewToolManager.showPopupWindow(t);
	}

}
