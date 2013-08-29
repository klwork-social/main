package com.klwork.explorer.ui.main.views;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.social.QQWeiboShowPage;
import com.klwork.explorer.ui.business.social.SinaWeiboShowPage;
import com.klwork.explorer.ui.business.social.SocialAccountList;
import com.klwork.explorer.ui.business.social.TeamSocialAccountList;
import com.klwork.explorer.ui.business.social.WeiboSendMainPage;

public class SocialMainPage extends AbstractTabViewPage{
	SocialUserAccountService saService;
	public SocialMainPage(){
		super();
		saService = ViewToolManager.getBean("socialUserAccountService");
	}
	public void initTabData() {
		addTab(new SocialAccountList(this),"社交账号管理");
		addTab(new TeamSocialAccountList(this),"团队账号");
		addTab(new WeiboSendMainPage(),"微博发送管理");
		//打开默认的微博
		openDefaultWeibo();
	}
	public void openDefaultWeibo() {
		//SocialUserAccount sc = saService.findSocialUserAccountById("3501");
		//openWeiboTab(sc);
	}
	
	public void openWeiboTab(SocialUserAccount socialUserAccount) {
		if(socialUserAccount.getType() == 0){//新浪微博
			addTabSpecial(new SinaWeiboShowPage(socialUserAccount,this), "新浪_" + socialUserAccount.getUserScreenName());
		}else {
			addTabSpecial(new QQWeiboShowPage(socialUserAccount,this), "腾讯_" +  socialUserAccount.getUserScreenName());
		}
	}
}
