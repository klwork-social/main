package com.klwork.explorer.ui.business.outproject;

import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.base.AbstractAccordionComponent;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

public class OutProjectManagerLeft extends AbstractAccordionComponent {
	private static final long serialVersionUID = 7916755916967574384L;
	protected I18nManager i18nManager;
	OutProjectManagerMainPage mainPage;
	TeamService teamService;
	//ui
	OutProjectOfMyAddInManager myAddInManager;
	OutProjectOfMyPublishManager myPublishManager;
	
	public OutProjectManagerLeft(OutProjectManagerMainPage projectMain) {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = projectMain;
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
	}

	@Override
	protected void initUi() {
		super.initUi();
		this.getAccordion().addSelectedTabChangeListener(new SelectedTabChangeListener() {
			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {
				final Accordion source = (Accordion) event.getSource();
		            // If the first tab was selected.
		            if (source.getSelectedTab() == myAddInManager) {//重新切换右边的内容
		            	myAddInManager.refreshRightContent();
		            }
		            if (source.getSelectedTab() == myPublishManager) {
		            	myPublishManager.refreshRightContent();
		            }
			}
			
		});
		 myAddInManager = new OutProjectOfMyAddInManager(mainPage);
		addAccordion(myAddInManager, "我参与的项目");
		myPublishManager = new OutProjectOfMyPublishManager(mainPage);
		addAccordion(myPublishManager, "我发布的项目");
		selectedAccordion(myPublishManager);
	}

}
