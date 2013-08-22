package com.klwork.business.domain.service.infs;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.SocialUserWeiboQuery;
import com.klwork.business.domain.service.SocialUserAccountInfoService;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.business.domain.service.SocialUserWeiboService;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.security.LoginHandler;

public abstract class AbstractSocialService implements SocialService,
		InitializingBean {
	
	@Autowired
	public SocialUserWeiboService socialUserWeiboService;
	
	@Autowired
	public SocialUserAccountInfoService socialUserAccountInfoService;
	
	@Autowired
	public SocialUserAccountService socialUserAccountService;
	
	private static Map<String, AbstractSocialService> socialServiceMaps = new HashMap<String, AbstractSocialService>();

	private static void registerSocial(String roleCode,
			AbstractSocialService abstractSocial) {
		socialServiceMaps.put(roleCode, abstractSocial);
	}

	public void afterPropertiesSet() {
		AbstractSocialService.registerSocial(getSocialType(), this);
	}
	
	/**
	 * 查询指定帐号按照时间排列的最后一条微博
	 * @param ac
	 * @param weiboType
	 * @param weiboUserName
	 * @return
	 */
	protected SocialUserWeibo queryLastSpeWeibo(SocialUserAccount ac, Integer weiboType, String weiboUserName) {
		SocialUserWeiboQuery query = new SocialUserWeiboQuery();
		query.setUserAccountId(ac.getId());
		query.setUserName(weiboUserName);
		query.setWeiboType(weiboType);
		SocialUserWeibo lastWeibo = socialUserWeiboService.queryLastWeibo(query);
		return lastWeibo;
	}

	/**
	 * 我的微博的时间
	 * @param ac
	 * @return
	 */
	public String queryMyWeiboTime(SocialUserAccount ac) {
		String webFetchTime = StringDateUtil.addYear(new Date(), -2).getTime()/1000 + "";
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,null,ac.getName());
		String startTime = StringDateUtil.addDay(new Date(), -1).getTime()/1000 + "";//1347444000
		if(lastWeibo != null){//数据库中最后一条记录
			webFetchTime=  String
				.valueOf(lastWeibo.getCreateAt().getTime() / 1000);
			//webFetchTime = lastTime.getValueDate().getTime()/1000 + "";
		}
		return webFetchTime;
	}

	/**
	 * 在@我的微博的时间
	 * @param ac
	 * @return
	 */
	public String queryMentionWeiboTime(SocialUserAccount ac) {
		String webFetchTime = StringDateUtil.addYear(new Date(), -2).getTime()/1000 + "";
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,DictDef.dictInt("weibo_type_mention"),null);
		if(lastWeibo != null){//数据库中最后一条记录
			webFetchTime=  String
				.valueOf(lastWeibo.getCreateAt().getTime() / 1000);
			//webFetchTime = lastTime.getValueDate().getTime()/1000 + "";
		}
		return webFetchTime;
	}

	/**
	 * 当前用户及所关注用户的最新微博的时间
	 * @param ac
	 * @return
	 */
	public String queryHomeWeiboTime(SocialUserAccount ac) {
		//三天的
		String webFetchTime = StringDateUtil.addDay(new Date(), -1).getTime()/1000 + "";
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,null,null);
		SocialUserAccountInfo lastTime = socialUserAccountInfoService
				.findAccountOfInfoByKey(DictDef.dict("weibo_last_time"), ac.getId());
		if(lastTime != null){//数据库中最后一条记录
			webFetchTime = lastTime.getValueDate().getTime()/1000 + "";
		}
		return webFetchTime;
	}
	
	/**
	 * 社交类型
	 * @return
	 */
	public int getSocialTypeInt() {
		return StringTool.parseInt(getSocialType());
	}
	
	/**
	 * 生成一个新的weibo对象
	 * @param ownerUserId
	 * @param ac
	 * @return
	 */
	protected SocialUserWeibo currentNewSocialUserWeibo(String ownerUserId, SocialUserAccount ac) {
		SocialUserWeibo soruceWeibo = new SocialUserWeibo();
		soruceWeibo.setUserAccountId(ac.getId());
		soruceWeibo.setOwner(ownerUserId);
		soruceWeibo.setType(getSocialTypeInt());
		return soruceWeibo;
	}

	/**
	 * 查询出现注册的service类型
	 * @param key
	 * @return
	 */
	public static AbstractSocialService querySocialClass(String key) {
		AbstractSocialService role = socialServiceMaps.get(key);
		return role;
	}
	
	public  boolean saveWeiboUserEntity(SocialUserWeibo weibo) {
		if(socialUserWeiboService.existWeibo(weibo.getUserAccountId(),weibo.getWeiboId())){
			return false;
		}else{
			socialUserWeiboService.createSocialUserWeibo(weibo);
			return true;
		}
	}
	
	public int myWeiboToDb() {
		String ownerUserId = LoginHandler.getLoggedInUser().getId();
		SocialUserAccount ac = socialUserAccountService.findSocialUserByType(
				ownerUserId,
				getSocialTypeInt());
		return weiboToDb(ac, 1);

	}
	

}
