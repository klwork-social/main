package com.klwork.business.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountInfoQuery;
import com.klwork.business.domain.repository.SocialUserAccountInfoRepository;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.StringDateUtil;


/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Service
public class SocialUserAccountInfoService {
	@Autowired
	private SocialUserAccountInfoRepository rep;

	public void createSocialUserAccountInfo(SocialUserAccountInfo socialUserAccountInfo) {
		SocialUserAccountInfo s = queryExist(socialUserAccountInfo);
		if(s == null){
			socialUserAccountInfo.setId(rep.getNextId());
			rep.insert(socialUserAccountInfo);
		}else {
			socialUserAccountInfo.setId(s.getId());
			updateSocialUserAccountInfo(socialUserAccountInfo);
		}
	}

	private SocialUserAccountInfo queryExist(SocialUserAccountInfo socialUserAccountInfo) {
		SocialUserAccountInfoQuery query = new SocialUserAccountInfoQuery();
		query.setKey(socialUserAccountInfo.getKey()).setAccountId(socialUserAccountInfo.getAccountId()).setType(socialUserAccountInfo.getType()).setUserId(socialUserAccountInfo.getUserId());
		List<SocialUserAccountInfo> infos =  findSocialUserAccountInfoByQueryCriteria(query,null);
		if(infos != null && infos.size() > 0){
			return infos.get(0);
		}
		return null;
	}


	public int updateSocialUserAccountInfo(SocialUserAccountInfo socialUserAccountInfo) {
		Date lastUpdate = StringDateUtil.now();
		socialUserAccountInfo.setLastUpdate(lastUpdate);
		return rep.update(socialUserAccountInfo);
	}

	public List<SocialUserAccountInfo> findSocialUserAccountInfoByQueryCriteria(SocialUserAccountInfoQuery query,
			ViewPage<SocialUserAccountInfo> page) {
		return rep.findSocialUserAccountInfoByQueryCriteria(query, page);
	}

	public SocialUserAccountInfo findSocialUserAccountInfoById(String id) {
		return rep.find(id);
	}
	
	public SocialUserAccountInfo findAccountOfInfoByKey(String key,String accountId) {
		SocialUserAccountInfoQuery query = new SocialUserAccountInfoQuery();
		query.setKey(key).setAccountId(accountId);
		List<SocialUserAccountInfo> list = findSocialUserAccountInfoByQueryCriteria(query,null);
		if(list != null && list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
	public int count(SocialUserAccountInfoQuery query) {
		return rep.findSocialUserAccountInfoCountByQueryCriteria(query);
	}
}