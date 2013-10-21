package com.klwork.explorer.ui.task;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.klwork.business.domain.model.UserDataStatistic;
import com.klwork.business.domain.service.UserDataStatisticService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.base.AbstractVCustomComponent;
import com.klwork.explorer.ui.custom.ConfirmationDialogPopupWindow;
import com.klwork.explorer.ui.event.ConfirmationEvent;
import com.klwork.explorer.ui.event.ConfirmationEventListener;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.Table.RowHeaderMode;
import com.vaadin.ui.themes.Reindeer;

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
			String title = "<span style='color:red'>" + e.getOverdueCount() + "</span>";
			Label s = new Label(title + "",ContentMode.HTML);
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
			final TaskRemindEntity e = BinderHandler.getTableBean(source,itemId);
			Button s = new Button(e.getCount() + "");
			s.addStyleName(Reindeer.BUTTON_LINK);
			s.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					ViewToolManager.navigateTo("tasks" + "/" + e.getType());
				}
			});
			return s;
		}

	}


	public TaskRemindComponent() {
		 String userId = LoginHandler.getLoggedInUser().getId();
		 userDataStatisticService = ViewToolManager.getBean("userDataStatisticService");
		 cUserDataStatistic = userDataStatisticService.queryUserTaskStatistic(userId);
		
	}

	@Override
	protected void initUi() {
		setCaption("任务提醒");
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
		TaskRemindEntity first = new TaskRemindEntity("代办任务", cUserDataStatistic.getTodoTaskTotal(),cUserDataStatistic.getOverdueTodoTaskTotal(),"todoTask");
		container.addBean(first);
		
		first = new TaskRemindEntity("我的任务", cUserDataStatistic.getMyTaskTotal(),cUserDataStatistic.getOverdueMyTaskTotal(),"myTask");
		container.addBean(first);
		
		first = new TaskRemindEntity("团队任务", cUserDataStatistic.getTeamTaskTotal(),cUserDataStatistic.getOverdueTeamTaskTotal(),"teamTask");
		container.addBean(first);
		
		first = new TaskRemindEntity("参与任务", cUserDataStatistic.getInvolvedTaskTotal(),cUserDataStatistic.getOverdueInvolvedTaskTotal(),"involvedTask");
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
		private String type;
		public TaskRemindEntity(String name, Integer count, Integer overdueCount,String type) {
			super();
			this.name = name;
			this.count = count;
			this.overdueCount = overdueCount;
			this.type = type;
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
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	}
}
