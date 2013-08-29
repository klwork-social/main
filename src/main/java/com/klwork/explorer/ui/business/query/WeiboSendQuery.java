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

import com.klwork.business.domain.model.SocialUserWeiboSend;
import com.klwork.business.domain.model.SocialUserWeiboSendQuery;
import com.klwork.business.domain.service.SocialUserWeiboSendService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.explorer.ViewToolManager;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class WeiboSendQuery extends AbstractWeiboSendQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1647959665934595909L;
	protected transient SocialUserWeiboSendService socialUserWeiboSendService;
	protected String sortby;
	protected boolean ascending;

	public WeiboSendQuery() {
		socialUserWeiboSendService =ViewToolManager. getBean("socialUserWeiboSendService");
	}

	@Override
	public int size() {
		return socialUserWeiboSendService.count(createQuery());
	}

	private SocialUserWeiboSendQuery createQuery() {
		return this;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<SocialUserWeiboSend> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		SocialUserWeiboSendQuery q = createQuery();
		List<SocialUserWeiboSend> ps = socialUserWeiboSendService.findSocialUserWeiboSendByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			SocialUserWeiboSend s = (SocialUserWeiboSend) iterator.next();
			BeanItem<SocialUserWeiboSend> tItem = new BeanItem<SocialUserWeiboSend>(s);
			items.add(tItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		return  new BeanItem<SocialUserWeiboSend>(socialUserWeiboSendService.findSocialUserWeiboSendById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}
}
