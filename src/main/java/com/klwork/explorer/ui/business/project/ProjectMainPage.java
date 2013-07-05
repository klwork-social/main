package com.klwork.explorer.ui.business.project;

import java.util.HashMap;

import com.klwork.explorer.ui.base.AbstractManagePage;
import com.vaadin.ui.Component;

public class ProjectMainPage extends AbstractManagePage{
	ProjectMainRight pright = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1511268217914435763L;
	
	public ProjectMainPage() {
		super();
		setFirstInitRight(true);
	}

	@Override
	protected Component initLeftComponent() {
		return new ProjectMainLeft(this);
	}
	
	
	@Override
	protected Component initRightComponent() {
		if(pright == null){
			pright =  new ProjectMainRight(this,getLeftParameter());
		}else {
			if(getLeftParameter() != null){
				@SuppressWarnings("unchecked")
				HashMap<String,String> map = (HashMap<String,String>)getLeftParameter();
				pright.initRightContent(map.get("id"), map.get("name"));
			}
		}
		return pright;
	}

	public void refreshCalendarView() {
		
	}
}
