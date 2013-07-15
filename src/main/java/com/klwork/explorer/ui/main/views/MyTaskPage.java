package com.klwork.explorer.ui.main.views;

import com.klwork.explorer.ui.base.AbstractHCustomComponent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Table;

public class MyTaskPage extends AbstractHCustomComponent{
	protected Table table;
	
	  protected AbstractSelect createSelectComponent() {
		    //table = createList();
		    table.setEditable(false);
		    table.setImmediate(true);
		    table.setSelectable(true);
		    table.setNullSelectionAllowed(false);
		    table.setSortEnabled(true);
		    table.setSizeFull();
		    return table;
		  }
	  
	@Override
	protected void initUi() {
		//init task de
		//init
		//init task table
	}

}
