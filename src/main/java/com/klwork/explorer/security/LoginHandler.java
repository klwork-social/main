package com.klwork.explorer.security;

import org.activiti.engine.impl.identity.Authentication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


public class LoginHandler {
	
	public static final String LOGIN_USER_KEY = "social_login_user";
	// Thread local storage of instance for each user
	private static final ThreadLocal<LoggedInUser> loginHandler_currentUser = new ThreadLocal<LoggedInUser>();

	public static LoggedInUser getLoggedInUser() {
		return getUser();
	}

	public static LoggedInUser getUser() {
		LoggedInUser u =  loginHandler_currentUser.get();
		if(u == null){
			Subject subject = SecurityUtils.getSubject();
            if (subject.isAuthenticated())
            {
            	Object ou = SecurityUtils.getSubject().getSession().getAttribute(LOGIN_USER_KEY);
                if(ou != null){
                	setUser((LoggedInUser)ou);
                	return (LoggedInUser)ou;
                }
            }
		}
		return u;
	}
	
	public static void resetUser() {
		Object ou = SecurityUtils.getSubject().getSession().getAttribute(LOGIN_USER_KEY);
        if(ou != null){
        	setUser((LoggedInUser)ou);
        }
	}
	
	public static void setUser(LoggedInUser user) {
		loginHandler_currentUser.set(user);
		if(user != null){
			Authentication.setAuthenticatedUserId(user.getId());
		}else {
			Authentication.setAuthenticatedUserId(null);
		}
	}

	/**
	 * 用户认证
	 * @param id
	 * @param password
	 */
	public static void authenticate(String id, String password) {
		
	}

	public static void logout() {
		SecurityUtils.getSubject().logout();
		setUser(null);
	}

}
