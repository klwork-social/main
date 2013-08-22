package com.klwork.business.domain.model;

public class WeiboForwardSend {
	// weibid
	private String weibId;
	// 微博内容
	private String content = "";
	// 评论的类型,同时评论给自己...
	private Integer commentType = 0;// 是否评论给原微博，0：否、1：是，默认为0
	// userWeibo.getUserAccountId()
	private String userAccountId;

	/**
	 * 发送微博的类型
	 */
	private Integer repostType = 0;

	public String getWeibId() {
		return weibId;
	}

	public void setWeibId(String weibId) {
		this.weibId = weibId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCommentType() {
		return commentType;
	}

	public void setCommentType(Integer commentType) {
		this.commentType = commentType;
	}

	public String getUserAccountId() {
		return userAccountId;
	}

	public void setUserAccountId(String userAccountId) {
		this.userAccountId = userAccountId;
	}

	public Integer getRepostType() {
		return repostType;
	}

	public void setRepostType(Integer repostType) {
		this.repostType = repostType;
	}
}
