package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.test.base.BaseTxWebTests;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.repository.SocialUserWeiboRepository;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class SocialUserWeiboRepositoryTest extends BaseTxWebTests {
	@Autowired
	private SocialUserWeiboRepository rep;

	@Test
	public void testCrud() {
		
	}

}
