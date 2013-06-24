package com.klwork.explorer.ui.business.flow.act;

import java.io.Serializable;

public class ActWorkApplication implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4543353395515612319L;
	
	// 公司名
	private String companyName;
	// 任务描述
	private String description;
	// 悬赏金额
	private Long bounty;
	// 附件大小
	private String attachment;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getBounty() {
		return bounty;
	}

	public void setBounty(Long bounty) {
		this.bounty = bounty;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}
