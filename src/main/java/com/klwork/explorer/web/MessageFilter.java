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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.MDC;

import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;

/**
 * The Class MessageFilter.
 */
public class MessageFilter implements Filter {
	private transient Logger logger = LoggerFactory.getLogger(getClass());
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		StringBuffer requestURL = request.getRequestURL();
		ViewToolManager.setWebContextPath(request.getContextPath());
		/*System.out.println("当前url:" + requestURL);
		request.setAttribute("requestURL", requestURL);*/
		
		LoginHandler.resetUser();
		if(LoginHandler.getUser() != null){
			MDC.put("userId",LoginHandler.getUser().getId());
			logger.debug("设置MDC" + LoginHandler.getUser().getId());
		}else {
			MDC.put("userId","");
		}
		
		chain.doFilter(req, res);
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
