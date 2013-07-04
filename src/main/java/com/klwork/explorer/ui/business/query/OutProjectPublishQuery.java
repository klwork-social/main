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

import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.OutsourcingProjectQuery;

public class OutProjectPublishQuery extends PublicProjectListQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1647959665934595909L;
	private String userId = null;
	private String status = null;

	public OutProjectPublishQuery(String userId, String status) {
		super();
		this.userId = userId;
		this.status = status;
	}

	@Override
	protected OutsourcingProjectQuery createQuery() {
		OutsourcingProjectQuery q = new OutsourcingProjectQuery();
		q.setOwnUser(userId);
		if(status != null){
			// 需求发布中
			q.setPrgStatus(EntityDictionary.OUTSOURCING_STATUS_PUBLISHED);
		}
		return q;
	}
}
