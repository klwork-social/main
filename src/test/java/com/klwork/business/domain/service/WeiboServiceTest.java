package com.klwork.business.domain.service;
import static org.junit.Assert.assertNotNull;

import org.activiti.engine.IdentityService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import com.klwork.business.domain.model.SocialUserWeiboQuery;
import com.klwork.explorer.security.LoggedInUserImpl;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class WeiboServiceTest extends BaseTxWebTests {
	@Autowired
	SocialUserWeiboService socialUserWeiboService;
	@Autowired
	SocialTencentService socialTencentService;
	@Autowired 
	SocialSinaService socialSinaService;
	@Autowired
	IdentityService identityService;
	@Autowired
	UserService userService;
	
	@Test
	public void testService() {
		SocialUserWeiboQuery query = new SocialUserWeiboQuery();
		query.setUserAccountId("29601");
		assertNotNull(socialUserWeiboService.queryLastWeibo(query));
		
		
	}
	
	@Test
	@Rollback(false)
	public void testqqToDb() {
		LoggedInUserImpl u = userService.subjectToUserEntity("klwork", "123456");
		LoginHandler.setUser(u);
		socialTencentService.myWeiboToDb();
	}
	
	@Test
	@Rollback(false)
	public void testSianToDb() {
		LoggedInUserImpl u = userService.subjectToUserEntity("fir5671", "123456");
		LoginHandler.setUser(u);
		socialSinaService.myWeiboToDb(1);
	}
}
