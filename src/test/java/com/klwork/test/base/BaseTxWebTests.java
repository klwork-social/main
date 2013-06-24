package com.klwork.test.base;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath:/spring-config/applicationContext.xml" })
public  abstract class BaseTxWebTests extends AbstractTransactionalJUnit4SpringContextTests {


	protected Logger logger = LoggerFactory.getLogger(getClass());

	public BaseTxWebTests() {
		
	}
	
	/**
	 * 刷新session
	 */
	private void flushSession() {
		SqlSessionFactory sessionFactory = (SqlSessionFactory) applicationContext
				.getBean("sqlSessionFactory");
		sessionFactory.openSession().commit();

	}
	protected static boolean isTest = false;
	
	/**
	 * 执行此方法后将会把测试数据写进数据库
	 */
	public void flushToDataBase() {
		if (!isTest) {
			flushSession();
			//setComplete();
		}
	}
}