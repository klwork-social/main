package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.oauthv2.OAuthV2;

public class AddPicParameter {
	public OAuth oAuth;
	public String format = "json";
	public String content;
	public String clientip = "127.0.0.1";
	public String jing;
	public String wei;
	public String picpath;
	public String syncflag;

	public AddPicParameter(OAuth oAuth, String format, String content,
			String clientip, String jing, String wei, String picpath,
			String syncflag) {
		this.oAuth = oAuth;
		this.format = format;
		this.content = content;
		this.clientip = clientip;
		this.jing = jing;
		this.wei = wei;
		this.picpath = picpath;
		this.syncflag = syncflag;
	}
	
	public AddPicParameter(OAuthV2 oAuth2, String text,String picpath) {
		this.oAuth = oAuth2;
		this.content = text;
		this.picpath = picpath;
	}
}