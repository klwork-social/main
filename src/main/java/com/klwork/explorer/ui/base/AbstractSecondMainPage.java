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

import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.custom.ToolBar;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 二级页面的展现
 */
public abstract class AbstractSecondMainPage extends AbstractHCustomComponent {

	private static final long serialVersionUID = 1L;

	protected ToolBar toolBar;
	protected AbstractSelect select;
	protected GridLayout grid;
	


	@Override
	protected void initUi() {
		initMainLayout();
		addSelectComponent();

	}


	protected void initMainLayout() {
		grid = new GridLayout(2, 1);
		grid.setColumnExpandRatio(0, .10f);
		grid.setColumnExpandRatio(1, .90f);
		// setCompositionRoot(grid);
		getMainLayout().addComponent(grid);
		grid.setSizeFull();
	}

	protected void addSelectComponent() {
		VerticalLayout secondMenuLayout = new VerticalLayout();
		secondMenuLayout.addStyleName("sidebar");
		secondMenuLayout.addStyleName("menu");
		//secondMenuLayout.setMargin(new MarginInfo(true,false,false,false));
		secondMenuLayout.setSpacing(true);
		secondMenuLayout.setSizeFull();

		HorizontalLayout tableHeadLayout = new HorizontalLayout();
		tableHeadLayout.setStyleName("tableHead");
		tableHeadLayout.setHeight("20px");
		Label nameLabel = new Label("hello,word");
	     tableHeadLayout.addComponent(nameLabel);
	     tableHeadLayout.setComponentAlignment(nameLabel,
					Alignment.BOTTOM_CENTER);
	     
		secondMenuLayout.addComponent(tableHeadLayout);
		//secondMenuLayout.setExpandRatio(tableHeadLayout, 0.1f);
		//一般为table
		AbstractSelect select = createSelectComponent();
		secondMenuLayout.addComponent(select);
		secondMenuLayout.setExpandRatio(select, 1.0f);
		if (select != null) {
			grid.addComponent(secondMenuLayout, 0, 0);
		}
	}

	/**
	 * Returns an implementation of {@link AbstractSelect}, which will be
	 * displayed on the left side of the page, allowing to select elements from
	 * eg. a list, tree, etc.
	 */
	protected abstract AbstractSelect createSelectComponent();

	/**
	 * Refreshes the elements of the list, and selects the next one (useful when
	 * the selected element is deleted).
	 */
	public abstract void refreshSelectNext();

	/**
	 * Select a specific element from the selection component.
	 */
	public abstract void selectElement(int index);

	protected void setDetailComponent(Component detail) {
		if (grid.getComponent(1, 0) != null) {
			grid.removeComponent(1, 0);
		}
		if (detail != null) {
			grid.addComponent(detail, 1, 0);
		}
	}

	protected Component getDetailComponent() {
		return grid.getComponent(1, 0);
	}
}
