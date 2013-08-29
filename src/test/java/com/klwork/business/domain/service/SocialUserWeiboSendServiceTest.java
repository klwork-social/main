package com.klwork.business.domain.service;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.test.base.BaseTxWebTests;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserWeiboSend;
import com.klwork.business.domain.repository.SocialUserWeiboSendRepository;
import com.klwork.business.domain.service.SocialUserWeiboSendService;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class SocialUserWeiboSendServiceTest extends BaseTxWebTests {
	@Autowired
	SocialUserWeiboSendService socialUserWeiboSendService;

	@Test
	public void testService() {
	
	}

}
