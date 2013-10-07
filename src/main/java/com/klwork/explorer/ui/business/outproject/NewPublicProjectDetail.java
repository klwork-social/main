package com.klwork.explorer.ui.business.outproject;

import java.util.HashMap;
import java.util.List;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.ProjectParticipant;
import com.klwork.business.domain.service.ProjectManagerService;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.base.AbstractVCustomComponent;
import com.klwork.explorer.ui.business.project.NewProjectWindow;
import com.klwork.explorer.ui.business.query.PublicProjectListQuery;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class NewPublicProjectDetail extends AbstractVCustomComponent {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;
	protected transient ProjectManagerService projectManagerService;
	protected transient TaskService taskService;

	final HashMap<Object, HashMap<Object, Field>> fields = new HashMap<Object, HashMap<Object, Field>>();
	FieldGroup fieldGroup = new FieldGroup();
	Table listTable;
	VerticalLayout tableLayout;
	TeamService teamService;
	private DictDef dictDef;
	String loginUserId = null;
	public NewPublicProjectDetail(DictDef def) {
		loginUserId = LoginHandler.getLoggedInUser().getId();
		this.taskService = ProcessEngines.getDefaultProcessEngine()
				.getTaskService();
		this.i18nManager = ViewToolManager.getI18nManager();
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
		projectManagerService = ViewToolManager
				.getBean("projectManagerService");

		this.dictDef = def;
	}

	@Override
	protected void initUi() {
		getMainLayout().addStyleName("project");
		getMainLayout().addComponent(new VerticalLayout() {
			{
				setSizeFull();
				addStyleName("content");
				CustomLayout cusLayout = new CustomLayout("project");
				// cusLayout.addStyleName("social");
				Label nameLabel = new Label(dictDef.getName());
				nameLabel.addStyleName("c_span1");
				cusLayout.addComponent(nameLabel, "m_title");

				// 推荐项目
				final Button commendButton = new Button("推荐项目");
				commendButton.addStyleName(Reindeer.BUTTON_LINK);
				commendButton.addStyleName("new_button");
				commendButton.addStyleName("w_current");
				commendButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						Notification.show("推荐项目",
								Notification.Type.HUMANIZED_MESSAGE);
					}
				});
				cusLayout.addComponent(commendButton, "m_recommend");

				final Button newestButton = new Button("最新项目");
				newestButton.addStyleName(Reindeer.BUTTON_LINK);
				newestButton.addStyleName("new_button");
				newestButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						newestButton.addStyleName("w_current");
						commendButton.removeStyleName("w_current");
						Notification.show("最新项目",
								Notification.Type.HUMANIZED_MESSAGE);
					}
				});
				cusLayout.addComponent(newestButton, "m_newest");

				Table listTable = new Table();
				listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
				listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
				listTable.setWidth(100, Unit.PERCENTAGE);
				listTable.setHeight(100, Unit.PERCENTAGE);

				LazyLoadingQuery lazyLoadingQuery = new PublicProjectListQuery(dictDef.getValue());
				
				LazyLoadingContainer listContainer = new LazyLoadingContainer(
						lazyLoadingQuery, 10);
				listTable.setContainerDataSource(listContainer);
				if (lazyLoadingQuery.size() < 10) {
					listTable.setPageLength(0);
				} else {
					listTable.setPageLength(10);
				}

				listTable.addGeneratedColumn("",
						new ProjectHeadColumnGenerator());
				TableHandler.setTableNoHead(listTable);
				listTable.setImmediate(false);
				listTable.setSelectable(false);

				cusLayout.addComponent(listTable, "m_project_table");

				addComponent(cusLayout);

			}
		});

	}

	public void initLinkButton(Button addButton) {
		addButton.addStyleName(Reindeer.BUTTON_LINK);
		addButton.addStyleName("new_button");
		addButton.addStyleName("w_current");
	}

	public GridLayout initSecondGrid(final Table source, final Object itemId,
			final OutsourcingProject project) {
		
		final ProjectParticipant currentPart = projectManagerService.queryUserParticipant(project,
				loginUserId,null);
		
		GridLayout secondGrid = new GridLayout(2, 3);
		secondGrid.setSizeFull();
		secondGrid.setMargin(true);
		secondGrid.setSpacing(true);
		secondGrid.addStyleName(Reindeer.SPLITPANEL_SMALL);
		

		Label desLabel = new Label(project.getName());
		desLabel.addStyleName("label_title_head");
		secondGrid.addComponent(desLabel, 0, 0, 1, 0);

		secondGrid.addComponent(new HorizontalLayout() {
			{
				setSizeFull();
				//setSpacing(true);
				Label desLabel = new Label("金额：");
				addComponent(desLabel);
				String amontTitle = project.getBounty() + "元";
				Label amontLabel = new Label(amontTitle);
				amontLabel.addStyleName("label_red");
				addComponent(amontLabel);
			}
		}, 0, 1);

		secondGrid.addComponent(new HorizontalLayout() {
			{
				setSizeFull();
				//setSpacing(true);
				Label desLabel = new Label("目前参与人数：");
				addComponent(desLabel);
				String amontTitle = "20人";
				Label amontLabel = new Label(amontTitle);
				amontLabel.addStyleName("label_red");
				addComponent(amontLabel);
			}
		}, 1, 1);

		secondGrid.addComponent(new HorizontalLayout() {
			{
				setSizeFull();
				//setSpacing(true);
			    setMargin(true);
				String caption = "加入";
				Button addButton = new Button(caption);
				addButton.setDisableOnClick(true);
				
				if(currentPart != null){//已经加入过了允许在加入
					addButton.setEnabled(false);
				}
				initLinkButton(addButton);

				addButton.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						//加入成功后，弹出任务的填写页面
						projectManagerService
								.participateProject(project);
						noticeAddSucess(project.getProcInstId());
					}

				});
				addComponent(addButton);
			}
		}, 0, 2);

		secondGrid.addComponent(new HorizontalLayout() {
			{
				setSizeFull();
				setMargin(true);
				//setSpacing(true);
				Button pubInWorkButton = new Button("提交作品");
				initLinkButton(pubInWorkButton);
				pubInWorkButton.setEnabled(false);
				//参与成功,则可以进行提交作品操作
				if(currentPart != null && EntityDictionary.PARTICIPANTS_STATUS_START.equals(currentPart.getHandleStatus())){
					pubInWorkButton.setEnabled(true);
				}

				pubInWorkButton.addStyleName(Reindeer.BUTTON_SMALL);
				// editButton.setDisableOnClick(true);
				pubInWorkButton
						.addClickListener(new ClickListener() {
							public void buttonClick(ClickEvent event) {
								// WW_TODO 后台进行修改
								Item item = source.getItem(itemId);
								String id = (String) item
										.getItemProperty("id")
										.getValue();
								NewProjectWindow newProjectWindow = new NewProjectWindow(
										id);

								newProjectWindow
										.addListener(new SubmitEventListener() {
											private static final long serialVersionUID = 1L;

											@Override
											protected void cancelled(
													SubmitEvent event) {

											}

											@Override
											protected void submitted(
													SubmitEvent event) {
												if (event.getData() != null) {

												}
											}
										});

								// ViewToolManager.showPopupWindow(newProjectWindow);
							}
						});
				addComponent(pubInWorkButton);
			}
		}, 1, 2);
		return secondGrid;
	}

	public class ProjectHeadColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId,
				Object columnId) {
			CustomLayout cusLayout = new CustomLayout("one-project");
			final OutsourcingProject project = BinderHandler.getTableBean(
					source, itemId);
			final GridLayout grid = new GridLayout(2, 2);
			grid.addStyleName(Reindeer.SPLITPANEL_SMALL);
			//grid.setSizeFull();
			// grid.setMargin(true);
			grid.setColumnExpandRatio(0, 0.6f);
			grid.setColumnExpandRatio(1, 0.4f);

			VerticalLayout firstLayout = new VerticalLayout();
			// firstLayout.setMargin(true);
			// 头像
			Image image = new Image(null, new ThemeResource(
					"img/project/exm.jpg"));
			firstLayout.addComponent(image);
			grid.addComponent(firstLayout, 0, 0);

			// 第二档
			VerticalLayout secondLayout = new VerticalLayout() {
				{
					setSizeFull();
					setSpacing(true);
					setMargin(true);

					GridLayout secondGrid = initSecondGrid(source, itemId,
							project);
					addComponent(secondGrid);
					setExpandRatio(secondGrid, 1);
					secondGrid.setRowExpandRatio(0, 0.4f);
					secondGrid.setRowExpandRatio(1, 0.4f);
					secondGrid.setRowExpandRatio(2, 0.6f);
				}
			};
			grid.addComponent(secondLayout, 1, 0);
			
			grid.addComponent(new HorizontalLayout() {
				{
					setHeight("20px");
					setWidth("100%");
					
					addStyleName("layout_blank");
				}
			}, 0, 1,1,1);
			return grid;
		}
	}
	public void noticeAddSucess(String processInstanceId) {
		//查询当前流程的，这个用户的任务，是否存在，如果有进行添加
		List<Task> loggedInUsersTasks = taskService.createTaskQuery()
				.taskAssignee(LoginHandler.getLoggedInUser().getId())
				.processInstanceId(processInstanceId).list();
		if (loggedInUsersTasks.size() > 0) {
			String message = "参与成功，您可以进行作品的提交";
			Notification.show(message, Notification.Type.HUMANIZED_MESSAGE);
		}else {
			String message = "参与失败，请等待处理...";
			Notification.show(message, Notification.Type.HUMANIZED_MESSAGE);
		}
	}
}
