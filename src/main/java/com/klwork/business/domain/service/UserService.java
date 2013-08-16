package com.klwork.business.domain.service;

import java.util.Date;
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.util.IoUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.explorer.Constants;
import com.klwork.explorer.security.LoggedInUserImpl;
import com.klwork.explorer.security.LoginHandler;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class UserService {
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private SocialUserAccountInfoService socialUserAccountInfoService;

	public User createUser(String userId, String firstName, String lastName,
			String password, String email, String imageResource,
			List<String> groups, List<String> userInfo) {
		User user = null;
		if (identityService.createUserQuery().userId(userId).count() == 0) {

			// Following data can already be set by demo setup script

			user = identityService.newUser(userId);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setPassword(password);
			user.setEmail(email);
			identityService.saveUser(user);

			if (groups != null) {
				for (String group : groups) {
					identityService.createMembership(userId, group);
				}
			}
		}else {
			return identityService.createUserQuery().userId(userId).singleResult();
		}

		// Following data is not set by demo setup script

		// image
		if (imageResource != null) {
			//WW_TODO url图像怎么处理
			byte[] pictureBytes = IoUtil.readInputStream(getClass()
					.getClassLoader().getResourceAsStream(imageResource), null);
			Picture picture = new Picture(pictureBytes, "image/jpeg");
			identityService.setUserPicture(userId, picture);
		}

		// user info
		if (userInfo != null) {
			for (int i = 0; i < userInfo.size(); i += 2) {
				identityService.setUserInfo(userId, userInfo.get(i),
						userInfo.get(i + 1));
			}
		}
		return user;

	}

	public void createGroup(String groupId, String type) {
		if (identityService.createGroupQuery().groupId(groupId).count() == 0) {
			Group newGroup = identityService.newGroup(groupId);
			newGroup.setName(groupId.substring(0, 1).toUpperCase()
					+ groupId.substring(1));
			newGroup.setType(type);
			identityService.saveGroup(newGroup);
		}
	}

	public LoggedInUserImpl doUserLogin(
			String userName, String password) {
		LoggedInUserImpl loggedInUser = null;
		if (identityService.checkPassword(userName, password)) {
			loggedInUser = subjectToUserEntity(userName, password);
		}

		if (loggedInUser != null) {
			// WW_TODO 登录成功设置用户信息
			LoginHandler.setUser(loggedInUser);
			Subject subject = SecurityUtils.getSubject();
			Session session = subject.getSession();
			session.setAttribute(LoginHandler.LOGIN_USER_KEY, loggedInUser);

		}
		return loggedInUser;
	}
	
	public String login(Subject currentUser, String username, String password) {
		String result = "login";
		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password);
		token.setRememberMe(false);
		try {
			currentUser.login(token);
			//登录成功后进行记录
			
			SocialUserAccountInfo info = new SocialUserAccountInfo();
			info.setKey("user_last_logged_time");
			Date d = new Date();
			info.setType(DictDef.dict("user_info_type"));
			info.setUserId(username);
			info.setValue(StringDateUtil.parseString(d, 4));
			info.setValueType(DictDef.dictInt("date"));
			info.setValueDate(d);
			socialUserAccountInfoService.createSocialUserAccountInfo(info);
			result = "success";
		} catch (UnknownAccountException uae) {
			result = "failure";
		} catch (IncorrectCredentialsException ice) {
			result = "failure";
		} catch (LockedAccountException lae) {
			result = "failure";
		} catch (AuthenticationException ae) {
			result = "failure";
		}
		return result;
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

	
	/**
	 * 进行用户的登录处理
	 * @param user
	 */
	public void doLogin(User user) {
		Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			login(currentUser,user.getId(), user.getPassword());
		}
		
	}

}