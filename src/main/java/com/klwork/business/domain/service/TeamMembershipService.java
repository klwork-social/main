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

	public TeamMembership createTeamMembership(TeamMembership teamMembership) {
		return null;
	}

	public void deleteTeamMembership(TeamMembership teamMembership) {
	}

	public int updateTeamMembership(TeamMembership teamMembership) {
		return 0;
	}

	public List<TeamMembership> findTeamMembershipByQueryCriteria(TeamMembershipQuery query,
			ViewPage<TeamMembership> page) {
		return rep.findTeamMembershipByQueryCriteria(query, page);
	}

	public TeamMembership findTeamMembershipById(long id) {
		return null;
	}

}