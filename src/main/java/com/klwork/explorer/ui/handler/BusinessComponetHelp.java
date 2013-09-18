package com.klwork.explorer.ui.handler;

import java.util.Map;

import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.vaadin.ui.ComboBox;

public class BusinessComponetHelp {
	protected TeamService teamService;
	protected I18nManager i18nManager;
	
	public BusinessComponetHelp() {
		this.i18nManager = ViewToolManager.getI18nManager();
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
	}
	
	/**
	 * 得到登录用户团队的comboBox
	 * @return
	 */
	public ComboBox getUserOfTeamComboBox() {
		Map<String, String> groupsMap = getUserOfMyMap();
		//"用户组"
		return CommonFieldHandler.createComBox(i18nManager
				.getMessage(Messages.TEAM_SELECT), groupsMap, "null");
		
	}

	public Map<String, String> getUserOfMyMap() {
		Map<String, String> groupsMap = teamService.queryTeamMapOfUser(LoginHandler
				.getLoggedInUser().getId());
		groupsMap.put("", i18nManager
				.getMessage(Messages.SELECT_DEFAULT));
		return groupsMap;
	}
	
	public Map<String, String> getUserOfMyInMap() {
		Map<String, String> groupsMap = teamService.queryTeamMapOfUserIn(LoginHandler
				.getLoggedInUser().getId());
		groupsMap.put("", i18nManager
				.getMessage(Messages.SELECT_DEFAULT));
		return groupsMap;
	}
	
	/**
	 * 得到登录用户团队的comboBox
	 * @return
	 */
	public ComboBox getUserOfTeamComboBox(String caption,String defaultValue) {
		Map<String, String> groupsMap = getUserOfMyMap();
		//"用户组"
		return CommonFieldHandler.createComBox(caption, groupsMap,defaultValue);
		
	}
	
}
