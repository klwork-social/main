package com.klwork.business.domain.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class SocialUserAccountServiceTest extends BaseTxWebTests {
	@Autowired
	public
	SocialUserAccountService socialUserAccountService;
	
	@Autowired
	public
	SocialUserAccountInfoService socialUserAccountInfoService;

	@Test
	public void testService() throws WeiboException {
		User sinaUser = new User(null);
		sinaUser.setFollowersCount(50);
		sinaUser.setId("222222");
		sinaUser.setDescription("sdfsdf");
		
		//socialUserAccountService.addSinaSocialInfo(sinaUser);
	}

}
