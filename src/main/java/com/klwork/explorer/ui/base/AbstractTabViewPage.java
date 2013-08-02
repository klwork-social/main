package com.klwork.explorer.ui.base;

import java.util.HashMap;
import java.util.Map;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.CloseHandler;
import com.vaadin.ui.TabSheet.Tab;

public class AbstractTabViewPage extends CustomComponent {
	
	protected I18nManager i18nManager;
	protected Map<String,Component> tabCache = new HashMap<String, Component>();
	
	public AbstractTabViewPage() {
		 this.i18nManager = ViewToolManager.getI18nManager();
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 8872314577819805067L;
	private TabSheet tabSheet = new TabSheet();

	public TabSheet getTabSheet() {
		return tabSheet;
	}

	public void setTabSheet(TabSheet tabSheet) {
		this.tabSheet = tabSheet;
	}

	@Override
	public void attach() {
		super.attach();
		initUi();
	}

	protected void initUi() {
		setSizeFull();
		initMainLayout();
		initTabData();
	}

	protected void initTabData() {
		
	}

	private void initMainLayout() {
		setCompositionRoot(tabSheet);
		tabSheet.setSizeFull();
		tabSheet.addStyleName("borderless");
		tabSheet.addStyleName("editors");
		tabSheet.setCloseHandler(new CloseHandler() {
			private static final long serialVersionUID = -1764556772862038086L;

			@Override
			public void onTabClose(TabSheet tabsheet, Component tabContent) {
				Tab addTab = tabsheet.getTab(tabContent);
				String name = addTab.getCaption();
				if(tabCache.get(name) != null){
					tabCache.remove(name);
				}
				tabsheet.removeComponent(tabContent);
			}
		});
	}
	
	public Tab addTab(Component c, String caption) {
		return addTab(c, caption,null);
	}
	
	public Tab addTabSpecial(Component c, String caption) {
		Component todoTabObj = null;
		if(tabCache.get(caption) != null){
			todoTabObj = tabCache.get(caption);
			setSelectedTab(todoTabObj);
			return tabSheet.getTab(todoTabObj);
		}else {
			Tab t =  addTab(c, caption);
			t.setClosable(true);
			setSelectedTab(c);
			return t;
		}
	}

	public Tab addTab(Component c, String caption,Resource icon) {
		tabCache.put(caption, c);
		return tabSheet.addTab(c, caption,icon);
	}
	
	public Tab addTab(Component c) {
		return addTab(c,c.getCaption());
	}
	
	public void setSelectedTab(Component tabObj) {
		tabSheet.setSelectedTab(tabObj);
	}

	public  void refreshRelatedView() {
		// TODO Auto-generated method stub
		
	}
	
}
