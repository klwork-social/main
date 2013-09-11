package com.klwork.explorer.ui.task;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.klwork.business.domain.model.UserDataStatistic;
import com.klwork.business.domain.service.UserDataStatisticService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.base.AbstractVCustomComponent;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.RowHeaderMode;

public class TaskRemindComponent extends AbstractVCustomComponent {
	private UserDataStatistic cUserDataStatistic;
	private UserDataStatisticService userDataStatisticService;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6003933191390375461L;


	public class OverdueTaskCountColumnGenerator implements ColumnGenerator {

		/**
		 * 
		 */
		private static final long serialVersionUID = 5832463718324711446L;

		@Override
		public Object generateCell(Table source, Object itemId, Object columnId) {
			TaskRemindEntity e = BinderHandler.getTableBean(source,itemId);
			Label s = new Label(e.getOverdueCount() + "");
			return s;
		}
	}


	public class TaskCountColumnGenerator implements ColumnGenerator {

		/**
		 * 
		 */
		private static final long serialVersionUID = 6498016941419629562L;

		@Override
		public Object generateCell(Table source, Object itemId, Object columnId) {
			TaskRemindEntity e = BinderHandler.getTableBean(source,itemId);
			Label s = new Label(e.getCount() + "");
			return s;
		}

	}


	public TaskRemindComponent() {
		 setCaption("任务提醒");
		 String userId = LoginHandler.getLoggedInUser().getId();
		 userDataStatisticService = ViewToolManager.getBean("userDataStatisticService");
		 cUserDataStatistic = userDataStatisticService.queryUserTaskStatistic(userId);
		
	}

	@Override
	protected void initUi() {
		getMainLayout().setMargin(true);
		
		Table t = new Table() {
            @Override
            protected String formatPropertyValue(Object rowId, Object colId,
                    Property<?> property) {
                if (colId.equals("Revenue")) {
                    if (property != null && property.getValue() != null) {
                        Double r = (Double) property.getValue();
                        String ret = new DecimalFormat("#.##").format(r);
                        return "$" + ret;
                    } else {
                        return "";
                    }
                }
                return super.formatPropertyValue(rowId, colId, property);
            }
        };
        //t.setCaption("Top 10 Titles by Revenue");

        //t.setWidth("100%");
        t.setSizeFull();
        t.setPageLength(0);
        t.addStyleName("plain");
        t.addStyleName("borderless");
        t.setSortEnabled(false);
        //t.setColumnAlignment("Revenue", Align.RIGHT);
        t.setRowHeaderMode(RowHeaderMode.INDEX);
        BeanItemContainer<TaskRemindEntity> container = new BeanItemContainer<TaskRemindEntity>(
				TaskRemindEntity.class);
		TaskRemindEntity first = new TaskRemindEntity("代办任务", cUserDataStatistic.getTodoTaskTotal(),cUserDataStatistic.getOverdueTodoTaskTotal());
		container.addBean(first);
		
		first = new TaskRemindEntity("我的任务", cUserDataStatistic.getMyTaskTotal(),cUserDataStatistic.getOverdueMyTaskTotal());
		container.addBean(first);
		
		first = new TaskRemindEntity("团队任务", cUserDataStatistic.getTeamTaskTotal(),cUserDataStatistic.getOverdueTeamTaskTotal());
		container.addBean(first);
		
		first = new TaskRemindEntity("参与任务", cUserDataStatistic.getInvolvedTaskTotal(),cUserDataStatistic.getOverdueInvolvedTaskTotal());
		container.addBean(first);
		//container.addBean(new MemberType("正在邀请..", "13",EntityDictionary.TEAM_GROUP_TYPE_INVITE));
		t.setContainerDataSource(container);
		ArrayList<Object> visibleColumnIds = new ArrayList<Object>();
		visibleColumnIds.add("name");
		t.setVisibleColumns(visibleColumnIds.toArray());
		//visibleColumnIds.add("count");
		//visibleColumnIds.add("overdueCount");
		t.addGeneratedColumn("count", new TaskCountColumnGenerator());
		t.addGeneratedColumn("overdueCount", new OverdueTaskCountColumnGenerator());
		ArrayList<String> visibleColumnLabels = new ArrayList<String>();
		visibleColumnLabels.add("");
		visibleColumnLabels.add("任务数");
		visibleColumnLabels.add("任务数-逾期");
		//visibleColumnIds.add("count");
		
		t.setColumnHeaders(visibleColumnLabels.toArray(new String[0]));
		
        this.getMainLayout().addComponent(t);
		
	}
	
	
	public class TaskRemindEntity {
		private String name;
		private Integer count;
		private Integer overdueCount;
		public TaskRemindEntity(String name, Integer count, Integer overdueCount) {
			super();
			this.name = name;
			this.count = count;
			this.overdueCount = overdueCount;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Integer getCount() {
			return count;
		}
		public void setCount(Integer count) {
			this.count = count;
		}
		public Integer getOverdueCount() {
			return overdueCount;
		}
		public void setOverdueCount(Integer overdueCount) {
			this.overdueCount = overdueCount;
		}
		
		
	}
}
