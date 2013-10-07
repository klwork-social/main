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

import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.navigation.UriFragment;
import com.klwork.explorer.ui.Images;
import com.klwork.explorer.ui.base.AbstractTablePage;
import com.klwork.explorer.ui.custom.TaskListHeader;
import com.klwork.explorer.ui.mainlayout.DashBoardLayout;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;

public abstract class AbstractFlowManagerPage extends AbstractTablePage {

	private static final long serialVersionUID = 1L;


	protected String outsourcingProjectId;
	protected Table taskTable;
	protected LazyLoadingContainer taskListContainer;
	protected LazyLoadingQuery lazyLoadingQuery;

	public AbstractFlowManagerPage() {
	}

	public AbstractFlowManagerPage(String outsourcingProjectId) {
		this();
		this.outsourcingProjectId = outsourcingProjectId;
	}

	@Override
	protected void initUi() {
		super.initUi();
		if (outsourcingProjectId == null) {
			// WW_TODO 没有任务显示第一个
			selectElement(0);
		} else {
			// 懒加载容器
			int index = taskListContainer
					.getIndexForObjectId(outsourcingProjectId);
			selectElement(index);
		}

		if (taskListContainer.size() == 0) {
			// ExplorerApp.get().setCurrentUriFragment(getUriFragment(null));
		}
	}
	
	@Override
	public HorizontalLayout createSelectHead() {
		HorizontalLayout tableHeadLayout = new HorizontalLayout();
		// tableHeadLayout.setSizeFull();
		tableHeadLayout.setSpacing(true);
		tableHeadLayout.setMargin(true);
		Resource pictureResource = Images.TASK_LIST;
		Embedded picture = new Embedded(null, pictureResource);
		picture.addStyleName(ExplorerLayout.STYLE_TASK_EVENT_PICTURE);
		picture.setType(Embedded.TYPE_IMAGE);
		tableHeadLayout.addComponent(picture);
		tableHeadLayout.setComponentAlignment(picture, Alignment.MIDDLE_LEFT);

		Label nameLabel = createTitleLabel();
		nameLabel.addStyleName("taskListLabel");
		// nameLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
		tableHeadLayout.addComponent(nameLabel);
		tableHeadLayout.setComponentAlignment(nameLabel, Alignment.MIDDLE_LEFT);
		return tableHeadLayout;
	}

	public Label createTitleLabel() {
		Label nameLabel = null;
		nameLabel = new Label("项目列表");
		return nameLabel;
	}

	@Override
	protected Table createList() {
		// WW_TODO taskpage 的createList,右边的列
		taskTable = new Table();
		taskTable.addStyleName(DashBoardLayout.TABLE_BOARD);
		taskTable.addValueChangeListener(getListSelectionListener());
		this.lazyLoadingQuery = createLazyLoadingQuery();
		this.taskListContainer = new LazyLoadingContainer(lazyLoadingQuery, 10);
		taskTable.setContainerDataSource(taskListContainer);

		taskTable.addContainerProperty("name", String.class, "");
		taskTable.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);

		return taskTable;
	}
	
	
	protected ValueChangeListener getListSelectionListener() {
		return new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				Item item = taskTable.getItem(event.getProperty().getValue());
				if (item != null) {
					String id = (String) item.getItemProperty("id").getValue();
					// WW_TODO 点击用户任务table,设置详细
					setDetailComponent(createDetailComponent(id));
				} else {
					setDetailComponent(null);
				}
			}
		};
	}

	protected Component createDetailComponent(String id) {
		return null;
	}

	@Override
	public Component getSearchComponent() {
		return new TaskListHeader();
	}

	protected abstract LazyLoadingQuery createLazyLoadingQuery();

	protected UriFragment getUriFragment(String taskId) {
		return null;
	}

	public void refreshTableContent() {
		refreshSelectNext();
	}

}
