package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.oauthv2.OAuthV2;

public class AddParameter {
	public OAuth oAuth;
	public String format = "json";
	public String content;
	public String clientip = "127.0.0.1";
	public String jing = "";
	public String wei = "";
	public String syncflag = "";

	public AddParameter(OAuth oAuth, String format, String content,
			String clientip, String jing, String wei, String syncflag) {
		this.oAuth = oAuth;
		this.format = format;
		this.content = content;
		this.clientip = clientip;
		this.jing = jing;
		this.wei = wei;
		this.syncflag = syncflag;
	}

	public AddParameter(OAuthV2 oAuth2, String text) {
		this.oAuth = oAuth2;
		this.content = text;
	}
}