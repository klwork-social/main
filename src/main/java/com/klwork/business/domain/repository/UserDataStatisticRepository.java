package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.UserDataStatistic;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "userDataStatisticRepository")
public class UserDataStatisticRepository extends
		MbDomainRepositoryImp<UserDataStatistic, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<UserDataStatistic> findUserDataStatisticByQueryCriteria(T query,
			ViewPage page) {
		List<UserDataStatistic> list = getDao().selectList(
				"selectUserDataStatisticByQueryCriteria", query, page);
		if (page != null) {
			int count = findUserDataStatisticCountByQueryCriteria(query);
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
	public Integer findUserDataStatisticCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectUserDataStatisticCountByQueryCriteria", query);
	}
}
