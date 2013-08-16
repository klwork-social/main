package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;

public class HomeTimelineParameter {
	public OAuth oAuth;
	public String format;
	public String pageflag;
	public String pagetime;
	public String reqnum;
	public String type;
	public String contenttype;

	public HomeTimelineParameter(OAuth oAuth, String format, String pageflag,
			String pagetime, String reqnum, String type, String contenttype) {
		this.oAuth = oAuth;
		this.format = format;
		this.pageflag = pageflag;
		this.pagetime = pagetime;
		this.reqnum = reqnum;
		this.type = type;
		this.contenttype = contenttype;
	}
}