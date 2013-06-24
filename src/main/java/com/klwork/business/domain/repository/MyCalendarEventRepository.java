package com.klwork.business.domain.repository;
import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.MyCalendarEvent;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */
 
@Repository(value = "myCalendarEventRepository")
public class MyCalendarEventRepository extends
		MbDomainRepositoryImp<MyCalendarEvent, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<MyCalendarEvent> findMyCalendarEventByQueryCriteria(T query,
			ViewPage page) {
		List<MyCalendarEvent> list = getDao().selectList(
				"selectMyCalendarEventByQueryCriteria", query, page);
		if (page != null) {
			int count = findMyCalendarEventCountByQueryCriteria(query);
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
	public Integer findMyCalendarEventCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectMyCalendarEventCountByQueryCriteria", query);
	}
}
