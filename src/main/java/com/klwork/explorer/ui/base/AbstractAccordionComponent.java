package com.klwork.explorer.ui.base;

import com.vaadin.ui.Accordion;
import com.vaadin.ui.Component;

public abstract class AbstractAccordionComponent extends
		AbstractVCustomComponent {
	Accordion accordion = new Accordion();

	public Accordion getAccordion() {
		return accordion;
	}

	public void setAccordion(Accordion accordion) {
		this.accordion = accordion;
	}

	@Override
	protected void initUi() {
		// 分割
		getMainLayout().addComponent(accordion);
		accordion.setSizeFull();
		//accordion.setImmediate(true);
	}

	
	public void addAccordion(Component c, String caption) {
		getAccordion().addTab(c, caption);
	}
	
	public void selectedAccordion(Component c){
		getAccordion().setSelectedTab(c);
	}
}
