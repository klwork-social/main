package com.klwork.business.domain.service;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.ProjectParticipant;
import com.klwork.business.domain.model.ProjectParticipantQuery;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class ProjectParticipantServiceTest extends BaseTxWebTests {
	@Autowired
	ProjectParticipantService projectParticipantService;

	@Test
	public void testService() {
		ProjectParticipant p = new ProjectParticipant();
		
		p.setIsWinner(true);
		String outPrgId = "333";
		p.setOutPrgId(outPrgId);
		projectParticipantService.createProjectParticipant(p);
		
		
		ProjectParticipantQuery query = new ProjectParticipantQuery();
		query.setOutPrgId(outPrgId);
		List<ProjectParticipant> s = projectParticipantService.findProjectParticipantByQueryCriteria(query , null);
		assertEquals(1,s.size());
	}

}
