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
import com.klwork.business.domain.model.DictDefQuery;
import com.klwork.business.domain.service.DictDefService;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.explorer.ViewToolManager;
import com.klwork.explorer.data.LazyLoadingContainer;
import com.klwork.explorer.data.LazyLoadingQuery;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;

public class DictLazyQuery extends DictDefQuery implements LazyLoadingQuery {

	private static final long serialVersionUID = 5739235979571955087L;
	protected transient DictDefService dictDefService;
	protected String sortby;
	protected boolean ascending;

	public DictLazyQuery() {
		dictDefService = ViewToolManager.getBean("dictDefService");
	}

	@Override
	public int size() {
		return dictDefService.count(createQuery());
	}

	private DictLazyQuery createQuery() {
		return this;
	}

	@Override
	public List<Item> loadItems(int start, int count) {
		List<Item> items = new ArrayList<Item>();
		ViewPage<DictDef> page = new ViewPage();
		page.setStart(start);
		page.setPageSize(count);
		DictDefQuery q = createQuery();
		List<DictDef> ps = dictDefService.findDictDefByQueryCriteria(q, page);
		for (Iterator iterator = ps.iterator(); iterator.hasNext();) {
			DictDef s = (DictDef) iterator.next();
			BeanItem<DictDef> tItem = new BeanItem<DictDef>(s);
			items.add(tItem);
		}
		return items;
	}

	@Override
	public Item loadSingleResult(String id) {
		return new BeanItem<DictDef>(dictDefService.findDictDefById(id));
	}

	@Override
	public void setSorting(Object[] propertyIds, boolean[] ascending) {
		throw new UnsupportedOperationException();
	}

	protected LazyLoadingContainer lazyLoadingContainer;

	public void setLazyLoadingContainer(
			LazyLoadingContainer lazyLoadingContainer) {
		this.lazyLoadingContainer = lazyLoadingContainer;
	}
}
