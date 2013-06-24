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

package com.klwork.explorer;

import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.ComponentFactory;
import com.klwork.explorer.ui.content.AttachmentRendererManager;
import com.klwork.explorer.ui.form.FormPropertyRendererManager;
import com.klwork.explorer.ui.main.views.MainView;
import com.klwork.explorer.ui.user.UserCache;
import com.klwork.explorer.web.DiscoveryNavigator;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

/**
 * @author ww
 */
public class ViewToolManager {
	public static ComponentFactories componentFactories;
	public static NotificationManager notificationManager;
	
	public static String webContextPath;

	public static I18nManager getI18nManager() {
		I18nManager i18nManager = (I18nManager) SpringApplicationContextUtil
				.getContext().getBean("i18nManager");
		return i18nManager;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String springBeanName) {
		return (T)SpringApplicationContextUtil
		.getContext().getBean(springBeanName);
	}
	
	public static MainView getMainView() {
		Navigator n = UI.getCurrent().getNavigator();
		View r = ((DiscoveryNavigator)n).getView("", "mainView", true);
		if(r instanceof MainView){
			return (MainView)r;
		}
		return null;
	}

	/**
	 * 在当前窗口弹出一个窗口
	 * @param window
	 */
	public static void showPopupWindow(Window window) {
		UI.getCurrent().addWindow(window);
	}

	public static <T> ComponentFactory<T> getComponentFactory(
			Class<? extends ComponentFactory<T>> clazz) {
		initComponentFactories();
		return componentFactories.get(clazz);
	}

	public static void initComponentFactories() {
		if (componentFactories == null)
			componentFactories = (ComponentFactories) SpringApplicationContextUtil
					.getContext().getBean("componentFactories");
	}

	public static MarginInfo currentCommMargin() {
		MarginInfo info = new MarginInfo(false, true, false, true);
		return info;
	}

	public static NotificationManager getNotificationManager() {
		NotificationManager notificationManager = (NotificationManager) SpringApplicationContextUtil
				.getContext().getBean("notificationManager");
		return notificationManager;
	}

	public static void logout() {
		UI.getCurrent().close();
		Page.getCurrent().setLocation(webContextPath + "/logout/");
		LoginHandler.logout();
	}

	public static void setWebContextPath(String contextPath) {
		if(webContextPath == null){
			webContextPath = contextPath;
		}
	}

	public static AttachmentRendererManager getAttachmentRendererManager() {
		AttachmentRendererManager r = (AttachmentRendererManager) SpringApplicationContextUtil
				.getContext().getBean("attachmentRendererManager");
		return r;
	}

	public static FormPropertyRendererManager getFormPropertyRendererManager() {
		FormPropertyRendererManager r = (FormPropertyRendererManager) SpringApplicationContextUtil
				.getContext().getBean("formPropertyRendererManager");
		return r;
	}

	public static UserCache getUserCache() {
		UserCache r = (UserCache) SpringApplicationContextUtil
				.getContext().getBean("userCache");
		return r;
	}

}
