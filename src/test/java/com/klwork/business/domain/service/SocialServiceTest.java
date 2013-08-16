package com.klwork.business.domain.service;
import static org.junit.Assert.assertNotNull;

import org.activiti.engine.IdentityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.SocialUserWeiboQuery;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.explorer.security.LoggedInUserImpl;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class SocialServiceTest extends BaseTxWebTests {
	@Autowired
	SocialUserWeiboService socialUserWeiboService;
	@Autowired
	SocialTencentService socialTencentService;
	@Autowired 
	AbstractSocialService socialSinaService;
	@Autowired
	IdentityService identityService;
	@Autowired
	UserService userService;
	@Autowired
	SocialMainService socialService;
	
	@Test
	public void testService() {
		SocialUserWeibo socialUserWeibo = new SocialUserWeibo();
		socialUserWeibo.setWeiboUid("11111111");
		String userAccountId = "99999";
		socialUserWeibo.setUserAccountId(userAccountId);
		//socialUserWeibo
		socialUserWeiboService.createSocialUserWeibo(socialUserWeibo );
		
		//查询
		SocialUserWeiboQuery query = new SocialUserWeiboQuery();
		query.setUserAccountId(userAccountId);
		assertNotNull(socialUserWeiboService.queryLastWeibo(query));
		
	}
	
	@Test
	@Rollback(true)
	public void testqqToDb() {
		LoggedInUserImpl u = userService.subjectToUserEntity("wangwei", "123456");
		LoginHandler.setUser(u);
		socialTencentService.myWeiboToDb();
	}
	
	@Test
	@Rollback(false)
	public void testSianToDb() {
		LoggedInUserImpl u = userService.subjectToUserEntity("wangwei", "123456");
		LoginHandler.setUser(u);
		socialSinaService.myWeiboToDb();
	}
}
