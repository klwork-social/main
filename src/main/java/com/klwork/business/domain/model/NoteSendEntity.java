package com.klwork.business.domain.model;

public class NoteSendEntity {
	// weibid
	private String weibId;

	private String weibUrl;

	private String title;
	// 内容
	private String content = "";

	private String parent;
	private String userAccountId;

	public String getWeibId() {
		return weibId;
	}

	public void setWeibId(String weibId) {
		this.weibId = weibId;
	}

	public String getWeibUrl() {
		return weibUrl;
	}

	public void setWeibUrl(String weibUrl) {
		this.weibUrl = weibUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}
}
