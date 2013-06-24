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

package com.klwork.explorer.ui.mainlayout;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.Messages;
import com.klwork.explorer.ViewToolManager;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;


/**
 * @author Joram Barrez
 * @author Frederik Heremans
 */
public class MainLayout extends VerticalLayout {
  
  private static final long serialVersionUID = 1L;
  


  protected MainMenuBar mainMenuBar;
  
  protected CssLayout header;
  protected CssLayout main;
  protected CssLayout footer;



  protected I18nManager i18nManager;
  
  public MainLayout() {
	
    this.i18nManager = ViewToolManager.getI18nManager();
    
    setSizeFull();
    addStyleName(ExplorerLayout.STYLE_MAIN_WRAPPER);//样式main
    
    initHeader();
    //WW_TODO x初始化主菜单,首页title,也在
    initMainMenuBar();
    
    initMain();//1.0，main-content
    initFooter();
  }
  
  public void setMainContent(Component mainContent) {
    main.removeAllComponents();
    main.addComponent(mainContent);
    //main.setExpandRatio(mainContent, 1.0f);
  }
  
  public void setFooter(Component footerContent) {
    footer.removeAllComponents();
    footer.addComponent(footerContent);
  }
  
  public void setMainNavigation(String navigation) {
    mainMenuBar.setMainNavigation(navigation);
  }
  
  protected void initHeader() {
    header = new CssLayout();
    header.addStyleName(ExplorerLayout.STYLE_HEADER);
    header.setWidth(100, Unit. PERCENTAGE);
    addComponent(header);
  }

  protected void initMain() {
    main = new CssLayout();
    main.setSizeFull();
    main.addStyleName(ExplorerLayout.STYLE_MAIN_CONTENT);
    addComponent(main);
    setExpandRatio(main, 1.0f);
  }

  protected void initFooter() {
    footer = new CssLayout();
    footer.setWidth(100, Unit.PERCENTAGE);
    footer.addStyleName(ExplorerLayout.STYLE_MAIN_FOOTER);
    addComponent(footer);
    
    Label footerLabel = new Label();
    footerLabel.setContentMode(ContentMode.HTML);
    footerLabel.setValue(i18nManager.getMessage(Messages.FOOTER_MESSAGE));
    footerLabel.setWidth(100, Unit. PERCENTAGE);
    footer.addComponent(footerLabel);
  }

  protected void initMainMenuBar() {
    this.mainMenuBar = ViewToolManager.getComponentFactory(MainMenuBarFactory.class).create(); 
    header.addComponent(mainMenuBar);
  }

	public void clearNavigation() {
		mainMenuBar.clearNavigation();
	}
}
