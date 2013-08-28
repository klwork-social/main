package com.klwork.business.domain.service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.model.Team;
import com.klwork.test.base.BaseTxWebTests;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
public class SocialUserAccountServiceTest extends BaseTxWebTests {
	@Autowired
	public
	SocialUserAccountService socialUserAccountService;
	
	@Autowired
	public
	SocialUserAccountInfoService socialUserAccountInfoService;
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	TeamMembershipService teamMembershipService;
	
	@Autowired
	ResourcesAssignManagerService resourcesAssignManagerService;

	@Test
	public void testService() throws WeiboException {
		//新增一个team
		Team team  = new Team();
		team.setName("group1");
		team.setType(EntityDictionary.TEAM_GROUP_TYPE_COMM);
		String mainUser = "testUser";
		team.setOwnUser(mainUser);
		teamService.createTeam(team);
		
		String zhangshan = "zhangshan123";
		//把张三放到其组中
		teamMembershipService.createTeamMembership(zhangshan, team.getId());
		//创建一个账号
		SocialUserAccount socialUserAccount = new SocialUserAccount();
		socialUserAccount.setName("testUser");
		socialUserAccount.setOwnUser(mainUser);
		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		
		//账号进行共享
		List permits = new ArrayList();
		String entityType = DictDef.dict("user_account_info_type");
		String type = DictDef.dict("team_type_weibo_permit");
		permits.add(entityType);
		permits.add("0");
		resourcesAssignManagerService.addAccountPermit(socialUserAccount ,team.getId(), permits,entityType,type);
		
		//查询组的账号
		List<String> groups = teamService.queryUserInTeamIds(zhangshan);
		//groups = Collections.EMPTY_LIST;
		SocialUserAccountQuery q = new SocialUserAccountQuery();
		q.setTeams(groups);
		List list = socialUserAccountService.findSocialUserAccountByQueryCriteria(q, null);
		assertEquals(1,list.size());
		//socialUserAccountService.addSinaSocialInfo(sinaUser);
	}

}
