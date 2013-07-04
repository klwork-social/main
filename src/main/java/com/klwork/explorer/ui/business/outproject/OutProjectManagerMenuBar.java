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

package com.klwork.explorer.ui.business.outproject;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoggedInUser;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.custom.ToolBar;
import com.klwork.explorer.ui.custom.ToolbarEntry;
import com.klwork.explorer.ui.custom.ToolbarEntry.ToolbarCommand;
import com.klwork.explorer.ui.custom.ToolbarPopupEntry;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.data.ArchivedListQuery;
import com.klwork.explorer.ui.task.data.InboxListQuery;
import com.klwork.explorer.ui.task.data.InvolvedListQuery;
import com.klwork.explorer.ui.task.data.QueuedListQuery;
import com.klwork.explorer.ui.task.data.TasksListQuery;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * The menu bar which is shown when 'Tasks' is selected in the main menu.
 * 
 * @author Joram Barrez
 * @author Frederik Heremans
 */
public class OutProjectManagerMenuBar extends ToolBar {

	private static final long serialVersionUID = -8553026348687125782L;
	protected transient IdentityService identityService;
	protected I18nManager i18nManager;

	public OutProjectManagerMenuBar() {
		this.identityService = ProcessEngines.getDefaultProcessEngine()
				.getIdentityService();
		this.i18nManager = ViewToolManager.getI18nManager();
		// WW_TODO x代办任务、我的任务,已归档
		initItems();
		// WW_TODO x新任务
		initActions();
	}

	protected void initItems() {
		setWidth("100%");

		// TODO: the counts should be done later by eg a Refresher component
		ToolbarEntry teamManagerEntry = addToolbarEntry("outproject_manager1",
				i18nManager.getMessage(Messages.OUTPROJECT_MY_PARTICIPATION),
				new ToolbarCommand() {
					public void toolBarItemSelected() {
						ViewToolManager.getMainView()
								.showOutProjectManagerPage();
					}
				});

		ToolbarEntry teamMemberEntry = addToolbarEntry("outproject_manager2",
				i18nManager.getMessage(Messages.OUTPROJECT_MY_PUBLISH),
				new ToolbarCommand() {
					public void toolBarItemSelected() {
						ViewToolManager.getMainView()
								.showOutProjectManagerPage();
					}
				});

	}

	protected void initActions() {
		Button newCaseButton = new Button();
		newCaseButton.setCaption(i18nManager
				.getMessage(Messages.EXPAND_MENU_INVITE));
		// WW_TODO Fix
		newCaseButton.setIcon(Images.TASK_16);
		addButton(newCaseButton);

		newCaseButton.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {

			}
		});
	}

	public void addButton(Button button) {
		button.addStyleName(ExplorerLayout.STYLE_TOOLBAR_BUTTON);

		actionButtons.add(button);
		// Button is added after the spacer
		addComponent(button);
		setComponentAlignment(button, Alignment.MIDDLE_LEFT);
	}

}
