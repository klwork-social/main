package com.klwork.explorer.ui.business.outproject;

import java.util.ArrayList;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.business.domain.service.ProjectParticipantService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.business.flow.act.ParticipantListQuery;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.util.ThemeImageColumnGenerator;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class WinnersListPage extends DetailPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3649580842552158517L;
	// Services
	protected transient TaskService taskService;
	protected transient ProjectParticipantService projectParticipantService;
	protected transient ProjectManagerService projectManagerService;
	protected VerticalLayout centralLayout;
	protected transient I18nManager i18nManager;
	
	private OutsourcingProject relateOutSourceingProject;

	public WinnersListPage(OutsourcingProject relateOutSourceingProject) {
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		this.projectParticipantService = ViewToolManager
				.getBean("projectParticipantService");
		projectManagerService = ViewToolManager
				.getBean("projectManagerService");
		this.relateOutSourceingProject = relateOutSourceingProject;
		this.i18nManager = ViewToolManager.getI18nManager();
	}

	@Override
	protected void initUI() {
		setSizeFull();
		addStyleName(ExplorerLayout.THEME);
		addStyleName(Reindeer.LAYOUT_WHITE);

		// Central panel: all task data
		centralLayout = new VerticalLayout();
		centralLayout.setMargin(true);
		setDetailContainer(centralLayout);

		Label title = new Label("");
		title.addStyleName(ExplorerLayout.STYLE_H3);
		title.setWidth(100, Unit.PERCENTAGE);
		centralLayout.addComponent(title);

		Table winTable = new Table();
		winTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		winTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		winTable.setWidth(100, Unit.PERCENTAGE);
		winTable.setHeight(100, Unit.PERCENTAGE);
		addComponent(winTable);
		// setExpandRatio(listTable, 1);

		initWinTableData(winTable);

		// Create column header
		winTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(
				Images.TASK_22));
		winTable.setColumnWidth("icon", 22);

		winTable.addContainerProperty("scoreUserId", String.class, "");
		winTable.addContainerProperty("score", String.class, "");
		winTable.addContainerProperty("winningAmount", String.class, "");
		winTable.addGeneratedColumn("action", new TableActionColumnGenerator());

		ArrayList<String> visibleColumnLabels = new ArrayList<String>();
		visibleColumnLabels.add("");
		visibleColumnLabels.add("上传用户");
		visibleColumnLabels.add("得分");
		visibleColumnLabels.add("奖金分配");
		visibleColumnLabels.add("操作");

		winTable.setColumnHeaders(visibleColumnLabels.toArray(new String[0]));
		winTable.setEditable(true);// 只有设置上面的factory才生效

		TableHandler.setTableHasHead(winTable);
		// winTable.setImmediate(false);
		centralLayout.addComponent(winTable);

	}

	public void initWinTableData(Table winTable) {
		LazyLoadingQuery lazyLoadingQuery = new ParticipantListQuery(
				relateOutSourceingProject, true);
		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, 10);
		winTable.setContainerDataSource(listContainer);
		if (lazyLoadingQuery.size() < 10) {
			winTable.setPageLength(0);
		} else {
			winTable.setPageLength(10);
		}
		
		if(lazyLoadingQuery.size() <= 0) {// 没有任何获胜者
			Label noDataLabel = new Label(
					i18nManager.getMessage(Messages.OUTPROJECT_WINNER_NO_ONES));
			centralLayout.addComponent(noDataLabel);
		}
	}

	public class TableActionColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {

			com.vaadin.ui.Embedded deleteButton = new com.vaadin.ui.Embedded(
					null, Images.DELETE);
			deleteButton.addStyleName(ExplorerLayout.STYLE_CLICKABLE);

			deleteButton
					.addClickListener(new com.vaadin.event.MouseEvents.ClickListener() {
						private static final long serialVersionUID = 3483384365910084806L;

						@Override
						public void click(
								com.vaadin.event.MouseEvents.ClickEvent event) {
						}

					});
			return deleteButton;
		}
	}

}
