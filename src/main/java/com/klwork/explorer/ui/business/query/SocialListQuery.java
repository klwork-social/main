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

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class SocialListQuery extends AbstractLazyLoadingQuery {

	SocialUserAccountService saService;

	public SocialListQuery() {
		saService = (SocialUserAccountService) SpringApplicationContextUtil
				.getContext().getBean("socialUserAccountService");
	}

	@Override
	public int size() {
		SocialUserAccountQuery q = createQuery();
		return saService.count(q);
	}

	private SocialUserAccountQuery createQuery() {
		String userId = LoginHandler.getLoggedInUser().getId();
		SocialUserAccountQuery q = new SocialUserAccountQuery();
		q.setOwnUser(userId);
		return q;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<SocialUserAccount> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		SocialUserAccountQuery q = createQuery();
		List<SocialUserAccount> ps = saService.findSocialUserAccountByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			SocialUserAccount account = (SocialUserAccount)iterator.next();
			BeanItem<SocialUserAccount> pItem = new BeanItem<SocialUserAccount>(account);
			items.add(pItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		return  new BeanItem<SocialUserAccount>(saService.findSocialUserAccountById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}
}
