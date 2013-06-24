package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.TeamMembership;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "teamMembershipRepository")
public class TeamMembershipRepository extends
		MbDomainRepositoryImp<TeamMembership, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<TeamMembership> findTeamMembershipByQueryCriteria(T query,
			ViewPage page) {
		List<TeamMembership> list = getDao().selectList(
				"selectTeamMembershipByQueryCriteria", query, page);
		if (page != null) {
			int count = findTeamMembershipCountByQueryCriteria(query);
			page.setTotalSize(count);
			// 总数
			page.setPageDataList(list);
		}
		return list;
	}

	/**
	 * 查询总数
	 * 
	 * @param query
	 * @return
	 */
	public Integer findTeamMembershipCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectTeamMembershipCountByQueryCriteria", query);
	}
}
