package com.klwork.business.domain.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.business.domain.model.SocialUserWeiboComment;
import com.klwork.business.domain.model.SocialUserWeiboCommentQuery;
import com.klwork.business.domain.repository.SocialUserWeiboCommentRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class SocialUserWeiboCommentService {
	@Autowired
	private SocialUserWeiboCommentRepository rep;

	public void createSocialUserWeiboComment(SocialUserWeiboComment socialUserWeiboComment) {
		socialUserWeiboComment.setId(rep.getNextId());
		rep.insert(socialUserWeiboComment);
	}

	public void deleteSocialUserWeiboComment(SocialUserWeiboComment socialUserWeiboComment) {
		rep.deleteById(socialUserWeiboComment.getId());
	}

	public int updateSocialUserWeiboComment(SocialUserWeiboComment socialUserWeiboComment) {
		return rep.update(socialUserWeiboComment);
	}

	public List<SocialUserWeiboComment> findSocialUserWeiboCommentByQueryCriteria(SocialUserWeiboCommentQuery query,
			ViewPage<SocialUserWeiboComment> page) {
		return rep.findSocialUserWeiboCommentByQueryCriteria(query, page);
	}

	public SocialUserWeiboComment findSocialUserWeiboCommentById(String id) {
		return rep.find(id);
	}
	
	public int count(SocialUserWeiboCommentQuery query) {
		return rep.findSocialUserWeiboCommentCountByQueryCriteria(query);
	}

	public SocialUserWeiboComment queryLastComment(
			SocialUserWeiboCommentQuery query) {
		return rep.queryLastComment(query);
	}
}