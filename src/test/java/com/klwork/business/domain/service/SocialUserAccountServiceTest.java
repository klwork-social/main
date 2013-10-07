package com.klwork.business.domain.service;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import weibo4j.model.WeiboException;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.model.TeamQuery;
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
	ResourcesAssignManagerService resourcesAssignManagerService;

	@Test
	public void testService() throws WeiboException {
		intTestUser();
		//新增一个team
		TeamQuery query = new TeamQuery();
		query.setOwnUser(TEST_USER1);
		query.setName("weibo");
		List<Team> teams  = teamService.findTeamByQueryCriteria(query, null);
		Team weiboTeam = teams.get(0);
		
		//创建一个账号
		SocialUserAccount socialUserAccount = new SocialUserAccount();
		socialUserAccount.setName("mysinaWeibo");
		socialUserAccount.setOwnUser(TEST_USER1);
		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		
		//账号进行共享
		List permits = new ArrayList();
		String entityType = DictDef.dict("user_account_info_type");
		String type = DictDef.dict("team_type_weibo_permit");
		permits.add(entityType);
		permits.add("0");
		//设置账户的被其他team共享,weibo[testuser2,testuser4]
		resourcesAssignManagerService.addAccountPermit(socialUserAccount ,weiboTeam.getId(), permits,entityType,type);
		
		//查询用户所在的团队,weibo,和sale团队中
		List<String> groups = teamService.queryUserInTeamIds(TEST_USER4);
		assertEquals(2,groups.size());
		
		SocialUserAccountQuery q = new SocialUserAccountQuery();
		q.setTeams(groups);
		List list = socialUserAccountService.findSocialUserAccountByQueryCriteria(q, null);
		assertEquals(1,list.size());
	}

}
