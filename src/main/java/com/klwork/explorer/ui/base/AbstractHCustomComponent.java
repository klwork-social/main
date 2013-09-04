package com.klwork.explorer.ui.base;

import com.vaadin.ui.HorizontalLayout;

public abstract class AbstractHCustomComponent extends BaseCustomComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HorizontalLayout mainLayout;

	public AbstractHCustomComponent(boolean b) {
		super(b);
	}
	
	public AbstractHCustomComponent() {
	}

	public HorizontalLayout getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(HorizontalLayout mainLayout) {
		this.mainLayout = mainLayout;
	}


	

	@Override
	public void startInit() {
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
