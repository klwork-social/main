package com.klwork.explorer.ui.business.outproject;

import java.util.HashMap;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractVCustomComponent;
import com.klwork.explorer.ui.business.query.OutProjectPublishQuery;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.util.ThemeImageColumnGenerator;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class OutProjectOfMyPublishManager extends AbstractVCustomComponent {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;

	final HashMap<Object, HashMap<Object, Field>> fields = new HashMap<Object, HashMap<Object, Field>>();
	FieldGroup fieldGroup = new FieldGroup();
	Table listTable;
	VerticalLayout tableLayout;
	OutProjectManagerMainPage mainPage;
	TeamService teamService;

	public OutProjectOfMyPublishManager(OutProjectManagerMainPage projectMain) {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = projectMain;
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
	}

	@Override
	protected void initUi() {
		initHead();
		getMainLayout().addComponent(CommonFieldHandler.getSpacer());
		// table
		initTableList();
		// 分割
		// getMainLayout().addComponent(CommonFieldHandler.getSpacer());

	}

	public void initLabelOfGrid(GridLayout taskDetails, Label nameLabel) {
		nameLabel.addStyleName(ExplorerLayout.STYLE_PROFILE_FIELD);
		nameLabel.setSizeUndefined();
		taskDetails.setComponentAlignment(nameLabel, Alignment.MIDDLE_RIGHT);
	}

	private void initTableList() {
		tableLayout = new VerticalLayout();
		tableLayout.setSizeFull();
		tableLayout.setSpacing(true);
		getMainLayout().addComponent(tableLayout);
		getMainLayout().setExpandRatio(tableLayout, 0.5f);
		// tab
		initProjectTable(tableLayout);

	}

	public void initProjectTable(VerticalLayout tableLayout) {
		LazyLoadingQuery lazyLoadingQuery = new OutProjectPublishQuery(
				LoginHandler.getLoggedInUser().getId(), null);
		if (lazyLoadingQuery.size() > 0) {
			listTable = new Table();
			tableLayout.addComponent(listTable);
			listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
			listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
			// Listener to change right panel when clicked on a task

			listTable
					.addValueChangeListener(new Property.ValueChangeListener() {
						private static final long serialVersionUID = -2889453697674379998L;

						public void valueChange(ValueChangeEvent event) {
							Item item = listTable.getItem(event.getProperty()
									.getValue());

							if (item != null) {
								BeanItem<OutsourcingProject> r = (BeanItem<OutsourcingProject>) item;
								mainPage.setLeftParameter(r.getBean());
								mainPage.initRight();
							} else {
								System.out.println("erro....null?");
							}
						}
					});

			LazyLoadingContainer listContainer = new LazyLoadingContainer(
					lazyLoadingQuery, 10);
			listTable.setContainerDataSource(listContainer);

			// Create column header
			listTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(
					Images.TASK_22));
			listTable.setColumnWidth("icon", 22);

			listTable.addContainerProperty("name", String.class, "");
			listTable.setEditable(false);
			TableHandler.setTableNoHead(listTable);
			// 默认选择第一个table
			TableHandler.selectElement(listTable, 0);
		} else {// 没有任何发布
			Label noMembersTable = new Label(
					i18nManager
							.getMessage(Messages.OUTPROJECT_NO_PUBLISH_PROMPT));
			tableLayout.addComponent(noMembersTable);
		}
	}

	private void reflashTable() {
		tableLayout.removeAllComponents();
		initProjectTable(tableLayout);
	}

	private void initHead() {
		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setWidth(100, Unit.PERCENTAGE);
		headerLayout.setMargin(true);
		getMainLayout().addComponent(headerLayout);

		Component searchComponent = getSearchComponent();
		headerLayout.addComponent(searchComponent);

	}

	public Component getSearchComponent() {
		return new ProjectSearchPanel();
	}

	/**
	 * 选择table的下一行
	 */
	public void refreshSelectNext() {
		TableHandler.refreshSelectNext(listTable);
	}

}
