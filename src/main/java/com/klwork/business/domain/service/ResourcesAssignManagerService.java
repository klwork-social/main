package com.klwork.business.domain.service;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ui.business.social.AccountAuthorityPopupWindow;
import com.klwork.business.domain.model.Project;
import com.klwork.business.domain.model.ResourcesAssignManager;
import com.klwork.business.domain.model.ResourcesAssignManagerQuery;
import com.klwork.business.domain.model.SocialUseAuthorityList;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.repository.ResourcesAssignManagerRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class ResourcesAssignManagerService {
	@Autowired
	private ResourcesAssignManagerRepository rep;
	@Autowired
	SocialUseAuthorityListService socialUseAuthorityListService;
	
	public void createResourcesAssignManager(ResourcesAssignManager resourcesAssignManager) {
		resourcesAssignManager.setId(rep.getNextId());
		Date now = StringDateUtil.now();
		resourcesAssignManager.setLastUpdate(now);
		rep.insert(resourcesAssignManager);
	}

	public void deleteResourcesAssignManager(ResourcesAssignManager resourcesAssignManager) {
		socialUseAuthorityListService.deleteSocialUseAuthorityListByReId(resourcesAssignManager.getId());
		rep.deleteById(resourcesAssignManager.getId());
	}

	public int updateResourcesAssignManager(ResourcesAssignManager resourcesAssignManager) {
		Date now = StringDateUtil.now();
		resourcesAssignManager.setLastUpdate(now);
		return rep.update(resourcesAssignManager);
	}

	public List<ResourcesAssignManager> findResourcesAssignManagerByQueryCriteria(ResourcesAssignManagerQuery query,
			ViewPage<ResourcesAssignManager> page) {
		return rep.findResourcesAssignManagerByQueryCriteria(query, page);
	}

	public ResourcesAssignManager findResourcesAssignManagerById(String id) {
		return rep.find(id);
	}
	
	public int count(ResourcesAssignManagerQuery query) {
		return rep.findResourcesAssignManagerCountByQueryCriteria(query);
	}

	public ResourcesAssignManager addResourcesAssignManager(String entityId, String teamId,
			String entityType, String type) {
		ResourcesAssignManager m = queryOneByEntityIdAndType(entityId,
				entityType, type);
		if(m == null){
			m = new ResourcesAssignManager(entityId,entityType,teamId,type);
			createResourcesAssignManager(m);
		}else {
			m.setTeamId(teamId);
			updateResourcesAssignManager(m);
		}
		return m;
	}

	public ResourcesAssignManager queryOneByEntityIdAndType(String entityId,
			String entityType, String type) {
		ResourcesAssignManagerQuery query = new ResourcesAssignManagerQuery();
		query.setEntityId(entityId).setEntityType(entityType).setType(type);
		List<ResourcesAssignManager> list = rep.findResourcesAssignManagerByQueryCriteria(query, null);
		ResourcesAssignManager m = rep.getOnlyOne(list);
		return m;
	}

	public void deleteResourcesAssignManager(String entityId,
			String entityType, String type) {
		ResourcesAssignManagerQuery query = new ResourcesAssignManagerQuery();
		query.setEntityId(entityId).setEntityType(entityType).setType(type);
		List<ResourcesAssignManager> list = rep.findResourcesAssignManagerByQueryCriteria(query, null);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			ResourcesAssignManager resourcesAssignManager = (ResourcesAssignManager) iterator
					.next();
			deleteResourcesAssignManager(resourcesAssignManager);
			//
		}
	}
	
	/**
	 * 给账号增加权限
	 * @param sc
	 * @param teamId
	 * @param perimt
	 */
	public void addAccountPermit(SocialUserAccount sc,String teamId, Collection perimt,String entityType, String type) {
		if(StringTool.judgeBlank(teamId)){
			ResourcesAssignManager re = addResourcesAssignManager(sc.getId(),teamId,entityType,type);
			socialUseAuthorityListService.deleteSocialUseAuthorityListByReId(re.getId());
			for (Iterator iterator = perimt.iterator(); iterator.hasNext();) {
				String permitId = (String) iterator.next();
				SocialUseAuthorityList auth = new SocialUseAuthorityList();
				auth.setKey(permitId);
				auth.setManagerGroupId(re.getId());
				socialUseAuthorityListService.createSocialUseAuthorityList(auth);
			}
		}else {
			deleteResourcesAssignManager(sc.getId(),entityType,type);
		}
	}
	
	public void addProjectPlanPermit(Project pj,String teamId, Collection perimt,String entityType, String type) {
		if(StringTool.judgeBlank(teamId)){
			ResourcesAssignManager re = addResourcesAssignManager(pj.getId(),teamId,entityType,type);
			socialUseAuthorityListService.deleteSocialUseAuthorityListByReId(re.getId());
			for (Iterator iterator = perimt.iterator(); iterator.hasNext();) {
				String permitId = (String) iterator.next();
				SocialUseAuthorityList auth = new SocialUseAuthorityList();
				auth.setKey(permitId);
				auth.setManagerGroupId(re.getId());
				socialUseAuthorityListService.createSocialUseAuthorityList(auth);
			}
		}else {
			deleteResourcesAssignManager(pj.getId(),entityType,type);
		}
	}
}