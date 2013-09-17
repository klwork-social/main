/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.klwork.business.domain.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.service.SocialEvernoteService;
import com.klwork.business.domain.service.SocialMainService;
import com.klwork.business.domain.service.SocialSinaService;
import com.klwork.business.domain.service.SocialTencentService;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.business.domain.service.UserService;
import com.klwork.business.utils.SinaSociaTool;
import com.klwork.business.utils.TencentSociaTool;
import com.klwork.common.DataBaseParameters;
import com.klwork.common.SystemConstants;
import com.klwork.common.dto.vo.AjaxResult;
import com.klwork.explorer.security.LoginHandler;

/**
 * The Class LoginController.
 */
@Controller
@RequestMapping("*")
public class LoginController {
	@Autowired
	private SocialSinaService socialSinaService;

	@Autowired
	private SocialTencentService socialTencentService;

	@Autowired
	public UserService userService;

	@Autowired
	public SocialUserAccountService socialUserAccountService;

	@Autowired
	IdentityService identityService;

	@Autowired
	SocialMainService socialService;
	
	@Autowired
	SocialEvernoteService socialEvernoteService;

	/**
	 * 进入到普通登录页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		// System.out.println("login:" + request.getRequestURL());
		return "login";
	}

	/**
	 * 进入到普通登录页面
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String loginForm(HttpServletRequest request, ModelMap modelMap) {
		if (request.getAttribute("shiroLoginFailure") != null) {
			String exception = (String) request
					.getAttribute("shiroLoginFailure");
			if (exception.endsWith("UnknownAccountException")
					|| exception.endsWith("IncorrectCredentialsException")) {
				modelMap.put("userNotFound", true);
			} else if (exception.endsWith("LockedAccountException")) {
				modelMap.put("userLocked", true);
			} else {
				modelMap.put("error", true);
			}
		}

		return "login";
	}

	/**
	 * 普通登录的提交
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "login-submit")
	public void loginSubmit(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		Subject currentUser = SecurityUtils.getSubject();
		String result = "login";
		if (!currentUser.isAuthenticated()) {
			userService.login(currentUser, username, password);
			afterLoginSuccess(username);
		} else {// 重复登录
			/*
			 * ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
			 * if(!
			 * shiroUser.getLoginName().equalsIgnoreCase(username)){//如果登录名不同
			 * currentUser.logout(); result =
			 * login(currentUser,username,password); }
			 */
		}
		
		goAuthorizeSuccessUrl(request, response);
	}

	/**
	 * 新浪微博的回调
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "weibo-login")
	public ModelAndView weiboLogin(HttpServletRequest request,
			HttpServletResponse response, String code) throws IOException {
		ModelMap retMap = new ModelMap();
		
		Map thirdUserMap = socialSinaService.queryUserInfoByCode(code, null, null);
		request.getSession().setAttribute(
				SystemConstants.SESSION_THIRD_USER_MAP, thirdUserMap);
		request.getSession().setAttribute(
				SystemConstants.SESSION_THIRD_LOGIN_TYPE,
				DataBaseParameters.SINA);
		if(checkResetOauth(request)){//后台进行测试
			String userId = (String) request.getSession().getAttribute(SystemConstants.COOKIE_USER_ID);
			org.activiti.engine.identity.User u = identityService
					.createUserQuery().userId(userId)
					.singleResult();
			saveSocialInfo(request,u);
			return new ModelAndView("updateOauthSuccess", retMap);
		}
		org.activiti.engine.identity.User user = socialSinaService
				.handlerUserAuthorize(thirdUserMap);
		if (user != null) {// 以前已经登录过
			userService.doLogin(user);
			goAuthorizeSuccessUrl(request, response);
		} else {// 生成一个用户放到前台
			user = socialSinaService.initUserByThirdUser(thirdUserMap);
		}
		
		retMap.put("user", user);
		return new ModelAndView("oauthPage", retMap);
	}

	public void goAuthorizeSuccessUrl(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String index = request.getContextPath() + "/";
		if(checkResetOauth(request)){
			index = request.getContextPath() + "/user/updateOauthSuccess";
		}
		response.sendRedirect(index);
	}

	public boolean checkResetOauth(HttpServletRequest request) {
		String state = queryWeiboState(request);
		return "reset-oauth".equals(state);
	}
	
	@RequestMapping(value = "updateOauthSuccess")
	public ModelAndView updateOauthSuccess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelMap retMap = new ModelMap();
		return new ModelAndView("updateOauthSuccess", retMap);
	}
	
	/**
	 * 腾讯微博的回调
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @param openid
	 * @param openkey
	 * @throws IOException
	 */
	@RequestMapping(value = "qq-login")
	public ModelAndView qqLogin(HttpServletRequest request,
			HttpServletResponse response, String code, String openid,
			String openkey) throws IOException {
		ModelMap retMap = new ModelMap();
		Map thirdUserMap = socialTencentService.queryUserInfoByCode(code,
				openid, openkey);
		request.getSession().setAttribute(
				SystemConstants.SESSION_THIRD_USER_MAP, thirdUserMap);
		request.getSession().setAttribute(
				SystemConstants.SESSION_THIRD_LOGIN_TYPE,
				DataBaseParameters.TENCENT);
		
		if(checkResetOauth(request)){//后台进行测试,添加完毕直接进行测试
			String userId = (String) request.getSession().getAttribute(SystemConstants.COOKIE_USER_ID);
			org.activiti.engine.identity.User u = identityService
					.createUserQuery().userId(userId)
					.singleResult();
			saveSocialInfo(request,u);
			return new ModelAndView("updateOauthSuccess", retMap);
		}
		org.activiti.engine.identity.User user = socialTencentService
				.handlerUserAuthorize(thirdUserMap);
		if (user != null) {// 以前已经登录过
			userService.doLogin(user);
			afterLoginSuccess(user.getId());
			goAuthorizeSuccessUrl(request, response);
		} else {// 生成一个用户放到前台
			user = socialTencentService.initUserByThirdUser(thirdUserMap);
		}
		
		retMap.put("user", user);
		return new ModelAndView("oauthPage", retMap);
	}
	
	
	/**
	 * 微盘的回调
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "vdisk")
	public ModelAndView vdisk(HttpServletRequest request,
			HttpServletResponse response, String code) throws IOException {
		ModelMap retMap = new ModelMap();
		retMap.put("code", code);
		socialSinaService.queryVidiskInfoByCode(code,null,null);
		return new ModelAndView("vdiskPage", retMap);
	}

	/**
	 * 用户绑定进行提交
	 * 
	 * @param request
	 * @param response
	 * @param user
	 * @throws IOException
	 */
	@RequestMapping(value = "bindSubmit")
	@ResponseBody
	public AjaxResult bindSubmit(HttpServletRequest request,
			HttpServletResponse response, String bindName, String bindPassword)
			throws IOException {
		AjaxResult result = new AjaxResult(false);
		if (identityService.checkPassword(bindName, bindPassword)) {
			User user = identityService.createUserQuery().userId(bindName)
					.singleResult();
			saveSocialInfo(request, user);
			userService.doLogin(user);
			afterLoginSuccess(user.getId());
			result = new AjaxResult(true);
		} else {
			result = new AjaxResult(false, "用户名密码不正确");
		}

		return result;

	}
	
	/**
	 * 保存新的账号数据
	 * @param request
	 * @param user
	 */
	public void saveSocialInfo(HttpServletRequest request, User user) {
		String type = (String) request.getSession().getAttribute(
				SystemConstants.SESSION_THIRD_LOGIN_TYPE + "");
		HashMap thirdUserMap = (HashMap) request.getSession().getAttribute(
				SystemConstants.SESSION_THIRD_USER_MAP);
		if (DataBaseParameters.TENCENT.equals(type.toString())) {
			socialTencentService.addTencentSocialInfo(user, thirdUserMap);
		} 
		if (DataBaseParameters.SINA.equals(type.toString())) {
			socialSinaService.addSinaSocialInfo(user, thirdUserMap);
		}
	}

	/**
	 * 用户完善信息后进行提交
	 * 
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "perfectSubmit")
	@ResponseBody
	public AjaxResult perfectSubmit(
			org.activiti.engine.impl.persistence.entity.UserEntity user,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		AjaxResult result = new AjaxResult(false);
		if (identityService.createUserQuery().userId(user.getId()).count() > 0) {
			result = new AjaxResult(false, "用户名已经存在");
			return result;
		}
		HashMap thirdUserMap = (HashMap) request.getSession().getAttribute(
				SystemConstants.SESSION_THIRD_USER_MAP);
		String type = (String) request.getSession().getAttribute(
				SystemConstants.SESSION_THIRD_LOGIN_TYPE);
		if (thirdUserMap != null) {
			identityService.saveUser(user);
			if (DataBaseParameters.TENCENT.equals(type.toString())) {
				socialTencentService.addTencentSocialInfo(user, thirdUserMap);
			} else {
				socialSinaService.addSinaSocialInfo(user, thirdUserMap);
			}
			userService.doLogin(user);
			afterLoginSuccess(user.getId());
			result = new AjaxResult(true);
		}

		return result;
	}
	
	private void afterLoginSuccess(String userId) {
		//socialService.handleUserWeibo(userId);
	}

	/**
	 * 跳转到微博的授权页
	 * 
	 * @param request
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "oauth", method = RequestMethod.GET)
	public String oauthPage(HttpServletRequest request, String type) {
		String state = queryWeiboState(request);
		if(request.getParameter("userId") != null){
			request.getSession().setAttribute(SystemConstants.COOKIE_USER_ID, request.getParameter("userId"));
		}
		String url = "";
		if (DictDef.dict("tencent").equals(type)) {// tencent
			url = TencentSociaTool.generateAuthorizationURL(state);
		}
		if (DictDef.dict("sina").equals(type)) {// sina
			url = SinaSociaTool.generateAuthorizationURL(state);
		}
		
		if (DictDef.dict("evernote").equals(type)) {// evernote
			Map map = socialEvernoteService.queryEvernoteRequestTokenInfo();
			request.getSession().setAttribute(
					SystemConstants.SESSION_THIRD_USER_MAP, map);
			url = (String)map.get("authorizationUrl");
		}
		return "redirect:" + url;
	}
	
	/**
	 * 回调后返回的url,后面跟的标示性参数
	 * @param request
	 * @return
	 */
	public String queryWeiboState(HttpServletRequest request) {
		String state = "";
		if(request.getParameter("state") != null){
			state = (String)request.getParameter("state");
		}
		return state;
	}
	
	
	
	/**
	 * evernote的回调
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "evernote")
	public ModelAndView evernote(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		ModelMap retMap = new ModelMap();
		socialEvernoteService.handlerEvernoteAccessToken(request);
		return new ModelAndView("vdiskPage", retMap);
	}
	
	@RequestMapping(value = "testCurrentData")
	@ResponseBody
	public HashMap testCurrentData(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// socialTencentService.myWeiboToDb();
		socialService.weiboInit();
		HashMap test = new HashMap();
		test.put("info", "success");
		return test;
	}

}
