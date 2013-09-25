package com.klwork.business.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
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
		query.setKey(socialUserAccountInfo.getKey()).setAccountId(socialUserAccountInfo.getAccountId()).setType(socialUserAccountInfo.getType()).setUserId(socialUserAccountInfo.getUserId()).setEntityId(socialUserAccountInfo.getEntityId());
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
	
	/**
	 * 查询今天登录的用户的帐号信息
	 * @return
	 */
	public List<SocialUserAccountInfo> queryTodayLoginSocialUserAccountInfo() {
		SocialUserAccountInfoQuery query = new SocialUserAccountInfoQuery();
		Date d = new Date();
		Date aDate = StringDateUtil.dateToYMD(d);
		query.setDateAfter(aDate);
		query.setType(DictDef.dict("user_info_type"));
		query.setKey("user_last_logged_time");
		query.setOrderBy("value_date_ asc");
		// 查询今天登录的用户
		List<SocialUserAccountInfo> list = findSocialUserAccountInfoByQueryCriteria(query, null);
		return list;
	}
	
	/**
	 * 设置用户帐号信息
	 * @param socialUserAccount
	 * @param key
	 * @param value
	 */
	public void setSocialUserAccountInfo(SocialUserAccount socialUserAccount, String key, String value) {
		SocialUserAccountInfo info = new SocialUserAccountInfo();
	    info.setKey( key );//
	    info.setAccountId(socialUserAccount.getId());
	    info.setType(DictDef.dict( "user_account_info_type")); //帐号类型
		info.setValue(value);
	    info.setValueString(value);
	    info.setValueType(DictDef. dictInt("string"));
	    createSocialUserAccountInfo(info);
	}
	
	/**
	 * 设置用户帐号信息
	 * @param socialUserAccount
	 * @param key
	 * @param value
	 */
	public void setEntityInfo(String entityId, String entityType,String key, String value) {
		SocialUserAccountInfo info = new SocialUserAccountInfo();
	    info.setKey( key );//
	    info.setEntityId(entityId);
	    info.setType(entityType); //帐号类型
		info.setValue(value);
	    info.setValueString(value);
	    info.setValueType(DictDef. dictInt("string"));
	    createSocialUserAccountInfo(info);
	}
}