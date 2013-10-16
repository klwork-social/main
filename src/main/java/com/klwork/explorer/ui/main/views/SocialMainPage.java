package com.klwork.explorer.ui.main.views;

import java.util.Iterator;
import java.util.List;

import org.mortbay.log.Log;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.service.SocialUserAccountInfoService;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.business.social.QQWeiboShowPage;
import com.klwork.explorer.ui.business.social.SinaWeiboShowPage;
import com.klwork.explorer.ui.business.social.SocialAccountList;
import com.klwork.explorer.ui.business.social.TeamSocialAccountList;
import com.klwork.explorer.ui.business.social.WeiboSendMainPage;
import com.klwork.explorer.ui.business.social.WeiboShowPage;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.Tab;

public class SocialMainPage extends AbstractTabViewPage{
	SocialUserAccountService saService;
	SocialUserAccountInfoService socialUserAccountInfoService;
	
	private SocialAccountList socialAccountList = null;
	
	public SocialMainPage(){
		super();
		saService = ViewToolManager.getBean("socialUserAccountService");
		socialUserAccountInfoService = ViewToolManager.getBean("socialUserAccountInfoService");
	}
	
	@Override
	public void initTabData() {
		socialAccountList = new SocialAccountList(this);
		addTab(socialAccountList,"社交账号管理");
		addTab(new TeamSocialAccountList(this),"团队账号");
		addTab(new WeiboSendMainPage(),"微博发送管理");
		//打开默认的微博
		openDefaultWeibo();
	}
	
	//进行默认打开
	public void openDefaultWeibo() {
		SocialUserAccountQuery query = new SocialUserAccountQuery();
		query.setOwnUser(LoginHandler. getLoggedInUser().getId());
		query.setKey("account_tab_open");
		query.setKeyValue("1");
		List<SocialUserAccount> list = saService.findSocialUserAccountByQueryCriteria(query , null);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			SocialUserAccount socialUserAccount = (SocialUserAccount) iterator
					.next();
			openWeiboTab(socialUserAccount);
		}
	}
	
	public void openWeiboTab(SocialUserAccount socialUserAccount) {
		if(socialUserAccount.getType() == 0){//新浪微博
			addTabSpecial(new SinaWeiboShowPage(socialUserAccount,this), "新浪_" + socialUserAccount.getUserScreenName(),socialUserAccount.getId());
		}else {
			addTabSpecial(new QQWeiboShowPage(socialUserAccount,this), "腾讯_" +  socialUserAccount.getUserScreenName(),socialUserAccount.getId());
		}
		//打开一个微博时进行记录
		socialUserAccountInfoService.setSocialUserAccountInfo(socialUserAccount, "account_tab_open", "1");//打开状态
	}
	
	@Override
	public CloseHandler currentCloseHandler() {
		return new CloseHandler() {
			private static final long serialVersionUID = -1764556772862038086L;

			@Override
			public void onTabClose(TabSheet tabsheet, Component tabContent) {
				
				Tab addTab = tabsheet.getTab(tabContent);
				if(tabContent instanceof  WeiboShowPage){
					String accountId = queryTabKey(tabContent);
					SocialUserAccount s = new SocialUserAccount();
					s.setId(accountId);
					socialUserAccountInfoService.setSocialUserAccountInfo(s, "account_tab_open", "0");//关闭状态
					Log.debug("当前关闭：" + accountId);
				}
				String name = addTab.getCaption();
				if(getTabCache().get(name) != null){
					getTabCache().remove(name);
				}
				tabsheet.removeComponent(tabContent);
			}
		};
	}
	
	//查询用户记录表数据
	public PushDataResult getPushData() {
		SocialUserAccountInfo lock = socialUserAccountInfoService
				.findUserOfInfoByKey(DictDef.dict("user_third_account_dirty"),
						LoginHandler. getLoggedInUser().getId());
		PushDataResult r = new PushDataResult();
		r.setNeedUpdate(false);
		if(lock != null && StringTool.parseBoolean(lock.getValue())){
			r.setNeedUpdate(true);
		}
		return r;
	}

	public void reflashUIByPush() {
		socialAccountList.reflashTable();
		socialUserAccountInfoService.setEntityInfo(LoginHandler. getLoggedInUser().getId(),DictDef.dict("user_info_type"), "user_third_account_dirty", "0");
	}
}
