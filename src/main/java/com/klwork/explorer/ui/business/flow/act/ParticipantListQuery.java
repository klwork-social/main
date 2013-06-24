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
package com.klwork.explorer.ui.business.flow.act;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.ProjectParticipant;
import com.klwork.business.domain.model.ProjectParticipantQuery;
import com.klwork.business.domain.service.ProjectParticipantService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class ParticipantListQuery extends AbstractLazyLoadingQuery {
	
	private boolean isWin = false;
	private OutsourcingProject project;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1647959665934595909L;
	ProjectParticipantService projectParticipantService;

	public ParticipantListQuery(OutsourcingProject project,boolean win) {
		projectParticipantService = (ProjectParticipantService) SpringApplicationContextUtil
				.getContext().getBean("projectParticipantService");
		this.isWin = win;
		this.project = project;
	}

	@Override
	public int size() {
		ProjectParticipantQuery q = createQuery();
		return projectParticipantService.count(q);
	}

	private ProjectParticipantQuery createQuery() {
		ProjectParticipantQuery q = new ProjectParticipantQuery();
		//需求发布中
		q.setIsWinner(isWin).setOutPrgId(project.getId()).setProcInstId(project.getProcInstId()).setParticipantsType(EntityDictionary.PARTICIPANTS_TYPE_SCORER);
		return q;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<ProjectParticipant> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		ProjectParticipantQuery q = createQuery();
		List<ProjectParticipant> ps = projectParticipantService.findProjectParticipantByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			ProjectParticipant project = (ProjectParticipant) iterator.next();
			BeanItem<ProjectParticipant> projectItem = new BeanItem<ProjectParticipant>(project);
			//items.add(new ProjectParticipantListItem(project));
			items.add(projectItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		//return new ProjectParticipantListItem(projectService.findProjectParticipantById(id));
		return  new BeanItem<ProjectParticipant>(projectParticipantService.findProjectParticipantById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}
}
