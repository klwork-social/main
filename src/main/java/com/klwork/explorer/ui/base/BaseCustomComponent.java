package com.klwork.explorer.ui.base;

import com.klwork.explorer.I18nManager;
import com.klwork.explorer.ViewToolManager;
import com.vaadin.ui.CustomComponent;

public class BaseCustomComponent extends CustomComponent {
	private static final long serialVersionUID = 1L;
	protected transient I18nManager i18nManager;
	
	public BaseCustomComponent() {
		super();
		this.i18nManager = ViewToolManager.getI18nManager();
	}
}
