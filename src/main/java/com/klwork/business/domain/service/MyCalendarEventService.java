package com.klwork.business.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.business.domain.model.MyCalendarEvent;
import com.klwork.business.domain.model.MyCalendarEventQuery;
import com.klwork.business.domain.repository.MyCalendarEventRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class MyCalendarEventService {
	@Autowired
	private MyCalendarEventRepository rep;

	public void createMyCalendarEvent(MyCalendarEvent myCalendarEvent) {
		Date lastUpdate = StringDateUtil.now();
		myCalendarEvent.setLastUpdate(lastUpdate);
		if(StringTool.judgeBlank(myCalendarEvent.getId())){
			rep.update(myCalendarEvent);
		}else {
			myCalendarEvent.setId(rep.getNextId());
			myCalendarEvent.setCreationDate(lastUpdate);
			rep.insert(myCalendarEvent);
		}
	}

	public void deleteMyCalendarEvent(MyCalendarEvent myCalendarEvent) {
		if(StringTool.judgeBlank(myCalendarEvent.getId())){
			rep.deleteById(myCalendarEvent.getId());
		}
	}

	public int updateMyCalendarEvent(MyCalendarEvent myCalendarEvent) {
		return rep.update(myCalendarEvent);
	}

	public List<MyCalendarEvent> findMyCalendarEventByQueryCriteria(MyCalendarEventQuery query,
			ViewPage<MyCalendarEvent> page) {
		return rep.findMyCalendarEventByQueryCriteria(query, page);
	}

	public MyCalendarEvent findMyCalendarEventByQuery(MyCalendarEventQuery query) {
		List<MyCalendarEvent> list =  rep.findMyCalendarEventByQueryCriteria(query, null);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	public MyCalendarEvent findMyCalendarEventById(String id) {
		return rep.find(id);
	}
	
	public int count(MyCalendarEventQuery query) {
		return rep.findMyCalendarEventCountByQueryCriteria(query);
	}
}