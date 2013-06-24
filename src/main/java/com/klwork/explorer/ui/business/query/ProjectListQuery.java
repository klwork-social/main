/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.klwork.explorer.ui.business.query;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.model.ProjectQuery;
import com.klwork.business.domain.service.ProjectService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.I18nManager;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.task.data.TaskListItem;
import com.vaadin.data.Item;

public class ProjectListQuery extends AbstractLazyLoadingQuery {

	ProjectService projectService;

	public ProjectListQuery() {
		projectService = (ProjectService) SpringApplicationContextUtil
				.getContext().getBean("projectService");
	}

	@Override
	public int size() {
		ProjectQuery q = createQuery();
		return projectService.count(q);
	}

	private ProjectQuery createQuery() {
		String userId = LoginHandler.getLoggedInUser().getId();
		ProjectQuery q = new ProjectQuery();
		q.setOwnuser(userId);
		return q;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<Project> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		ProjectQuery q = createQuery();
		List<Project> ps = projectService.findByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			Project project = (Project) iterator.next();
			items.add(new ProjectListItem(project));
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		return new ProjectListItem(projectService.findProjectById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}
}
