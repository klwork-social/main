package com.klwork.explorer.ui.business.social;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.task.InboxPage;
import com.klwork.explorer.ui.task.InvolvedPage;
import com.klwork.explorer.ui.task.TasksPage;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class QQWeiboShowPage extends AbstractTabViewPage{
	
	SocialUserAccount socialUserAccount;
	public QQWeiboShowPage(SocialUserAccount socialUserAccount, AbstractTabViewPage mainPage) {
		this.socialUserAccount = socialUserAccount;
	}

	@Override
	public void initTabData() {
		addTab(new QQWeiboDisplayPage(socialUserAccount,0),"全部微博");
		//t.
		addTab(new QQWeiboDisplayPage(socialUserAccount,1),"我的微博");
        addTab(new InboxPage(),"@我的微博");
        addTab(new InvolvedPage(),"我的评论");
	}

	public VerticalLayout createTaskCompon(String title) {
		VerticalLayout center = new VerticalLayout();
        center.setSizeFull();
        center.setCaption(title);
		return center;
	}
	
	public HorizontalLayout createMyTask() {
		 HorizontalLayout editor = new HorizontalLayout();
	        editor.setSizeFull();
	        editor.addStyleName("editor");
	        editor.addStyleName("no-horizontal-drag-hints");
	        return editor;
	}
}