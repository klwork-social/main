package com.klwork.business.domain.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.model.ProjectQuery;
import com.klwork.business.domain.model.Todo;
import com.klwork.business.domain.model.TodoQuery;
import com.klwork.business.domain.repository.ProjectRepository;
import com.klwork.business.domain.repository.TodoRepository;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class ProjectService {
	@Autowired
	private ProjectRepository rep;

	@Autowired
	private TodoRepository todoRep;
	
	@Autowired
	private TodoService todoService;
	
	/**
	 *  默认在项目下创建一个todo
	 * @param project
	 */
	public void createProject(Project project) {
		project.setId(rep.getNextId());
		Date now = StringDateUtil.now();
		project.setCreationdate(now);
		project.setLastupdate(now);
		rep.insert(project);
		// 默认在项目下创建一个todo
		Todo t = todoRep.newTodo();
		t.setProId(project.getId());
		todoRep.insert(t);

	}

	public void deleteProject(Project project) {
		//删除所有相关的todo list
		TodoQuery query = new TodoQuery();
		query.setProId(project.getId());
		List<Todo> list = todoService.findTodoByQueryCriteria(query , null);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Todo todo = (Todo) iterator.next();
			todoService.deleteTodo(todo);
		}
		rep.deleteById(project.getId());
	}

	public int updateProject(Project project) {
		return rep.update(project);
	}

	public List<Project> findByQueryCriteria(ProjectQuery query,
			ViewPage<Project> page) {
		return rep.findProjectByQueryCriteria(query, page);
	}

	public Project findProjectById(String id) {
		return rep.find(id);
	}

	public int count(ProjectQuery query) {
		return rep.findProjectCountByQueryCriteria(query);
	}

	public void updateProjectName(String id, String name) {
		Project s = rep.find(id);
		s.setLastupdate(new Date());
		s.setName(name);
		rep.update(s);
	}
}