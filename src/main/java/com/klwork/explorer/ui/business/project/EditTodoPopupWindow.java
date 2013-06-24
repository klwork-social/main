package com.klwork.explorer.ui.business.project;

import com.klwork.business.domain.model.Todo;
import com.klwork.business.domain.service.TodoService;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

public class EditTodoPopupWindow extends PopupWindow {

	private FieldGroup scheduleEventFieldGroup = new FieldGroup();
	private String projectId;

	private BeanItem<Todo> currentBeanItem;

	// 到期时间
	DateField completionDateField;

	//
	TodoService todoService;

	public EditTodoPopupWindow(BeanItem<Todo> currentBeanItem,String projectId) {
		this.currentBeanItem = currentBeanItem;
		this.projectId = projectId;
		todoService = (TodoService) ViewToolManager.getBean("todoService");
		initUI();
	}

	public Todo saveFieldGroupToDB() {
		commitGroup();
		Todo fieldGroupTodo = getFieldGroupTodo();
		todoService.updateTodo(fieldGroupTodo);
		return fieldGroupTodo;
	}

	private void initUI() {
		addStyleName(Reindeer.WINDOW_LIGHT);
		setModal(true);
		setHeight("30%");
		setWidth("50%");
		center();
		
		VerticalLayout bottom = new VerticalLayout();
		bottom.setSizeFull();
		// bottom.setMargin(true);
		bottom.setSpacing(true);
		bottom.addStyleName(Runo.LAYOUT_DARKER);
		this.setContent(bottom);

		scheduleEventFieldGroup = new FieldGroup();
		scheduleEventFieldGroup.setBuffered(true);
		if (currentBeanItem != null) {
			scheduleEventFieldGroup.setItemDataSource(currentBeanItem);
		}

		// layout.setExpandRatio(bottom, 1f);
		HorizontalLayout line = new HorizontalLayout() {
			@Override
			public void addComponent(Component c) {
				super.addComponent(c);
				setComponentAlignment(c, Alignment.MIDDLE_LEFT);
				c.setSizeUndefined();
			}
		};
		line.setWidth("100%");
		line.setSpacing(true);
		/*
		 * Label first = new Label("优先级:"); line.addComponent(first);
		 * first.setWidth("80px");
		 */
		NativeSelect select = new NativeSelect("优先级:");
		select.addItem("无");
		select.addItem("0(最低)");
		String itemId = "1(中)";
		select.addItem(itemId);
		select.addItem("2(高)");
		select.setNullSelectionAllowed(false);
		select.select(itemId);
		line.addComponent(select);

		// line.addComponent(new Label("完成百分比:"));
		TextField tf = new TextField("完成百分比:");
		tf.setInputPrompt("50%");
		line.addComponent(tf);

		// 创建一个时间后台变化的listener
		BlurListener timeReCountListener = createTimeReCountListener();
		DateField startDateField = CommonFieldHandler.createDateField("开始时间",
				false);

		scheduleEventFieldGroup.bind(startDateField, "startDate");
		startDateField.addBlurListener(timeReCountListener);
		line.addComponent(startDateField);

		// line.addComponent(new Label("估算时间"));
		HorizontalLayout x = new HorizontalLayout();
		TextField estimateField = new TextField("估算时间");
		// estimateField.setInputPrompt("");
		x.addComponent(estimateField);

		estimateField.addBlurListener(timeReCountListener);
		scheduleEventFieldGroup.bind(estimateField, "estimate");
		// gs.setWidth("100%");
		// WW_TODO 估算时间单位
		ComboBox unit_cb = createTimeUnitComboBox();
		x.addComponent(unit_cb);
		scheduleEventFieldGroup.bind(unit_cb, "estimateUnit");
		unit_cb.addBlurListener(timeReCountListener);
		// unit_cb.setWidth("15px");

		line.addComponent(x);
		// line.setS

		completionDateField = CommonFieldHandler.createDateField("到期时间", false);
		line.addComponent(completionDateField);
		scheduleEventFieldGroup.bind(completionDateField, "completionDate");
		line.setExpandRatio(completionDateField, 1.0f);

		bottom.addComponent(line);
		line = new HorizontalLayout() {
			@Override
			public void addComponent(Component c) {
				super.addComponent(c);
				setComponentAlignment(c, Alignment.MIDDLE_LEFT);
				c.setSizeUndefined();
			}
		};
		line.setWidth("100%");
		line.setSpacing(true);

		TextField nameField = new TextField("标题");
		nameField.setWidth("100%");
		// nameField.setSizeUndefined();
		scheduleEventFieldGroup.bind(nameField, "name");
		line.addComponent(nameField);
		line.setExpandRatio(nameField, 0.7f);

		// line.addComponent(new Label("消耗时间"));
		TextField gs1 = new TextField("消耗时间");
		gs1.setInputPrompt("50%");
		line.addComponent(gs1);

		CheckBox relatedCalendar_cb = new CheckBox("关联日历");
		relatedCalendar_cb.setValue(false);
		line.addComponent(relatedCalendar_cb);
		scheduleEventFieldGroup.bind(relatedCalendar_cb, "relatedCalendar");

		CheckBox cb = new CheckBox("是否关联外部任务");
		cb.setValue(true);
		line.addComponent(cb);
		scheduleEventFieldGroup.bind(cb, "relatedTask");

		NativeSelect select2 = new NativeSelect("外部任务类型");
		select2.addItem("外包任务");
		select2.addItem("外包任务-类型2");
		select2.setNullSelectionAllowed(false);
		line.addComponent(select2);
		// select2.select("Timed");

		final Button updateSave = new Button("save");
		updateSave.addClickListener(new ClickListener() {
			@SuppressWarnings("unchecked")
			public void buttonClick(ClickEvent event) {
				// WW_TODO 修改保存到数据库
				Todo fieldGroupTodo = saveFieldGroupToDB(); 
				fireEvent(new SubmitEvent(updateSave, SubmitEvent.SUBMITTED,scheduleEventFieldGroup.getItemDataSource()));

				// close popup window
				close();
				/*
				 * Todo fieldGroupTodo = saveFieldGroupToDB(); //reflash current
				 * Item copyBeanValueToContainer(hContainer,(BeanItem<Todo>)(
				 * scheduleEventFieldGroup.getItemDataSource())); //刷新日历
				 * main.refreshCalendarView(); Notification.show("保存成功",
				 * Notification.Type.HUMANIZED_MESSAGE); //如果有外部流程，启动外部流程 if
				 * (fieldGroupTodo.getRelatedTask()) { ViewToolManager
				 * .showPopupWindow(new ActivityStartPopupWindow( "1111")); }
				 */
				if(fieldGroupTodo.getRelatedTask()) { ViewToolManager.showPopupWindow(new ActivityStartPopupWindow( "1111")); }
			}
		});
		line.addComponent(updateSave);
		line.setExpandRatio(updateSave, 1.0f);

		bottom.addComponent(line);
	}

