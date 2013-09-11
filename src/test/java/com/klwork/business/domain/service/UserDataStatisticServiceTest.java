package com.klwork.business.domain.service;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.UserDataStatistic;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class UserDataStatisticServiceTest extends BaseTxWebTests {
	@Autowired
	UserDataStatisticService userDataStatisticService;

	@Test
	public void testService() {
		String userId = "wangwei";
		userDataStatisticService.saveUserTaskStatistic(userId);
		UserDataStatistic c = userDataStatisticService.findUserDataStatisticById(userId);
		assertEquals(1,c.getMyTaskTotal().intValue());
		assertEquals(2,c.getTodoTaskTotal().intValue());
	}

}
