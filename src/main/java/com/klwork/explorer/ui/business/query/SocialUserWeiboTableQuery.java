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
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.SocialUserWeiboQuery;
import com.klwork.business.domain.service.SocialUserAccountInfoService;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class SocialUserWeiboTableQuery extends AbstractLazyLoadingQuery {

	private static final long serialVersionUID = -1647959665934595909L;
	private SocialUserAccountInfoService socialUserAccountInfoService;
	
	private SocialUserWeiboService socialUserWeiboService;
	
	private SocialUserAccount socialUserAccount;
	private int type = 0;
	


	public SocialUserWeiboTableQuery(SocialUserAccount socialUserAccount,
			int type) {
		socialUserAccountInfoService = (SocialUserAccountInfoService) SpringApplicationContextUtil
				.getContext().getBean("socialUserAccountInfoService");
		
		socialUserWeiboService = (SocialUserWeiboService) SpringApplicationContextUtil
				.getContext().getBean("socialUserWeiboService");
		this.socialUserAccount = socialUserAccount;
		this.type = type;
	}

	@Override
	public int size() {
		SocialUserWeiboQuery q = createQuery();
		return socialUserWeiboService.count(q);
	}

	protected SocialUserWeiboQuery createQuery() {
		SocialUserWeiboQuery q = new SocialUserWeiboQuery();
		q.setUserAccountId(socialUserAccount.getId());
		if(type == 1){//我的微薄
			q.setUserName(socialUserAccount.getName());
		}
		q.setOrderBy("create_at_ desc");
		return q;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<SocialUserWeibo> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		SocialUserWeiboQuery q = createQuery();
		List<SocialUserWeibo> ps = socialUserWeiboService.findSocialUserWeiboByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			SocialUserWeibo weibo = (SocialUserWeibo) iterator.next();
			BeanItem<SocialUserWeibo> weiboItem = new BeanItem<SocialUserWeibo>(weibo);
			items.add(weiboItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		return new BeanItem<SocialUserWeibo>(
				socialUserWeiboService.findSocialUserWeiboById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}
}
