package com.klwork.explorer.ui.business.project;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
		setHeight("50%");
		setWidth("60%");
		center();
		
		HorizontalLayout bottom = new HorizontalLayout();
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
//		HorizontalLayout line = new HorizontalLayout() ;
		GridLayout line = new GridLayout(4,7);
		line.addStyleName("v-gridlayout");
//		{
//			@Override
//			public void addComponent(Component c) {
//				super.addComponent(c);
//				setComponentAlignment(c, Alignment.MIDDLE_LEFT);
//				c.setSizeUndefined();
//			}
//		};
//		line.addComponent(c)
		line.setWidth("100%");
		line.setSpacing(true);
		line.setMargin(true);

		Label label = CommonFieldHandler.createLable("标题:");
		line.addComponent(label);
		line.setComponentAlignment(label, Alignment.MIDDLE_RIGHT);
		TextField nameField = new TextField();
		nameField.setWidth("80%");
		// nameField.setSizeUndefined();
		scheduleEventFieldGroup.bind(nameField, "name");
		line.addComponent(nameField,1,0,3,0);
		line.setComponentAlignment(nameField, Alignment.MIDDLE_LEFT);
//		line.setExpandRatio(nameField, 0.7f);
		
		Label label2 = CommonFieldHandler.createLable("开始时间:");
		line.addComponent(label2,0,1,0,1);
		line.setComponentAlignment(label2, Alignment.MIDDLE_RIGHT);
		// 创建一个时间后台变化的listener
		BlurListener timeReCountListener = createTimeReCountListener();
		DateField startDateField = CommonFieldHandler.createDateField("",false);
		scheduleEventFieldGroup.bind(startDateField, "startDate");
		startDateField.addBlurListener(timeReCountListener);
		line.addComponent(startDateField,1,1,1,1);
		line.setComponentAlignment(startDateField, Alignment.MIDDLE_LEFT);

		Label label3 = CommonFieldHandler.createLable("估算时间:");
		line.addComponent(label3,2,1,2,1);
		line.setComponentAlignment(label3, Alignment.MIDDLE_RIGHT);
		HorizontalLayout hlay = new HorizontalLayout() ;
		TextField estimateField = new TextField();
		// estimateField.setInputPrompt("");
//		line.addComponent(estimateField,3,1,3,1);
//		hlay.setComponentAlignment(label3, Alignment.MIDDLE_CENTER);
		hlay.addComponent(estimateField);
		estimateField.addBlurListener(timeReCountListener);
		scheduleEventFieldGroup.bind(estimateField, "estimate");
		// gs.setWidth("100%");
		// WW_TODO 估算时间单位
		ComboBox unit_cb = createTimeUnitComboBox();
//		line.addComponent(unit_cb,4,1,4,1);
//		hlay.setComponentAlignment(unit_cb, Alignment.MIDDLE_CENTER);
		hlay.addComponent(unit_cb);
		scheduleEventFieldGroup.bind(unit_cb, "estimateUnit");
		unit_cb.addBlurListener(timeReCountListener);
		// unit_cb.setWidth("15px");
		line.addComponent(hlay,3,1,3,1);
		line.setComponentAlignment(hlay, Alignment.MIDDLE_LEFT);

		Label label4 = CommonFieldHandler.createLable("到期时间:");
		line.addComponent(label4,0,2,0,2);
		line.setComponentAlignment(label4, Alignment.MIDDLE_RIGHT);
		completionDateField = CommonFieldHandler.createDateField("", false);
		line.addComponent(completionDateField,1,2,1,2);
		line.setComponentAlignment(completionDateField, Alignment.MIDDLE_LEFT);
		scheduleEventFieldGroup.bind(completionDateField, "completionDate");
//		line.setExpandRatio(completionDateField, 1.0f);
		
		Label label6 = CommonFieldHandler.createLable("消耗时间:");
		line.addComponent(label6,2,2,2,2);
		line.setComponentAlignment(label6, Alignment.MIDDLE_RIGHT);
		TextField gs1 = new TextField();
		gs1.setInputPrompt("50%");
		line.addComponent(gs1,3,2,3,2);
		line.setComponentAlignment(gs1, Alignment.MIDDLE_LEFT);
		
		Label label5 = CommonFieldHandler.createLable("优先级:");
