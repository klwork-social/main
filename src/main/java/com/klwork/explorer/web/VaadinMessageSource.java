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

import com.vaadin.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.io.Serializable;
import java.util.Locale;

/**
 * The Class VaadinMessageSource.
 */
public class VaadinMessageSource implements Serializable
{
    @Autowired
    private transient MessageSource messageSource;

    public String getMessage(String code)
    {
        Locale locale = VaadinSession.getCurrent().getLocale();
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code, String defaultMessage)
    {
        Locale locale = VaadinSession.getCurrent().getLocale();
        return messageSource.getMessage(code, null, defaultMessage, locale);
    }

    public String getMessage(String code, Object[] args)
    {
        Locale locale = VaadinSession.getCurrent().getLocale();
        return messageSource.getMessage(code, args, locale);
    }
}
