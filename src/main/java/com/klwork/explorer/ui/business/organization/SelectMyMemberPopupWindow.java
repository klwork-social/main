package com.klwork.explorer.ui.business.organization;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.custom.SelectUsersPopupWindow;

public class SelectMyMemberPopupWindow extends SelectUsersPopupWindow {
	protected transient IdentityService identityService;
	protected transient TeamService teamService;
	
	public SelectMyMemberPopupWindow(String message, boolean b, boolean c,
			List<String> currentMembers) {
		super(message, b, c, currentMembers);
		identityService = ProcessEngines.getDefaultProcessEngine()
				.getIdentityService();
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
	}

	@Override
	protected void initSearchField() {

	}
	
	@Override
	protected void initEnd() {
		List<User> results = currentSpecialUser();
		handleQueryUserList(results);
	}
	
	protected List<User> currentSpecialUser() {
		//查询正式成员组
		Team t = teamService.createTeamByUserAndType(LoginHandler.getLoggedInUser().getId(), EntityDictionary.TEAM_GROUP_TYPE_FORMAL,"default");
		List<User> users = identityService.createUserQuery()
				.memberOfTeam(t.getId()).list();
		return users;
	}
}
