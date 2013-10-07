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

import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;
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
 * The Class AbstractTaskMainPage.
 */
public abstract class AbstractMainGridPage extends AbstractHCustomComponent {

	private static final long serialVersionUID = 1L;

	protected AbstractSelect select;
	protected boolean showEvents;
	protected GridLayout grid;
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public AbstractMainGridPage() {
		super(true);
	}

	@Override
	protected void initUi() {
		initMainLayout();
		addSelectComponent();

	}


	protected void initMainLayout() {
		grid = new GridLayout(2, 1);
		grid.setColumnExpandRatio(0, .85f);
		//任务导航
		grid.setColumnExpandRatio(1, .15f);
		// setCompositionRoot(grid);
		getMainLayout().addComponent(grid);
		grid.setSizeFull();
	}

	public void setEventHiden(boolean show) {
		if (show) {// 不显示
			grid.setColumnExpandRatio(2, .23f);
		} else {// 显示
			grid.setColumnExpandRatio(2, 0f);
		}
		showEvents = show;
	}

	protected void addSelectComponent() {
		VerticalLayout vLayout = new VerticalLayout();
		vLayout.addStyleName("sidebar");
		vLayout.addStyleName("menu");
		vLayout.addStyleName("tasks");
		vLayout.setMargin(new MarginInfo(true,false,false,false));
		vLayout.setSizeFull();

		HorizontalLayout tableHeadLayout = createSelectHead();
		vLayout.addComponent(tableHeadLayout);
		vLayout.setComponentAlignment(tableHeadLayout, Alignment.MIDDLE_LEFT);
		
		AbstractSelect select = createSelectComponent();
		vLayout.addComponent(select);
		vLayout.setExpandRatio(select, 1.0f);
		if (select != null) {
			grid.addComponent(vLayout, 1, 0);
		}
	}



	/**
	 * Returns an implementation of {@link AbstractSelect}, which will be
	 * displayed on the left side of the page, allowing to select elements from
	 * eg. a list, tree, etc.
	 */
	protected abstract AbstractSelect createSelectComponent();
	
	protected abstract HorizontalLayout createSelectHead();
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
		if (grid.getComponent(0, 0) != null) {
			grid.removeComponent(0, 0);
		}
		if (detail != null) {
			grid.addComponent(detail, 0, 0);
		}
	}

	protected Component getDetailComponent() {
		return grid.getComponent(0, 0);
	}

	/**
	 * Override to get the search component to display above the table. Return
	 * null when no search should be displayed.
	 */
	public Component getSearchComponent() {
		return null;
	}

	/**
	 * Get the component to display the events in.
	 * 
	 * Return null by default: no event-component will be used, in that case the
	 * main UI will be two columns instead of three.
	 * 
	 * Override in case the event component must be shown: three columns will be
	 * used then.
	 */
	protected Component getEventComponent() {
		return null;
	}

	public boolean isShowEvents() {
		return showEvents;
	}

	public void setShowEvents(boolean showEvents) {
		this.showEvents = showEvents;
	}

}
