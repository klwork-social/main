package com.klwork.explorer.ui.business.social;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.utils.TencentSociaTool;

public class QQCommentDisplayPage extends AbstractCommentDisplayPage{

	public QQCommentDisplayPage(SocialUserAccount socialUserAccount, int type) {
		super(socialUserAccount, type);
	}

	@Override
	public String getSocialType() {
		return "1";
	}
	
	@Override
	protected String textTranslate(String text) {
		return TencentSociaTool.textTranslate(text);
	}
	
	@Override
	public String getWeiboMainUrl() {
		return "http://t.qq.com/";
	}
}
