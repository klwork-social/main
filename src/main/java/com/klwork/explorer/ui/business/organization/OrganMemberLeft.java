package com.klwork.explorer.ui.business.organization;

import java.util.ArrayList;
import java.util.HashMap;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.base.AbstractVCustomComponent;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;

public class OrganMemberLeft extends AbstractVCustomComponent {

	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;

	final HashMap<Object, HashMap<Object, Field>> fields = new HashMap<Object, HashMap<Object, Field>>();
	FieldGroup fieldGroup = new FieldGroup();
	Table listTable;
	VerticalLayout tableLayout;
	OrganizationMemberMainPage mainPage;
	TeamService teamService;

	public OrganMemberLeft(OrganizationMemberMainPage mainPage) {
		this.mainPage = mainPage;
		this.i18nManager = ViewToolManager.getI18nManager();
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
	}

	@Override
	protected void initUi() {
		//
		initHead();
		// table
		initGroupList();
	}

	private void initGroupList() {
		tableLayout = new VerticalLayout();
		tableLayout.setSizeFull();
		tableLayout.setSpacing(true);
		getMainLayout().addComponent(tableLayout);
		getMainLayout().setExpandRatio(tableLayout, 0.5f);
		// tab
		initGroupTable(tableLayout);

	}

	public void initGroupTable(VerticalLayout tableLayout) {

		listTable = new Table();
		tableLayout.addComponent(listTable);
		listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		// Listener to change right panel when clicked on a task
		
		BeanItemContainer<MemberType> container = new BeanItemContainer<MemberType>(
				MemberType.class);
		MemberType first = new MemberType(i18nManager.getMessage(Messages.TEAM_GROUP_TYPE_FORMAL), "3",EntityDictionary.TEAM_GROUP_TYPE_FORMAL);
		container.addBean(first);
		//container.addBean(new MemberType("正在邀请..", "13",EntityDictionary.TEAM_GROUP_TYPE_INVITE));
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

	private void reflashTable() {
		tableLayout.removeAllComponents();
		initGroupTable(tableLayout);
	}

	private void initHead() {
		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setWidth(100, Unit.PERCENTAGE);
		headerLayout.setMargin(true);
		getMainLayout().addComponent(headerLayout);
		initTitle(headerLayout);
	}

	protected void initTitle(HorizontalLayout headerLayout) {
		//"团队成员管理"
		Label title = new Label(i18nManager.getMessage(Messages.ORGANIZATION_TEAM_MEMBER_MANAGER));
		title.addStyleName(ExplorerLayout.STYLE_H3);
		title.setWidth(100, Unit.PERCENTAGE);
		headerLayout.addComponent(title);
		headerLayout.setExpandRatio(title, 1.0f);
	}

	/**
	 * 选择table的下一行
	 */
	public void refreshSelectNext() {
		TableHandler.refreshSelectNext(listTable);
	}

	public class MemberType {
		private String name;
		private String count;
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

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public MemberType(String name, String count, String code) {
			super();
			this.name = name;
			this.count = count;
			this.code = code;
		}

	}
}
