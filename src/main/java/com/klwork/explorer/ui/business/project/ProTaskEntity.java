package com.klwork.explorer.ui.business.project;

import java.io.Serializable;

public class ProTaskEntity implements Serializable {

	private static final long serialVersionUID = -8943498783302996516L;
	// 优先级！
	String priority;
	// %
	String complete;

	// 结束时间
	String endTime;
	// 消耗
	String useUp;
	// 到期
	String due;
	String status;
	String type;
	String tags;
	String name;

	boolean container;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUseUp() {
		return useUp;
	}

	public void setUseUp(String useUp) {
		this.useUp = useUp;
	}

	public String getDue() {
		return due;
	}

	public void setDue(String due) {
		this.due = due;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isContainer() {
		return container;
	}

	public void setContainer(boolean container) {
		this.container = container;
	}

	public ProTaskEntity() {
		this.priority = priority;
		this.complete = complete;
		this.endTime = endTime;
		this.useUp = useUp;
		this.due = due;
		this.status = status;
		this.type = type;
		this.tags = tags;
		this.name = name;
		//this.container = pcontainer;
	}

	@Override
	public String toString() {
		return "ProTaskEntity [priority=" + priority + ", complete=" + complete
				+ ", endTime=" + endTime + ", useUp=" + useUp + ", due=" + due
				+ ", status=" + status + ", type=" + type + ", tags=" + tags
				+ ", name=" + name + ", container=" + container + "]";
	}

	
}