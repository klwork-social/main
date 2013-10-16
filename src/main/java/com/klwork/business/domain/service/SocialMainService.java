package com.klwork.business.domain.service;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.business.domain.service.infs.SocialService;
import com.klwork.common.utils.StringTool;

@Service
public class SocialMainService {
	@Autowired
	private AbstractSocialService socialSinaService;

	@Autowired
	private AbstractSocialService socialTencentService;

	@Autowired
	SocialUserAccountInfoService socialUserAccountInfoService;

	@Autowired
	SocialUserAccountService socialUserAccountService;

	/**
	 * 进行所有登录用户的微博进行初始化
	 */
	public void weiboInit() {
		//查询今天登录的用户的帐号信息
		List<SocialUserAccountInfo> list = socialUserAccountInfoService.queryTodayLoginSocialUserAccountInfo();
		
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {//帐号
			SocialUserAccountInfo socialUserAccountInfo = (SocialUserAccountInfo) iterator
					.next();
			String userId = socialUserAccountInfo.getUserId();
			handleUserWeibo(userId);
		}
	}
	
	/**
	 * 处理用户下的微博信息
	 * @param userId
	 */
	public void handleUserWeibo(String userId) {
		if (StringTool.judgeBlank(userId)) {
			//查询用户下所有帐号
			List<SocialUserAccount> userAccountList = socialUserAccountService.queryUserAccountByUserId(userId);
			for (Iterator it = userAccountList.iterator(); it.hasNext();) {
				final SocialUserAccount socialUserAccount = (SocialUserAccount) it
						.next();
				final AbstractSocialService service =AbstractSocialService.querySocialClass(socialUserAccount.getType() + "");
				//WW_TODO 可以改成线程的方式
				new Thread(){
					@Override
					public void run(){
						service.handleUserAccountWeibo(socialUserAccount);
					}
				}.start();
			}
		}
	}
	
	/**
	 * 给每个微博账号发布内容
	 * @param list
	 * @param value
	 * @param type
	 */
	public void sendWeibo(Collection list, String value, String type) {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String accountId = (String) iterator.next();
			SocialUserAccount socialUserAccount = socialUserAccountService.findSocialUserAccountById(accountId);
			AbstractSocialService service =AbstractSocialService.querySocialClass(socialUserAccount.getType() + "");
			service.sendWeibo(socialUserAccount,value,type);
		}
	}
	
	/**
	 * 
	 * @param list
	 * @param content  微薄内容
	 * @param imageUrl 图片路径
	 * @param type
	 */
	public void sendWeiboAndImage(Collection list, String content,String imageUrl, String type)  {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			String accountId = (String) iterator.next();
			SocialUserAccount socialUserAccount = socialUserAccountService.findSocialUserAccountById(accountId);
			AbstractSocialService service =AbstractSocialService.querySocialClass(socialUserAccount.getType() + "");
			service.sendWeiboAndImage(socialUserAccount,content,imageUrl,type);
		}
	}

}
