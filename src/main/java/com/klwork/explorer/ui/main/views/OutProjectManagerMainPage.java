package com.klwork.explorer.ui.main.views;

import com.klwork.explorer.Messages;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.outproject.ProjectOfMyAddInMainPage;
import com.klwork.explorer.ui.business.outproject.ProjectOfMyPublishMainPage;
import com.klwork.explorer.ui.business.outproject.PublicProjectListPage;

public class OutProjectManagerMainPage extends AbstractTabViewPage{
	
	@Override
	public void initTabData() {
		 addTab(new PublicProjectListPage(),i18nManager.getMessage(Messages.MAIN_MENU_PUBLIC_TASK));
        addTab(new ProjectOfMyAddInMainPage(),i18nManager.getMessage(Messages.OUTPROJECT_MY_PARTICIPATION));
		addTab(new ProjectOfMyPublishMainPage(),i18nManager.getMessage(Messages.OUTPROJECT_MY_PUBLISH));
	}
}
