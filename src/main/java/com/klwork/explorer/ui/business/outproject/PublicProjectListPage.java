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
package com.klwork.explorer.ui.business.outproject;

import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Tree;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;

/**
 * Superclass for all Explorer pages
 * 
 * @author Joram Barrez
 * @author Frederik Heremans
 */
public class PublicProjectListPage extends CustomComponent {

	private static final long serialVersionUID = 1L;
	protected boolean showEvents;
	
	final HorizontalSplitPanel split = new HorizontalSplitPanel();

	@Override
	public void attach() {
		super.attach();
		initUi();
	}

	/**
	 * Override this method (and call super()) when you want to influence the
	 * UI.
	 */
	protected void initUi() {
		showEvents = false;
		// 如果有事件为GridLayout 3,3
		setSizeFull();
		initMainLayout();
		initLeft(split);
		initRight(split);
	}
    void createTreeItem(Tree tree, String caption, String parent) {
        tree.addItem(caption);
        if (parent != null) {
            tree.setChildrenAllowed(parent, true);
            tree.setParent(caption, parent);
            if (parent.equals("Archive")) {
                tree.setChildrenAllowed(caption, false);
            }
        }
    }
    
	Layout buildTree() {
        CssLayout margin = new CssLayout();
        margin.setWidth("100%");
        //margin.setMargin(new MarginInfo(true, false, true, true));
        // Spacing
        margin.addComponent(new Label("&nbsp;", ContentMode.HTML));
        Label text = new Label(
                "hello,word");
        text.addStyleName(Runo.LABEL_SMALL);
        margin.addComponent(text);
        text.setWidth("90%");
        
        Tree t = new Tree();

        String itemId = "广告创意";
		t.addItem(itemId);
        t.select(itemId);
        t.setItemIcon(itemId, new ThemeResource("icons/16/calendar.png"));
        createTreeItem(t, "视频(5000元)", itemId);
        t.expandItem(itemId);
        

        String itemId2 = "美工设计";
		t.addItem(itemId2);
        t.setItemIcon(itemId2, new ThemeResource(
                "icons/16/document.png"));
        createTreeItem(t, "页面制作(300元)", itemId2);
        createTreeItem(t, "log设计", itemId2);
        t.expandItem(itemId2);
        
        
        String itemId3 = "系统开发";
		t.addItem(itemId3);
        t.setItemIcon(itemId3, new ThemeResource(
                "icons/16/document.png"));
        createTreeItem(t, "程序外包(3009元)", itemId3);
        createTreeItem(t, "系统设计(40000元)", itemId3);
        t.expandItem(itemId3);
        margin.addComponent(t);

       

        return margin;
    }
	
	private void initRight(HorizontalSplitPanel split2) {
		split2.setSecondComponent(new OutProjectListRight());
	}

	private void initLeft(HorizontalSplitPanel split2) {
		split2.setFirstComponent(buildTree());
	}


	protected void initMainLayout() {
		split.setHeight(Sizeable.SIZE_UNDEFINED, Unit. PERCENTAGE);
		split.setSizeFull();
		
		//split.setStyleName(Runo.SPLITPANEL_REDUCED);
		split.setStyleName(Reindeer.SPLITPANEL_SMALL);
		//设置分割线的位置
		split.setSplitPosition(15, Unit.PERCENTAGE);
		split.setLocked(false);

		setCompositionRoot(split);
	}

	protected void addLeft() {
		Label title = new Label("我的所有项目");
		title.addStyleName(ExplorerLayout.STYLE_H3);
		title.setWidth(100, Unit.PERCENTAGE);
		//grid.addComponent(title, 0, 0);
	}

	protected void addRight() {
		//grid.addComponent(new OutProjectListRight(), 1, 0);
	}

	protected void addSelectComponent() {
		/*
		 * AbstractSelect select = createSelectComponent(); if (select != null)
		 * { grid.addComponent(select, 0, 2); }
		 */
	}

	protected Component getEventComponent() {
		return null;
	}

}
