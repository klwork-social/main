package com.klwork.explorer.ui.main.views;

import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.social.SocialAccountList;

public class SocialMainPage extends AbstractTabViewPage{
	public void initTabData() {
		addTab(new SocialAccountList(this),"社交账号管理");
	}
}