package com.klwork.explorer.ui.base;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public abstract class AbstractHCustomComponent extends CustomComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HorizontalLayout mainLayout;

	public HorizontalLayout getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(HorizontalLayout mainLayout) {
		this.mainLayout = mainLayout;
	}

	@Override
	public void attach() {
		super.attach();
		init();
	}

	private void init() {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.setSpacing(true);
		setCompositionRoot(layout);
		setMainLayout(layout);
		initUi();
		setSizeFull();
	}

	protected abstract void initUi();
}
