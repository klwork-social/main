package com.klwork.explorer.ui.base;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;

public class AbstractTabPage extends CustomComponent {
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
	}
	
	public Tab addTab(Component c, String caption) {
		try{
			return tabSheet.addTab(c, caption);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public Tab addTab(Component c) {
		return tabSheet.addTab(c);
	}
	
	public void setSelectedTab(Component tabObj) {
		tabSheet.setSelectedTab(tabObj);
	}
	
}
