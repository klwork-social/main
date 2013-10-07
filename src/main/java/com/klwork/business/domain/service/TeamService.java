package com.klwork.business.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.model.TeamQuery;
import com.klwork.business.domain.repository.TeamMembershipRepository;
import com.klwork.business.domain.repository.TeamRepository;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class TeamService {
	@Autowired
	private TeamRepository rep;

	@Autowired
	private TeamMembershipRepository teamMembershipRepositoryep;

	public void createTeam(Team team) {
		Date now = StringDateUtil.now();
		team.setLastUpdate(now);
		team.setId(rep.getNextId());
		rep.insert(team);
	}

	public void deleteTeam(Team team) {
		teamMembershipRepositoryep.deleteTeamMembershipByTeamId(team.getId());
		rep.deleteById(team.getId());
	}

	public int updateTeam(Team team) {
		Date now = StringDateUtil.now();
		team.setLastUpdate(now);
		return rep.update(team);
	}

	public List<Team> findTeamByQueryCriteria(TeamQuery query,
			ViewPage<Team> page) {
		return rep.findTeamByQueryCriteria(query, page);
	}
	
	
	/**
	 * 查询用户创建的团队
	 * @param userId
	 * @return map
	 */
	public Map<String, String> queryTeamMapOfUser(String userId) {
		Map<String, String> map = new HashMap<String, String>();
		List<Team> list = queryTeamListOfUser(userId);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Team team = (Team) iterator.next();
			map.put(team.getId(), team.getName());
		}
		return map;
	}
	
	/**
	 * 查询用户参与的团队
	 * @param userId
	 * @return map
	 */
	public Map<String, String> queryTeamMapOfUserIn(String userId) {
		Map<String, String> map = new HashMap<String, String>();
		List<Team> list = queryUserInTeams(userId);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Team team = (Team) iterator.next();
			if(team.getOwnUser().equals(userId)){//不包含自己的
				continue;
			}
			map.put(team.getId(), team.getName() + "(" + team.getOwnUser() + ")");
		}
		return map;
	}
	
	/**
	 * 查询用户创建的团队
	 * @param userId
	 * @return
	 */
	public Map<Object, String> queryTeamOfUser(String userId) {
		Map<Object, String> map = new HashMap<Object, String>();
		List<Team> list = queryTeamListOfUser(userId);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Team team = (Team) iterator.next();
			map.put(team.getId(), team.getName());
		}
		return map;
	}

	public List<Team> queryTeamListOfUser(String userId) {
		TeamQuery query = new TeamQuery();
		query.setOwnUser(userId);
		query.setType(EntityDictionary.TEAM_GROUP_TYPE_COMM);
		List<Team> list = rep.findTeamByQueryCriteria(query, null);
		return list;
	}
	
	
	/**
	 * 查询用户创建的团队
	 * @param userId
	 * @return 团队的id集合
	 */
	public List<String> queryTeamsOfUser(String userId) {
		List<String> ret = new ArrayList<String>();
		List<Team> list = queryTeamListOfUser(userId);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Team team = (Team) iterator.next();
			ret.add(team.getId());
		}
		return ret;
	}

	
	/**
	 * 查询用户创建的指定类型的团队
	 * @param userId
	 * @param type
	 * @return
	 */
	public Team findTeamByUserAndType(String userId, String type) {
		TeamQuery query = new TeamQuery();
		query.setOwnUser(userId);
		query.setType(type);
		List<Team> list = rep.findTeamByQueryCriteria(query, null);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 创建一个团队
	 * @param userId 团队的所有人
	 * @param type 团队类型
	 * @param teamName 团队名称
	 * @return
	 */
	public Team createTeamByUserAndType(String userId, String type,
			String teamName) {
		TeamQuery query = new TeamQuery();
		query.setOwnUser(userId);
		query.setType(type);
		List<Team> list = rep.findTeamByQueryCriteria(query, null);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			Team team = new Team();
			team.setType(type);
			team.setName(teamName);
			team.setOwnUser(userId);
			createTeam(team);
			return team;
		}
	}

	public Team findTeamById(String id) {
		return rep.find(id);
	}

	public int count(TeamQuery query) {
		return rep.findTeamCountByQueryCriteria(query);
	}
	
	/**
	 * 查询用户下是否存在指定名称的团队
	 * @param userId
	 * @param name
	 * @return
	 */
	public boolean checkExistName(String userId, String name) {
		TeamQuery query = new TeamQuery();
		query.setOwnUser(userId);
		query.setName(name);
		// query.setType(type);
		List<Team> list = rep.findTeamByQueryCriteria(query, null);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 查询用户所在的团队
	 * @param userId
	 * @return
	 */
	public List<Team> queryUserInTeams(String userId) {
		TeamQuery query = new TeamQuery();
		query.setInUser(userId);
		query.setType(EntityDictionary.TEAM_GROUP_TYPE_COMM);
		List<Team> list = rep.findTeamByQueryCriteria(query, null);
		return list;
	}
	
	/**
	 * 查询用户所在的团队
	 * @param userId
	 * @return 团队的id集合
	 */
	public List<String> queryUserInTeamIds(String userId) {
		List<String> ret = new ArrayList<String>();
		List<Team> list = queryUserInTeams(userId);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Team team = (Team) iterator.next();
			ret.add(team.getId());
		}
		if(ret.isEmpty()){
			ret.add("-99");
		}
		return ret;
	}
}