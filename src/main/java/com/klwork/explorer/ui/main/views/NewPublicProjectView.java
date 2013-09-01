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

import com.klwork.explorer.ui.business.outproject.NewPublicMainPage;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.web.VaadinView;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
@org.springframework.stereotype.Component
@Scope("prototype")
@Theme(ExplorerLayout.THEME)
@VaadinView(value = NewPublicProjectView.NAME, cached = false)
@PreserveOnRefresh
public class NewPublicProjectView extends HorizontalLayout implements View {
	private static final long serialVersionUID = 3777276798651518941L;
	public static final String NAME = "newPublicProject";
    private TabSheet editors;
    NewPublicMainPage page = null;
    @Override
    public void enter(ViewChangeEvent event) {
        setSizeFull();
        //addStyleName("social");
        if(page == null){
        	page = new NewPublicMainPage();
			addComponent(page);
        }
    }

}
