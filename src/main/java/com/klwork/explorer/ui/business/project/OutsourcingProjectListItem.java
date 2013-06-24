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

import com.klwork.business.domain.model.OutsourcingProject;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;


/**
 * @author Joram Barrez
 */
public  class OutsourcingProjectListItem extends PropertysetItem implements Comparable<OutsourcingProjectListItem>{

  private static final long serialVersionUID = 1L;
  
  public OutsourcingProjectListItem(OutsourcingProject p) {
    addItemProperty("id", new ObjectProperty<String>(p.getId(), String.class));
    addItemProperty("name", new ObjectProperty<String>(p.getName(), String.class));
  }
  


  public int compareTo(OutsourcingProjectListItem other) {
    String projectId = (String) getItemProperty("id").getValue();
    String otherTaskId = (String) other.getItemProperty("id").getValue();
    return projectId.compareTo(otherTaskId);
  }
  
}
