package com.klwork.explorer.ui.business.outproject;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.UserDataStatistic;
import com.klwork.business.domain.service.UserDataStatisticService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.base.AbstractVCustomComponent;
import com.klwork.explorer.ui.business.query.PublicProjectListQuery;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.TaskRemindComponent.TaskRemindEntity;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class OutProjectRemindComponent extends AbstractVCustomComponent {
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
			TaskRemindEntity e = BinderHandler.getTableBean(source, itemId);
			String title = "<span style='color:red'>" + e.getOverdueCount()
					+ "</span>";
			Label s = new Label(title + "", ContentMode.HTML);
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
			final TaskRemindEntity e = BinderHandler.getTableBean(source,
					itemId);
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

	public OutProjectRemindComponent() {
		
		String userId = LoginHandler.getLoggedInUser().getId();
		userDataStatisticService = ViewToolManager
				.getBean("userDataStatisticService");
		cUserDataStatistic = userDataStatisticService
				.queryUserTaskStatistic(userId);

	}

	@Override
	protected void initUi() {
		setCaption("正在开展的项目");
		setSizeFull();
		
		Table listTable = new Table();
		listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		listTable.setWidth(100, Unit.PERCENTAGE);
		listTable.setHeight(100, Unit.PERCENTAGE);
		// 查询所有的
		LazyLoadingQuery lazyLoadingQuery = new PublicProjectListQuery(null);

		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, 10);
		listTable.setContainerDataSource(listContainer);
		if (lazyLoadingQuery.size() < 10) {
			listTable.setPageLength(0);
		} else {
			listTable.setPageLength(10);
		}

		listTable.addGeneratedColumn("", new ProjectHeadColumnGenerator());
		TableHandler.setTableNoHead(listTable);
		listTable.setImmediate(false);
		listTable.setSelectable(false);

		this.getMainLayout().addComponent(listTable);

	}

	public class ProjectHeadColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			final OutsourcingProject project = BinderHandler.getTableBean(
					source, itemId);
			final GridLayout grid = new GridLayout(2, 1);
			grid.addStyleName(Reindeer.SPLITPANEL_SMALL);
			// grid.setSizeFull();
			// grid.setMargin(true);
			grid.setColumnExpandRatio(0, 0.6f);
			grid.setColumnExpandRatio(1, 0.4f);

			VerticalLayout firstLayout = new VerticalLayout();
			Label lable = new Label(project.getName());
			firstLayout.addComponent(lable);
			grid.addComponent(firstLayout, 0, 0);

			// 第二档
			VerticalLayout secondLayout = new VerticalLayout() {
				{
					setSizeFull();
					//setSpacing(true);
					//setMargin(true);

					Button commendButton = new Button("详细");
					commendButton.addStyleName(Reindeer.BUTTON_LINK);
					commendButton.addStyleName("new_button");
					commendButton.addStyleName("w_current");
					commendButton.addClickListener(new ClickListener() {
						public void buttonClick(ClickEvent event) {
							Notification.show("推荐项目",
									Notification.Type.HUMANIZED_MESSAGE);
						}
					});
					addComponent(commendButton);
					setComponentAlignment(commendButton, Alignment.MIDDLE_CENTER);
				}
			};
			grid.addComponent(secondLayout, 1, 0);
			return grid;
		}
	}
}
