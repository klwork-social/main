package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.ResourcesAssignManager;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "resourcesAssignManagerRepository")
public class ResourcesAssignManagerRepository extends
		MbDomainRepositoryImp<ResourcesAssignManager, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<ResourcesAssignManager> findResourcesAssignManagerByQueryCriteria(T query,
			ViewPage page) {
		List<ResourcesAssignManager> list = getDao().selectList(
				"selectResourcesAssignManagerByQueryCriteria", query, page);
		if (page != null) {
			int count = findResourcesAssignManagerCountByQueryCriteria(query);
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
	public Integer findResourcesAssignManagerCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectResourcesAssignManagerCountByQueryCriteria", query);
	}
}
