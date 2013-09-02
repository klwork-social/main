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

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;

import com.klwork.business.domain.model.DictDef;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.base.AbstractSecondTablePage;
import com.klwork.explorer.ui.business.query.DictLazyQuery;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.mainlayout.DashBoardLayout;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;


public  class NewPublicMainPage extends AbstractSecondTablePage {
  
  private static final long serialVersionUID = 1L;

  protected transient TaskService taskService;

  protected String taskId;
  protected Table taskTable;
  protected LazyLoadingContainer taskListContainer;
  protected LazyLoadingQuery lazyLoadingQuery;
  
  
  public NewPublicMainPage() {
    taskService =  ProcessEngines.getDefaultProcessEngine().getTaskService();
  }
  
  public NewPublicMainPage(String taskId) {
    this();
    this.taskId = taskId;
  }
  
  @Override
  protected void initUi() {
    super.initUi();
    if (taskId == null) {
      //WW_TODO 没有任务显示第一个
      selectElement(0);
    } else {
      //懒加载容器
      int index = taskListContainer.getIndexForObjectId(taskId);
      selectElement(index);
    }
    
    if (taskListContainer.size() == 0) {
      //ExplorerApp.get().setCurrentUriFragment(getUriFragment(null));
    }
  }
  
  
  @Override
  protected Table createList() {
	//WW_TODO taskpage 的createList,右边的列
    taskTable = new Table();
    taskTable.addStyleName(DashBoardLayout.TABLE_BOARD);
   /* taskTable.addStyleName(ExplorerLayout.STYLE_TASK_LIST);
    taskTable.addStyleName(ExplorerLayout.STYLE_SCROLLABLE);*/
    
    // Listener to change right panel when clicked on a task
    taskTable.addValueChangeListener(getListSelectionListener());
    
    this.lazyLoadingQuery = createLazyLoadingQuery();
    this.taskListContainer = new LazyLoadingContainer(lazyLoadingQuery, 10);
    taskTable.setContainerDataSource(taskListContainer);
    taskTable.addContainerProperty("name", String.class, null);
    taskTable.setColumnHeaderMode(Table.ColumnHeaderMode.HIDDEN);
    
    return taskTable;
  }
  
  private LazyLoadingQuery createLazyLoadingQuery() {
	DictLazyQuery dictLazyQuery = new DictLazyQuery();
	dictLazyQuery.setType(DictDef.dict("outsourcing_type"));
	return dictLazyQuery;
}

protected ValueChangeListener getListSelectionListener() {
    return new Property.ValueChangeListener() {
      private static final long serialVersionUID = 1L;
      public void valueChange(ValueChangeEvent event) {
        Item item = taskTable.getItem(event.getProperty().getValue()); // the value of the property is the itemId of the table entry
       
        if(item != null) {
         // String id = (String) item.getItemProperty("id").getValue();
        	DictDef def = BinderHandler.getTableBean(taskTable,event.getProperty().getValue());
          setDetailComponent(createDetailComponent(def));
        } else {
          setDetailComponent(null);
        }
      }
    };
  }
  
  protected Component createDetailComponent(DictDef def) {
    return new NewPublicProjectDetail(def);
  }
}
