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
import com.klwork.business.domain.model.SocialUserWeiboComment;
import com.klwork.business.domain.model.SocialUserWeiboCommentQuery;
import com.klwork.business.domain.service.SocialUserAccountInfoService;
import com.klwork.business.domain.service.SocialUserWeiboCommentService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class SocialUserWeiboCommentTableQuery extends AbstractLazyLoadingQuery {

	private static final long serialVersionUID = -1647959665934595909L;
	private SocialUserAccountInfoService socialUserAccountInfoService;
	
	private SocialUserWeiboCommentService socialUserWeiboCommentService;
	
	private SocialUserAccount socialUserAccount;
	private int weiboType = 0;
	


	public SocialUserWeiboCommentTableQuery(SocialUserAccount socialUserAccount,
			int type) {
		socialUserAccountInfoService = (SocialUserAccountInfoService) SpringApplicationContextUtil
				.getContext().getBean("socialUserAccountInfoService");
		
		socialUserWeiboCommentService = (SocialUserWeiboCommentService) SpringApplicationContextUtil
				.getContext().getBean("socialUserWeiboCommentService");
		this.socialUserAccount = socialUserAccount;
		this.weiboType = type;
	}

	@Override
	public int size() {
		SocialUserWeiboCommentQuery q = createQuery();
		return socialUserWeiboCommentService.count(q);
	}

	protected SocialUserWeiboCommentQuery createQuery() {
		SocialUserWeiboCommentQuery q = new SocialUserWeiboCommentQuery();
		q.setUserAccountId(socialUserAccount.getId());
		q.setCommentType(weiboType);
		q.setOrderBy("create_at_ desc");
		return q;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<SocialUserWeiboComment> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		SocialUserWeiboCommentQuery q = createQuery();
		List<SocialUserWeiboComment> ps = socialUserWeiboCommentService.findSocialUserWeiboCommentByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			SocialUserWeiboComment weibo = (SocialUserWeiboComment) iterator.next();
			BeanItem<SocialUserWeiboComment> weiboItem = new BeanItem<SocialUserWeiboComment>(weibo);
			items.add(weiboItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		return new BeanItem<SocialUserWeiboComment>(
				socialUserWeiboCommentService.findSocialUserWeiboCommentById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}
}
