package com.klwork.business.domain.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.service.UserDataStatisticService;
import com.klwork.common.job.AbstractJob;

public class UserStatisticsJob extends AbstractJob {
	@Autowired
	private UserDataStatisticService userDataStatisticService;

	@Override
	public void doJob() {
		userDataStatisticService.userTaskStatistics();
	}

	@Override
	public String getType() {
		return "taskStatistics";
	}

}
