package com.klwork.business.domain.service;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.ResourcesAssignManager;
import com.klwork.business.domain.model.ResourcesAssignManagerQuery;
import com.klwork.business.domain.model.SocialUseAuthorityListQuery;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class ResourcesAssignManagerServiceTest extends BaseTxWebTests {
	@Autowired
	ResourcesAssignManagerService resourcesAssignManagerService;
	
	@Autowired
	SocialUseAuthorityListService socialUseAuthorityListService;

	@Test
	public void testService() {
		String teamId = "111";
		List permits = new ArrayList();
		String entityType = "1";
		String type = "2";
		permits.add(entityType);
		permits.add("0");
		SocialUserAccount sc = new SocialUserAccount();
		sc.setId("1234");
		resourcesAssignManagerService.addAccountPermit(sc ,teamId, permits,entityType,type);
		
		teamId = "222";
		permits.add("3");
		resourcesAssignManagerService.addAccountPermit(sc ,teamId, permits,entityType,type);
		
		ResourcesAssignManagerQuery query = new ResourcesAssignManagerQuery();
		query.setEntityId(sc.getId()).setEntityType(entityType).setType(type);
		List<ResourcesAssignManager> list = resourcesAssignManagerService.findResourcesAssignManagerByQueryCriteria(query, null);
		assertEquals(1,list.size());
		SocialUseAuthorityListQuery squery = new SocialUseAuthorityListQuery();
		//query.setManagerGroupId(managerGroupId)
		List t = socialUseAuthorityListService.findSocialUseAuthorityListByQueryCriteria(squery, null);
		//assertEquals(3,t.size());
	}

}
