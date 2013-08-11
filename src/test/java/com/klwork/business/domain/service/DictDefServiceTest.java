package com.klwork.business.domain.service;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.Assert.assertEquals;
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

}
