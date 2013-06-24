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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * The Class LoginController.
 */
@Controller
@RequestMapping("/")
public class LoginController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String login(HttpServletRequest request)
    {
    	System.out.println("login:" + request.getRequestURL());
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
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
}
