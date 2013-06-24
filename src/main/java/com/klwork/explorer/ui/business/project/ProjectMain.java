package com.klwork.explorer.ui.business.project;

import java.util.HashMap;
import java.util.Map;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ProjectMain extends CustomComponent {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;

	private TabSheet right = new TabSheet();
	private TabSheet styles = new TabSheet();
	
	MyCalendarView myCalendarView = null;
	
	private Map<String,Component> tabCache = new HashMap<String, Component>();
	
	final HorizontalSplitPanel split = new HorizontalSplitPanel();

	public ProjectMain() {
		this.i18nManager = ViewToolManager.getI18nManager();
		init();
		setSizeFull();
	}

	protected void init() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSizeFull();
		setCompositionRoot(layout);
		//Panel rootPanel = new Panel();
		//layout.addComponent(rootPanel);
		//layout.setExpandRatio(rootPanel, 1);
		
		
		split.setHeight(Sizeable.SIZE_UNDEFINED, Unit. PERCENTAGE);
		split.setSizeFull();
		//rootPanel.setContent(split);
		layout.addComponent(split);
		layout.setExpandRatio(split, 1);
		
		//split.setStyleName(Runo.SPLITPANEL_REDUCED);
		split.setStyleName(Reindeer.SPLITPANEL_SMALL);
		//设置分割线的位置
		split.setSplitPosition(15, Unit.PERCENTAGE);
		split.setLocked(false);
		

		initLeft(split);

		initRight(split);
	}

	public void initRight(final HorizontalSplitPanel split) {
		right = new TabSheet();
		//right.setSizeFull();
		//right.setHeight(Sizeable.SIZE_UNDEFINED, Unit.PIXELS);
		//right.setHeight(350,Unit.PIXELS);
		split.setSecondComponent(right);
		//right.add
		//split.setExpandRatio(right, 1.0f);
		//split.setLocked(true);

		
		
		myCalendarView = buildMyCalendarView();
		right.addTab(myCalendarView, "我的日程");
		right.setSelectedTab(myCalendarView);
/*		right.addSelectedTabChangeListener(new TabSheet.SelectedTabChangeListener() {
			public void selectedTabChange(SelectedTabChangeEvent event) {
				if (right.getSelectedTab() == samples) {
					split.setSplitPosition(25, Unit.PERCENTAGE);
					split.setLocked(false);
				} else {
					//split.setSplitPosition(1, Unit.PIXELS);
					//split.setLocked(true);
				}
			}
		});*/
		right.setCloseHandler(new CloseHandler() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 8503739573983905811L;

			@Override
			public void onTabClose(TabSheet tabsheet, Component tabContent) {
				/*//先保存当前的tab的内容
				if(tabContent instanceof  ProjectTreeTable){
					((ProjectTreeTable) tabContent).allCommit();
				}*/
				Tab addTab = tabsheet.getTab(tabContent);
				String name = addTab.getCaption();
				if(tabCache.get(name) != null){
					tabCache.remove(name);
				}
				tabsheet.removeComponent(tabContent);
			}
		
		});
	}
	
	public void initRightContent(String prgId,String name) {
		Component todoTabObj = null;
		if(tabCache.get(name) != null){
			todoTabObj = tabCache.get(name);
			//right.getTab(todoTabObj).setClosable(false);
		}else {
			todoTabObj = buildTodoListScreen(prgId);
			Tab addTab = right.addTab(todoTabObj, "" + name);
			//addTab.
			addTab.setClosable(true);
			
			tabCache.put(name, todoTabObj);
		}
		right.setSelectedTab(todoTabObj);
	}
	
	private void initLeft(final HorizontalSplitPanel split) {
		Panel leftPanel = new Panel();
		VerticalLayout l = new VerticalLayout();
		leftPanel.setContent(l);
		leftPanel.setSizeFull();
		//leftPanel.setHeight(450,Unit.PIXELS);
		split.setFirstComponent(leftPanel);
		
		// Trim its layout to allow the Accordion take all space.
		l.setSizeFull();
		l.setMargin(false);

		Accordion accordion = new Accordion();
		accordion.setSizeFull();
		l.addComponent(accordion);
		
		accordion.addTab(new ProjectList(this), "项目管理");
		//accordion.set
		accordion.addTab(new Label("过滤"), "过滤");
	}

	private MyCalendarView buildMyCalendarView() {
		return new MyCalendarView();
	}
	
	/**
	 * 生成一个项目的任务树
	 * @param prgId
	 * @return
	 */
	private Component buildTodoListScreen(String prgId) {
		return new ProjectTreeTable(prgId,this);
	}
	
	
	public void refreshCalendarView() {
		myCalendarView.reflashData();
	}



}
