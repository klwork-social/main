package com.klwork.business.domain.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.MyCalendarEvent;
import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.model.Todo;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

public class ProjectServiceTest extends BaseTxWebTests {
	@Autowired
	ProjectService projectService;
	
	@Autowired
	TodoService todoService;
	
	@Test
	public void testService() {
		Project project = new Project();
		project.setName("我是谁");
		project.setDescription("descript");
		projectService.createProject(project);
		
		
		List<Todo> beanList = new ArrayList<Todo>();
		Todo a = todoService.newTodo();
		a.setName("sdfsdf");
		a.setProId(project.getId());
		Todo t = todoService.newTodo();
		t.setPid("11");
		t.setStartDate(StringDateUtil.now());
		//关联一个日程
		t.setCompletionDate(StringDateUtil.addDay(StringDateUtil.now(), 3));
		t.setRelatedCalendar(true);
		t.setName("sdfsdf");
		t.setProId(project.getId());
		beanList.add(a);
		beanList.add(t);
		todoService.saveTodoList(beanList);
		
		//加上默认的一共3个
		
		MyCalendarEvent event = todoService.queryRelatedEvent(t);
		assertNotNull(event);
		
		projectService.deleteProject(project);
		event = todoService.queryRelatedEvent(t);
		assertNull(event);
	}

}
