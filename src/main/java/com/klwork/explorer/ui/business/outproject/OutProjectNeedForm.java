package com.klwork.explorer.ui.business.outproject;

import org.activiti.engine.history.HistoricTaskInstance;

import com.klwork.explorer.ui.business.flow.act.CommHistoryForm;
import com.vaadin.ui.ComboBox;

public class OutProjectNeedForm extends CommHistoryForm {
	public ComboBox checkResultField;
	public OutProjectNeedForm(HistoricTaskInstance historicTask) {
		super(historicTask);
	}
}
