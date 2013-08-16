package com.klwork.business.domain.model;

public class WeiboHandleResult {
	private String pagetime;
	private String lastid;
	private boolean success = false;
	private int infoSize = 0;
	public String getPagetime() {
		return pagetime;
	}
	public void setPagetime(String pagetime) {
		this.pagetime = pagetime;
	}
	public String getLastid() {
		return lastid;
	}
	public void setLastid(String lastid) {
		this.lastid = lastid;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getInfoSize() {
		return infoSize;
	}
	public void setInfoSize(int infoSize) {
		this.infoSize = infoSize;
	}
	
	
}
