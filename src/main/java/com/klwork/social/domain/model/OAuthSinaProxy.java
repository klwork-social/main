package com.klwork.social.domain.model;

import com.tencent.weibo.oauthv2.OAuthV2;

public class OAuthSinaProxy implements SocialOAuth {
	OAuthV2 oAuth;
	public OAuthSinaProxy(OAuthV2 oAuth) {
		this.oAuth = oAuth;
	}
	
}
