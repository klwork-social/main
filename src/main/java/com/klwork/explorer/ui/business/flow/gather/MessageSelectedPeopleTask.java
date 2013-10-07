package com.klwork.explorer.ui.business.flow.gather;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class MessageSelectedPeopleTask implements JavaDelegate {
	
	//通知获奖者
	public void execute(DelegateExecution execution) {
		String participants  = "ww_management,ww";
		//找到任务的审核人进行审核
		String[] participantsArray = participants.split(",");
		List<String> assigneeList = new ArrayList<String>();
		for (String assignee : participantsArray) {
			System.out.println("" + assignee + ",你好，你的作品已经通过，请来领奖！");
		}
	}
}
