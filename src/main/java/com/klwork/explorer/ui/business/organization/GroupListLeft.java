package com.klwork.explorer.ui.business.organization;

import java.util.HashMap;

import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractVCustomComponent;
import com.klwork.explorer.ui.business.query.TeamListQuery;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.util.ThemeImageColumnGenerator;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

public class GroupListLeft extends AbstractVCustomComponent {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;

	final HashMap<Object, HashMap<Object, Field>> fields = new HashMap<Object, HashMap<Object, Field>>();
	FieldGroup fieldGroup = new FieldGroup();
	VerticalLayout tableLayout;
	OrganizationMainPage mainPage;
	TeamService teamService;

	public GroupListLeft(OrganizationMainPage projectMain) {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = projectMain;
		teamService = (TeamService) SpringApplicationContextUtil
				.getContext().getBean("teamService");
	}

	@Override
	protected void initUi() {
		//
		initHead();
		// table
		initGroupList();
		//分割
		getMainLayout().addComponent(CommonFieldHandler.getSpacer());
		
		initQuickAdd();
	}

	private void initQuickAdd() {
		GridLayout grid = new GridLayout(2, 2);
		grid.addStyleName(Reindeer.SPLITPANEL_SMALL);
		grid.setMargin(true);
		// grid.setMargin(new MarginInfo(true, false, true, false));
		// 加点空
		grid.setSpacing(true);
		grid.setSizeFull();
		
		Team relateTeam = new Team();
		relateTeam.setOwnUser(LoginHandler
				.getLoggedInUser().getId());
		BeanItem<Team> item = new BeanItem<Team>(
				relateTeam);
		fieldGroup.setItemDataSource(item);
		
		Label title = new Label("新建");
		title.addStyleName(ExplorerLayout.STYLE_H3);
		title.setWidth(100, Unit.PERCENTAGE);
		grid.addComponent(title, 0, 0);
		initLabelOfGrid(grid,title);
		
		TextField nameField = CommonFieldHandler.createTextField("");
		grid.addComponent(nameField, 1, 0);
		grid.setComponentAlignment(nameField, Alignment.MIDDLE_RIGHT);
		fieldGroup.bind(nameField, "name");
		
		Button addButton = new Button("确定");
		addButton.addStyleName(Reindeer.BUTTON_SMALL);
		grid.addComponent(addButton, 1, 1);

		addButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				try {
					fieldGroup.commit();
					Team team = BinderHandler
							.getFieldGroupBean(fieldGroup);
					teamService.createTeam(team);
					reflashTable();
					// 通知外边的调用任务,任务相关信息已经保存
					fireEvent(new SubmitEvent(GroupListLeft.this,
							SubmitEvent.SUBMITTED));
				} catch (CommitException e) {
					e.printStackTrace();
				}
			}

		});
		getMainLayout().addComponent(grid);
		getMainLayout().setExpandRatio(grid, 0.3f);
	}
	
	public void initLabelOfGrid(GridLayout taskDetails, Label nameLabel) {
		nameLabel.addStyleName(ExplorerLayout.STYLE_PROFILE_FIELD);
		nameLabel.setSizeUndefined();
		taskDetails.setComponentAlignment(nameLabel, Alignment.MIDDLE_RIGHT);
	}

	private void initGroupList() {
		tableLayout = new VerticalLayout();
		tableLayout.setSizeFull();
		tableLayout.setSpacing(true);
		getMainLayout().addComponent(tableLayout);
		getMainLayout().setExpandRatio(tableLayout, 0.5f);
		initTable(tableLayout);
		
	}

	public void initTable(VerticalLayout tableLayout) {
		final Table listTable = new Table();
		tableLayout.addComponent(listTable);
		listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		// Listener to change right panel when clicked on a task

		listTable.addValueChangeListener(getListSelectionListener(listTable));
		LazyLoadingQuery lazyLoadingQuery = new TeamListQuery(LoginHandler
				.getLoggedInUser().getId());
		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, 10);
		listTable.setContainerDataSource(listContainer);

		// Create column header
		listTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(
				Images.TASK_22));
		listTable.setColumnWidth("icon", 22);

		listTable.addContainerProperty("name", String.class, "");
		listTable.addGeneratedColumn("count", new MemberCountColumnGenerator());

		TableHandler.setTableNoHead(listTable);
	}

	public class MemberCountColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			Label r = new Label("3");
			return r;
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
				Item item = listTable.getItem(event.getProperty().getValue());
				if (item != null) {
					String id = (String) item.getItemProperty("id").getValue();
					// 保存当前项目id
					//currentProjectId = id;
					String name = (String) item.getItemProperty("name")
							.getValue();
					System.out.println(id);
					if (id != null) {
						mainPage.initRightContent(id);
					}
				} else {
					System.out.println("erro....null?");
				}
			}
		};
	}
	
	private void reflashTable() {
		tableLayout.removeAllComponents();
		initTable(tableLayout);
	}
	
	private void initHead() {
		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setWidth(100, Unit.PERCENTAGE);
		headerLayout.setMargin(true);
		getMainLayout().addComponent(headerLayout);
		initTitle(headerLayout);
	}

	protected void initTitle(HorizontalLayout headerLayout) {
		Label title = new Label("所有分组");
		title.addStyleName(ExplorerLayout.STYLE_H3);
		title.setWidth(100, Unit.PERCENTAGE);
		headerLayout.addComponent(title);
		headerLayout.setExpandRatio(title, 1.0f);
	}

}
