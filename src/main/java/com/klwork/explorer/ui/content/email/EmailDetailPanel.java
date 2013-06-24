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

package com.klwork.explorer.ui.content.email;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.impl.util.json.JSONTokener;
import org.activiti.engine.task.Attachment;

import com.klwork.explorer.Constants;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * @author Frederik Heremans
 */
public class EmailDetailPanel extends Panel {

  private static final long serialVersionUID = 1L;

  protected I18nManager i18nManager;
  protected transient TaskService taskService;
  
  protected Label content;
  protected Attachment attachment;
  
  protected GridLayout gridLayout;

  protected VerticalLayout mainLayout;
  public EmailDetailPanel(Attachment attachment) {
    setSizeFull();
    mainLayout = new VerticalLayout();
    mainLayout.setMargin(true);
    mainLayout.setSpacing(true);
    addStyleName(Reindeer.PANEL_LIGHT);

    this.attachment = attachment;
    this.i18nManager = ViewToolManager.getI18nManager();
    this.taskService = ProcessEngines.getDefaultProcessEngine().getTaskService();
    
    gridLayout = new GridLayout(2, 4);
    gridLayout.setSpacing(true);
    mainLayout.addComponent(gridLayout);
    
    InputStream contentStream = taskService.getAttachmentContent(attachment.getId());
    // TODO: Error handling
    JSONObject emailJson = new JSONObject(new JSONTokener(new InputStreamReader(contentStream)));

    String html = emailJson.getString(Constants.EMAIL_HTML_CONTENT);
    String subject = emailJson.getString(Constants.EMAIL_SUBJECT);
    String recipients = emailJson.getString(Constants.EMAIL_RECIPIENT);
    String sentDate = emailJson.getString(Constants.EMAIL_SENT_DATE);
    String receivedDate = emailJson.getString(Constants.EMAIL_RECEIVED_DATE);
    
    // Add subject
    addSimpleRow(Messages.EMAIL_SUBJECT, subject);
    addSimpleRow(Messages.EMAIL_RECIPIENTS, recipients);
    addSimpleRow(Messages.EMAIL_SENT_DATE, sentDate);
    addSimpleRow(Messages.EMAIL_RECEIVED_DATE, receivedDate);

    // Add HTML content
    addHtmlContent(html);
   
  }

  protected void addHtmlContent(String html) {
    Panel panel = new Panel();
    panel.setWidth(800, UNITS_PIXELS);
    panel.setHeight(300, UNITS_PIXELS);
    
    content = new Label(html, Label.CONTENT_XHTML);
    content.setHeight(100, UNITS_PERCENTAGE);
    
    panel.setContent(content);
    mainLayout.addComponent(panel);
  }

  protected void addSimpleRow(String labelMessageKey, String content) {
    addLabel(labelMessageKey);
    
    Label subjectLabel = new Label(content);
    subjectLabel.setSizeUndefined();
    subjectLabel.addStyleName(ExplorerLayout.STYLE_LABEL_BOLD);
    
    gridLayout.addComponent(subjectLabel);
    gridLayout.setComponentAlignment(subjectLabel, Alignment.MIDDLE_LEFT);
  }
  
  protected void addLabel(String messageKey) {
    Label theLabel  = new Label(i18nManager.getMessage(messageKey));
    theLabel.setSizeUndefined();
    gridLayout.addComponent(theLabel);
    
  }

}
