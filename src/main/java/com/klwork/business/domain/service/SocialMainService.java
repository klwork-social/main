package com.klwork.business.domain.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountInfoQuery;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.business.domain.service.infs.SocialService;
import com.klwork.common.utils.StringDateUtil;
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
		SocialUserAccountInfoQuery query = new SocialUserAccountInfoQuery();
		Date d = new Date();
		Date aDate = StringDateUtil.dateToYMD(d);
		query.setDateAfter(aDate);
		query.setType(DictDef.dict("user_info_type"));
		query.setKey("user_last_logged_time");
		query.setOrderBy("value_date_ asc");
		// 查询今天登录的用户
		List<SocialUserAccountInfo> list = socialUserAccountInfoService
				.findSocialUserAccountInfoByQueryCriteria(query, null);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			SocialUserAccountInfo socialUserAccountInfo = (SocialUserAccountInfo) iterator
					.next();
			String userId = socialUserAccountInfo.getUserId();
			if (StringTool.judgeBlank(userId)) {
				SocialUserAccountQuery sQuery = new SocialUserAccountQuery();
				sQuery.setOwnUser(userId);
				List<SocialUserAccount> sList = socialUserAccountService
						.findSocialUserAccountByQueryCriteria(sQuery, null);
				for (Iterator iterator2 = sList.iterator(); iterator2.hasNext();) {
					final SocialUserAccount socialUserAccount = (SocialUserAccount) iterator2
							.next();
					final SocialService s =AbstractSocialService.querySocialClass(socialUserAccount.getType() + "");
					//WW_TODO 可以改成线程的方式
					new Thread(){
						@Override
						public void run(){ 
							s.weiboToDb(socialUserAccount, 1);
						}
					}.start();
				}
			}
		}
	}

}
