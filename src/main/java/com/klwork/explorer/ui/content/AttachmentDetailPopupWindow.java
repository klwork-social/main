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

package com.klwork.explorer.ui.content;

import org.activiti.engine.task.Attachment;

import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.custom.PopupWindow;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.themes.Reindeer;


/**
 * @author Frederik Heremans
 * @author Joram Barrez
 */
public class AttachmentDetailPopupWindow extends PopupWindow {

  private static final long serialVersionUID = 1L;
  
  public AttachmentDetailPopupWindow(Attachment attachment) {
    super(attachment.getName());
    
    addStyleName(Reindeer.PANEL_LIGHT);
    center();
    setModal(true);
    setResizable(false);
    
    AttachmentRenderer renderer = ViewToolManager.getAttachmentRendererManager().getRenderer(attachment.getType());
    Component detail = renderer.getDetailComponent(attachment);
    
    if(detail instanceof ComponentContainer) {
      setContent((ComponentContainer) detail);
    } else {//zhuyi
    	
    	setContent(detail);
    }
    getContent().setSizeUndefined();
  }
  
}
