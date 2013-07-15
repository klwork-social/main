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
import com.klwork.explorer.ui.base.AbstractManagePage;
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

public class OrganizationMainPage extends AbstractManagePage {
	GroupListLeft groupListLeft;

	@Override
	protected Component initLeftComponent() {
		groupListLeft = new GroupListLeft(this);
		groupListLeft.addListener(new SubmitEventListener() {
			private static final long serialVersionUID = -3893467157397686736L;

			@Override
			protected void submitted(SubmitEvent event) {

			}

			@Override
			protected void cancelled(SubmitEvent event) {
			}
		});
		return groupListLeft;
	}

	@Override
	protected Component initRightComponent() {
		return new GroupListRight(this, getLeftParameter());
	}

	/*@Override
	protected Component initHeadComponent() {
		return new GroupMenuBar();
	}*/
	
	
	public void refreshSelectNext() {
		groupListLeft.refreshSelectNext();
	}
}
