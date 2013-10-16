package com.klwork.explorer.ui.business.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItemClickEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItemClickListener;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedListener;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedListener.TableListener;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedOnTableFooterEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedOnTableHeaderEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuOpenedOnTableRowEvent;

import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.model.Todo;
import com.klwork.business.domain.model.TodoQuery;
import com.klwork.business.domain.service.TodoService;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.social.WeiboSendPopupWindow;
import com.klwork.explorer.ui.custom.ConfirmationDialogPopupWindow;
import com.klwork.explorer.ui.custom.DetailPanel;
import com.klwork.explorer.ui.event.ConfirmationEvent;
import com.klwork.explorer.ui.event.ConfirmationEventListener;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.klwork.explorer.ui.handler.TableFieldCache;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.NewTodoToTaskPopupWindow;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.HierarchicalContainer;
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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.TreeTable;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ProjectTreeTable extends DetailPanel {
	private static final long serialVersionUID = 7916755916967574384L;
	private String projectId;
	protected I18nManager i18nManager;

	AbstractTabViewPage  mainPage;
	HashMap<String, BeanItem<Todo>> inventoryStore = new HashMap<String, BeanItem<Todo>>();
	BeanItem<Todo> testBeanItem = null;
	Property<String> integerPropety = null;
	private final ArrayList<Object> visibleColumnIds = new ArrayList<Object>();
	private final ArrayList<String> visibleColumnLabels = new ArrayList<String>();
	
	private TableFieldCache fieldCache = new TableFieldCache();
	
	final TreeTable mainTreeTable = new TreeTable();

	private BeanItem<Todo> currentBeanItem;
	HierarchicalContainer hContainer = null;

	private FieldGroup scheduleEventFieldGroup = new FieldGroup();
	protected HorizontalLayout projectPlans;
	protected TextField addTxt;
	protected NativeSelect changeRoot;
	private String todoId;

	VerticalLayout bottomLayout;
	//
	TodoService todoService;

	public ProjectTreeTable(String prgId, AbstractTabViewPage projectMain) {
		super(true);
		//System.out.println("ProjectTreeTable 初始化");
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = projectMain;
		this.projectId = prgId;
		todoService = (TodoService) SpringApplicationContextUtil.getContext()
				.getBean("todoService");
//		init();
	}
	
	@Override
	protected void initUI() {
		//初始化头部
		initHead();
		// 主界面
		initMain();

	}

	@SuppressWarnings("unchecked")
	private Todo getFieldGroupTodo() {
		BeanItem<Todo> item = (BeanItem<Todo>) scheduleEventFieldGroup
				.getItemDataSource();
		Todo todo = item.getBean();
		initTodoProId(todo);
		return todo;
	}

	public void initTodoProId(Todo todo) {
		// WW_TODO 保存是设置项目id和用户
		if (!StringTool.judgeBlank(todo.getProId())) {
			todo.setProId(projectId);
		}
		if (todo.getAssignedUser() == null) {
			todo.setAssignedUser(LoginHandler.getLoggedInUser().getId());
		}
	}

	private void initHead() {
		// Header
		HorizontalLayout header = new HorizontalLayout();
		header.setWidth(100, Unit.PERCENTAGE);
		header.addStyleName(ExplorerLayout.STYLE_TITLE_BLOCK);
		header.setSpacing(true);
		header.setMargin(new MarginInfo(false, false, true, false));
		addDetailComponent(header);
		/**
		 * final Button saveButton = new Button(
		 * i18nManager.getMessage(Messages.PROFILE_SAVE));
		 * saveButton.setIcon(Images.SAVE); saveButton.addClickListener(new
		 * ClickListener() { public void buttonClick(ClickEvent event) {
		 * commit(); }
		 * 
		 * });
		 * 
		 * header.addComponent(saveButton);
		 * header.setComponentAlignment(saveButton, Alignment.MIDDLE_RIGHT);
		 */
		Embedded groupImage = new Embedded(null, Images.GROUP_50);
		header.addComponent(groupImage);
		Label groupName = new Label("项目计划");
		groupName.setSizeUndefined();
		groupName.addStyleName(Reindeer.LABEL_H2);
		header.addComponent(groupName);
		header.setComponentAlignment(groupName, Alignment.MIDDLE_LEFT);
		header.setExpandRatio(groupName, 1.0f);
		
	}
	
	private void initProjectPlanTitle(HorizontalLayout membersHeader) {
		Label usersHeader = new Label("项目计划列表");
		usersHeader.addStyleName(ExplorerLayout.STYLE_H3);
		membersHeader.addComponent(usersHeader);
		membersHeader.setComponentAlignment(usersHeader, Alignment.MIDDLE_RIGHT);
		membersHeader.setExpandRatio(usersHeader, 1.0f);
		membersHeader.setMargin(true);
	}
	
	private void initAddProjectPlanButton(HorizontalLayout headerLayout) {
		Map<Object, String> data = new HashMap<Object, String>();
		data.put("1", "给根节点处增加子节点");
		data.put("2", "给选中的根节点增加子节点");
		changeRoot = CommonFieldHandler.createNativeSelect(null, data,"1");
		headerLayout.addComponent(changeRoot);
		headerLayout.setComponentAlignment(changeRoot, Alignment.MIDDLE_LEFT);
		addTxt = new TextField();
		addTxt.setWidth("200px");
		addTxt.setInputPrompt("在这里快速输入你的计划");
		headerLayout.addComponent(addTxt);
		headerLayout.setComponentAlignment(addTxt, Alignment.MIDDLE_LEFT);
		Button addButton = new Button();
		addButton.setCaption("快速新加");
		headerLayout.addComponent(addButton);
		headerLayout.setComponentAlignment(addButton, Alignment.MIDDLE_LEFT);
		//headerLayout.setExpandRatio(addButton, 0.4f);
		headerLayout.setSpacing(true);
		addButton.addClickListener(new ClickListener() {
			private static final long serialVersionUID = -8050449471041932066L;

			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) {
				if(changeRoot.getValue().equals("1")){
					if(projectId!=null){
						createNewAct();
					}
				}else if (changeRoot.getValue().equals("2")){
					if(StringTool.judgeBlank(todoId)){
						createNewSubAct();
					}
				}
			}
		});
		
		Button shareWeiboButton = new Button();
	    shareWeiboButton.setCaption("分享到微博");
	    headerLayout.addComponent(shareWeiboButton);
	    headerLayout.setComponentAlignment(shareWeiboButton, Alignment.MIDDLE_LEFT);
	    headerLayout.setExpandRatio(shareWeiboButton, 0.4f);
	    
	    shareWeiboButton.addClickListener(new ClickListener() {
	      public void buttonClick(ClickEvent event) {
	    	  TodoSharePopupWindow newWeiboPopupWindow = new TodoSharePopupWindow(projectId);
	        ViewToolManager.showPopupWindow(newWeiboPopupWindow);
	      }
	    });
	}

	private void initMain() {
		//初始化项目计划列表容器
		HorizontalLayout projectsHeader = new HorizontalLayout();
		projectsHeader.setSpacing(true);
		projectsHeader.setWidth(100, Unit.PERCENTAGE);
		projectsHeader.addStyleName(ExplorerLayout.STYLE_DETAIL_BLOCK);
		addDetailComponent(projectsHeader);
		//增加treeTable标题
		initProjectPlanTitle(projectsHeader);
		//增加快速增加任务计划按钮
		initAddProjectPlanButton(projectsHeader);
		//实例化项目计划treeTable
		initMainTreeTable();
	}
	
	private void initMainTreeTable(){
		projectPlans = new HorizontalLayout();
		projectPlans.setWidth(100, Unit.PERCENTAGE);
		addDetailComponent(projectPlans);
		projectPlans.addComponent(mainTreeTable);
		// init tabletree
		mainTreeTable.setEditable(true);
		mainTreeTable.setImmediate(true);
		mainTreeTable.setWidth("100%");
		mainTreeTable.setHeight("500px");
		mainTreeTable.setColumnExpandRatio("name", 1);
		//mainTreeTable.setColumnExpandRatio("edit", 0.1f);
		//mainTreeTable.setColumnExpandRatio("edit", 0.2f);
		mainTreeTable.setSelectable(true);
		mainTreeTable.setColumnReorderingAllowed(true);

		// 数据构造
		hContainer = createTreeContent();
		mainTreeTable.setContainerDataSource(hContainer);

		// table的表现信息,绑定数据
		initTableField(mainTreeTable);

		// 拖动
		// handDrop(ttable);
		// 设置表头的显示(那些字段可见及其中文)
		setTableHeadDisplay(mainTreeTable);

		// 设置表行的双击操作，和其他监听
		setTableListener(mainTreeTable);

		Object hierarchyColumnId = "name";
		// 那个列为树形的
		mainTreeTable.setHierarchyColumn(hierarchyColumnId);

		// 右键功能
		rightClickHandler(mainTreeTable);
		mainTreeTable.setImmediate(true);
		// 展开所有节点
		collapsedAll(mainTreeTable);
	}

	private void collapsedAll(final TreeTable ttable) {
		for (Object item : ttable.getItemIds().toArray()) {
			collapsedSub(ttable, item);
		}
	}

	private void collapsedSub(final TreeTable ttable, Object item) {
		ttable.setCollapsed(item, false);
		if (ttable.hasChildren(item)) {
			for (Object a : ttable.getChildren(item).toArray()) {
				ttable.setCollapsed(a, false);
				collapsedSub(ttable, a);
			}
		}
	}

	private void setTableListener(final TreeTable ttable) {
		ttable.addItemClickListener(new ItemClickEvent.ItemClickListener() {
			private static final long serialVersionUID = -348059189217149508L;

			@Override
			public void itemClick(ItemClickEvent event) {
				Object source = event.getItemId();
				todoId = event.getItem().getItemProperty("id").getValue().toString();
				if (event.isDoubleClick()) {
					// Notification.show(event.getSource().toString());
//					Object source = event.getItemId();
					fieldCache.setFieldFocus(source);
				}
			}
		});

		ttable.addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				Object value = ttable.getValue();
				if (value instanceof BeanItem) {
					currentBeanItem = (BeanItem<Todo>) value;
				}
				// 同时更新下面的数据
				// updateBottomLayout(bottomLayout);
			}
		});

		addListener(new SubmitEventListener() {
			private static final long serialVersionUID = 1L;

			protected void submitted(SubmitEvent event) {
			}

			protected void cancelled(SubmitEvent event) {
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void rightClickHandler(final TreeTable ttable) {
		ContextMenu tableContextMenu = new ContextMenu();
		tableContextMenu.addContextMenuTableListener(createOpenListener());
		tableContextMenu.addItem("新建子任务").addItemClickListener(
				createNewSubActItemClick());
		tableContextMenu.addItem("新建任务").addItemClickListener(
				createNewActItemClick());
		tableContextMenu.setAsTableContextMenu(ttable);
	}

	private ContextMenuItemClickListener createNewActItemClick() {
		return new ContextMenu.ContextMenuItemClickListener() {
			@Override
			public void contextMenuItemClicked(
					ContextMenuItemClickEvent event) {
				createNewAct();
			}
		};
	}

	private ContextMenuItemClickListener createNewSubActItemClick() {
		return new ContextMenu.ContextMenuItemClickListener() {
			@Override
			public void contextMenuItemClicked(
					ContextMenuItemClickEvent event) {
				createNewSubAct();
			}
		};
	}
	
	private void createNewAct() {
		Todo newTodo = todoService.newTodo(addTxt.getValue());
		newTodo.setProId(projectId);
		initTodoProId(newTodo);
		BeanItem<Todo> newbeanItem = new BeanItem<Todo>(newTodo);
		Item nItem = hContainer.addItem(newbeanItem);
		hContainer.setChildrenAllowed(newbeanItem, false);
		copyBeanValueToContainer(hContainer, newbeanItem);
		// 设置父节点
		hContainer.setParent(newbeanItem, null);
		//快速新增计划后，将计划保存到数据库
		List<Todo> todos = new ArrayList<Todo>();
		todos.add(newTodo);
		todoService.saveTodoList(todos);
	}
	private void createNewSubAct() {
		Item parentItem = hContainer.getItem(currentBeanItem);
		Todo newTodo = todoService.newTodo(addTxt.getValue());
		initTodoProId(newTodo);
		@SuppressWarnings("rawtypes")
		BeanItem newbeanItem = new BeanItem<Todo>(newTodo);
		Item nItem = hContainer.addItem(newbeanItem);
		hContainer.setChildrenAllowed(newbeanItem, false);
		hContainer.setChildrenAllowed(currentBeanItem, true);
		copyBeanValueToContainer(hContainer, newbeanItem);
		// 设置父节点
		hContainer.setParent(newbeanItem, currentBeanItem);
		Todo paretTodo = currentBeanItem.getBean();
		// 新的记录设置为
		newTodo.setPid(todoId);
		hContainer.getContainerProperty(newbeanItem, "pid")
				.setValue(paretTodo.getId());
		// 老的记录
		paretTodo.setIsContainer(1);
		hContainer.getContainerProperty(currentBeanItem,
				"isContainer").setValue(1);
		List<Todo> todos = new ArrayList<Todo>();
		todos.add(newTodo);
		todos.add(paretTodo);
		todoService.saveTodoList(todos);
	}

	private TableListener createOpenListener() {
		ContextMenuOpenedListener.TableListener openListener = new ContextMenuOpenedListener.TableListener() {

			@Override
			public void onContextMenuOpenFromRow(
					ContextMenuOpenedOnTableRowEvent event) {
				Object itemId = event.getItemId();
				if (itemId instanceof BeanItem) {
					currentBeanItem = (BeanItem<Todo>) itemId;
				}
				// contextMenu.open(event.getX(), event.getY());
			}

			@Override
			public void onContextMenuOpenFromHeader(
					ContextMenuOpenedOnTableHeaderEvent event) {

			}

			@Override
			public void onContextMenuOpenFromFooter(
					ContextMenuOpenedOnTableFooterEvent event) {

			}
		};
		return openListener;
	}

	private void openEdit(TreeTable ttable, final Object itemId) {
		fieldCache.setFieldReadOnly(itemId, false);
		ttable.select(itemId);
	}

	private void initTableField(final TreeTable ttable) {
		ttable.setTableFieldFactory(new TableFieldFactory() {
			private static final long serialVersionUID = -5741977060384915110L;

			public Field createField(Container container, Object itemId,
					final Object propertyId, Component uiContext) {
				TextField tf = null;
				if ("name".equals(propertyId)) {
					final BeanItem<Todo> beanItem = (BeanItem<Todo>) itemId;

					if (fieldCache.getPropertyFieldFromCache(itemId, propertyId) != null) {
						tf = fieldCache.getPropertyFieldFromCache(itemId, propertyId);
						// bindFieldToObje(itemId, propertyId, tf, beanItem);
						return tf;
					}

					tf = new TextField((String) propertyId);
					// bindFieldToObje(itemId, propertyId, tf, beanItem);
					// Needed for the generated column
					tf.setImmediate(true);
					tf.setReadOnly(true);
					tf.setWidth("100%");
					tf.addFocusListener(new FocusListener() {
						private static final long serialVersionUID = 1006388127259206641L;

						public void focus(FocusEvent event) {
							openEdit(ttable, beanItem);
						}

					});
					tf.addBlurListener(new BlurListener() {
						private static final long serialVersionUID = -4497552765206819985L;

						public void blur(BlurEvent event) {
							fieldCache.setFieldReadOnly(beanItem, true);
							// copy toBean
							Todo todo = tableItemToBean(currentBeanItem);
							List<Todo> l = new ArrayList();
							l.add(todo);
							todoService.saveTodoList(l);

							// reflashBottom();
						}

					});
					// 把name设置到cache中
					fieldCache.savePrppertyFieldToCache(itemId, propertyId, tf);
				} else {
					tf = new TextField((String) propertyId);
					tf.setData(itemId);
					tf.setImmediate(true);
					// tf.setSizeFull();
					// tf.setSizeUndefined();
					tf.setWidth(50, Unit.PIXELS);
					tf.setReadOnly(true);
				}

				return tf;
			}

			private void bindFieldToObje(Object itemId,
					final Object propertyId, TextField tf,
					final BeanItem<Todo> beanItem) {
				BeanItem<Todo> f = inventoryStore.get(getBeanSign(beanItem));
				tf.setPropertyDataSource(f.getItemProperty(propertyId));
				System.out.println(propertyId + "---------" + itemId);
			}


		}

		);

	}
	
	private void setTableHeadDisplay(final TreeTable ttable) {

		visibleColumnIds.add("name");
		visibleColumnIds.add("priority");
		visibleColumnIds.add("complete");
		visibleColumnIds.add("endTime");
		visibleColumnIds.add("useUp");
		visibleColumnIds.add("due");
		visibleColumnIds.add("status");
		visibleColumnIds.add("tags");
		visibleColumnIds.add("type");
		visibleColumnIds.add("edit");

		visibleColumnLabels.add("标题");
		visibleColumnLabels.add("！");
		visibleColumnLabels.add("%");
		visibleColumnLabels.add("结束时间");
		visibleColumnLabels.add("耗尽");
		visibleColumnLabels.add("到期");
		visibleColumnLabels.add("状态");
		visibleColumnLabels.add("标签");
		visibleColumnLabels.add("类型");
		visibleColumnLabels.add("操作");

		ttable.addGeneratedColumn("edit", new ValueEditColumnGenerator());
		ttable.setVisibleColumns(visibleColumnIds.toArray());
		ttable.setColumnHeaders(visibleColumnLabels.toArray(new String[0]));

	}

	/**
	 * table属性的创建
	 * 
	 * @return
	 */
	public HierarchicalContainer createTreeContent() {
		HierarchicalContainer container = new HierarchicalContainer();
		container.addContainerProperty("priority", String.class, "");
		container.addContainerProperty("complete", String.class, "");
		container.addContainerProperty("endTime", String.class, "");
		container.addContainerProperty("useUp", String.class, "");
		container.addContainerProperty("due", String.class, "");
		container.addContainerProperty("status", String.class, "");
		container.addContainerProperty("tags", String.class, "");
		container.addContainerProperty("type", String.class, "");
		container.addContainerProperty("name", String.class, "");
		container.addContainerProperty("id", String.class, "");
		container.addContainerProperty("pid", String.class, "");
		container.addContainerProperty("container", Boolean.class, null);
		container.addContainerProperty("isContainer", Integer.class, null);
		initContainerData(container, getQuery("-1"), null);
		return container;
	}

	private TodoQuery getQuery(String pid) {
		TodoQuery query = new TodoQuery();
		query.setProId(projectId).setPid(pid).setOrderBy(" pid asc,id asc");
		return query;
	}

	private void initContainerData(HierarchicalContainer container,
			TodoQuery query, BeanItem<Todo> parent) {
		List<Todo> beanList = todoService.findTodoByQueryCriteria(query, null);
		for (Iterator iterator = beanList.iterator(); iterator.hasNext();) {
			Todo todo = (Todo) iterator.next();
			BeanItem<Todo> newBeanItem = new BeanItem(todo);
			container.addItem(newBeanItem);
			container.setParent(newBeanItem, parent);
			copyBeanValueToContainer(container, newBeanItem);
			inventoryStore.put(getBeanSign(newBeanItem), newBeanItem);
			boolean isContainer = StringTool.parseBoolean(todo.getIsContainer()
					+ "");
			if (isContainer) {
				container.setChildrenAllowed(newBeanItem, true);
				initContainerData(container, getQuery(todo.getId()),
						newBeanItem);
			} else {
				container.setChildrenAllowed(newBeanItem, false);
			}
			/*
			 * if(parent != null){ mainTreeTable.setCollapsed(parent, false); }
			 */

		}
	}

	/**
	 * 把Bean的值copy到容器中
	 * 
	 * @param container
	 * @param newBeanItem
	 */
	public void copyBeanValueToContainer(HierarchicalContainer container,
			BeanItem<Todo> newBeanItem) {
		for (Object propertyId : container.getContainerPropertyIds()) {
			setContainerValueByBean(container, newBeanItem, propertyId);
		}
	}

	private String getBeanSign(BeanItem<Todo> beanItem) {
		return beanItem.getBean().getId() + "_" + beanItem.getBean().getName();
	}

	/**
	 * 给容器设置为bean的字
	 * 
	 * @param container
	 * @param beanItem
	 * @param propertyId
	 */
	private void setContainerValueByBean(HierarchicalContainer container,
			BeanItem<Todo> beanItem, Object propertyId) {
		Todo t = beanItem.getBean();
		String[] benTo = { "priority", "type", "tag", "name", "id", "pid" };
		for (int i = 0; i < benTo.length; i++) {
			if (benTo[i].equals(propertyId)) {
				Property itemProperty = beanItem.getItemProperty(propertyId);
				// bean的属性copy到其他
				/*
				 * Property itemProperty2 =
				 * container.getItem(beanItem).getItemProperty(propertyId);
				 * itemProperty2 .setValue(itemProperty.getValue());
				 */
				String newValue = itemProperty.getValue() + "";
				container.getContainerProperty(beanItem, propertyId).setValue(
						newValue);
			}
		}
		/*
		 * container.addContainerProperty("priority", String.class, "");
		 * container.addContainerProperty("complete", String.class, "");
		 * container.addContainerProperty("endTime", String.class, "");
		 * container.addContainerProperty("useUp", String.class, "");
		 * container.addContainerProperty("due", String.class, "");
		 * container.addContainerProperty("status", String.class, "");
		 * container.addContainerProperty("tags", String.class, "");
		 * container.addContainerProperty("type", String.class, "");
		 * container.addContainerProperty("name", String.class, "");
		 * container.addContainerProperty("container", Boolean.class, null);
		 */
		if ("isContainer".equals(propertyId)) {
			container.getContainerProperty(beanItem, propertyId).setValue(
					t.getIsContainer());
		}

		if ("container".equals(propertyId)) {
			boolean v = StringTool.parseBoolean(t.getIsContainer() + "");
			container.getContainerProperty(beanItem, propertyId).setValue(v);
		}

		if ("due".equals(propertyId)) {
			String v = StringDateUtil.dateToYMDString(new Date());
			container.getContainerProperty(beanItem, propertyId).setValue(v);
		}
	}

	/**
	 * 提交table的所有内容
	 */
	public void allCommit() {
		mainTreeTable.commit();
		List<Todo> beanList = tableDataBeanList();
		todoService.saveTodoList(beanList);
	}

	// 表格的展开数据，变成Bean，getItemIds只包含指定的数据
	private List<Todo> tableDataBeanList() {
		List<Todo> beanList = new ArrayList<Todo>();

		Collection<?> list = mainTreeTable.getItemIds();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {// table记录
			BeanItem<Todo> beanItem = (BeanItem) iterator.next();

			Todo todo = tableItemToBean(beanItem);
			initTodoProId(todo);
			beanList.add(todo);
			System.out.println("--------------" + todo);

		}
		return beanList;
	}

	/**
	 * 将table的数据复制到bing中
	 * 
	 * @param beanItem
	 * @return
	 */
	public Todo tableItemToBean(BeanItem<Todo> beanItem) {
		String[] benTo = { "name", "id", "pid", "isContainer" };
		for (int i = 0; i < benTo.length; i++) {// 需要进行赋值的
			Property itemProperty = beanItem.getItemProperty(benTo[i]);
			Object newValue = mainTreeTable.getContainerDataSource()
					.getContainerProperty(beanItem, benTo[i]).getValue();
			itemProperty.setValue(newValue);
		}
		Todo todo = beanItem.getBean();
		initTodoProId(todo);
		return todo;
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
			if (target instanceof TreeTable) {
				fieldCache.setFieldFocus(currentBeanItem);
			}
		}
	}

	public class ValueEditColumnGenerator implements ColumnGenerator {
		private static final long serialVersionUID = -5950078454864053894L;

		@Override
		public Object generateCell(final Table source, final Object itemId, Object columnId) {
			HorizontalLayout h = new HorizontalLayout();
			getEditButtonClick(itemId, h);//修改事件
			getDeleteButtonClick(source, itemId, h);//删除事件
			getAddActHref(itemId, h);//生成任务
			return h;
		}

		private HorizontalLayout getEditButtonClick(final Object itemId, final HorizontalLayout h) {
			h.setSizeUndefined();
			Button editButton = new Button("");
			editButton.addStyleName(Reindeer.BUTTON_LINK);
			editButton.setIcon(Images.EDIT);
			editButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = -590705173775423620L;

				public void buttonClick(ClickEvent event) {
					// WW_TODO 后台进行修改
					EditTodoPopupWindow editTodoPop = new EditTodoPopupWindow(
							(BeanItem<Todo>) itemId,projectId);
					editTodoPop.addListener(new SubmitEventListener() {
					    private static final long serialVersionUID = 1L;
					
						@Override
						protected void cancelled(SubmitEvent event) {
							
						}

						@Override
						protected void submitted(SubmitEvent event) {
							if(event.getData() != null){
								//将值copy回来，进行日历刷新
								copyBeanValueToContainer(hContainer,(BeanItem<Todo>) event.getData());
								mainPage.refreshRelatedView();
							}
						}
					});

					ViewToolManager.showPopupWindow(editTodoPop);
				}
			});
			h.addComponent(editButton);
			return h;
		}
		
		private HorizontalLayout getDeleteButtonClick(final Table source, final Object itemId ,final HorizontalLayout h){
			h.setSizeUndefined();
			Button deleteButton = new Button("");
			deleteButton.addStyleName(Reindeer.BUTTON_LINK);
			deleteButton.setIcon(Images.DELETE);
			
			deleteButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 2341078230127917348L;

				@Override
				public void buttonClick(ClickEvent event) {
					// TODO Auto-generated method stub
					ConfirmationDialogPopupWindow confirmationPopup = 
						      new ConfirmationDialogPopupWindow(i18nManager.getMessage(Messages.USER_CONFIRM_DELETE_GROUP, itemId,itemId));
						    confirmationPopup.addListener(new ConfirmationEventListener() {
							private static final long serialVersionUID = -3914283089571615862L;
							protected void rejected(ConfirmationEvent event) {
						      }
						      protected void confirmed(ConfirmationEvent event) {
						    	  Item item = source.getItem(itemId);
						    	  String id = (String) item.getItemProperty("id").getValue();
						    	  Todo todo = new Todo();
						    	  todo.setId(id);
						    	  todoService.deleteTodo(todo);
						    	  TextField tf = fieldCache.getPropertyFieldFromCache(itemId,
											"name");
						    	  fieldCache.deletePrppertyFieldToCache(itemId, "name", tf);
						    	  hContainer.removeItem(itemId);
//						    	  notifyProjectListChanged();
						      }
						    });
						    ViewToolManager.showPopupWindow(confirmationPopup);
				}
			});
			h.addComponent(deleteButton);
			return h;
		}

		@SuppressWarnings("unchecked")
		private void getAddActHref(final Object itemId, HorizontalLayout h) {
			final Todo relTodo = ((BeanItem<Todo>) itemId).getBean();
			//新增任务
			 Button newCaseButton = new Button();
			 newCaseButton.addStyleName(Reindeer.BUTTON_LINK);
			 newCaseButton.setCaption("生成任务");
			    
			 newCaseButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 3068522358824686916L;

				public void buttonClick(ClickEvent event) {
			    	NewTodoToTaskPopupWindow newTaskPopupWindow = new NewTodoToTaskPopupWindow(relTodo);
			        ViewToolManager.showPopupWindow(newTaskPopupWindow);
			      }
			    });
			    
		    h.addComponent(newCaseButton);
		}
		
		public void notifyProjectListChanged(){
			projectPlans.removeAllComponents();
			initMainTreeTable();
		}

	}
	
}
