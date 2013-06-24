package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.Project;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "projectRepository")
public class ProjectRepository extends
		MbDomainRepositoryImp<Project, String> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<Project> findProjectByQueryCriteria(T query,
			ViewPage page) {
		List<Project> list = getDao().selectList(
				"selectProjectByQueryCriteria", query, page);
		if (page != null) {
			int count = findProjectCountByQueryCriteria(query);
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
	public Integer findProjectCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectProjectCountByQueryCriteria", query);
	}
}
