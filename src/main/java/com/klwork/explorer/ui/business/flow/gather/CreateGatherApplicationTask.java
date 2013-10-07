package com.klwork.explorer.ui.business.flow.gather;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.common.utils.StringTool;

public class CreateGatherApplicationTask implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		String checker = (String)execution.getVariable(EntityDictionary.CHECKER_USER_ID);
		if(StringTool.judgeBlank(checker)){
			execution.setVariable("goPreAudit", true);
		}else {
			execution.setVariable("goPreAudit", false);
		}
	}
}
