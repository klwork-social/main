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

package com.klwork.explorer.ui.custom;



import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.event.ConfirmationEvent;
import com.klwork.explorer.ui.event.ConfirmationEventListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;


/**
 * Generic Yes/No confirmation dialog. Add {@link ConfirmationEventListener} to
 * perform actions when confirmation or rejection is given.
 * 
 * @author Frederik Heremans
 */
public class ConfirmationDialogPopupWindow extends PopupWindow {

  private static final long serialVersionUID = 1L;
  
  protected GridLayout layout;
  protected Label descriptionLabel;
  protected Button yesButton;
  protected Button noButton;
  
  public ConfirmationDialogPopupWindow(String title, String description) {
    setWidth(400,Unit.PIXELS);
    setModal(true);
    setResizable(false);
    setHeight("30%");
	setWidth("50%");
	center();
    
    addStyleName(Reindeer.PANEL_LIGHT);
    
    layout = new GridLayout(2,2);
    layout.setMargin(true);
    layout.setSpacing(true);
    layout.setSizeFull();
    
    setContent(layout);
    
    I18nManager i18nManager = ViewToolManager.getI18nManager();
    if(title != null) {
      setCaption(title);
    } else {
      setCaption(i18nManager.getMessage(Messages.CONFIRMATION_DIALOG_DEFAULT_TITLE));
    }
    
    initLabel(description);
    initButtons(i18nManager);
  }
  
  public ConfirmationDialogPopupWindow(String description) {
    this(null, description);
  }
  
  /**
   * Show the confirmation popup.
   */
  public void showConfirmation() {
    yesButton.focus();
    ViewToolManager.showPopupWindow(this);
  }
  
  
  protected void initButtons(I18nManager i18nManager) {
    yesButton = new Button(i18nManager.getMessage(Messages.CONFIRMATION_DIALOG_YES));
    layout.addComponent(yesButton, 0, 1);
    layout.setComponentAlignment(yesButton, Alignment.BOTTOM_RIGHT);
    yesButton.addClickListener(new ClickListener() {
      private static final long serialVersionUID = 1L;
      public void buttonClick(ClickEvent event) {
        close();
        fireEvent(new ConfirmationEvent(ConfirmationDialogPopupWindow.this, true));
      }
    });
    
    noButton = new Button(i18nManager.getMessage(Messages.CONFIRMATION_DIALOG_NO));
    layout.addComponent(noButton, 1, 1);
    layout.setComponentAlignment(noButton, Alignment.BOTTOM_LEFT);
    noButton.addClickListener(new ClickListener() {
      private static final long serialVersionUID = 1L;
      public void buttonClick(ClickEvent event) {
        close();
        fireEvent(new ConfirmationEvent(ConfirmationDialogPopupWindow.this, false));
      }
    });
  }

  protected void initLabel(String description) {
    descriptionLabel = new Label(description, ContentMode.HTML);
    descriptionLabel.setSizeFull();
    layout.addComponent(descriptionLabel, 0, 0 , 1, 0);
    layout.setRowExpandRatio(0, 1.0f);
  }
}
