package com.klwork.business.domain.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.business.domain.model.EntityDictionary;
import com.klwork.business.domain.model.MyCalendarEvent;
import com.klwork.business.domain.model.MyCalendarEventQuery;
import com.klwork.business.domain.model.OutsourcingProject;
import com.klwork.business.domain.model.OutsourcingProjectQuery;
import com.klwork.business.domain.repository.OutsourcingProjectRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class OutsourcingProjectService {
	@Autowired
	private OutsourcingProjectRepository rep;

	public void createOutsourcingProject(OutsourcingProject outsourcingProject) {
		outsourcingProject.setId(rep.getNextId());
		Date lastUpdate = StringDateUtil.now();
		outsourcingProject.setLastUpdate(lastUpdate);
		//默认为新建状态
		outsourcingProject.setPrgStatus(EntityDictionary.OUTSOURCING_STATUS_NEW);
		outsourcingProject.setCreationDate(lastUpdate);
		rep.insert(outsourcingProject);
	}

	/*public void deleteOutsourcingProject(OutsourcingProject outsourcingProject) {
	}*/

	public int updateOutsourcingProject(OutsourcingProject outsourcingProject) {
		Date lastUpdate = StringDateUtil.now();
		outsourcingProject.setLastUpdate(lastUpdate);
		return rep.update(outsourcingProject);
	}

	public List<OutsourcingProject> findOutsourcingProjectByQueryCriteria(OutsourcingProjectQuery query,
			ViewPage<OutsourcingProject> page) {
		return rep.findOutsourcingProjectByQueryCriteria(query, page);
	}
	
	public OutsourcingProject findOneEntityByQuery(OutsourcingProjectQuery query) {
		List<OutsourcingProject> list =  rep.findOutsourcingProjectByQueryCriteria(query, null);
		if(!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public OutsourcingProject findOutsourcingProjectById(String id) {
		return rep.find(id);
	}
	
	public int count(OutsourcingProjectQuery query) {
		return rep.findOutsourcingProjectCountByQueryCriteria(query);
	}
}