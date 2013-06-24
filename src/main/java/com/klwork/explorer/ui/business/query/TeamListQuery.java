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

import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.model.TeamQuery;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class TeamListQuery extends AbstractLazyLoadingQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1647959665934595909L;
	TeamService teamService;
	private String ownUser;

	public TeamListQuery(String userId) {
		teamService = (TeamService) SpringApplicationContextUtil
				.getContext().getBean("teamService");
		this.ownUser = userId;
	}

	@Override
	public int size() {
		TeamQuery q = createQuery();
		return teamService.count(q);
	}

	private TeamQuery createQuery() {
		TeamQuery q = new TeamQuery();
		//需求发布中
		q.setOwnUser(ownUser);
		return q;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<Team> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		TeamQuery q = createQuery();
		List<Team> ps = teamService.findTeamByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			Team team = (Team) iterator.next();
			BeanItem<Team> teamItem = new BeanItem<Team>(team);
			items.add(teamItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		return  new BeanItem<Team>(teamService.findTeamById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}
}
