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

package com.klwork.explorer.ui.base;

import com.klwork.explorer.ui.custom.PopupWindow;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * The Class AbstractFormComponent.
 */
public abstract class AbstractFormPoputWindow extends PopupWindow {


	protected FormLayout form;
	protected FieldGroup binder = new FieldGroup();
	
	

	public FormLayout getForm() {
		return form;
	}

	public void setForm(FormLayout form) {
		this.form = form;
	}

	public FieldGroup getBinder() {
		return binder;
	}

	public void setBinder(FieldGroup binder) {
		this.binder = binder;
	}

	public AbstractFormPoputWindow() {
		
	}

	@Override
	public void attach() {
		super.attach();
		initUi();
	}

	protected void initUi() {
		setModal(true);
		center();
		setResizable(false);
		addStyleName(Reindeer.WINDOW_LIGHT);
		intPopWindowCustom();
		initForm();
	}
	
	protected void intPopWindowCustom() {
		
	}

	/**
	 * Inits the form.
	 */
	protected void initForm() {
		form = new FormLayout();
		form.setSizeFull();
		//form.setValidationVisibleOnCommit(true);
		form.setImmediate(true);
		setContent(form);
		initFormSubComponent();
		//setComponentAlignment(form, Alignment.TOP_CENTER);
	}

	protected abstract void initFormSubComponent();

}
