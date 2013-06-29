package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.ProjectParticipant;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "projectParticipantRepository")
public class ProjectParticipantRepository extends
		MbDomainRepositoryImp<ProjectParticipant, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<ProjectParticipant> findProjectParticipantByQueryCriteria(T query,
			ViewPage page) {
		List<ProjectParticipant> list = getDao().selectList(
				"selectProjectParticipantByQueryCriteria", query, page);
		if (page != null) {
			int count = findProjectParticipantCountByQueryCriteria(query);
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
	public Integer findProjectParticipantCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectProjectParticipantCountByQueryCriteria", query);
	}

	public Double distributeBonusTotal(String outsourcingProjectId) {
		Object ret =  getDao().selectOne(
				"selectDistributeBonusTotal", outsourcingProjectId);
		if(ret == null)
			return 0.0;
		else
			return (Double)ret;
	}
}
