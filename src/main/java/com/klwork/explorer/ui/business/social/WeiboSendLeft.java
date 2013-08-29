package com.klwork.explorer.ui.business.social;

import java.util.ArrayList;
import java.util.HashMap;

import com.klwork.business.domain.model.EntityDictionary;
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
import com.klwork.explorer.ui.business.organization.OrganMemberLeft.MemberType;
import com.klwork.explorer.ui.business.query.OutProjectAddInQuery;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.util.ThemeImageColumnGenerator;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

public class WeiboSendLeft extends AbstractVCustomComponent {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;

	final HashMap<Object, HashMap<Object, Field>> fields = new HashMap<Object, HashMap<Object, Field>>();
	FieldGroup fieldGroup = new FieldGroup();
	Table listTable;
	VerticalLayout tableLayout;
	WeiboSendMainPage mainPage;
	TeamService teamService;
	OutsourcingProject outsourcingProject;

	public WeiboSendLeft(WeiboSendMainPage projectMain) {
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
		listTable = new Table();
		tableLayout.addComponent(listTable);
		listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		// Listener to change right panel when clicked on a task
		
		BeanItemContainer<MemberType> container = new BeanItemContainer<MemberType>(
				MemberType.class);
		MemberType first = new MemberType("直接发送", "0");
		container.addBean(first);
		
		first = new MemberType("定时发送", "1");
		container.addBean(first);
		listTable.setContainerDataSource(container);
		
		listTable.addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = -2889453697674379998L;

			public void valueChange(ValueChangeEvent event) {
				Item item = listTable.getItem(event.getProperty().getValue());
				if (item != null) {
					String code = (String) item.getItemProperty("code")
							.getValue();
					MemberType type = ((BeanItem<MemberType>)item).getBean();
					mainPage.setLeftParameter(type);
					mainPage.initRight();
				} else {
					System.out.println("erro....null?");
				}
			}
		});

		listTable.setEditable(false);
		ArrayList<Object> visibleColumnIds = new ArrayList<Object>();
		visibleColumnIds.add("name");
		//visibleColumnIds.add("count");
		listTable.setVisibleColumns(visibleColumnIds.toArray());
		TableHandler.setTableNoHead(listTable);
		// 默认选择第一个table
		TableHandler.selectElement(listTable, 0);
		listTable.select(first);
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
		initTitle(headerLayout);
	}

	protected void initTitle(HorizontalLayout headerLayout) {
		Label title = new Label("微博发送管理");
		title.addStyleName(ExplorerLayout.STYLE_H3);
		title.setWidth(100, Unit.PERCENTAGE);
		headerLayout.addComponent(title);
		headerLayout.setExpandRatio(title, 1.0f);
	}

	/**
	 * 选择table的下一行
	 */
	public void refreshSelectNext() {
		if(listTable != null)
		TableHandler.refreshSelectNext(listTable);
	}

	public void refreshRightContent() {
		if(listTable != null){
			initRight();
		}
		
	}

	public void initRight() {
		mainPage.setLeftParameter(outsourcingProject);
		mainPage.initRight();
	}
	
	public class MemberType {
		private String name;
		private String code;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}


		public MemberType(String name, String code) {
			super();
			this.name = name;
			this.code = code;
		}

	}
}
