package com.klwork.business.domain.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.DictDefQuery;
import com.klwork.common.dao.QueryParameter;
import com.klwork.common.domain.repository.MbDomainRepositoryImp;
import com.klwork.common.dto.vo.ViewPage;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 */

@Repository(value = "dictDefRepository")
public class DictDefRepository extends
		MbDomainRepositoryImp<DictDef, Serializable> implements
		InitializingBean {

	public static Map<String, DictDef> defMaps = null;
	
	public static Map<String, List<DictDef>> defTypeMaps = new HashMap<String, List<DictDef>>();

	public static Map<String, DictDef> getDefMaps() {
		return defMaps;
	}
	
	public static Map<String, List<DictDef>> getDefTypeMap() {
		return defTypeMaps;
	}


	@SuppressWarnings("unchecked")
	public <T extends QueryParameter> List<DictDef> findDictDefByQueryCriteria(
			T query, ViewPage page) {
		List<DictDef> list = getDao().selectList(
				"selectDictDefByQueryCriteria", query, page);
		if (page != null) {
			int count = findDictDefCountByQueryCriteria(query);
			page.setTotalSize(count);
			// 总数
			page.setPageDataList(list);
		}
		return list;
	}

	/**
	 * 查询总数
	 * 
	 * @param query
	 * @return
	 */
	public Integer findDictDefCountByQueryCriteria(Object query) {
		return (Integer) getDao().selectOne(
				"selectDictDefCountByQueryCriteria", query);
	}

	public void afterPropertiesSet() throws Exception {
		initData();
		if (defMaps == null) {
			defMaps = initAllDictyToMap();
		}
	}

	private void initData() {
		// String id,String code, Integer type, String name, String value
		DictDef dictDef = new DictDef("1", "test_sign_value", "-1", "数据字典类型1", "1");
		dictDef = new DictDef("2", "social_type", "-1", "社交类型", "2");
		insertData(dictDef);
		dictDef = new DictDef("3", "social_account_info", "-1", "社交账号信息", "3");
		insertData(dictDef);
		
		dictDef = new DictDef("4", "social_token_info", "-1", "社交账号token信息", "4");
		insertData(dictDef);
		
		dictDef = new DictDef("5", "data_type", "-1", "数据类型", "5");
		insertData(dictDef);
		
		dictDef = new DictDef("6", "weibo_type", "-1", "微博分类", "6");
		insertData(dictDef);
		
		insertData(dictDef);
		dictDef = new DictDef("100", "sina", "2", "新浪", "0");
		insertData(dictDef);
		dictDef = new DictDef("101", "tencent", "2", "腾讯", "1");
		insertData(dictDef);
		
		dictDef = new DictDef("110", "followersCount", "3", "粉丝数", "followersCount");
		insertData(dictDef);
		dictDef = new DictDef("111", "friendsCount", "3", "关注数", "friendsCount");
		insertData(dictDef);
		
		dictDef = new DictDef("112", "statusesCount", "3", "微博数", "statusesCount");
		insertData(dictDef);
		dictDef = new DictDef("113", "favouritesCount", "3", "收藏数", "favouritesCount");
		insertData(dictDef);
		/*dictDef = new DictDef("114", "allowAllActMsg", "3", "是否允许所有人给我发私信", "allowAllActMsg");
		insertData(dictDef);
		dictDef = new DictDef("115", "allowAllComment", "3", "是否允许所有人对我的微博进行评论", "allowAllComment");
		insertData(dictDef);
		dictDef = new DictDef("116", "geoEnabled", "3", "是否允许标识用户的地理位置", "geoEnabled");
		insertData(dictDef);*/
		dictDef = new DictDef("117", "verifiedType", "3", "官方认证", "verifiedType");
		insertData(dictDef);
		dictDef = new DictDef("118", "avatarLarge", "3", "用户大头像地址", "avatarLarge");
		insertData(dictDef);
		dictDef = new DictDef("119", "biFollowersCount", "3", "用户的互粉数", "biFollowersCount");
		insertData(dictDef);
		
		dictDef = new DictDef("220", "screenName", "3", "用户昵称", "screenName");
		insertData(dictDef);
		
		dictDef = new DictDef("221", "name", "3", "友好显示名称", "name");
		insertData(dictDef);
		
		dictDef = new DictDef("222", "province", "3", "用户所在省级ID", "province");
		insertData(dictDef);
		
		dictDef = new DictDef("223", "city", "3", "用户所在城市ID", "city");
		insertData(dictDef);
		
		
		dictDef = new DictDef("224", "location", "3", "用户所在地", "location");
		insertData(dictDef);
		dictDef = new DictDef("225", "description", "3", "用户个人描述", "description");
		insertData(dictDef);
		dictDef = new DictDef("226", "url", "3", "用户博客地址", "url");
		insertData(dictDef);
		dictDef = new DictDef("227", "profileImageUrl", "3", "用户头像地址", "profileImageUrl");
		insertData(dictDef);
		
		dictDef = new DictDef("230", "weihao", "3", "用户的微号", "weihao");
		insertData(dictDef);
		
		dictDef = new DictDef("231", "gender", "3", "性别", "gender");
		insertData(dictDef);
		
		dictDef = new DictDef("250", "expiredTime", "4", "token过期时间", "expiredTime");
		insertData(dictDef);
		
		dictDef = new DictDef("251", "accessToken", "4", "token过期时间", "accessToken");
		insertData(dictDef);
		
		dictDef = new DictDef("252", "appkeyId", "4", "appkeyId", "appkeyId");
		insertData(dictDef);
		
		dictDef = new DictDef("253", "openId", "4", "openId", "openId");
		insertData(dictDef);
		
		dictDef = new DictDef("254", "openKey", "4", "openKey", "openKey");
		insertData(dictDef);
		
		dictDef = new DictDef("255", "refreshToken", "4", "refreshToken", "refreshToken");
		insertData(dictDef);
		
		
		dictDef = new DictDef("260", "string", "5", "字符", "0");
		insertData(dictDef);
		
		dictDef = new DictDef("261", "int", "5", "整型", "1");
		insertData(dictDef);
		
		dictDef = new DictDef("262", "double", "5", "浮点型", "2");
		insertData(dictDef);
		
		dictDef = new DictDef("263", "date", "5", "日期型", "3");
		insertData(dictDef);
		
		
		dictDef = new DictDef("270", "comment_to_me", "6", "我收到的评论", "1");
		insertData(dictDef);
		
		dictDef = new DictDef("271", "at_me_comment", "6", "at@我的评论", "2");
		insertData(dictDef);
		
		dictDef = new DictDef("272", "at_me_status", "6", "at@我的微博状态", "3");
		insertData(dictDef);
	}

	private void insertData(DictDef dictDef) {
		DictDef r = queryDictByCodeType(dictDef.getCode(),dictDef.getType());
		if (r == null) {
			insert(dictDef);
		}
	}

	/**
	 * 将数据字典以数组的形式保存
	 * 
	 * @return
	 */
	private Map<String, DictDef> initAllDictyToMap() {
		Map<String, DictDef> s = new HashMap<String, DictDef>();
		List<DictDef> list = findDictDefByQueryCriteria(new DictDefQuery(),
				null);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DictDef dictDef = (DictDef) iterator.next();
			s.put(dictDef.getCode(), dictDef);
			if(defTypeMaps.get(dictDef.getType()) == null) {
				List<DictDef> innerList = new ArrayList<DictDef>();
				innerList.add(dictDef);
				defTypeMaps.put(dictDef.getType(), innerList);
			}else {
				defTypeMaps.get(dictDef.getType()).add(dictDef);
			}
		}
		return s;
	}

	/**
	 * 根据类型查询所有的数据字典
	 * 
	 * @param i
	 * @return
	 */
	public List<DictDef> queryDictByType(String type) {
		DictDefQuery query = new DictDefQuery();
		query.setType(type);
		List<DictDef> list = findDictDefByQueryCriteria(query, null);
		return list;
	}

	public DictDef queryDictByCodeType(String code,String type) {
		DictDefQuery query = new DictDefQuery();
		query.setCode(code).setType(type);
		List<DictDef> list = findDictDefByQueryCriteria(query, null);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
}
