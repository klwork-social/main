package com.klwork.explorer.ui.business.social;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.ui.base.AbstractTabViewPage;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class QQWeiboShowPage extends AbstractTabViewPage implements WeiboShowPage{
	
	SocialUserAccount socialUserAccount;
	public QQWeiboShowPage(SocialUserAccount socialUserAccount, AbstractTabViewPage mainPage) {
		this.socialUserAccount = socialUserAccount;
	}

	@Override
	public void initTabData() {
		addTab(new QQWeiboDisplayPage(socialUserAccount,DictDef.dictInt("weibo_public_timeline")),"全部微博");
		addTab(new QQWeiboDisplayPage(socialUserAccount,DictDef.dictInt("weibo_user_timeline")),"我的微博");
        addTab(new QQWeiboDisplayPage(socialUserAccount,DictDef.dictInt("weibo_mentions_timeline")),"@我的微博");
        addTab(new QQCommentDisplayPage(socialUserAccount,DictDef.dictInt("comment_to_me")),"我收到的评论");
        addTab(new QQCommentDisplayPage(socialUserAccount,DictDef.dictInt("comment_by_me")),"我发出的评论");
        initAddNewWeiboButton();
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
	
	protected void initAddNewWeiboButton() {
	    Button newCaseButton = new Button();
	    newCaseButton.setStyleName("myTabButton");
	    newCaseButton.setCaption("快速发微博");
	    //newCaseButton.setIcon(Images.TASK_16);
	    getMainLayout().addComponent(newCaseButton);
	    
	    newCaseButton.addClickListener(new ClickListener() {
	      public void buttonClick(ClickEvent event) {
	    	  WeiboSendPopupWindow newWeiboPopupWindow = new WeiboSendPopupWindow(socialUserAccount);
	        ViewToolManager.showPopupWindow(newWeiboPopupWindow);
	      }
	    });
	  }
}
