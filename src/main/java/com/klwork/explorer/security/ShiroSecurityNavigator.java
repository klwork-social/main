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
package com.klwork.explorer.security;

import com.klwork.explorer.web.DiscoveryNavigator;
import com.vaadin.navigator.NavigationStateManager;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.UI;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * The Class ShiroSecurityNavigator.
 */
public class ShiroSecurityNavigator extends DiscoveryNavigator
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3180021276126839421L;
	private static Logger logger = LoggerFactory.getLogger(ShiroSecurityNavigator.class);

    public ShiroSecurityNavigator(UI ui, ComponentContainer container)
    {
        super(ui, container);
    }

    public ShiroSecurityNavigator(UI ui, SingleComponentContainer container)
    {
        super(ui, container);
    }

    public ShiroSecurityNavigator(UI ui, ViewDisplay display)
    {
        super(ui, display);
    }

    public ShiroSecurityNavigator(UI ui, NavigationStateManager stateManager, ViewDisplay display)
    {
        super(ui, stateManager, display);
    }

    @Override
    protected void addCachedBeans()
    {
        for (ViewCache view : views)
        {
            // Only allowed beans
            if (hasAccess(view.getClazz()))
            {
                logger.debug("view name: \"{}\", class: {}, viewCached: {}", new Object[]{view.getName(), view.getClazz(), view.isCached()});
                addBeanView(view.getName(), view.getBeanName(), view.getClazz(), view.isCached());
            }
        }
    }

    @Override
    public void addBeanView(String viewName, Class<? extends View> viewClass, boolean cached)
    {
        if (!hasAccess(viewClass))
        {
            return;
        }

        super.addBeanView(viewName, viewClass, cached);
    }

    /**
     * Check access for class
     * 配合Shiro框架进行授权的检查
     * @param clazz
     * @return
     */
    public static boolean hasAccess(Class<?> clazz)
    {
        boolean isAllow = true;

        if (clazz.isAnnotationPresent(RequiresRoles.class))
        {
            isAllow = false;

            RequiresRoles requiresRoles = clazz.getAnnotation(RequiresRoles.class);
            String[] roles = requiresRoles.value();
            Logical logical = requiresRoles.logical();
            if (roles.length > 0)
            {
                Subject subject = SecurityUtils.getSubject();
                if (!subject.isAuthenticated())
                {
                    return false;
                }

                if (logical == Logical.AND && subject.hasAllRoles(Arrays.asList(roles)))
                {
                    isAllow = true;
                }

                if (logical == Logical.OR)
                {
                    for (boolean hasRole : subject.hasRoles(Arrays.asList(roles)))
                    {
                        if (hasRole)
                        {
                            isAllow = true;
                            break;
                        }
                    }
                }
            }
        }

        if (isAllow && clazz.isAnnotationPresent(RequiresPermissions.class))
        {
            isAllow = false;

            RequiresPermissions requiresPermissions = clazz.getAnnotation(RequiresPermissions.class);
            String[] permissions = requiresPermissions.value();
            Logical logical = requiresPermissions.logical();
            Subject subject = SecurityUtils.getSubject();

            if (permissions.length > 0)
            {
                if (!subject.isAuthenticated())
                {
                    return false;
                }

                if (logical == Logical.AND && subject.isPermittedAll(permissions))
                {
                    isAllow = true;
                }

                if (logical == Logical.OR && subject.isPermittedAll(permissions))
                {
                    for (boolean isPermitted : subject.isPermitted(permissions))
                    {
                        if (isPermitted)
                        {
                            isAllow = true;
                            break;
                        }
                    }
                }
            }
        }

        if (isAllow && clazz.isAnnotationPresent(RequiresAuthentication.class))
        {
            Subject subject = SecurityUtils.getSubject();
            isAllow = subject.isAuthenticated();
        }

        if (isAllow && clazz.isAnnotationPresent(RequiresGuest.class))
        {
            Subject subject = SecurityUtils.getSubject();
            isAllow = subject.getPrincipals() == null;
        }

        if (isAllow && clazz.isAnnotationPresent(RequiresUser.class))
        {
            Subject subject = SecurityUtils.getSubject();
            isAllow = subject.getPrincipals() != null && !subject.getPrincipals().isEmpty();
        }

        return isAllow;
    }
}
