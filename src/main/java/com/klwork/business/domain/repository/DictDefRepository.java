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

public class DictDefRepository extends
		MbDomainRepositoryImp<DictDef, Serializable> implements
		InitializingBean {
	
	/**
	 * 可以code值,code值必须为一
	 */
	public static Map<String, DictDef> defMaps = null;
	
	/**
	 * key为类型值
	 */
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
		init();
	}

	public void init() {
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
		
		dictDef = new DictDef("7", "color", "-1", "颜色分类", "7");
		insertData(dictDef);
		
		dictDef = new DictDef("8", "account_info_type", "-1", "帐号信息类型", "8");
		insertData(dictDef);
		
		dictDef = new DictDef("9", "user_info_dynamic", "-1", "用户附加动态信息", "9");
		insertData(dictDef);
		
		dictDef = new DictDef("10", "social_weibo_type", "-1", "微博类型", "10");
		insertData(dictDef);
		
		dictDef = new DictDef("11", "social_weibo_status", "-1", "微博状态", "11");
		insertData(dictDef);
		
		dictDef = new DictDef("15", "user_weibo_authority", "-1", "用户微博的权限", "15");
		insertData(dictDef);
		
		dictDef = new DictDef("16", "resources_team_type", "-1", "组的类型", "16");
		insertData(dictDef);
		
		dictDef = new DictDef("17", "outsourcing_type", "-1", "外包分类", "17");
		insertData(dictDef);
		
		dictDef = new DictDef("18", "project_plan_authority", "-1", "项目计划的权限", "18");
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
		
		dictDef = new DictDef("232", "weibo_last_time", "3", "微博最后更新时间", "weibo_last_time");
		dictDef = new DictDef("233", "weibo_first_time", "3", "微博第一次更新时间", "weibo_first_time");
		
		dictDef = new DictDef("234", "my_weibo_last_time", "3", "微博最后更新时间", "my_weibo_last_time");
		dictDef = new DictDef("235", "my_weibo_first_time", "3", "微博第一次更新时间", "my_weibo_first_time");
		//记录数据库@我的最后时间
		dictDef = new DictDef("236", "at_weibo_last_time", "3", "微博最后更新时间", "at_weibo_last_time");
		dictDef = new DictDef("237", "at_weibo_first_time", "3", "微博第一次更新时间", "at_weibo_first_time");
		
		dictDef = new DictDef("238", "account_data_lock", "3", "帐号数据锁定", "account_data_lock");
		insertData(dictDef);
		
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
		
		
		dictDef = new DictDef("270", "weibo_public_timeline", "6", "全部微博", "0");
		insertData(dictDef);
		
		dictDef = new DictDef("271", "weibo_user_timeline", "6", "我的微博", "1");
		insertData(dictDef);
		
		dictDef = new DictDef("272", "weibo_mentions_timeline", "6", "@我的微博", "2");
		insertData(dictDef);
		
		dictDef = new DictDef("273", "comment_to_me", "6", "我收到的评论", "3");
		insertData(dictDef);
		
		dictDef = new DictDef("274", "comment_by_me", "6", "我发出的评论", "4");
		insertData(dictDef);
		
		dictDef = new DictDef("275", "at_me_comment", "6", "@我的评论", "5");
		insertData(dictDef);
		
		
		dictDef = new DictDef("280", "green", "7", "绿", "1");
		insertData(dictDef);
		dictDef = new DictDef("281", "blue", "7", "蓝", "2");
		insertData(dictDef);
		dictDef = new DictDef("282", "red", "7", "红", "3");
		insertData(dictDef);
		dictDef = new DictDef("283", "orange", "7", "橙", "4");
		insertData(dictDef);
		
		dictDef = new DictDef("290", "user_info_type", "8", "用户信息", "0");
		insertData(dictDef);
		dictDef = new DictDef("291", "user_account_info_type", "8", "用户帐号信息", "1");
		insertData(dictDef);
		
		dictDef = new DictDef("295", "user_last_logged_time", "9", "用户登录最后时间", "user_last_logged_time");
		insertData(dictDef);
		
		
		
		//1-原创发表，2-转载，3-私信，4-回复，5-空回，6-提及，7-评论,
		dictDef = new DictDef("300", "weibo_type_ori_post", "10", "原创发表", "1");
		insertData(dictDef);
		dictDef = new DictDef("301", "weibo_type_reship", "10", "转载", "2");
		insertData(dictDef);
		dictDef = new DictDef("302", "weibo_type_private_letter", "10", "私信", "3");
		insertData(dictDef);
		dictDef = new DictDef("303", "weibo_type_revert", "10", "回复", "4");
		insertData(dictDef);
		dictDef = new DictDef("304", "weibo_type_blank_revert", "10", "空回", "5");
		insertData(dictDef);
		dictDef = new DictDef("305", "weibo_type_mention", "10", "提及", "6");
		insertData(dictDef);
		dictDef = new DictDef("306", "weibo_type_comment", "10", "评论", "7");
		insertData(dictDef);
		
		//status : 微博状态，0-正常，1-系统删除，2-审核中，3-用户删除，4-根删除（根节点被系统审核删除）
		dictDef = new DictDef("400", "weibo_status_normal", "11", "正常", "0");
		insertData(dictDef);
		dictDef = new DictDef("401", "weibo_status_sys_delete", "11", "系统删除", "1");
		insertData(dictDef);
		dictDef = new DictDef("402", "weibo_status_reviewing", "11", "审核中", "2");
		insertData(dictDef);
		dictDef = new DictDef("403", "weibo_status_user_delete", "11", "用户删除", "3");
		insertData(dictDef);
		dictDef = new DictDef("404", "weibo_status_root_delete", "11", "根删除", "4");
		insertData(dictDef);
		
		
		dictDef = new DictDef("500", "weibo_authority_new", "15", "发微博", "0");
		insertData(dictDef);
		
		dictDef = new DictDef("501", "weibo_authority_transmit", "15", "微博转发", "1");
		insertData(dictDef);
		
		dictDef = new DictDef("502", "weibo_authority_revert", "15", "微博回复", "2");
		insertData(dictDef);
		
		dictDef = new DictDef("503", "weibo_authority_comment_revert", "15", "评论回复", "3");
		insertData(dictDef);
		
		dictDef = new DictDef("600", "team_type_weibo_permit", "16", "组的类型-微博权限分配", "0");
		insertData(dictDef);
		
		dictDef = new DictDef("605", "out_ad_originality", "17", "广告创意", "0");
		insertData(dictDef);
		dictDef = new DictDef("606", "out_art_design ", "17", "美工设计", "1");
		insertData(dictDef);
		dictDef = new DictDef("607", "out_system_develop", "17", "系统开发", "2");
		insertData(dictDef);
		
		dictDef = new DictDef("700", "project_authority_update", "18", "修改", "0");
		insertData(dictDef);
		dictDef = new DictDef("701", "project_authority_move", "18", "移动", "1");
		insertData(dictDef);
		dictDef = new DictDef("702", "project_authority_delete", "18", "删除", "2");
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
