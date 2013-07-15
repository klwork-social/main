package com.klwork.explorer.ui.main.views;

import com.klwork.explorer.Messages;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.organization.OrganizationMainPage;
import com.klwork.explorer.ui.business.organization.OrganizationMemberMainPage;

public class TeamManagerMainPage extends AbstractTabViewPage{
	
	@Override
	public void initTabData() {
        addTab(new OrganizationMemberMainPage(),i18nManager.getMessage(Messages.ORGANIZATION_TEAM_MEMBER));
		addTab(new OrganizationMainPage(),i18nManager.getMessage(Messages.ORGANIZATION_TEAM_MANAGER));
	}
}
