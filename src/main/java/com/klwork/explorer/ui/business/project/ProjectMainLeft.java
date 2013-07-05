package com.klwork.explorer.ui.business.project;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.base.AbstractAccordionComponent;
import com.vaadin.ui.Label;

public class ProjectMainLeft extends AbstractAccordionComponent {
	protected I18nManager i18nManager;
	ProjectMainPage mainPage;
	
	public ProjectMainLeft(ProjectMainPage projectMain) {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.mainPage = projectMain;
	}
	
	@Override
	protected void initUi() {
		super.initUi();
		ProjectList c = new ProjectList(mainPage);
		addAccordion(c, "项目管理");
		addAccordion(new Label("过滤"), "过滤");
		//selectedAccordion(c);
	}
}
