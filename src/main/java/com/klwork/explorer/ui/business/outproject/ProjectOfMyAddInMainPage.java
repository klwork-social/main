package com.klwork.explorer.ui.business.outproject;

import com.klwork.explorer.ui.base.AbstractManagePage;
import com.vaadin.ui.Component;

public class ProjectOfMyAddInMainPage extends AbstractManagePage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1511268217914435763L;

	@Override
	protected Component initLeftComponent() {
		return new ProjectOfMyAddInManagerLeft(this);
	}

	@Override
	protected Component initRightComponent() {
		return new ProjectManagerAddInRight(this,getLeftParameter());
	}

	/*@Override
	protected Component initHeadComponent() {
		return new GroupMenuBar();
	}*/
}
