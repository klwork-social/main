/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.klwork.explorer.ui.main.views;

import org.springframework.context.annotation.Scope;

import com.klwork.explorer.web.VaadinView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
@org.springframework.stereotype.Component
@Scope("prototype")
@VaadinView(value = OutProjectManagerView.NAME, cached = false)
@PreserveOnRefresh
public class OutProjectManagerView extends HorizontalLayout implements View {
	public static final String NAME = "outProject";
    private TabSheet editors;
    OutProjectManagerMainPage page = null;
    @Override
    public void enter(ViewChangeEvent event) {
        setSizeFull();
        if(page == null){
        	page = new OutProjectManagerMainPage();
			addComponent(page);
        }
    }

}
