package com.klwork.explorer.ui.business.project;

import java.util.HashMap;

import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.business.query.ProjectListQuery;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableFieldCache;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.util.ThemeImageColumnGenerator;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ProjectList extends CustomComponent {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;

	final HashMap<Object, String> projectNames = new HashMap<Object, String>();
	
	private TableFieldCache fieldCache = new TableFieldCache();
	//当前table id
	private String currentProjectId;
	ProjectMain projectMain;
	ProjectService projectService;

	public ProjectList(ProjectMain projectMain) {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.projectMain = projectMain;
		projectService = ViewToolManager.getBean("projectService");
		init();
		setSizeFull();
	}

/*	public ProjectList(ProjectMain projectMain, String projectId) {
		this(projectMain);
		this.projectId = projectId;
	}*/

	protected void init() {
		VerticalLayout layout = new VerticalLayout();
		// layout.setMargin(true);
		layout.setSizeFull();
		setCompositionRoot(layout);
		initHead(layout);
		layout.setSpacing(true);
		// 项目table
		initProjectList(layout);
	}

	

	private void initProjectList(VerticalLayout layout) {
		final Table listTable = new Table();
		listTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
		listTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);
		layout.addComponent(listTable);
		layout.setExpandRatio(listTable, 1);
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
				//final ProjectListItem c = (ProjectListItem)container.getItem(itemId);
				final Project c = BinderHandler.getTableBean(
						listTable, itemId);
				final Object key = itemId.toString()+propertyId.toString();
				
				if ("name".equals(propertyId)) {
					if (fieldCache.getPropertyFieldFromCache(itemId, propertyId) != null) {
						tf = fieldCache.getPropertyFieldFromCache(itemId, propertyId);
						return tf;
					}
					
					tf = new TextField((String) propertyId);
					//tf.setPropertyDataSource(c.getItemProperty(propertyId));
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
							if(!oldNameValue.equals(c.getName())){
								projectService.updateProjectName(c.getId(),c.getName());
							}
							/*for (Field f : itemMap.values()) {// 所有字段只读
								f.setReadOnly(true);
								String oldNameValue = projectNames.get(f);
								if(!oldNameValue.equals(f.getValue())){
									projectService.updateProjectName(c.getItemProperty("id").toString(),f.getValue().toString());
								}
							}*/
						}
					});
					// 把name设置到cache中
					fieldCache.savePrppertyFieldToCache(itemId, propertyId, tf);
					projectNames.put(key, c.getName());
				}else {
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

		// Create column header
		listTable.addGeneratedColumn("icon", new ThemeImageColumnGenerator(
				Images.TASK_22));
		listTable.setColumnWidth("icon", 22);
		
		

		listTable.addContainerProperty("name", String.class, "");
		listTable.addGeneratedColumn("edit", new ProjectEditColumnGenerator());
		
		TableHandler.setTableNoHead(listTable);
		// 默认选中able处理
		//TableHandler.disposeSelectTable(listTable, listContainer, projectId);
	}
	
	public class ProjectEditColumnGenerator implements ColumnGenerator {
		/**
		 * 
		 */
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId, Object columnId) {
			
			Button editButton = new Button("");
			editButton.addStyleName(Reindeer.BUTTON_LINK);
			editButton.setIcon(Images.EDIT);
			//editButton.setDisableOnClick(true);
			editButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					// WW_TODO 后台进行修改
					Item item = source.getItem(itemId);
					String id = (String)item.getItemProperty("id").getValue();
					NewProjectWindow newProjectWindow = new NewProjectWindow(id);
					
					
					newProjectWindow.addListener(new SubmitEventListener() {
					    private static final long serialVersionUID = 1L;
					
						@Override
						protected void cancelled(SubmitEvent event) {
							
						}

						@Override
						protected void submitted(SubmitEvent event) {
							if(event.getData() != null){
							
							}
						}
					});

					ViewToolManager.showPopupWindow(newProjectWindow);
				}
			});
			return editButton;
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
					//保存当前项目id
					currentProjectId = id;
					String name = (String) item.getItemProperty("name").getValue();
					System.out.println(id);
					if (id != null) {
						projectMain.initRightContent(id,name);
					}
				} else {
					System.out.println("erro....null?");
				}
			}
		};
	}

	private void initHead(VerticalLayout layout) {
		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setWidth(100, Unit.PERCENTAGE);
		headerLayout.setMargin(true);
		layout.addComponent(headerLayout);
		initTitle(headerLayout);
		initAddProjectButton(headerLayout);
	}

	protected void initTitle(HorizontalLayout headerLayout) {
		Label title = new Label("我的所有项目");
		title.addStyleName(ExplorerLayout.STYLE_H3);
		title.setWidth(100, Unit.PERCENTAGE);
		headerLayout.addComponent(title);
		headerLayout.setExpandRatio(title, 1.0f);
	}

	protected void initAddProjectButton(HorizontalLayout headerLayout) {
		Button addButton = new Button();
		addButton.addStyleName(ExplorerLayout.STYLE_ADD);
		headerLayout.addComponent(addButton);

		addButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8050449471041932066L;

			public void buttonClick(ClickEvent event) {
				final NewProjectWindow newProjectWindow = new NewProjectWindow(null);
				ViewToolManager.showPopupWindow(newProjectWindow);
			}
		});
	}
}
