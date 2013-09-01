package com.klwork.business.domain.service.infs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.SocialUserWeiboComment;
import com.klwork.business.domain.model.SocialUserWeiboCommentQuery;
import com.klwork.business.domain.model.SocialUserWeiboQuery;
import com.klwork.business.domain.model.SocialUserWeiboSend;
import com.klwork.business.domain.model.WeiboForwardSend;
import com.klwork.business.domain.service.SocialUserAccountInfoService;
import com.klwork.business.domain.service.SocialUserAccountService;
import com.klwork.business.domain.service.SocialUserWeiboCommentService;
import com.klwork.business.domain.service.SocialUserWeiboSendService;
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
	
	@Autowired
	public SocialUserWeiboSendService socialUserWeiboSendService;
	
	@Autowired
	SocialUserWeiboCommentService socialUserWeiboCommentService;
	
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
	 * 查询指定帐号按照时间排列的最后一条评论
	 * @param ac
	 * @param commentType
	 * @param weiboUserName
	 * @return
	 */
	protected SocialUserWeiboComment queryLastComment(SocialUserAccount ac, Integer commentType, String weiboUserName) {
		SocialUserWeiboCommentQuery query = new SocialUserWeiboCommentQuery();
		query.setUserAccountId(ac.getId());
		query.setUserName(weiboUserName);
		query.setCommentType(commentType);
		SocialUserWeiboComment lastComment = socialUserWeiboCommentService.queryLastComment(query);
		return lastComment;
	}
	
	

	/**
	 * 我的微博的时间
	 * @param ac
	 * @return
	 */
	public String queryMyWeiboTime(SocialUserAccount ac) {
		String webFetchTime = StringDateUtil.addYear(new Date(), -2).getTime()/1000 + "";
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,DictDef.dictInt("weibo_user_timeline"),ac.getName());
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
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,DictDef.dictInt("weibo_mentions_timeline"),null);
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
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,DictDef.dictInt("weibo_public_timeline"),null);
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
	 * @param weiboType 
	 * @return
	 */
	protected SocialUserWeibo currentNewSocialUserWeibo(String ownerUserId, SocialUserAccount ac, Integer weiboType) {
		SocialUserWeibo soruceWeibo = new SocialUserWeibo();
		soruceWeibo.setUserAccountId(ac.getId());
		soruceWeibo.setOwner(ownerUserId);
		soruceWeibo.setType(getSocialTypeInt());
		soruceWeibo.setWeiboType(weiboType);
		soruceWeibo.setWeiboHandleType(0);//表明正常帖
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
		return handleUserAccountWeibo(ac);

	}
	
	/**
	 * 处理指定帐号的微博信息
	 * @param socialUserAccount
	 */
	public int handleUserAccountWeibo(SocialUserAccount socialUserAccount) {
		String key = "account_data_lock";
		SocialUserAccountInfo lock = socialUserAccountInfoService
				.findAccountOfInfoByKey(DictDef.dict(key),
						socialUserAccount.getId());
		if(lock != null && StringTool.parseBoolean(lock.getValue())){//如果锁定直接退出
			return 0;
		}
		socialUserAccountInfoService.setSocialUserAccountInfo(socialUserAccount, key, "1");//锁定
		int ret = weiboToDb(socialUserAccount);
		socialUserAccountInfoService.setSocialUserAccountInfo(socialUserAccount, key, "0");//解除锁定
		return ret;
	}

	public void saveSendWeiboRecord(SocialUserAccount socialUserAccount,
			String text, String type) {
		SocialUserWeiboSend s = new SocialUserWeiboSend();
		Date now = StringDateUtil.now();
		s.setCreateTime(now);
		s.setText(text);
		if(text.length() > 50){
			s.setShortText(text.substring(0,50));
		}else {
			s.setShortText(text);
		}
		s.setType(StringTool.parseInt(type));
		s.setUserAccountId(socialUserAccount.getId());
		s.setStatus(1);//已经完成
		s.setOwner(socialUserAccount.getOwnUser());
		socialUserWeiboSendService.createSocialUserWeiboSend(s);
	}
	
	public String findAccessTokenByAccout(String accountId) {
		SocialUserAccountInfo tok = socialUserAccountInfoService
				.findAccountOfInfoByKey(DictDef.dict("accessToken"),
						accountId);
		String assessToken = tok.getValue();
		return assessToken;
	}

	public List<Map<String, String>> queryFaces() {
		return null;
	}


	
}