	@SuppressWarnings("unchecked")
	private Todo getFieldGroupTodo() {
		BeanItem<Todo> item = (BeanItem<Todo>) scheduleEventFieldGroup
				.getItemDataSource();
		Todo todo = item.getBean();
		// WW_TODO 保存是设置项目id和用户
		if (!StringTool.judgeBlank(todo.getProId())) {
			todo.setProId(projectId);
		}
		if (todo.getAssignedUser() == null) {
			todo.setAssignedUser(LoginHandler.getLoggedInUser().getId());
		}
		return todo;
	}

	private ComboBox createTimeUnitComboBox() {
		ComboBox s = new ComboBox("");
		s.addContainerProperty("unit", String.class, 0);
		s.setItemCaptionPropertyId("unit");
		Item i = s.addItem(0);
		i.getItemProperty("unit").setValue("小时");
		s.select(0);
		i = s.addItem(1);
		i.getItemProperty("unit").setValue("天");
		i = s.addItem(2);
		i.getItemProperty("unit").setValue("分钟");
		s.setWidth("55px");

		return s;
	}

	/**
	 * 提交绑定的fieldGroup
	 */
	public void commitGroup() {
		try {
			scheduleEventFieldGroup.commit();
		} catch (CommitException e) {
			e.printStackTrace();
		}
	}

	public BlurListener createTimeReCountListener() {
		BlurListener timeReCountListener = new BlurListener() {
			public void blur(BlurEvent event) {
				commitGroup();
				Todo t = getFieldGroupTodo();
				Double estimate = t.getEstimate();
				if (estimate == null || t.getStartDate() == null) {
					return;
				}
				int fact = 0;
				if (new Integer(0).equals(t.getEstimateUnit())) {
					fact = estimate.intValue();
					t.setCompletionDate(StringDateUtil.addHour(
							t.getStartDate(), fact));
				}
				if (new Integer(1).equals(t.getEstimateUnit())) {
					fact = (int) (estimate * 24);
					t.setCompletionDate(StringDateUtil.addHour(
							t.getStartDate(), fact));
				}
				if (new Integer(2).equals(t.getEstimateUnit())) {
					fact = (int) (estimate * 1);
					t.setCompletionDate(StringDateUtil.addMinute(
							t.getStartDate(), fact));
				}
				completionDateField.setValue(t.getCompletionDate());
			}
		};
		return timeReCountListener;
	}
}
