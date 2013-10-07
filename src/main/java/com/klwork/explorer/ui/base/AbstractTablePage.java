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

import com.klwork.explorer.ui.handler.TableHandler;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Table;


/**
 * 
 * @author ww
 *
 */
public abstract class AbstractTablePage extends AbstractMainGridPage {

  private static final long serialVersionUID = 1L;
  
  protected Table table;
  
  protected AbstractSelect createSelectComponent() {
    table = createList();
    
    // Set non-editable, selectable and full-size
    table.setEditable(false);
    table.setImmediate(true);
    table.setSelectable(true);
    table.setNullSelectionAllowed(false);
    table.setSortEnabled(true);
    table.setSizeFull();
    return table;
  }
  
  /**
   * Concrete pages must implement this.
   * The table that is returned will be used for the 
   * list on the left side.
   */
  protected abstract Table createList();
  
  /**
   * Refresh the list on the left side and selects the next element in the table.
   * (useful when element of the list is deleted)
   */
  public void refreshSelectNext() {
	  TableHandler.refreshSelectNext(table);
  }
  
  public void selectElement(int index) {
	  TableHandler.selectElement(table, index);
  }
  
}
