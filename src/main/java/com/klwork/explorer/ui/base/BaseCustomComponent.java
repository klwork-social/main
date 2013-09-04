package com.klwork.explorer.ui.base;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.vaadin.ui.CustomComponent;

public  class BaseCustomComponent extends CustomComponent implements TabLayLoadComponent {
	private static final long serialVersionUID = 1L;
	//是否已经初始化
	private boolean startInit = false;
	//是否延迟加载
	private boolean lazyload = false;
	
	protected transient I18nManager i18nManager;
	
	@Override
	public void attach() {
		super.attach();
		if (!startInit && !lazyload) {
			startInit();
		}
	}
	
	public BaseCustomComponent() {
		super();
		this.i18nManager = ViewToolManager.getI18nManager();
	}
	
	/**
	 * true为懒加载
	 * @param lazyload
	 */
	public BaseCustomComponent(boolean lazyload) {
		this();
		this.lazyload = lazyload;
	}
	
	public boolean isStartInit() {
		return startInit;
	}

	public void setStartInit(boolean startInit) {
		this.startInit = startInit;
	}

	@Override
	public void startInit() {
		
	}

	public boolean isLazyload() {
		return lazyload;
	}

	public void setLazyload(boolean lazyload) {
		this.lazyload = lazyload;
	}
	
	
}
