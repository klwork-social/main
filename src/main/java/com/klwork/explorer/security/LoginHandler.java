package com.klwork.explorer.security;

import org.activiti.engine.impl.identity.Authentication;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.mortbay.log.Log;

import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;
import com.klwork.explorer.web.dashboard.DashboardUI;
import com.vaadin.ui.UI;

public class LoginHandler {

	public static final String LOGIN_USER_KEY = "social_login_user";
	// Thread local storage of instance for each user
	private static ThreadLocal<LoggedInUser> loginHandler_currentUser = new ThreadLocal<LoggedInUser>();
	private static Logger logger = LoggerFactory.getLogger(LoginHandler.class);

	public static LoggedInUser getLoggedInUser() {
		return getUser();
	}

	public static LoggedInUser getUser() {
		if (UI.getCurrent() != null) {
			LoggedInUser u = ((DashboardUI) UI.getCurrent()).getLoggedInUser();
			// LoggedInUser u = loginHandler_currentUser.get();
			if (u == null) {
				Log.debug("从线程中没有取到用户");
				return querySecurityUser();
			}
			return u;
		} else {
			LoggedInUser u = loginHandler_currentUser.get();
			return u;
		}
	}

	public static LoggedInUser querySecurityUser() {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			Object ou = SecurityUtils.getSubject().getSession()
					.getAttribute(LOGIN_USER_KEY);
			if (ou != null) {
				setUser((LoggedInUser) ou);
				return (LoggedInUser) ou;
			}
		}
		return null;
	}

	public static void resetUser() {
		Object ou = SecurityUtils.getSubject().getSession()
				.getAttribute(LOGIN_USER_KEY);
		if (ou != null) {
			LoggedInUser user = (LoggedInUser) ou;
			logger.debug("重新设置用户：" + user.getId());
			setUser(user);
		} else {
			logger.debug("重新设置用户id为空");
		}
	}

	public static void setUser(LoggedInUser user) {
		loginHandler_currentUser.set(user);
		if (user != null) {
			Authentication.setAuthenticatedUserId(user.getId());
		} else {
			Authentication.setAuthenticatedUserId(null);
		}
	}

	/**
	 * 用户认证
	 * 
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
