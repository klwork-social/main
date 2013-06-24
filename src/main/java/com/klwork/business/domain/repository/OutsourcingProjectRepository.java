package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.OutsourcingProject;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "outsourcingProjectRepository")
public class OutsourcingProjectRepository extends
		MbDomainRepositoryImp<OutsourcingProject, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<OutsourcingProject> findOutsourcingProjectByQueryCriteria(T query,
			ViewPage page) {
		List<OutsourcingProject> list = getDao().selectList(
				"selectOutsourcingProjectByQueryCriteria", query, page);
		if (page != null) {
			int count = findOutsourcingProjectCountByQueryCriteria(query);
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
	public Integer findOutsourcingProjectCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectOutsourcingProjectCountByQueryCriteria", query);
	}
}
