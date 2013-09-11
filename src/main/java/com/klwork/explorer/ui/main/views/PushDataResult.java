package com.klwork.explorer.ui.main.views;

import java.io.Serializable;

/**
 * 
 * @author ww
 */
public class PushDataResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4136905913360214604L;
	/**
	 * 需要更新
	 */
	private boolean needUpdate = false;
	/**
	 * 
	 */
	private Serializable data;

	public boolean isNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needUpdate = needUpdate;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}

}