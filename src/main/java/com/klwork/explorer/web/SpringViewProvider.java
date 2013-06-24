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

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import org.springframework.util.StringUtils;

/**
 * The Class SpringViewProvider.
 */
public class SpringViewProvider extends Navigator.ClassBasedViewProvider
{
    private final String beanName;
    private final boolean cached;
    private final ViewCacheContainer cacheContainer;

    /**
     * Create a new view provider which creates new view instances based on
     * a view class.
     *
     * @param viewName  name of the views to create (not null)
     * @param viewClass class to instantiate when a view is requested (not null)
     */
    public SpringViewProvider(String viewName, String beanName, Class<? extends View> viewClass, boolean cached, ViewCacheContainer cacheContainer)
    {
        super(viewName, viewClass);
        this.beanName = beanName;
        this.cached = cached;
        this.cacheContainer = cacheContainer;
    }

    @Override
    public View getView(String viewName)
    {
        if (getViewName().equals(viewName))
        {
            return cacheContainer.getView(viewName, beanName, cached);
        }

        return null;
    }
}
