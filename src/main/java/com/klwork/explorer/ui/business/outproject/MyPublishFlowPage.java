package com.klwork.explorer.ui.business.outproject;

import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.business.query.OutProjectPublishQuery;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;

public class MyPublishFlowPage extends AbstractFlowManagerPage{

	private static final long serialVersionUID = 6257041396612916535L;

	@Override
	protected LazyLoadingQuery createLazyLoadingQuery() {
		LazyLoadingQuery lazyLoadingQuery = new OutProjectPublishQuery(
				LoginHandler.getLoggedInUser().getId(), null);
		return lazyLoadingQuery;
	}
	
	@Override
	protected Component createDetailComponent(String id) {
		ProjectOfMyPublishDetail component = new ProjectOfMyPublishDetail(id);
		return component;
	}
	
	@Override
	public Label createTitleLabel() {
		Label nameLabel = null;
		nameLabel = new Label("发布项目列表");
		return nameLabel;
	}
}
