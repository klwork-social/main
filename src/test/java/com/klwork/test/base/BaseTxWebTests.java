package com.klwork.test.base;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.util.IoUtil;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.service.TeamMembershipService;
import com.klwork.business.domain.service.TeamService;
import com.klwork.business.domain.service.UserService;

@ContextConfiguration(locations = { "classpath:/spring-config/applicationContext.xml" })
public abstract class BaseTxWebTests extends
		AbstractTransactionalJUnit4SpringContextTests {
	
	protected static final String TEST_USER5 = "testUser5";

	protected static final String TEST_USER4 = "testUser4";

	protected static final String TEST_USER3 = "testUser3";

	protected static final String TEST_USER2 = "testUser2";

	protected static final String TEST_USER1 = "testUser1";

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public BaseTxWebTests() {

	}

	@Autowired
	protected IdentityService identityService;

	@Autowired
	protected TeamService teamService;

	@Autowired
	protected TeamMembershipService teamMembershipService;

	@Autowired
	protected UserService userService;

	/**
	 * 刷新session
	 */
	private void flushSession() {
		SqlSessionFactory sessionFactory = (SqlSessionFactory) applicationContext
				.getBean("sqlSessionFactory");
		sessionFactory.openSession().commit();

	}

	protected static boolean isTest = false;

	/**
	 * 执行此方法后将会把测试数据写进数据库
	 */
	public void flushToDataBase() {
		if (!isTest) {
			flushSession();
			// setComplete();
		}
	}

	/**
	 * 
	 * 创建用户及用户的team 用户：testUser1 team： develop,weibo
	 * develop[testuser2,testuser3], weibo[testuser2,testuser4]
	 * 
	 * testuser3 team: sales, sales[testuser4]
	 */
	protected void intTestUser() {
		String testUser1 = TEST_USER1;
		userService.createUser(testUser1, testUser1, "123", testUser1,
				"testUser1@126.com", null, Arrays.asList("user"), Arrays
						.asList("birthDate", "10-10-1955", "jobTitle",
								"Muppet", "location", "Hollywoord", "phone",
								"+123456789", "twitterName", "alfresco",
								"skype", "activiti_kermit_frog"));

		String testUser2 = TEST_USER2;
		userService.createUser(testUser2, testUser2, "yy", testUser2,
				"testUser2@126.com", null, Arrays.asList("user"), Arrays
						.asList("birthDate", "10-10-1955", "jobTitle",
								"Muppet", "location", "Hollywoord", "phone",
								"+123456789", "twitterName", "alfresco",
								"skype", "activiti_kermit_frog"));
		String testUser3 = TEST_USER3;
		userService.createUser(testUser3, testUser3, "yy2", testUser3,
				"testUser3@126.com", null, Arrays.asList("user"), Arrays
						.asList("birthDate", "10-10-1955", "jobTitle",
								"Muppe2t", "location", "Hollywoord", "phone",
								"+123456789", "twitterName", "alfr2esco",
								"skype", "activiti_kermit_frog"));
		String testUser4 = TEST_USER4;
		userService.createUser(testUser4, testUser4, "yy2", testUser4,
				"testUser4@126.com", null, Arrays.asList("user"), Arrays
						.asList("birthDate", "10-10-1955", "jobTitle",
								"Muppe2t", "location", "Hollywoord", "phone",
								"+123456789", "twitterName", "alfr2esco",
								"skype", "activiti_kermit_frog"));
		
		String testUser5 = TEST_USER5;
		userService.createUser(testUser5, testUser5, "yy2", testUser5,
				"testUser5@126.com", null, Arrays.asList("user"), Arrays
						.asList("birthDate", "10-10-1955", "jobTitle",
								"Muppe2t", "location", "Hollywoord", "phone",
								"+123456789", "twitterName", "alfr2esco",
								"skype", "activiti_kermit_frog"));
		// 一个管理员
		userService.createUser("testUser_management", "testUser_management",
				"yy", "testUser_management", "testUser_management@126.com",
				null, Arrays.asList("management", "user"), Arrays.asList(
						"birthDate", "10-10-1955", "jobTitle", "Muppet",
						"location", "Hollywoord", "phone", "+123456789",
						"twitterName", "alfresco", "skype",
						"activiti_kermit_frog"));

		
		// 新增一个team
		Team team = new Team();
		team.setName("develop");
		team.setType(EntityDictionary.TEAM_GROUP_TYPE_COMM);
		team.setOwnUser(testUser1);
		teamService.createTeam(team);
		teamMembershipService.createTeamMembership(testUser2, team.getId());
		teamMembershipService.createTeamMembership(testUser3, team.getId());

		team = new Team();
		team.setName("weibo");
		team.setType(EntityDictionary.TEAM_GROUP_TYPE_COMM);
		team.setOwnUser(testUser1);
		teamService.createTeam(team);

		teamMembershipService.createTeamMembership(testUser2, team.getId());
		teamMembershipService.createTeamMembership(testUser4, team.getId());

		team = new Team();
		team.setName("sales");
		team.setType(EntityDictionary.TEAM_GROUP_TYPE_COMM);
		team.setOwnUser(testUser3);
		teamService.createTeam(team);
		teamMembershipService.createTeamMembership(testUser4, team.getId());

	}
}