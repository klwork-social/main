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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.service.SocialSinaService;
import com.klwork.business.domain.service.SocialTencentService;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.business.domain.service.UserService;
import com.klwork.business.utils.SinaSociaTool;
import com.klwork.business.utils.TencentSociaTool;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;

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
	
	

	@RequestMapping(value = "login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		System.out.println("login:" + request.getRequestURL());
		return "login";
	}

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

	@RequestMapping(value = "login-submit")
	public void loginSubmit(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		Subject currentUser = SecurityUtils.getSubject();
		String result = "login";
		if (!currentUser.isAuthenticated()) {
			userService.login(currentUser,username, password);
		} else {// 重复登录
			/*
			 * ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();
			 * if(!
			 * shiroUser.getLoginName().equalsIgnoreCase(username)){//如果登录名不同
			 * currentUser.logout(); result =
			 * login(currentUser,username,password); }
			 */
		}
		String index = request.getContextPath() + "/";
		response.sendRedirect(index);
	}

	@RequestMapping(value = "weibo-login")
	public void weiboLogin(HttpServletRequest request,
			HttpServletResponse response, String code) throws IOException {
		org.activiti.engine.identity.User user =  socialSinaService.handlerUserAuthorize(code);
		userService.doLogin(user);
		String index = request.getContextPath() + "/";
		response.sendRedirect(index);
	}
	
	@RequestMapping(value = "qq-login")
	public void qqLogin(HttpServletRequest request,
			HttpServletResponse response, String code,String openid,String openkey) throws IOException {
		org.activiti.engine.identity.User user =  socialTencentService.handlerUserAuthorize(code,openid,openkey);
		userService.doLogin(user);
		
		//socialTencentService.weiboToDb();
		
		String index = request.getContextPath() + "/";
		response.sendRedirect(index);
	}
	
	@RequestMapping(value = "testCurrentData")
	@ResponseBody
	public HashMap testCurrentData(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//socialTencentService.myWeiboToDb();
		socialSinaService.myWeiboToDb(1);
		HashMap test = new HashMap();
		test.put("info", "success");
		return test;
	}
	
	
	@RequestMapping(value = "oauth", method = RequestMethod.GET)
	public String oauthPage(HttpServletRequest request,String type) {
		String url = "";
		if(DictDef.dict("tencent").equals(type)){//tencent
			url = TencentSociaTool.generateAuthorizationURL();
		}else {//sina
			url = SinaSociaTool.generateAuthorizationURL();
		}
		return "redirect:"+url;
	}

}
