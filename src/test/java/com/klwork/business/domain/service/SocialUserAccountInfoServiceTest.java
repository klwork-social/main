package com.klwork.business.domain.service;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.test.base.BaseTxWebTests;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountInfoQuery;
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
		SocialUserAccountInfo info = new SocialUserAccountInfo();
		info.setKey("user_last_logged_time");
		Date d = new Date();
		info.setType(DictDef.dict("user_info_type"));
		info.setUserId("test");
		info.setValue(StringDateUtil.parseString(d, 4));
		info.setValueType(DictDef.dictInt("date"));
		info.setValueDate(d);
		socialUserAccountInfoService.createSocialUserAccountInfo(info);
		
		
		SocialUserAccountInfoQuery query = new SocialUserAccountInfoQuery();
		query.setUserId("test");
		Date aDate = StringDateUtil.dateToYMD(d);
		query.setDateAfter(aDate);
		query.setOrderBy("value_date_ asc");
		List<SocialUserAccountInfo> list = socialUserAccountInfoService.findSocialUserAccountInfoByQueryCriteria(query,null);
		assertEquals(1,list.size());
		
	}

}
