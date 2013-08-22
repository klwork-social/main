package com.klwork.business.domain.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.utils.SinaSociaTool;
import com.klwork.test.base.BaseTxWebTests;

public class UtilTest extends BaseTxWebTests {
	@Autowired
	TodoService todoService;

	@Test
	public void testGenUrl() {
		String url = SinaSociaTool.generateAuthorizationURL();
		System.out.println(url);
	}
	
	
}
