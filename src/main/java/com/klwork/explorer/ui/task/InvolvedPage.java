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
package com.klwork.explorer.ui.task;



import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.ui.task.data.InvolvedListQuery;




/**
 * The page displaying all tasks for where the logged in user is involved with.
 * 
 * @author Joram Barrez
 */
public class InvolvedPage extends AbstractTaskPage {
  
  private static final long serialVersionUID = 1L;
  
  public InvolvedPage() {
  }
  
  /**
   * Constructor called when page is accessed straight through the url, eg. /task/id=123
   */
  public InvolvedPage(String taskId) {
    super(taskId);
  }
  
  @Override
  protected LazyLoadingQuery createLazyLoadingQuery() {
    return new InvolvedListQuery();
  }


  
}
