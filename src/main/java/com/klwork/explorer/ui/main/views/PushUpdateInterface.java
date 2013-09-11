package com.klwork.explorer.ui.main.views;


public interface PushUpdateInterface {
	/**
	 * 得到数据
	 * @return
	 */
	public PushDataResult getPushData();
	
	/**
	 * 刷新UI
	 */
	public void reflashUIByPush();
	
	public String getViewName();
}
