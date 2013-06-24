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

import com.vaadin.server.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.Date;

/**
 * The Class CustomVaadinServlet.
 */
public class CustomVaadinServlet extends VaadinServlet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6050577640458063694L;
	private static Logger logger = LoggerFactory.getLogger(CustomVaadinServlet.class);
    /**
     * Servlet parameter name for system message bean
     */
    private static final String SYSTEM_MESSAGES_BEAN_NAME_PARAMETER = "systemMessagesBeanName";
    /**
     * Spring Application Context
     */
    private transient ApplicationContext applicationContext;
    /**
     * system message bean name
     */
    private String systemMessagesBeanName = "";

    @Override
    public void init(ServletConfig config) throws ServletException
    {
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        //这里为DEFAULT
        if (config.getInitParameter(SYSTEM_MESSAGES_BEAN_NAME_PARAMETER) != null)
        {
            systemMessagesBeanName = config.getInitParameter(SYSTEM_MESSAGES_BEAN_NAME_PARAMETER);
            logger.debug("found SYSTEM_MESSAGES_BEAN_NAME_PARAMETER: {}", systemMessagesBeanName);
        }
        
        //进行spring上下文设置,org.springframework.web.context.ContextLoaderListener.ContextLoaderListener先定义
        if (SpringApplicationContext.getApplicationContext() == null)
        {
            SpringApplicationContext.setApplicationContext(applicationContext);
        }

        super.init(config);
    }
    
    //父类的init会进行调用
    @Override
    protected VaadinServletService createServletService(DeploymentConfiguration deploymentConfiguration) throws ServiceException
    {
        final VaadinServletService service = super.createServletService(deploymentConfiguration);

        // Spring system messages provider
        if (systemMessagesBeanName != null && systemMessagesBeanName != "")
        {
            SpringVaadinSystemMessagesProvider messagesProvider = new SpringVaadinSystemMessagesProvider(applicationContext, systemMessagesBeanName);
            logger.debug("set SpringVaadinSystemMessagesProvider");
            service.setSystemMessagesProvider(messagesProvider);
        }

        String uiProviderProperty = service.getDeploymentConfiguration().getApplicationOrSystemProperty(Constants.SERVLET_PARAMETER_UI_PROVIDER, null);

        // Add SpringUIProvider if custom provider doesn't defined.
        if (uiProviderProperty == null)
        {
            service.addSessionInitListener(new SessionInitListener()
            {
                @Override
                public void sessionInit(SessionInitEvent event) throws ServiceException
                {	
                	//定义默认的UIPROVIDER,CommunicationManager最终会定义
                    event.getSession().addUIProvider(new SpringUIProvider());
                }
            });
        }

        return service;
    }
}
