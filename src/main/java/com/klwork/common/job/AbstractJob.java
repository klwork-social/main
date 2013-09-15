package com.klwork.common.job;

import java.util.Date;

import org.springframework.util.StopWatch;

import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;


public abstract class AbstractJob implements Job {
	private transient Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 */
	public void execute() {
		Date current = new Date();
		Date dateymd = StringDateUtil.dateToYMD(current);
		// 查询当天的
		/*
		 * ScheduleJobInfo job = scheduleJobInfoRepository
		 * .queryInfoByTypeAndDate(getType(), dateymd);
		 */

		logger.info("定时开始:" + StringDateUtil.dateToALLString(current));
		StopWatch watch = new StopWatch();
		watch.start();
		boolean exceptionSign = false;
		try {
			doJob();
		} catch (Exception e) {
			exceptionSign = true;
		}
		watch.stop();
		logger.info(queryTypeName() + "批处理程序共执行时间："
				+ watch.getTotalTimeSeconds() + "秒");
	}

	public String queryTypeName() {
		return "";
	}
}
