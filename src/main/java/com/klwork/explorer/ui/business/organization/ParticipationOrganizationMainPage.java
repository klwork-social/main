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

import com.klwork.explorer.ui.base.AbstractManagePage;
import com.klwork.explorer.ui.event.SubmitEvent;
import com.klwork.explorer.ui.event.SubmitEventListener;
import com.vaadin.ui.Component;

public class ParticipationOrganizationMainPage extends AbstractManagePage {
	ParticipationGroupListLeft groupListLeft;

	@Override
	protected Component initLeftComponent() {
		groupListLeft = new ParticipationGroupListLeft(this);
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
		return new ParticipationGroupListRight(this, getLeftParameter());
	}

	/*@Override
	protected Component initHeadComponent() {
		return new GroupMenuBar();
	}*/
	
	
	public void refreshSelectNext() {
		groupListLeft.refreshSelectNext();
	}
}
