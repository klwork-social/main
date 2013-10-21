package com.klwork.explorer.ui.user;

import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.UserDataStatistic;
import com.klwork.business.domain.service.UserDataStatisticService;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.base.AbstractVCustomComponent;
import com.klwork.explorer.ui.business.query.PublicProjectListQuery;
import com.klwork.explorer.ui.handler.BinderHandler;
import com.klwork.explorer.ui.handler.TableHandler;
import com.klwork.explorer.ui.mainlayout.ExplorerLayout;
import com.klwork.explorer.ui.task.TaskRemindComponent.TaskRemindEntity;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class UserActionRemindComponent extends AbstractVCustomComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3972531042690221055L;
	public UserActionRemindComponent() {
		
		String userId = LoginHandler.getLoggedInUser().getId();

	}

	@Override
	protected void initUi() {
		setCaption("用户动态");
		setSizeFull();
		this.getMainLayout().addComponent(new UserEventsPanel());
	}
}
