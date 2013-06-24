package com.klwork.business.domain.service;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.OutsourcingProjectQuery;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

public class OutsourcingProjectServiceTest extends BaseTxWebTests {
	@Autowired
	OutsourcingProjectService outsourcingProjectService;

	@Test
	public void testService() {
		OutsourcingProject o = new OutsourcingProject();
		o.setBounty(333d);
		o.setName("sdfsfd");
		o.setDeadline(new Date());
		String relatedTodoId = "34567";
		String procInstId = "789999";
		o.setRelatedTodo(relatedTodoId);
		o.setProcInstId(procInstId);
		outsourcingProjectService.createOutsourcingProject(o);
		
		OutsourcingProjectQuery query = new OutsourcingProjectQuery();
		
		query.setProcInstId(procInstId );
		OutsourcingProject r = outsourcingProjectService.findOneEntityByQuery(query );
		assertEquals(r.getName(),o.getName());
	}

}
