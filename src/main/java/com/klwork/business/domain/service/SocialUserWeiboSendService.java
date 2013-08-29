package com.klwork.business.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserWeiboSend;
import com.klwork.business.domain.model.SocialUserWeiboSendQuery;
import com.klwork.business.domain.repository.SocialUserWeiboSendRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class SocialUserWeiboSendService {
	@Autowired
	private SocialUserWeiboSendRepository rep;

	public void createSocialUserWeiboSend(SocialUserWeiboSend socialUserWeiboSend) {
		socialUserWeiboSend.setId(rep.getNextId());
		rep.insert(socialUserWeiboSend);
	}

	public void deleteSocialUserWeiboSend(SocialUserWeiboSend socialUserWeiboSend) {
	}

	public int updateSocialUserWeiboSend(SocialUserWeiboSend socialUserWeiboSend) {
		return 0;
	}

	public List<SocialUserWeiboSend> findSocialUserWeiboSendByQueryCriteria(SocialUserWeiboSendQuery query,
			ViewPage<SocialUserWeiboSend> page) {
		return rep.findSocialUserWeiboSendByQueryCriteria(query, page);
	}

	public SocialUserWeiboSend findSocialUserWeiboSendById(String id) {
		return rep.find(id);
	}
	
	public int count(SocialUserWeiboSendQuery query) {
		return rep.findSocialUserWeiboSendCountByQueryCriteria(query);
	}
}