//		label.setWidth("80px");
		line.addComponent(label5,0,3,0,3);
		line.setComponentAlignment(label5, Alignment.MIDDLE_RIGHT);
		NativeSelect select = new NativeSelect();
		select.addItem("无");
		select.addItem("0(最低)");
		String itemId = "1(中)";
		select.addItem(itemId);
		select.addItem("2(高)");
		select.setNullSelectionAllowed(false);
		select.select(itemId);
		line.addComponent(select,1,3,1,3);
		line.setComponentAlignment(select, Alignment.MIDDLE_LEFT);

		Label label1 = CommonFieldHandler.createLable("完成百分比:");
		line.addComponent(label1,2,3,2,3);
		line.setComponentAlignment(label1, Alignment.MIDDLE_RIGHT);
		TextField tf = new TextField();
		tf.setInputPrompt("50%");
		line.addComponent(tf,3,3,3,3);
		line.setComponentAlignment(tf, Alignment.MIDDLE_LEFT);

		Label label7 = CommonFieldHandler.createLable("关联日历:");
		line.addComponent(label7,0,4,0,4);
		line.setComponentAlignment(label7, Alignment.MIDDLE_RIGHT);
		CheckBox relatedCalendar_cb = new CheckBox();
		relatedCalendar_cb.setValue(false);
		line.addComponent(relatedCalendar_cb,1,4,1,4);
		line.setComponentAlignment(relatedCalendar_cb, Alignment.MIDDLE_LEFT);
		scheduleEventFieldGroup.bind(relatedCalendar_cb, "relatedCalendar");

		Label label8 = CommonFieldHandler.createLable("关联外部任务:");
		label8.setWidth("20px");
		line.addComponent(label8,0,5,0,5);
		line.setComponentAlignment(label8, Alignment.MIDDLE_RIGHT);
		CheckBox cb = new CheckBox();
		cb.setValue(true);
		line.addComponent(cb,1,5,1,5);
		line.setComponentAlignment(cb, Alignment.MIDDLE_LEFT);
		scheduleEventFieldGroup.bind(cb, "relatedTask");

		Label label9 = CommonFieldHandler.createLable("外部任务类型:");
		label9.setWidth("20px");
		line.addComponent(label9,2,5,2,5);
		line.setComponentAlignment(label9, Alignment.MIDDLE_RIGHT);
		NativeSelect select2 = new NativeSelect();
		select2.addItem("外包任务");
		select2.addItem("外包任务-类型2");
		select2.setNullSelectionAllowed(false);
		line.addComponent(select2,3,5,3,5);
		line.setComponentAlignment(select2, Alignment.MIDDLE_LEFT);
		// select2.select("Timed");

		final Button updateSave = new Button("保存");
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
		line.addComponent(updateSave,3,6,3,6);
		line.setComponentAlignment(updateSave, Alignment.MIDDLE_RIGHT);
//		line.setExpandRatio(updateSave, 1.0f);

		// Align and size the labels.     
//		for (int col=0; col<line.getColumns(); col++) {     
//		    for (int row=0; row<line.getRows(); row++) {     
//		        Component c = line.getComponent(col, row);     
//		        line.setComponentAlignment(c, Alignment.TOP_CENTER);     
//		                  
//		        // Make the labels high to illustrate the empty     
//		        // horizontal space.     
//		        if (col != 0 || row != 0)     
//		            c.setHeight("100%");     
//		    }     
//		}
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
//		ComboBox s = new ComboBox();
//		s.addContainerProperty("unit", String.class, 0);
//		s.setItemCaptionPropertyId("unit");
//		Item i = s.addItem(0);
//		i.getItemProperty("unit").setValue("小时");
//		s.select(0);
//		i = s.addItem(1);
//		i.getItemProperty("unit").setValue("天");
//		i = s.addItem(2);
//		i.getItemProperty("unit").setValue("分钟");
//		s.setWidth("55px");
		
		Map<String, String> data = new HashMap();
		data.put("0", "分");
		data.put("1", "天");
		data.put("2", "时");
		ComboBox s = CommonFieldHandler.createComBox(null,
				data, "1");
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
