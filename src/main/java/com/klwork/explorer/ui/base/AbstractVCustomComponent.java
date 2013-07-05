package com.klwork.explorer.ui.base;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractVCustomComponent extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private VerticalLayout mainLayout;

	public VerticalLayout getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(VerticalLayout mainLayout) {
		this.mainLayout = mainLayout;
	}

	@Override
	public void attach() {
		super.attach();
		init();
	}

	private void init() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setSpacing(true);
		setCompositionRoot(layout);
		setMainLayout(layout);
		initUi();
		setSizeFull();
	}

	protected abstract void initUi();
}
