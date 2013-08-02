package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.test.base.BaseTxWebTests;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.repository.SocialUserAccountRepository;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class SocialUserAccountRepositoryTest extends BaseTxWebTests {
	@Autowired
	private SocialUserAccountRepository rep;

	@Test
	public void testCrud() {
		
	}

}
