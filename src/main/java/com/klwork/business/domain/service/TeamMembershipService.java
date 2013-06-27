package com.klwork.business.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.TeamMembership;
import com.klwork.business.domain.model.TeamMembershipQuery;
import com.klwork.business.domain.repository.TeamMembershipRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class TeamMembershipService {
	@Autowired
	private TeamMembershipRepository rep;

	public void createTeamMembership(String userId, String teamId) {
		TeamMembership ship = new TeamMembership();
		ship.setUserId(userId);
		ship.setTeamId(teamId);
		rep.insert(ship);
	}


	public List<TeamMembership> findTeamMembershipByQueryCriteria(TeamMembershipQuery query,
			ViewPage<TeamMembership> page) {
		return rep.findTeamMembershipByQueryCriteria(query, page);
	}

	public TeamMembership findTeamMembershipById(String id) {
		return rep.find(id);
	}
	
	public int count(TeamMembershipQuery query) {
		return rep.findTeamMembershipCountByQueryCriteria(query);
	}


	public void deleteTeamMembership(String userId, String teamId) {
		TeamMembership ship = new TeamMembership();
		ship.setUserId(userId);
		ship.setTeamId(teamId);
		rep.delete(ship);
	}


	public void deleteTeamMembershipByTeamId(String teamId) {
		rep.deleteTeamMembershipByTeamId(teamId);
	}
}