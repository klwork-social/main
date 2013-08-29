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

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.spring.SpringApplicationContextUtil;
import com.klwork.explorer.data.AbstractLazyLoadingQuery;
import com.klwork.explorer.security.LoginHandler;
import com.klwork.explorer.ui.business.social.SocialAccountList;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;

public class SocialListQuery extends AbstractLazyLoadingQuery {

	SocialUserAccountService saService;
	SocialAccountList socialAccountList;
	public SocialListQuery(SocialAccountList socialAccountList) {
		saService = (SocialUserAccountService) SpringApplicationContextUtil
				.getContext().getBean("socialUserAccountService");
		this.socialAccountList = socialAccountList;
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
		List<SocialUserAccount> ps = saService
				.findSocialUserAccountByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			SocialUserAccount account = (SocialUserAccount) iterator.next();
			SocialAccountItem pItem = new SocialAccountItem(account);
			items.add(pItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		return new BeanItem<SocialUserAccount>(
				saService.findSocialUserAccountById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}

	class SocialAccountItem extends PropertysetItem {
		private static final long serialVersionUID = 5248557445646101303L;
		private SocialUserAccount account;

		public SocialAccountItem(final SocialUserAccount sc) {
			account = sc;

			Button idButton = new Button(sc.getUserScreenName());
			idButton.addStyleName(Reindeer.BUTTON_LINK);
			idButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
				}
			});
			addItemProperty("userScreenName", new ObjectProperty<Button>(
					idButton, Button.class));
			
			addItemProperty("name", new ObjectProperty<String>(sc.getName(), String.class));
			addItemProperty("url", new ObjectProperty<String>(sc.getUrl(), String.class));
			DictDef v = DictDef.dictValue(DictDef.dict("social_type"), sc.getType()+"");
			addItemProperty("type", new ObjectProperty<String>(v.getName(), String.class));
			
			HorizontalLayout buttonLayout = new HorizontalLayout();
			addItemProperty("actions", new ObjectProperty<Component>(buttonLayout, Component.class));
			buttonLayout.setSpacing(true);
			buttonLayout.addStyleName("social");
		
			Button permitButton  = new Button("权限分配");
			permitButton.addStyleName(Reindeer.BUTTON_LINK);
			permitButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					socialAccountList.openAuthorityWindow(sc);
				}
			});
			buttonLayout.addComponent(permitButton);
			
			Button editButton  = new Button("查看微博");
			editButton.addStyleName(Reindeer.BUTTON_LINK);
			editButton.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					socialAccountList.selectedHandle(sc);
				}
			});
			buttonLayout.addComponent(editButton);
		}
	}
}
