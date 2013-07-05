package com.klwork.explorer.ui.business.outproject;

import com.klwork.explorer.ui.base.AbstractManagePage;
import com.vaadin.ui.Component;

public class OutProjectManagerMainPage extends AbstractManagePage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1511268217914435763L;

	@Override
	protected Component initLeftComponent() {
		return new OutProjectManagerLeft(this);
	}

	@Override
	protected Component initRightComponent() {
		return new OutProjectManagerRight(this,getLeftParameter());
	}

}
