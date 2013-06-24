package com.klwork.business.domain.service;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.test.base.BaseTxWebTests;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.MyCalendarEvent;
import com.klwork.business.domain.repository.MyCalendarEventRepository;
import com.klwork.business.domain.service.MyCalendarEventService;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class MyCalendarEventServiceTest extends BaseTxWebTests {
	@Autowired
	MyCalendarEventService myCalendarEventService;

	@Test
	public void testService() {
		MyCalendarEvent myCalendarEvent = new MyCalendarEvent();
		myCalendarEvent.setCaption("google3");
		myCalendarEvent.setAllDay(true);
		myCalendarEventService.createMyCalendarEvent(myCalendarEvent);
		MyCalendarEvent myCalendarEvent2 = myCalendarEventService.findMyCalendarEventById(myCalendarEvent.getId());
		assertEquals(myCalendarEvent.getCaption(),myCalendarEvent2.getCaption());
		assertEquals(true,myCalendarEvent2.getAllDay());
	}

}
