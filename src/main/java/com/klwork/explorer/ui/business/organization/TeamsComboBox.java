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

import java.util.Arrays;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.vaadin.ui.ComboBox;


/**
 * The Class TeamsComboBox.
 */
public class TeamsComboBox extends ComboBox {

  private static final long serialVersionUID = 1L;
  
  protected I18nManager i18nManager;
  
  public TeamsComboBox(I18nManager i18nManager) {
    super(null, Arrays.asList(
            i18nManager.getMessage(Messages.TASK_PRIORITY_LOW),
            i18nManager.getMessage(Messages.TASK_PRIORITY_MEDIUM),
            i18nManager.getMessage(Messages.TASK_PRIORITY_HIGH)));
    this.i18nManager = i18nManager;
    setValue(i18nManager.getMessage(Messages.TASK_PRIORITY_LOW));
    setNullSelectionAllowed(false);
    setInvalidAllowed(false);
    setImmediate(true);
    setWidth(125, UNITS_PIXELS);
  }
  
  public TeamsComboBox(I18nManager i18nManager, Object value) {
    this(i18nManager);
    setValue(value);
  }
  

}
