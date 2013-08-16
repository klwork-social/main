package com.klwork.business.domain.service;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.DictDef;
import com.klwork.common.DataBaseParameters;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class DictDefServiceTest extends BaseTxWebTests {
	@Autowired
	DictDefService dictDefService;

	@Test
	public void testService() {
		DictDef dictDef = new DictDef("1", "TEST_SIGN_VALUE2", "0", "测试值", "-1");
		dictDefService.createDictDef(dictDef);
		//String s = DataBaseParameters.TEST_SIGN_VALUE;
		//assertEquals("-1",s);
	}
	
	@Test
	public void testDataBaseParameters() {
		assertEquals("1",DataBaseParameters.TENCENT);
		assertEquals("0",DataBaseParameters.SINA);
		DictDef v = DictDef.dictValue(DictDef.dict("social_type"), "0");
		assertEquals("新浪",v.getName());
	}

}
