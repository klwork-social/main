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

package com.klwork.explorer.ui.main.views;

import org.springframework.context.annotation.Scope;

import com.klwork.explorer.ui.business.project.ProjectMainPage;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.web.VaadinView;
import com.klwork.explorer.web.dashboard.DashboardUI;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

/**
 * The Class SocialView.
 */
@org.springframework.stereotype.Component
@Scope("prototype")
@Theme(ExplorerLayout.THEME)
@VaadinView(value = SocialView.NAME, cached = false)
@PreserveOnRefresh
public class SocialView extends HorizontalLayout implements View,PushUpdateInterface {
	
	public static final String NAME = "social";
    private TabSheet editors;
    SocialMainPage mainPage = null;
    @Override
    public void enter(ViewChangeEvent event) {
    	View v = event.getNewView();
		((DashboardUI)UI.getCurrent()).setCurrentView(v);
        initUI();
    }
	public void initUI() {
		setSizeFull();
        if(mainPage == null){
        	mainPage = new SocialMainPage();
			addComponent(mainPage);
        }
	}
    @Override
	public PushDataResult getPushData() {
		return mainPage.getPushData();
	}

	@Override
	public void reflashUIByPush() {
		mainPage.reflashUIByPush();
	}

	@Override
	public String getViewName() {
		return NAME;
	}
}
