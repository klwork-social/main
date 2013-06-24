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
package com.klwork.explorer.ui.business.organization;

import java.util.Map;

import com.klwork.explorer.Messages;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.TaskMenuBar;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

/**
 * Superclass for all Explorer pages
 * 
 * @author Joram Barrez
 * @author Frederik Heremans
 */
public class OrganizationMainPage extends CustomComponent {

	private static final long serialVersionUID = 1L;
	protected boolean showEvents;
	VerticalLayout mainLayout;

	final HorizontalSplitPanel split = new HorizontalSplitPanel();

	@Override
	public void attach() {
		initUi();
	}

	/**
	 * Override this method (and call super()) when you want to influence the
	 * UI.
	 */
	protected void initUi() {
		showEvents = false;
		setSizeFull();
		initMainLayout();
		initHead();
		initSplitLayout();
		initLeft(split);
		//initRight(split,null);
	}

	private void initHead() {
		mainLayout.addComponent(new TaskMenuBar());
	}


	private void initRight(HorizontalSplitPanel split2,String groupId) {
		split2.setSecondComponent(new GroupListRight(groupId));
	}

	private void initLeft(HorizontalSplitPanel split2) {
		GroupListLeft c = new GroupListLeft(this);
		c.addListener(new SubmitEventListener() {
            private static final long serialVersionUID = -3893467157397686736L;
            
            @Override
			protected void submitted(SubmitEvent event) {
            	
            }
          

			@Override
			protected void cancelled(SubmitEvent event) {
			}
          });
		split2.setFirstComponent(c);
	}

	protected void initMainLayout() {
		mainLayout = new VerticalLayout();
		// layout.setMargin(true);
		mainLayout.setSizeFull();
		setCompositionRoot(mainLayout);
		mainLayout.setSpacing(false);
	}

	public void initSplitLayout() {
		//split.setHeight(Sizeable.SIZE_UNDEFINED, Unit.PERCENTAGE);
		split.setSizeFull();

		// split.setStyleName(Runo.SPLITPANEL_REDUCED);
		split.setStyleName(Reindeer.SPLITPANEL_SMALL);
		// 设置分割线的位置
		split.setSplitPosition(15, Unit.PERCENTAGE);
		split.setLocked(false);
		mainLayout.addComponent(split);
		mainLayout.setExpandRatio(split, 1.0f);
	}

	protected void addLeft() {
		Label title = new Label("我的所有项目");
		title.addStyleName(ExplorerLayout.STYLE_H3);
		title.setWidth(100, Unit.PERCENTAGE);
		// grid.addComponent(title, 0, 0);
	}

	protected void addRight() {
		// grid.addComponent(new OutProjectListRight(), 1, 0);
	}

	protected void addSelectComponent() {
		/*
		 * AbstractSelect select = createSelectComponent(); if (select != null)
		 * { grid.addComponent(select, 0, 2); }
		 */
	}

	protected Component getEventComponent() {
		return null;
	}
	
	
	public void initRightContent(String groupId) {
		initRight(split,groupId);
	}

}
