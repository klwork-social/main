package com.klwork.business.domain.service;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.Todo;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class TodoServiceTest extends BaseTxWebTests {
	@Autowired
	TodoService todoService;

	@Test
	public void testService() {
		Todo a = todoService.newTodo();
		todoService.createTodo(a);
		List<Todo> beanList = new ArrayList<Todo>();
		Todo t = todoService.newTodo();
		t.setPid("11");
		t.setName("sdfsdf");
		t.setId("123");
		beanList.add(a);
		beanList.add(t);
		todoService.saveTodoList(beanList );
	}

}
