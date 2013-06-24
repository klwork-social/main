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

package com.klwork.explorer;

import java.io.Serializable;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;


/**
 * @author Joram Barrez
 */
public class NotificationManager implements Serializable {
  
  private static final long serialVersionUID = 1L;


  
  @Autowired
  protected I18nManager i18nManager;
  
  public void showErrorNotification(String captionKey, String description) {
    Notification.show(i18nManager.getMessage(captionKey), 
            "<br/>" + description, 
            Notification.Type.ERROR_MESSAGE);
  }
  
  public void showErrorNotification(String captionKey, Exception exception) {
	  Notification.show(i18nManager.getMessage(captionKey), 
            "<br/>" + exception.getMessage(), 
            Notification.Type.ERROR_MESSAGE);
  }
  
  public void showWarningNotification(String captionKey, String descriptionKey) {
   /* Notification notification = new Notification(i18nManager.getMessage(captionKey), 
            i18nManager.getMessage(descriptionKey), 
            Notification.Type.WARNING_MESSAGE);
    notification.setDelayMsec(-1); // click to hide
*/    //notification.show(page)
    Notification.show(i18nManager.getMessage(captionKey), 
            i18nManager.getMessage(descriptionKey),
            Notification.Type.WARNING_MESSAGE);
    
  }
  
  public void showWarningNotification(String captionKey, String descriptionKey, Object ... params) {
    Notification notification = new Notification(i18nManager.getMessage(captionKey) + "<br/>", 
            MessageFormat.format(i18nManager.getMessage(descriptionKey), params), 
            Notification.Type.WARNING_MESSAGE);
    notification.setDelayMsec(5000); // click to hide
    notification.show(Page.getCurrent());
  }
  
  public void showInformationNotification(String key) {
	  Notification.show(i18nManager.getMessage(key), Notification.Type.HUMANIZED_MESSAGE);
  }
  
  public void showInformationNotification(String key, Object ... params) {
	  Notification.show(MessageFormat.format(i18nManager.getMessage(key), params),
            Notification.Type.HUMANIZED_MESSAGE);
  }
  
  public void setI18nManager(I18nManager i18nManager) {
    this.i18nManager = i18nManager;
  }
  
}
