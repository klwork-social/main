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

package com.klwork.explorer.ui.business.project;

import java.util.Collection;
import java.util.List;

import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.model.ProjectQuery;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * A popup window that is used to select people. Two possible modes: -
 * multiselect: displays two tables that allow to select users from the left
 * table to the table on the right - non-multiselect: one table where only one
 * user can be chosen from.
 * 
 * {@link SubmitEventListener} can be attached to listen to completion of the
 * selection. The selected user(s) can be retrieved using
 * {@link #getSelectedUserId()} ,{@link #getSelectedUserIds()} and
 * {@link #getSelectedUserRole(String)}.
 * 
 * @author Joram Barrez
 */
public class NewProjectWindow extends PopupWindow {

	private static final long serialVersionUID = 2288670048774518133L;

	protected I18nManager i18nManager;
	
	// 传过来的项目id
	private String currentProjectId;
	//标识新增还是修改
	boolean edit = false;
	ProjectService projectService;
	
	protected boolean multiSelect = true;
	
	public NewProjectWindow(String currentProjectId) {
		this.i18nManager = ViewToolManager.getI18nManager();
		this.projectService = ViewToolManager.getBean("projectService");
		if(currentProjectId != null){
			this.currentProjectId = currentProjectId;
			edit = true;
		}
	}
	
	@Override
	public void attach() {
		super.attach();
		initNewProjectWindow();
	}

	private void initNewProjectWindow(){
		setModal(true);
		center();
		setResizable(false);
		setCaption("新项目");
		addStyleName(Reindeer.WINDOW_LIGHT);
		setWidth(430, Unit.PIXELS);
		setHeight(320, Unit.PIXELS);
		final BeanItem pItem = getProjectBeanItem();
		initForm(pItem);
	}

	private void initForm(BeanItem pItem) {
		NewProjectForm form = new NewProjectForm(this,pItem);
		setContent(form);
	}

	private BeanItem getProjectBeanItem() {
		Project p = null;
		if(currentProjectId != null){
			p = projectService.findProjectById(currentProjectId);
		}else {
			p = new Project();
			p.setDescription("");
			p.setName("");
			String userId = LoginHandler.getLoggedInUser().getId();
			p.setOwnuser(userId);
		}
		BeanItem pItem = new BeanItem<Project>(p);
		return pItem;
	}
	

}
