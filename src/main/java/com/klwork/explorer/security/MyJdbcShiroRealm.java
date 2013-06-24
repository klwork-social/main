package com.klwork.explorer.security;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.klwork.explorer.Constants;

/**
 * Shiro Realm 实现
 * 
 * @author ww
 * 
 */
public class MyJdbcShiroRealm extends AuthorizingRealm {

	private IdentityService identityService;

	public IdentityService getIdentityService() {
		return identityService;
	}

	public void setIdentityService(IdentityService identityService) {
		this.identityService = identityService;
	}

	/**
	 * 授权信息
	 */
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		String username = (String) principals.fromRealm(getName()).iterator()
				.next();
		if (username != null) {
			User la = identityService.createUserQuery().userId(username)
					.singleResult();
			if (la != null && currentRoles() != null) {
				SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
				/*
				 * for (Role each : currentRoles) { if (each.getName() != null)
				 * info.addRole(each.getName()); Collection<String> pers =
				 * each.getPermissionsAsString(); if (pers != null)
				 * info.addStringPermissions(pers); }
				 */
				info.addRole("user");
				info.addStringPermission("main");
				return info;
			}
		}

		return null;
	}

	private List currentRoles() {
		List test = new ArrayList();
		return test;
	}

	/**
	 * 认证信息
	 */
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String userName = token.getUsername();
		String password = new String(token.getPassword());
		LoggedInUserImpl loggedInUser = null;
		if (identityService.checkPassword(userName, password)) {
			loggedInUser = subjectToUserEntity(userName, password);
		}

		if (loggedInUser != null) {
			//WW_TODO 登录成功设置用户信息
			LoginHandler.setUser(loggedInUser);
			Subject subject = SecurityUtils.getSubject();
	        Session session = subject.getSession();
	        session.setAttribute(LoginHandler.LOGIN_USER_KEY, loggedInUser);
			return new SimpleAuthenticationInfo(loggedInUser.getId(),
					loggedInUser.getPassword(), getName());
		}

		return null;
	}

	public LoggedInUserImpl subjectToUserEntity(String userName, String password) {
		LoggedInUserImpl loggedInUser;
		User user = identityService.createUserQuery().userId(userName)
				.singleResult();
		// Fetch and cache user data
		loggedInUser = new LoggedInUserImpl(user, password);
		List<Group> groups = identityService.createGroupQuery()
				.groupMember(user.getId()).list();
		for (Group group : groups) {
			if (Constants.SECURITY_ROLE.equals(group.getType())) {
				loggedInUser.addSecurityRoleGroup(group);
				if (Constants.SECURITY_ROLE_USER.equals(group.getId())) {
					loggedInUser.setUser(true);
				}
				if (Constants.SECURITY_ROLE_ADMIN.equals(group.getId())) {
					loggedInUser.setAdmin(true);
				}
			} else {
				loggedInUser.addGroup(group);
			}
		}
		return loggedInUser;
	}

}
