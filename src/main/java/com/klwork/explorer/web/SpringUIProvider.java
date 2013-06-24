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

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.attribute.standard.DateTimeAtCompleted;
import java.util.Date;

/**
 * The Class SpringUIProvider.
 */
public class SpringUIProvider extends UIProvider
{
    private static Logger logger = LoggerFactory.getLogger(SpringUIProvider.class);

    /**
     * Servlet parameter name for UI bean
     */
    protected static final String BEAN_NAME_PARAMETER = "beanName";

    public SpringUIProvider()
    {
    }

    @Override
    public UI createInstance(UICreateEvent event)
    {	
    	//从spring容器中得到UI对象,UI配置通过注释配置
        return (UI) SpringApplicationContext.getApplicationContext().getBean(getUIBeanName(event.getRequest()));
    }

    @Override
    public Class<? extends UI> getUIClass(UIClassSelectionEvent event)
    {
        return (Class<? extends UI>) SpringApplicationContext.getApplicationContext().getType(getUIBeanName(event.getRequest()));
    }

    @Override
    public boolean isPreservedOnRefresh(UICreateEvent event)
    {
        if (!SpringApplicationContext.getApplicationContext().isPrototype(getUIBeanName(event.getRequest())))
        {
            return true;
        }

        return super.isPreservedOnRefresh(event);
    }

    /**
     * Returns the bean name to be retrieved from the application bean context and
     * used as the UI. The default implementation uses the servlet init property
     * {@link #BEAN_NAME_PARAMETER} or "ui" if not defined.
     *
     * @param request the current Vaadin request
     * @return the UI bean name in the application context
     */
    protected String getUIBeanName(VaadinRequest request)
    {
        String vaadinBeanName = "ui";
        //beanName, web.xml 配置的MyUi
        Object uiBeanName = request.getService().getDeploymentConfiguration().getApplicationOrSystemProperty(BEAN_NAME_PARAMETER, null);
        if (uiBeanName != null && uiBeanName instanceof String)
        {
            vaadinBeanName = uiBeanName.toString();
        }

        logger.debug("found BEAN_NAME_PARAMETER: {}", vaadinBeanName);
        return vaadinBeanName;
    }
}
