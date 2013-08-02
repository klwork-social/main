package com.klwork.business.domain.service;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.test.base.BaseTxWebTests;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.repository.SocialUserAccountInfoRepository;
import com.klwork.business.domain.service.SocialUserAccountInfoService;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class SocialUserAccountInfoServiceTest extends BaseTxWebTests {
	@Autowired
	SocialUserAccountInfoService socialUserAccountInfoService;

	@Test
	public void testService() {
	
	}

}
