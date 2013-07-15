package com.klwork.explorer.ui.business.project;

import java.util.HashMap;
import java.util.Map;

import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.query.ProjectListQuery;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableFieldCache;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.Action;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

public class ProjectList extends DetailPanel {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;

	final HashMap<Object, String> projectNames = new HashMap<Object, String>();

	private TableFieldCache fieldCache = new TableFieldCache();
	// 当前table id
	private String currentProjectId;
	private Object currentItemId;

	AbstractTabViewPage  mainPage;
	ProjectService projectService;
	protected HorizontalLayout projectsLayout;

	public ProjectList(AbstractTabViewPage tabPage) {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = tabPage;
		projectService = ViewToolManager.getBean("projectService");
	}

	/*
	 * public ProjectList(ProjectMain projectMain, String projectId) {
	 * this(projectMain); this.projectId = projectId; }
	 */
	@Override
	public void attach() {
		super.attach();
		initPageTitle();
		// 项目table
		initProjectList();
	}

	private void initProjectList() {
		HorizontalLayout projectsHeader = new HorizontalLayout();
		projectsHeader.setSpacing(true);
		projectsHeader.setWidth(100, Unit.PERCENTAGE);
		projectsHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addDetailComponent(projectsHeader);

		// 组的标题
		initProjectsTitle(projectsHeader);
		// 新增成员按钮
		initAddProjectButton(projectsHeader);

		projectsLayout = new HorizontalLayout();
		projectsLayout.setWidth(100, Unit.PERCENTAGE);
		addDetailComponent(projectsLayout);

		initProjectsTable();
	}

	private void initProjectsTitle(HorizontalLayout membersHeader) {
		Label usersHeader = new Label("项目列表");
		usersHeader.addStyleName(ExplorerLayout.STYLE_H3);
		membersHeader.addComponent(usersHeader);

	}

