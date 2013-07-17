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
package com.klwork.explorer.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The Class LoginController.
 */
@Controller
@RequestMapping("*")
public class LoginController
{
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpServletRequest request)
    {
    	System.out.println("login:" + request.getRequestURL());
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String loginForm(HttpServletRequest request, ModelMap modelMap)
    {
        if (request.getAttribute("shiroLoginFailure") != null)
        {
            String exception = (String) request.getAttribute("shiroLoginFailure");
            if (exception.endsWith("UnknownAccountException") || exception.endsWith("IncorrectCredentialsException"))
            {
                modelMap.put("userNotFound", true);
            }
            else if (exception.endsWith("LockedAccountException"))
            {
                modelMap.put("userLocked", true);
            }
            else
            {
                modelMap.put("error", true);
            }
        }

        return "login";
    }
    
    @RequestMapping(value = "login-submit")
    public void loginSubmit(@RequestParam("username") String username,  
            @RequestParam("password") String password,HttpServletRequest request,HttpServletResponse response) throws IOException {  
  
        Subject currentUser = SecurityUtils.getSubject();  
        String result = "login";  
        if (!currentUser.isAuthenticated()) {  
            result = login(currentUser,username,password); 
            //return new ModelAndView(result);
        }else{//重复登录  
           /* ShiroUser shiroUser = (ShiroUser) currentUser.getPrincipal();  
            if(!shiroUser.getLoginName().equalsIgnoreCase(username)){//如果登录名不同  
                currentUser.logout();  
                result = login(currentUser,username,password);  
            }  */
        }  
        String index = request.getContextPath() + "/";
		response.sendRedirect(index);
    }
      
    private String login(Subject currentUser,String username,String password){  
        String result = "login";  
        UsernamePasswordToken token = new UsernamePasswordToken(username,  
                password);  
        token.setRememberMe(false);  
        try {  
            currentUser.login(token);  
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
    
}
