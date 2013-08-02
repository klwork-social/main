package com.klwork.business.domain.service;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weibo4j.http.AccessToken;
import weibo4j.model.User;

import com.klwork.common.SystemConstants;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.ReflectionUtils;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.repository.SocialUserAccountRepository;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class SocialUserAccountService {

	@Autowired
	IdentityService identityService;

	@Autowired
	UserService userService;

	@Autowired
	private SocialUserAccountRepository rep;

	@Autowired
	public SocialUserAccountInfoService socialUserAccountInfoService;

	public void createSocialUserAccount(SocialUserAccount socialUserAccount) {
		socialUserAccount.setId(rep.getNextId());
		Date lastUpdate = StringDateUtil.now();
		socialUserAccount.setLastUpdate(lastUpdate);
		rep.insert(socialUserAccount);
	}

	/*public void deleteSocialUserAccount(SocialUserAccount socialUserAccount) {
	}*/

	public int updateSocialUserAccount(SocialUserAccount socialUserAccount) {
		return rep.update(socialUserAccount);
	}

	public List<SocialUserAccount> findSocialUserAccountByQueryCriteria(
			SocialUserAccountQuery query, ViewPage<SocialUserAccount> page) {
		return rep.findSocialUserAccountByQueryCriteria(query, page);
	}

	public SocialUserAccount findSocialUserAccountById(String id) {
		return rep.find(id);
	}

	public int count(SocialUserAccountQuery query) {
		return rep.findSocialUserAccountCountByQueryCriteria(query);
	}
	
	

	public SocialUserAccount findSocialUserByType(String userId, int type) {
		SocialUserAccountQuery query = new SocialUserAccountQuery();
		query.setOwnUser(userId).setType(type);
		List<SocialUserAccount> list = findSocialUserAccountByQueryCriteria(query, null);
		return getOnlyOne(list);
	}

	public <T> T getOnlyOne(List<T> list) {
		if (list !=null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
}