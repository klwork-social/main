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
import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;

import com.klwork.business.domain.model.Team;
import com.klwork.business.domain.service.TeamService;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class TeamMemberQuery extends AbstractLazyLoadingQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1647959665934595909L;
	protected transient TeamService teamService;
	protected transient IdentityService identityService;
	private String teamId;
	protected String sortby;
	protected boolean ascending;

	public TeamMemberQuery(String teamId) {
		teamService = (TeamService) SpringApplicationContextUtil.getContext()
				.getBean("teamService");
		this.identityService = ProcessEngines.getDefaultProcessEngine()
				.getIdentityService();
		this.teamId = teamId;
	}

	@Override
	public int size() {
		UserQuery q = createQuery();
		return (int) q.count();
	}

	private UserQuery createQuery() {
		UserQuery q = identityService.createUserQuery().memberOfTeam(teamId);
		// 需求发布中
		return q;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		UserQuery query = identityService.createUserQuery().memberOfTeam(
				teamId);

		if (sortby == null || "id".equals(sortby)) {
			query.orderByUserId(); // default
		} else if ("firstName".equals(sortby)) {
			query.orderByUserFirstName();
		} else if ("lastName".equals(sortby)) {
			query.orderByUserLastName();
		} else if ("email".equals(sortby)) {
			query.orderByUserEmail();
		}

		if (sortby == null || ascending) {
			query.asc();
		} else {
			query.desc();
		}

		List<User> users = query.listPage(start, count);

		List<Item> items = new ArrayList<Item>();
		for (User user : users) {
			BeanItem<User> teamItem = new BeanItem<User>(user);
			items.add(teamItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		User u = identityService.createUserQuery().userId(id).singleResult();
		return new BeanItem<User>(u);
	}

	@Override
	  public void setSorting(Object[] propertyIds, boolean[] ascending) {
	    if (propertyIds.length > 0) {
	      this.sortby = propertyIds[0].toString();
	      this.ascending = ascending[0];
	    }
	  }
}
