package com.klwork.explorer.ui.business.social;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.utils.SinaSociaTool;

public class SinaCommentDisplayPage extends AbstractCommentDisplayPage{

	public SinaCommentDisplayPage(SocialUserAccount socialUserAccount, int type) {
		super(socialUserAccount, type);
	}

	@Override
	public String getSocialType() {
		return "0";
	}
	
	@Override
	protected String textTranslate(String text) {
		return SinaSociaTool.textTranslate(text);
	}
	
	@Override
	public String getWeiboMainUrl() {
		return "http://weibo.com/";
	}
}
