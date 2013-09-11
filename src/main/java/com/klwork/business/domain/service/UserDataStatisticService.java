package com.klwork.business.domain.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.explorer.ui.task.data.InboxListQuery;
import com.klwork.explorer.ui.task.data.InvolvedListQuery;
import com.klwork.explorer.ui.task.data.TasksListQuery;
import com.klwork.explorer.ui.task.data.TeamTaskListQuery;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.UserDataStatistic;
import com.klwork.business.domain.model.UserDataStatisticQuery;
import com.klwork.business.domain.repository.UserDataStatisticRepository;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class UserDataStatisticService {
	@Autowired
	private UserDataStatisticRepository rep;

	@Autowired
	SocialUserAccountInfoService socialUserAccountInfoService;

	@Autowired
	SocialUserAccountService socialUserAccountService;
	
	@Autowired
	TeamService teamService;

	public void createUserDataStatistic(UserDataStatistic userDataStatistic) {
		Date lastUpdate = StringDateUtil.now();
		userDataStatistic.setLastUpdate(lastUpdate);
		UserDataStatistic o = rep.find(userDataStatistic.getUserId());
		if(o != null){
			updateUserDataStatistic(userDataStatistic);
		}else {
			rep.insert(userDataStatistic);
		}
	}

	public int updateUserDataStatistic(UserDataStatistic userDataStatistic) {
		return rep.update(userDataStatistic);
	}

	public List<UserDataStatistic> findUserDataStatisticByQueryCriteria(
			UserDataStatisticQuery query, ViewPage<UserDataStatistic> page) {
		return rep.findUserDataStatisticByQueryCriteria(query, page);
	}

	public UserDataStatistic findUserDataStatisticById(String id) {
		return rep.find(id);
	}

	public int count(UserDataStatisticQuery query) {
		return rep.findUserDataStatisticCountByQueryCriteria(query);
	}

	/**
	 * 用户任务统计
	 */
	public void userTaskStatistics() {
		// 查询今天登录的用户的帐号信息
		List<SocialUserAccountInfo> list = socialUserAccountInfoService
				.queryTodayLoginSocialUserAccountInfo();

		for (Iterator iterator = list.iterator(); iterator.hasNext();) {// 帐号
			SocialUserAccountInfo socialUserAccountInfo = (SocialUserAccountInfo) iterator
					.next();
			String userId = socialUserAccountInfo.getUserId();
			saveUserTaskStatistic(userId);
		}
	}
	
	/**
	 * 返回用户任务统计，没有进行创建
	 * @param userId
	 * @return
	 */
	public UserDataStatistic queryUserTaskStatistic(String userId) {
		UserDataStatistic ret = findUserDataStatisticById(userId);
		if(ret == null){
			return saveUserTaskStatistic(userId);
		}
		return ret;
	}
	
	/**
	 * 保存用户的任务统计
	 * @param userId
	 */
	public UserDataStatistic saveUserTaskStatistic(String userId) {
		UserDataStatistic u = new UserDataStatistic();
		
		u.setDirty(false);
		u.setUserId(userId);
		int inboxCount = new InboxListQuery(userId).size();
		u.setTodoTaskTotal( inboxCount);
		
		//我的任务
		int tasksCount = new TasksListQuery(userId).size();
		u.setMyTaskTotal( tasksCount);
		
		//团队任务
		List<String> teams = teamService.queryUserInTeamIds(userId);
		int queuedCount = 0;
		if (!teams.isEmpty()) {
			queuedCount = new TeamTaskListQuery(teams).size();
		}
		u.setTeamTaskTotal( queuedCount);
		//参与任务
		int involvedCount = new InvolvedListQuery(userId).size();
		u.setInvolvedTaskTotal(involvedCount);
		createUserDataStatistic(u);
		return u;
	}
}