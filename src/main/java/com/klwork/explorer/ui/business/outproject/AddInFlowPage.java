package com.klwork.explorer.ui.business.outproject;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.business.query.OutProjectAddInQuery;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class AddInFlowPage extends AbstractFlowManagerPage{

	private static final long serialVersionUID = 6746279407443944945L;

	@Override
	protected LazyLoadingQuery createLazyLoadingQuery() {
		LazyLoadingQuery lazyLoadingQuery = new OutProjectAddInQuery(
			LoginHandler.getLoggedInUser().getId(), EntityDictionary.PARTICIPANTS_TYPE_USER);
		return lazyLoadingQuery;
	}
	
	@Override
	protected Component createDetailComponent(String id) {
		ProjectManagerAddInDetail component = new ProjectManagerAddInDetail(id);
		return component;
	}
	
	@Override
	public Label createTitleLabel() {
		Label nameLabel = null;
		nameLabel = new Label("参与项目列表");
		return nameLabel;
	}
}
