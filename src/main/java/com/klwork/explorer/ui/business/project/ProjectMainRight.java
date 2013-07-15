package com.klwork.explorer.ui.business.project;

import java.util.HashMap;
import java.util.Map;

import com.klwork.explorer.ui.base.AbstractTabPage;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.Tab;

public class ProjectMainRight extends AbstractTabPage {
	private Map<String,Component> tabCache = new HashMap<String, Component>();
	ProjectMainPage projectMainPage;
	public ProjectMainRight(ProjectMainPage projectMainPage,
			Object leftParameter) {
		this.projectMainPage = projectMainPage;
		if(leftParameter != null){
			
		}
	}

	@Override
	public void initTabData() {
		//MyCalendarView v = new MyCalendarView();
		this.getTabSheet().setCloseHandler(new CloseHandler() {
			private static final long serialVersionUID = -1764556772862038086L;

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
		//addTab(v, "我的日程");
	}
	
	public void initRightContent(String prgId,String name) {
		Component todoTabObj = null;
		if(tabCache.get(name) != null){
			todoTabObj = tabCache.get(name);
		}else {
			todoTabObj = buildTodoListScreen(prgId);
			Tab addTab = addTab(todoTabObj, "" + name);
			//addTab.
			addTab.setClosable(true);
			
			tabCache.put(name, todoTabObj);
		}
		setSelectedTab(todoTabObj);
	}
	


	/**
	 * 生成一个项目的任务树
	 * @param prgId
	 * @return
	 */
	private Component buildTodoListScreen(String prgId) {
		//return new ProjectTreeTable(prgId,projectMainPage);
		return null;
	}
}
