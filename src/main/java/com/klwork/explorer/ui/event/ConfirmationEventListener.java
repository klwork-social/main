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

package com.klwork.explorer.ui.event;

import com.vaadin.ui.Component.Event;
import com.vaadin.ui.Component.Listener;


/**
 * Listener that listens to {@link ConfirmationEvent}s and delegates to
 * confirmed or rejected methods.
 * 
 * @author Frederik Heremans
 */
public abstract class ConfirmationEventListener implements Listener {

  private static final long serialVersionUID = 1L;
  
  //关键
  public final void componentEvent(Event event) {
    if(event instanceof ConfirmationEvent) {
      ConfirmationEvent ce = (ConfirmationEvent) event;
      if(ce.isConfirmed()) {
        confirmed(ce);
      } else {
        rejected(ce);
      }
    }
  }
  
  /**
   * Called when confirmation is given.
   */
  protected void confirmed(ConfirmationEvent event) {
  }
  
  /**
   * Called  when rejection is given.
   */
  protected void rejected(ConfirmationEvent event) {
  }
  
}
