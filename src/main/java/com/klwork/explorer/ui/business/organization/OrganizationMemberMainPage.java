package com.klwork.explorer.ui.business.organization;

import com.klwork.explorer.ui.base.AbstractManagePage;
import com.vaadin.ui.Component;

public class OrganizationMemberMainPage extends AbstractManagePage{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1511268217914435763L;
	
	

	public OrganizationMemberMainPage() {
		super(false);
	}

	@Override
	protected Component initLeftComponent() {
		return new OrganMemberLeft(this);
	}

	@Override
	protected Component initRightComponent() {
		return new OrganMemberRight(this,getLeftParameter());
	}

	/*@Override
	protected Component initHeadComponent() {
		return new GroupMenuBar();
	}*/
}
