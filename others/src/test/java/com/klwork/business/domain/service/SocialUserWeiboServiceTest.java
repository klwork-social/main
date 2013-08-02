package com.klwork.business.domain.service;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.test.base.BaseTxWebTests;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.repository.SocialUserWeiboRepository;
import com.klwork.business.domain.service.SocialUserWeiboService;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class SocialUserWeiboServiceTest extends BaseTxWebTests {
	@Autowired
	SocialUserWeiboService socialUserWeiboService;

	@Test
	public void testService() {
	
	}

}
