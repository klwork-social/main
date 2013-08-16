package com.klwork.business.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.SocialUserWeiboQuery;
import com.klwork.business.domain.repository.SocialUserWeiboRepository;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class SocialUserWeiboService {
	@Autowired
	private SocialUserWeiboRepository rep;

	public void createSocialUserWeibo(SocialUserWeibo socialUserWeibo) {
		socialUserWeibo.setId(rep.getNextId());
		Date now = StringDateUtil.now();
		socialUserWeibo.setLastUpdate(now);
		rep.insert(socialUserWeibo);
	}

	public void deleteSocialUserWeibo(SocialUserWeibo socialUserWeibo) {
	}

	public int updateSocialUserWeibo(SocialUserWeibo socialUserWeibo) {
		return 0;
	}

	public List<SocialUserWeibo> findSocialUserWeiboByQueryCriteria(SocialUserWeiboQuery query,
			ViewPage<SocialUserWeibo> page) {
		return rep.findSocialUserWeiboByQueryCriteria(query, page);
	}

	public SocialUserWeibo findSocialUserWeiboById(String id) {
		return rep.find(id);
	}
	
	public int count(SocialUserWeiboQuery query) {
		return rep.findSocialUserWeiboCountByQueryCriteria(query);
	}

	public SocialUserWeibo newSocialUserWeibo() {
		SocialUserWeibo weibo = new SocialUserWeibo();
		weibo.setId(rep.getNextId());
		return weibo;
	}

	/**
	 * 得到最后一条微博
	 * @param ac
	 * @return
	 */
	public SocialUserWeibo queryLastWeibo(SocialUserWeiboQuery query) {
		return rep.queryLastWeibo(query);
	}

	public boolean existWeibo(String userAccountId, String weiboId) {
		SocialUserWeiboQuery query = new SocialUserWeiboQuery();
		query.setUserAccountId(userAccountId).setWeiboId(weiboId);
		List list = rep.findSocialUserWeiboByQueryCriteria(query, null);
		SocialUserWeibo r = rep.getOnlyOne(list);
		return r != null;
	}
}