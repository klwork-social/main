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

import com.klwork.business.domain.model.OutsourcingProjectQuery;

/**
 * 主要用来进行查询发布的项目
 * @author ww
 */
public class OutProjectAddInQuery extends PublicProjectListQuery {

	private static final long serialVersionUID = -1647959665934595909L;
	private String participant = null;
	private String participantType = null;
	

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public Object getParticipantType() {
		return participantType;
	}

	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}

	public OutProjectAddInQuery(String participant, String participantType) {
		super();
		this.participant = participant;
		this.participantType = participantType;
	}

	@Override
	protected OutsourcingProjectQuery createQuery() {
		OutsourcingProjectQuery q = new OutsourcingProjectQuery();
		if(participant != null){
			// 需求发布中
			q.setParticipant(participant);
		}
		if(participantType != null){
			// 需求发布中
			q.setParticipantType(participantType);
		}
		return q;
	}
}
