package com.klwork.explorer.ui.business.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.query.SocialListQuery;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableFieldCache;
import com.klwork.explorer.ui.main.views.SocialMainPage;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractOrderedLayout;
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

public class SocialAccountList extends DetailPanel {

	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;
	
	final HashMap<Object, String> projectNames = new HashMap<Object, String>();

	private TableFieldCache fieldCache = new TableFieldCache();

	public AbstractTabViewPage mainPage;
	ProjectService projectService;

	protected HorizontalLayout listLayout;
	protected Table listTable;
	protected Button addButton;
	BrowserWindowOpener opener = null;
	
	public SocialAccountList(AbstractTabViewPage tabPage) {
		super(false);
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = tabPage;
		projectService = ViewToolManager.getBean("projectService");

	}

	@Override
	protected void initUI() {
		initPageTitle();
		//table
		initList();
	}

	private void initList() {
		HorizontalLayout headLayout = new HorizontalLayout();
		headLayout.setSpacing(true);
		headLayout.setMargin(true);
		// projectsHeader.setWidth(100, Unit.PERCENTAGE);
		headLayout.setSizeFull();
		headLayout.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addDetailComponent(headLayout);

		//导航button
		initNavButtons(headLayout);
		// 新增成员按钮
		initAddAccountButton(headLayout);
		resetButtonByBrowWindow(DictDef.dictInt("sina"), addButton);
		
		//存放table的容器
		listLayout = new HorizontalLayout();
		listLayout.setWidth(100, Unit.PERCENTAGE);
		addDetailComponent(listLayout);
		
		//默认显示新浪
		initProjectsTable(DictDef.dictInt("sina"));
	}

	protected Button addMenuButton(String style, String label, Resource icon,
			boolean active, float width, AbstractOrderedLayout layoutout) {
		Button button = new Button(label);
		button.addStyleName(style);
		button.addStyleName(ExplorerLayout.STYLE_MAIN_MENU_BUTTON);
		button.addStyleName(Reindeer.BUTTON_LINK);
		button.setHeight(54, Unit.PIXELS);
		button.setIcon(icon);
		button.setWidth(width, Unit.PIXELS);
		layoutout.addComponent(button);
		layoutout.setComponentAlignment(button, Alignment.MIDDLE_RIGHT);
		return button;
	}

	private void initNavButtons(HorizontalLayout membersHeader) {
		Button sinaButton = addMenuButton("", "新浪微博", new ThemeResource(
				"img/weibo/weibo_sina.png"), false, 80, membersHeader);
		sinaButton.addClickListener(new ShowListClickListener(DictDef
				.dictInt("sina")));

		Button qqButton = addMenuButton("", "腾讯微博", new ThemeResource(
				"img/weibo/weibo_qq.png"), false, 80, membersHeader);
		qqButton.addClickListener(new ShowListClickListener(DictDef
				.dictInt("tencent")));

		Button noteButton = addMenuButton("", "印象笔记", new ThemeResource(
				"img/weibo/evernote.png"), false, 80, membersHeader);
		noteButton.addClickListener(new ShowListClickListener(DictDef
				.dictInt("evernote")));

	}

	public void initProjectsTable(int type) {
		listTable = new Table();
		listTable.setWidth(100, Unit.PERCENTAGE);
		listTable.setHeight(400, Unit.PIXELS);
		listTable.setEditable(false);
		listTable.setSelectable(false);
		listTable.setSortEnabled(true);
		listLayout.addComponent(listTable);
		//listTable.addValueChangeListener(getListSelectionListener(listTable));
		LazyLoadingQuery lazyLoadingQuery = new SocialListQuery(this,
				type);
		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, 10);
		listTable.setContainerDataSource(listContainer);
		listTable.addContainerProperty("userScreenName", Button.class, "");
		listTable.addContainerProperty("name", String.class, "");
		listTable.addContainerProperty("url", String.class, "");
		listTable.addContainerProperty("type", String.class, "");
		listTable.addContainerProperty("actions", Component.class, "");
		
		
		ArrayList<String> visibleColumnLabels = new ArrayList<String>();
		//中文名称
		visibleColumnLabels.add("显示名称");
		visibleColumnLabels.add("name");
		visibleColumnLabels.add("url");
		visibleColumnLabels.add("类型");
		visibleColumnLabels.add("操作");
		listTable.setColumnHeaders(visibleColumnLabels.toArray(new String[0]));
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
			final SocialUserAccount socialUserAccount = BinderHandler
					.getTableBean(source, itemId);

			Button editButton = new Button("微博");
			editButton.addStyleName(Reindeer.BUTTON_LINK);
			editButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
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
					//currentItemId = itemId;
					String id = (String) item.getItemProperty("id").getValue();
					// 保存当前项目id
					//currentProjectId = id;
					String name = (String) item.getItemProperty("name")
							.getValue();
					if (id != null) {
						Map<String, String> parameter = new HashMap<String, String>();
						parameter.put("id", id);
						parameter.put("name", name);
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

		addButton = new Button();
		addButton.setCaption("新增账号");
		// addButton.addStyleName(ExplorerLayout.STYLE_ADD);
		headerLayout.addComponent(addButton);
		headerLayout.setComponentAlignment(addButton, Alignment.BOTTOM_RIGHT);
		headerLayout.setExpandRatio(addButton, 1.0f);
		addButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8050449471041932066L;
			public void buttonClick(ClickEvent event) {
			}
		});
	}

	public void selectedHandle(SocialUserAccount socialUserAccount) {
		((SocialMainPage) mainPage).openWeiboTab(socialUserAccount);
	}

	public void openAuthorityWindow(SocialUserAccount sc) {
		AccountAuthorityPopupWindow t = new AccountAuthorityPopupWindow(sc);
		ViewToolManager.showPopupWindow(t);
	}
	
	public void resetButtonByBrowWindow(Integer scType, Button authorButton) {
		if(opener != null){
			opener.remove();
		}
		opener = new BrowserWindowOpener(queryAuthUrlByType(scType));
		opener.extend(authorButton);
		opener.setFeatures("height=500,width=600,resizable");
	}

	public void initButtonByBrowWindow(Integer scType, Button authorButton) {
		BrowserWindowOpener opener = new BrowserWindowOpener(queryAuthUrlByType(scType));
		opener.extend(authorButton);
		opener.setFeatures("height=500,width=600,resizable");
	}
	
	public String queryAuthUrlByType(Integer scType) {
		return "user/oauth?type=" + scType + "&state=reset-oauth&userId=" + LoginHandler.getUser().getId();
	}
	
	public class ShowListClickListener implements ClickListener {
		private static final long serialVersionUID = 5796632665136901742L;
		int type;

		public ShowListClickListener(int type) {
			this.type = type;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			listLayout.removeAllComponents();
			initProjectsTable(type);
			resetButtonByBrowWindow(type, addButton);
		}

	}

}
