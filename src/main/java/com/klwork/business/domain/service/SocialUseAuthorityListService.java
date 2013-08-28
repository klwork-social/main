package com.klwork.business.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUseAuthorityList;
import com.klwork.business.domain.model.SocialUseAuthorityListQuery;
import com.klwork.business.domain.repository.SocialUseAuthorityListRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class SocialUseAuthorityListService {
	@Autowired
	private SocialUseAuthorityListRepository rep;

	public void createSocialUseAuthorityList(SocialUseAuthorityList socialUseAuthorityList) {
		socialUseAuthorityList.setId(rep.getNextId());
		rep.insert(socialUseAuthorityList);
	}

	public int updateSocialUseAuthorityList(SocialUseAuthorityList socialUseAuthorityList) {
		return rep.update(socialUseAuthorityList);
	}

	public List<SocialUseAuthorityList> findSocialUseAuthorityListByQueryCriteria(SocialUseAuthorityListQuery query,
			ViewPage<SocialUseAuthorityList> page) {
		return rep.findSocialUseAuthorityListByQueryCriteria(query, page);
	}

	public SocialUseAuthorityList findSocialUseAuthorityListById(String id) {
		return rep.find(id);
	}
	
	public int count(SocialUseAuthorityListQuery query) {
		return rep.findSocialUseAuthorityListCountByQueryCriteria(query);
	}

	public void deleteSocialUseAuthorityListByReId(String reId) {
		rep.deleteSocialUseAuthorityListByReId(reId);
	}
}