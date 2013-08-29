package com.klwork.explorer.ui.business.social;

import com.klwork.explorer.ui.base.AbstractManagePage;
import com.vaadin.ui.Component;

public class WeiboSendMainPage extends AbstractManagePage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1511268217914435763L;

	@Override
	protected Component initLeftComponent() {
		return new WeiboSendLeft(this);
	}

	@Override
	protected Component initRightComponent() {
		return new WeiboSendRight(this,getLeftParameter());
	}

	/*@Override
	protected Component initHeadComponent() {
		return new GroupMenuBar();
	}*/
}
