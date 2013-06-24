package com.klwork.business.domain.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.Todo;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Repository(value = "todoRepository")
public class TodoRepository extends MbDomainRepositoryImp<Todo, Serializable> {

	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<Todo> findTodoByQueryCriteria(
			T query, ViewPage page) {
		List<Todo> list = getDao().selectList("selectTodoByQueryCriteria",
				query, page);
		if (page != null) {
			int count = findTodoCountByQueryCriteria(query);
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
	public Integer findTodoCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne("selectTodoCountByQueryCriteria",
				query);
	}
	
	/**
	 * 生成一个新的todo,id也进行生成
	 * @return
	 */
	public Todo newTodo() {
		Todo oEntity = new Todo();
		oEntity.setPriority(3);
		oEntity.setType(0);
		oEntity.setId(getNextId());
		oEntity.setStatus(0);
		oEntity.setPid("-1");
		oEntity.setName("新任务");
		return oEntity;
	}
}
