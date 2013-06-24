package com.klwork.flow;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.test.base.WebContextTestExecutionListener;

@ContextConfiguration(locations = { "classpath:/spring-xml/applicationContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ WebContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, })
public class UserSpringIntegrationTest {
	@Test
	public void test() {
		I18nManager i18nManager = ViewToolManager.getI18nManager();
		assertEquals("未设置到期日",
				i18nManager.getMessage(Messages.TASK_DUEDATE_UNKNOWN));
	}
}
