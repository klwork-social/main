package com.klwork.business.domain.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.service.SocialMainService;
import com.klwork.common.job.AbstractJob;

public class WeiboInitJob extends AbstractJob {
	@Autowired
	private SocialMainService socialService;

	@Override
	public void doJob() {
		socialService.weiboInit();
	}

	@Override
	public String getType() {
		return "weiboInitJob";
	}

}
