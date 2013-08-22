package com.klwork.explorer.ui.business.social;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.klwork.explorer.ui.task.InboxPage;
import com.klwork.explorer.ui.task.InvolvedPage;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class SinaWeiboShowPage extends AbstractTabViewPage{
	
	SocialUserAccount socialUserAccount;
	public SinaWeiboShowPage(SocialUserAccount socialUserAccount, AbstractTabViewPage mainPage) {
		this.socialUserAccount = socialUserAccount;
	}

	@Override
	public void initTabData() {
		addTab(new SinaWeiboDisplayPage(socialUserAccount,DictDef.dictInt("weibo_public_timeline")),"全部微博");
		addTab(new SinaWeiboDisplayPage(socialUserAccount,DictDef.dictInt("weibo_user_timeline")),"我的微博");
        addTab(new SinaWeiboDisplayPage(socialUserAccount,DictDef.dictInt("weibo_mentions_timeline")),"@我的微博");
        addTab(new SinaWeiboDisplayPage(socialUserAccount,DictDef.dictInt("comment_to_me")),"我收到的评论");
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
