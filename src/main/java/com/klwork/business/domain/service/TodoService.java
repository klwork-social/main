package com.klwork.business.domain.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.business.domain.model.MyCalendarEvent;
import com.klwork.business.domain.model.MyCalendarEventQuery;
import com.klwork.business.domain.model.Todo;
import com.klwork.business.domain.model.TodoQuery;
import com.klwork.business.domain.repository.MyCalendarEventRepository;
import com.klwork.business.domain.repository.TodoRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class TodoService {
	@Autowired
	protected TodoRepository rep;
	
	@Autowired
	private MyCalendarEventService myCalendarEventService;
	
	public void createTodo(Todo todo) {
		saveTodoCalendar(todo);
		rep.insert(todo);
	}

	public void deleteTodo(Todo todo) {
		MyCalendarEvent event = queryRelatedEvent(todo);
		if(event != null){
			myCalendarEventService.deleteMyCalendarEvent(event);
		}
		rep.deleteById(todo.getId());
	}

	public int updateTodo(Todo todo) {
		saveTodoCalendar(todo);
		return rep.update(todo);
	}

	private void saveTodoCalendar(Todo todo) {
		if(todo.getRelatedCalendar() && todo.getStartDate() != null && todo.getCompletionDate() != null){
			MyCalendarEvent myCalendarEvent = queryRelatedEvent(todo);
			if(myCalendarEvent == null){
				myCalendarEvent = new MyCalendarEvent();
			}
			myCalendarEvent.setCaption(todo.getName());
			if(myCalendarEvent.getOwnUser() == null){
				myCalendarEvent.setOwnUser(todo.getAssignedUser());
			}
			if(myCalendarEvent.getStyleName() == null){
				myCalendarEvent.setStyleName("color3");
			}
			if(myCalendarEvent.getDescription() == null){
				myCalendarEvent.setDescription(todo.getDescription());
			}
			myCalendarEvent.setRelatedTodo(todo.getId());
			myCalendarEvent.setStart(todo.getStartDate());
			myCalendarEvent.setEndDate(todo.getCompletionDate());
			myCalendarEventService.createMyCalendarEvent(myCalendarEvent);
		}
	}

	public MyCalendarEvent queryRelatedEvent(Todo todo) {
		MyCalendarEventQuery query = new MyCalendarEventQuery();
		query.setRelatedTodo(todo.getId());
		MyCalendarEvent myCalendarEvent = myCalendarEventService.findMyCalendarEventByQuery(query);
		return myCalendarEvent;
	}

	public List<Todo> findTodoByQueryCriteria(TodoQuery query,
			ViewPage<Todo> page) {
		return rep.findTodoByQueryCriteria(query, page);
	}

	public Todo findTodoById(String id) {
		return rep.find(id);
	}
	
	public int count(TodoQuery query) {
		return rep.findTodoCountByQueryCriteria(query);
	}
	
/*	public Todo creatTodoOfProject(String projectId) {
		Todo oEntity = rep.newTodo();
		oEntity.setProId(projectId);
		createTodo(oEntity);
		return oEntity;
	}*/
	
	/**
	 * 
	 * @param beanList
	 */
	public void saveTodoList(List<Todo> beanList) {
		Date now = StringDateUtil.now();
		for (Iterator iterator = beanList.iterator(); iterator.hasNext();) {
			Todo todo = (Todo) iterator.next();
			todo.setLastUpdate(now);
			if(findTodoById(todo.getId()) == null){//数据库没有就新增
				createTodo(todo);
			}else{
				updateTodo(todo);
			}
			
		}
	}

	public Todo newTodo() {
		return rep.newTodo();
	}
}