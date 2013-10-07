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

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.OutsourcingProjectQuery;
import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.service.OutsourcingProjectService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringTool;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class PublicProjectListQuery extends AbstractLazyLoadingQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1647959665934595909L;
	OutsourcingProjectService projectService;
	private String type = null;
	public PublicProjectListQuery() {
		projectService = (OutsourcingProjectService) SpringApplicationContextUtil
				.getContext().getBean("outsourcingProjectService");
	}

	public PublicProjectListQuery(String type) {
		this();
		this.type = type;
	}

	@Override
	public int size() {
		OutsourcingProjectQuery q = createQuery();
		return projectService.count(q);
	}

	protected OutsourcingProjectQuery createQuery() {
		OutsourcingProjectQuery q = new OutsourcingProjectQuery();
		q.setType(type);
		//需求发布中
		q.setPrgStatus(EntityDictionary.OUTSOURCING_STATUS_PUBLISHED);
		return q;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<OutsourcingProject> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		OutsourcingProjectQuery q = createQuery();
		List<OutsourcingProject> ps = projectService.findOutsourcingProjectByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			OutsourcingProject project = (OutsourcingProject) iterator.next();
			if(!StringTool.judgeBlank(project.getName())){
				project.setName("未知");
	    	}
			BeanItem<OutsourcingProject> projectItem = new BeanItem<OutsourcingProject>(project);
			//items.add(new OutsourcingProjectListItem(project));
			items.add(projectItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		//return new OutsourcingProjectListItem(projectService.findOutsourcingProjectById(id));
		return  new BeanItem<OutsourcingProject>(projectService.findOutsourcingProjectById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}
}
