package com.klwork.explorer.security;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.klwork.business.domain.service.UserService;

/**
 * Shiro Realm 实现
 * 
 * @author ww
 * 
 */
public class MyJdbcShiroRealm extends AuthorizingRealm {

	public IdentityService identityService;
	
	private UserService userService;
	
	

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

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
		LoggedInUserImpl loggedInUser = userService.doUserLogin(userName, password);
		
		if (loggedInUser != null) {
		return new SimpleAuthenticationInfo(loggedInUser.getId(),
				loggedInUser.getPassword(), getName());
		}
		return null;
	}

}
