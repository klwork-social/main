package com.klwork.explorer.ui.business.project;

import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;

import com.klwork.business.domain.model.Todo;
import com.klwork.business.domain.service.TeamService;
import com.klwork.business.domain.service.TodoService;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.handler.CommonFieldHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

public class EditTodoPopupWindow extends PopupWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1062782821671474615L;
	
	private FieldGroup scheduleEventFieldGroup = new FieldGroup();
	protected transient IdentityService identityService;
	protected TeamService teamService;
	private String projectId;

	private BeanItem<Todo> currentBeanItem;
	protected GridLayout line;
	/**任务标题*/
//	private static Label lbTitle;
//	private static TextField txtTitle;
//	/**任务处理组*/
//	private static Label lbOwnGrp;
////	private static NativeSelect sectOwnGrp;
//	/**任务处理人*/
//	private static Label lbOwner;
	protected ComboBox sectOwner = createComboBox(null,"150px");;

	// 到期时间
	DateField completionDateField;

	//
	TodoService todoService;

	public EditTodoPopupWindow(BeanItem<Todo> currentBeanItem,String projectId) {
		this.currentBeanItem = currentBeanItem;
		this.projectId = projectId;
		todoService = (TodoService) ViewToolManager.getBean("todoService");
		teamService = (TeamService) SpringApplicationContextUtil.getContext().getBean("teamService");
		identityService = ProcessEngines.getDefaultProcessEngine().getIdentityService();
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
		setHeight("90%");
		setWidth("60%");
		center();
		
		HorizontalLayout bottom = new HorizontalLayout();
		bottom.setStyleName(ExplorerLayout.THEME);
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

		line = new GridLayout(4,20);
		line.addStyleName("v-gridlayout");
		line.setWidth("100%");
		line.setSpacing(true);
		line.setMargin(true);

		final Label lbTitle = CommonFieldHandler.createLable("计划名称:");
		line.addComponent(lbTitle);
		line.setComponentAlignment(lbTitle, Alignment.MIDDLE_RIGHT);
		final TextField txtTitle = new TextField();
		txtTitle.setWidth("80%");
		scheduleEventFieldGroup.bind(txtTitle, "name");
		line.addComponent(txtTitle,1,0,3,0);
		line.setComponentAlignment(txtTitle, Alignment.MIDDLE_LEFT);
		
		Label label2 = CommonFieldHandler.createLable("开始时间:");
		line.addComponent(label2,0,1,0,1);
		line.setComponentAlignment(label2, Alignment.MIDDLE_RIGHT);
		// 创建一个时间后台变化的listener
		BlurListener startTimeListener = createTimeReCountListener();
		DateField startDateField = CommonFieldHandler.createDateField("",false);
		scheduleEventFieldGroup.bind(startDateField, "startDate");
		startDateField.addBlurListener(startTimeListener);
		line.addComponent(startDateField,1,1,1,1);
		line.setComponentAlignment(startDateField, Alignment.MIDDLE_LEFT);

		Label label3 = CommonFieldHandler.createLable("估算时间:");
		line.addComponent(label3,2,1,2,1);
		line.setComponentAlignment(label3, Alignment.MIDDLE_RIGHT);
		HorizontalLayout hlay = new HorizontalLayout() ;
		final TextField estimateField = new TextField();
		estimateField.setValue("1");
		estimateField.setWidth("60px");
		estimateField.setNullSettingAllowed(false);
		BlurListener timeReCountListener = createTimeReCountListener();
		estimateField.addBlurListener(timeReCountListener);
		scheduleEventFieldGroup.bind(estimateField, "estimate");
		hlay.addComponent(estimateField);
		Map<Object, String> data = new HashMap();
		data.put(0, "天");
		data.put(1, "时");
		data.put(2, "分");
		// WW_TODO 估算时间单位
		ComboBox unit_cb = createComboBox(data,"55px");
		scheduleEventFieldGroup.bind(unit_cb, "estimateUnit");
		hlay.addComponent(unit_cb);
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
		gs1.setValue("20%");
		gs1.setInputPrompt("50%");
		scheduleEventFieldGroup.bind(gs1, "useup");
		line.addComponent(gs1,3,2,3,2);
		line.setComponentAlignment(gs1, Alignment.MIDDLE_LEFT);
		
		Label label5 = CommonFieldHandler.createLable("优先级:");
//		label.setWidth("80px");
		line.addComponent(label5,0,3,0,3);
		line.setComponentAlignment(label5, Alignment.MIDDLE_RIGHT);
		Map<Object, String> dtp = new HashMap();
		dtp.put(0, "底");
		dtp.put(1, "中");
		dtp.put(2, "高");
		ComboBox selectPriority = createComboBox(dtp,"100px");
//		NativeSelect select = new NativeSelect();
//		select.addItem("无");
//		select.addItem("0(最低)");
//		String itemId = "1(中)";
//		select.addItem(itemId);
//		select.addItem("2(高)");
		selectPriority.setNullSelectionAllowed(false);
		selectPriority.select(2);
		scheduleEventFieldGroup.bind(selectPriority, "priority");
		line.addComponent(selectPriority,1,3,1,3);
		line.setComponentAlignment(selectPriority, Alignment.MIDDLE_LEFT);

		Label label1 = CommonFieldHandler.createLable("完成百分比:");
		line.addComponent(label1,2,3,2,3);
		line.setComponentAlignment(label1, Alignment.MIDDLE_RIGHT);
		TextField tf = new TextField();
		tf.setInputPrompt("50%");
		line.addComponent(tf,3,3,3,3);
		line.setComponentAlignment(tf, Alignment.MIDDLE_LEFT);

		Label label7 = CommonFieldHandler.createLable("关联日程:");
		line.addComponent(label7,0,4,0,4);
		line.setComponentAlignment(label7, Alignment.MIDDLE_RIGHT);
		CheckBox relatedCalendar_cb = new CheckBox();
		relatedCalendar_cb.setValue(false);
		line.addComponent(relatedCalendar_cb,1,4,1,4);
		line.setComponentAlignment(relatedCalendar_cb, Alignment.MIDDLE_LEFT);
		scheduleEventFieldGroup.bind(relatedCalendar_cb, "relatedCalendar");
		
		Label lbStatus = CommonFieldHandler.createLable("计划状态:");
		lbStatus.setWidth("20px");
		line.addComponent(lbStatus,2,4,2,4);
		line.setComponentAlignment(lbStatus, Alignment.MIDDLE_RIGHT);
		Map<Object, String> sta = new HashMap();
		sta.put(0, "新建");
		sta.put(1, "完成");
		sta.put(2, "关闭");
		sta.put(3, "取消");
		ComboBox sectStatus = createComboBox(sta,"100px");
		sectStatus.setNullSelectionAllowed(false);
		scheduleEventFieldGroup.bind(sectStatus, "status");
		line.addComponent(sectStatus,3,4,3,4);
		line.setComponentAlignment(sectStatus, Alignment.MIDDLE_LEFT);

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
		Map<Object, String> oat = new HashMap();
		oat.put(0, "外包任务");
		oat.put(1, "外包任务-类型2");
		ComboBox select2 = createComboBox(oat,"150px");
//		NativeSelect select2 = new NativeSelect();
//		select2.addItem("外包任务");
//		select2.addItem("外包任务-类型2");
		select2.setNullSelectionAllowed(false);
		scheduleEventFieldGroup.bind(select2, "type");
		line.addComponent(select2,3,5,3,5);
		line.setComponentAlignment(select2, Alignment.MIDDLE_LEFT);
		// select2.select("Timed");
		
		Label lbOwnGrp = CommonFieldHandler.createLable("计划分配团队:");
		lbOwnGrp.setWidth("20px");
		line.addComponent(lbOwnGrp,0,6,0,6);
		line.setComponentAlignment(lbOwnGrp, Alignment.MIDDLE_RIGHT);
		
//		NativeSelect sectOwnGrp = new NativeSelect();
		Map<Object, String> groupsMap = teamService.queryTeamOfUser(LoginHandler
				.getLoggedInUser().getId());
		groupsMap.put("", "请选择");
		ComboBox sectOwnGrp = createComboBox(groupsMap,"150px");
//		for (String p : groupsMap.keySet()) {
//			String title = groupsMap.get(p);
//			sectOwnGrp.addItem(p);
//			sectOwnGrp.setItemCaption(p, title);
//		}
		sectOwnGrp.setNullSelectionAllowed(false);
		ValueChangeListener valueChangeListener = createValueChangeListener();
		sectOwnGrp.addValueChangeListener(valueChangeListener);
		sectOwnGrp.setImmediate(true);
		scheduleEventFieldGroup.bind(sectOwnGrp, "assignedTeam");
		line.addComponent(sectOwnGrp,1,6,1,6);
		line.setComponentAlignment(sectOwnGrp, Alignment.MIDDLE_LEFT);
		
		final Label lbOwner = CommonFieldHandler.createLable("计划分配用户:");
		lbOwner.setWidth("20px");
		line.addComponent(lbOwner,2,6,2,6);
		line.setComponentAlignment(lbOwner, Alignment.MIDDLE_RIGHT);
//		sectOwner = new NativeSelect();
//		sectOwner.addItem("请选择");
		sectOwner.setNullSelectionAllowed(false);
		scheduleEventFieldGroup.bind(sectOwner, "assignedUser");
		line.addComponent(sectOwner,3,6,3,6);
		line.setComponentAlignment(sectOwner, Alignment.MIDDLE_LEFT);
		
		final Label lbDesc = CommonFieldHandler.createLable("计划描述:");
		lbDesc.setWidth("15px");
		line.addComponent(lbDesc,0,7,0,7);
		line.setComponentAlignment(lbDesc, Alignment.MIDDLE_RIGHT);
		final TextArea taDesc = CommonFieldHandler.createTextArea("");
		taDesc.setWidth("85%");
		taDesc.setHeight("290px");
		scheduleEventFieldGroup.bind(taDesc, "description");
		line.addComponent(taDesc,1,7,3,13);
		line.setComponentAlignment(taDesc, Alignment.MIDDLE_LEFT);
		
//		CKEditorConfig config = new CKEditorConfig();

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
				if(fieldGroupTodo.getRelatedTask()) { 
					ViewToolManager.showPopupWindow(new ActivityStartPopupWindow( "1111")); 
				}
			}
		});
		line.addComponent(updateSave,3,14,3,14);
		line.setComponentAlignment(updateSave, Alignment.MIDDLE_RIGHT);