	public void initProjectsTable() {
		final Table listTable = new Table();
		/*listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);*/
		
		listTable.setWidth(100, Unit.PERCENTAGE);
		listTable.setHeight(400, Unit.PIXELS);

		listTable.setEditable(false);
		listTable.setSelectable(false);
		listTable.setSortEnabled(true);
		
		projectsLayout.addComponent(listTable);
		/*
		 * layout.addComponent(listTable); layout.setExpandRatio(listTable, 1);
		 */
		// Listener to change right panel when clicked on a task

		listTable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			private static final long serialVersionUID = -348059189217149508L;

			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()) {
					Object itemId = event.getItemId();
					fieldCache.setFieldReadOnly(itemId, false);
				}
			}
		});
		listTable.addValueChangeListener(getListSelectionListener(listTable));
		listTable.setEditable(true);
		listTable.setTableFieldFactory(new TableFieldFactory() {
			private static final long serialVersionUID = -5741977060384915110L;

			public Field createField(Container container, final Object itemId,
					final Object propertyId, Component uiContext) {
				TextField tf = null;
				final Object objectId = itemId;
				// final ProjectListItem c =
				// (ProjectListItem)container.getItem(itemId);
				final Project c = BinderHandler.getTableBean(listTable, itemId);
				final Object key = itemId.toString() + propertyId.toString();

				if ("name".equals(propertyId)) {
					if (fieldCache
							.getPropertyFieldFromCache(itemId, propertyId) != null) {
						tf = fieldCache.getPropertyFieldFromCache(itemId,
								propertyId);
						return tf;
					}

					tf = new TextField((String) propertyId);
					tf.setImmediate(true);
					tf.setReadOnly(true);
					tf.setWidth("100%");

					tf.addFocusListener(new FocusListener() {
						private static final long serialVersionUID = 1006388127259206641L;

						public void focus(FocusEvent event) {
							fieldCache.setFieldReadOnly(itemId, false);
						}

					});
					tf.addBlurListener(new BlurListener() {
						private static final long serialVersionUID = -4497552765206819985L;

						public void blur(BlurEvent event) {
							fieldCache.setFieldReadOnly(itemId, true);

							String oldNameValue = projectNames.get(key);
							if (!oldNameValue.equals(c.getName())) {
								projectService.updateProjectName(c.getId(),
										c.getName());
							}
						}
					});
					// 把name设置到cache中
					fieldCache.savePrppertyFieldToCache(itemId, propertyId, tf);
					projectNames.put(key, c.getName());
				} else {
					tf = new TextField((String) propertyId);
					tf.setData(itemId);
					tf.setImmediate(true);
					// tf.setSizeFull();
					// tf.setSizeUndefined();
					tf.setReadOnly(true);
				}
				return tf;
			}
		});

		LazyLoadingQuery lazyLoadingQuery = new ProjectListQuery();
		LazyLoadingContainer listContainer = new LazyLoadingContainer(
				lazyLoadingQuery, 10);
		listTable.setContainerDataSource(listContainer);
		
		listTable.addContainerProperty("id", String.class, null);
		listTable.addContainerProperty("name", String.class, "");
		listTable.addGeneratedColumn("edit", new ProjectEditColumnGenerator());
	}

	public class ProjectEditColumnGenerator implements ColumnGenerator {
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
			
			Button editButton = new Button("");
			editButton.addStyleName(Reindeer.BUTTON_LINK);
			editButton.setIcon(Images.EDIT);
			// editButton.setDisableOnClick(true);
			editButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					// WW_TODO 后台进行修改
					Item item = source.getItem(itemId);
					String id = (String) item.getItemProperty("id").getValue();
					NewProjectWindow newProjectWindow = new NewProjectWindow(id);

					newProjectWindow.addListener(new SubmitEventListener() {
						private static final long serialVersionUID = 1L;

						@Override
						protected void cancelled(SubmitEvent event) {

						}

						@Override
						protected void submitted(SubmitEvent event) {
							if (event.getData() != null) {

							}
						}
					});

					ViewToolManager.showPopupWindow(newProjectWindow);
				}
			});
			
			buttonLayout.addComponent(editButton);
			
			
			editButton = new Button("项目计划");
			editButton.addStyleName(Reindeer.BUTTON_LINK);
			editButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					// WW_TODO 后台进行修改
					Item item = source.getItem(itemId);
					String id = (String) item.getItemProperty("id").getValue();
					String name = (String) item.getItemProperty("name").getValue();
					selectedHandle(id, name);
				
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
					currentItemId = itemId;
					String id = (String) item.getItemProperty("id").getValue();
					// 保存当前项目id
					currentProjectId = id;
					String name = (String) item.getItemProperty("name")
							.getValue();
					System.out.println(id);
					if (id != null) {
						Map<String, String> parameter = new HashMap<String, String>();
						parameter.put("id", id);
						parameter.put("name", name);
						/*mainPage.setLeftParameter(parameter);
						mainPage.initRightComponent();*/
						selectedHandle(id, name);
						// projectMain.initRightContent(id,name);
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

		Label groupName = new Label("项目管理");
		groupName.setSizeUndefined();
		groupName.addStyleName(Reindeer.LABEL_H2);
		layout.addComponent(groupName);
		layout.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(groupName, 1.0f);
	}

	protected void initAddProjectButton(HorizontalLayout headerLayout) {
		
		Button addButton = new Button();
		addButton.setCaption("新增");
		// addButton.addStyleName(ExplorerLayout.STYLE_ADD);
		headerLayout.addComponent(addButton);

		addButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8050449471041932066L;

			public void buttonClick(ClickEvent event) {
				final NewProjectWindow newProjectWindow = new NewProjectWindow(
						null);
				ViewToolManager.showPopupWindow(newProjectWindow);
			}
		});
	}

	public void openEdit() {
		fieldCache.setFieldReadOnly(currentItemId, false);
	}

	public void selectedHandle(String id, String name) {
		mainPage.addTabSpecial(new ProjectTreeTable(id,mainPage), name);
	}

	// Keyboard navigation
	class KbdHandler implements com.vaadin.event.Action.Handler {
		private static final long serialVersionUID = -2993496725114954915L;
		Action f2 = new ShortcutAction("F2", ShortcutAction.KeyCode.F2, null);

		@Override
		public Action[] getActions(Object target, Object sender) {
			return new Action[] { f2 };
		}

		@Override
		public void handleAction(Action action, Object sender, Object target) {
			if (target instanceof Table) {
				Table t = (Table) target;
				Integer selectedIndex = (Integer) t.getValue();
				openEdit();
			}
		}
	}
}
