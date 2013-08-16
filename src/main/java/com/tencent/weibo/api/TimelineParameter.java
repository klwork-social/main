package com.tencent.weibo.api;

import com.tencent.weibo.beans.OAuth;

public class TimelineParameter {
	public OAuth oAuth;
	public String format = "json";;
	public String pageflag= "2";// 0：第一页，1：向下翻页，2向上翻页
	public String reqnum = "50";
	public String lastid = "0";//和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id）
	public String pagetime = "0";//本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
	public String contenttype = "0";//内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
	public String type = "0";//0：所有类型，0x1：原创发表，0x2：转播

	public TimelineParameter(OAuth oAuth) {
		this.oAuth = oAuth;
	}
}