//		line.setExpandRatio(updateSave, 1.0f);

		bottom.addComponent(line);
	}
	
	/**
	 * 重新改变用户的选择
	 * @param groupId
	 */
	protected Map<Object, String> changeUserSelect(String groupId) {
		Map<Object, String> usersMap = new HashMap<Object, String>();
		List<User> users = identityService.createUserQuery()
				.memberOfTeam(groupId).list();
		sectOwner.removeAllItems();
		for (User user : users) {
			usersMap.put(user.getId(), user.getId());
		}
		return usersMap;
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
	
	private ComboBox createComboBox(Map<Object, String> data,String width){
		ComboBox s = CommonFieldHandler.createComBox(null, data, 0, true);
		s.setWidth(width);
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
					t.setCompletionDate(StringDateUtil.addDay(
							t.getStartDate(), fact));
				}
				if (new Integer(1).equals(t.getEstimateUnit())) {
//					fact = (int) (estimate * 24);
					fact = estimate.intValue();
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
	
	public ValueChangeListener createValueChangeListener(){
		ValueChangeListener valueChangeListener = new ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				// TODO Auto-generated method stub
				Object o  = event.getProperty().getValue();
				Map<Object,String> userMap = changeUserSelect(o.toString());
				sectOwner.setNullSelectionAllowed(false);
				//s.setImmediate(true);
				for (Object p : userMap.keySet()) {
					String title = userMap.get(p);
					Item i = sectOwner.addItem(p);
					sectOwner.setItemCaption(p, title);
					
				}
			}
		};
		return valueChangeListener;
	}
}
