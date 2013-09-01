/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.klwork.business.domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.SocialUserWeiboComment;
import com.klwork.business.domain.model.WeiboHandleResult;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.business.utils.TencentSociaTool;
import com.klwork.common.exception.ApplicationException;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ui.util.ImageUtil;
import com.tencent.weibo.api.AddParameter;
import com.tencent.weibo.api.StatusesAPI;
import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.api.TimelineParameter;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;

/**
 * The Class SocialSinaService.
 */
@Service
public class SocialTencentService extends AbstractSocialService {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	public SocialUserAccountService socialUserAccountService;

	@Autowired
	IdentityService identityService;

	@Autowired
	UserService userService;

	@Autowired
	public SocialUserAccountInfoService socialUserAccountInfoService;

	@Autowired
	public SocialUserWeiboService socialUserWeiboService;
	
	@Autowired
	SocialUserWeiboCommentService socialUserWeiboCommentService;
	
	@Override
	public Map queryUserInfoByCode(String code,
			String openid, String openkey) {
		HashMap map = new HashMap();
		OAuthV2 oAuth = TencentSociaTool.getQQAuthV2();
		oAuth.setAuthorizeCode(code);
		oAuth.setOpenid(openid);
		oAuth.setOpenkey(openkey);

		String accessToken = null, userOpenID = null;
		long tokenExpireIn = 0L;

		// 换取access token
		oAuth.setGrantType("authorize_code");
		try {
			OAuthV2Client.accessToken(oAuth);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// 检查是否正确取得access token
		if (oAuth.getStatus() == 3) {
			System.out.println("Get Access Token failed!");
			return null;
		}

		UserAPI userAPI = new UserAPI(oAuth.getOauthVersion());
		String userInfoJson = "";
		try {
			userInfoJson = userAPI.info(oAuth, "json");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Object userInfoObject = TencentSociaTool
				.getJsonDataObject(userInfoJson);
		if (judgeNull(userInfoObject)) {
			return null;
		}
		
		JSONObject userInfo = ((JSONObject) userInfoObject);
		map.put("user", userInfo);
		map.put("token", accessToken);
		map.put("oAuth", oAuth);
		map.put("code", code);
		return map;
	}
	
	@Override
	public org.activiti.engine.identity.User handlerUserAuthorize(Map map) {
		OAuthV2 oAuth = (OAuthV2) map.get("oAuth");
		JSONObject userInfo = (JSONObject) map.get("user");
		org.activiti.engine.identity.User retUser = null;
		try {
			SocialUserAccountQuery query = getQuery(userInfo);
			List<SocialUserAccount> list = socialUserAccountService
					.findSocialUserAccountByQueryCriteria(query, null);
			if (list != null && list.size() > 0) {
				SocialUserAccount c = list.get(0);
				retUser = updateSocialInfo(c, userInfo, oAuth);
			} else {
				//retUser = addSocialInfo(userInfo, oAuth);
			}
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage());
		}
		return retUser;
	}

	public SocialUserAccountQuery getQuery(JSONObject userInfo) {
		SocialUserAccountQuery query = new SocialUserAccountQuery();
		query.setName(userInfo.getString("name"));
		query.setType(getSocialTypeInt());
		return query;
	}

	private org.activiti.engine.identity.User updateSocialInfo(
			SocialUserAccount c, JSONObject userInfo, OAuthV2 oAuth) {
		org.activiti.engine.identity.User la = identityService
				.createUserQuery().userId(c.getOwnUser()).singleResult();
		// updateSocialInfos(c, accessTokenObj);
		updateSocialUserAccountByThird(c,userInfo);
		updateSocialUserInfo(userInfo, c);
		addTokenInfos(oAuth, c);
		return la;
	}

	private void addTokenInfos(OAuthV2 oAuth,
			SocialUserAccount socialUserAccount) {
		SocialUserAccountInfo info = newAccountInfo(socialUserAccount);
		info.setKey("accessToken");
		info.setValue(oAuth.getAccessToken());
		info.setValueType(DictDef.dictInt("string"));
		socialUserAccountInfoService.createSocialUserAccountInfo(info);

		String expireIn = oAuth.getExpiresIn();
		// 失效时间
		Date expiredTime = StringDateUtil.addSecond(new Date(),
				Integer.parseInt(expireIn));
		SocialUserAccountInfo expireInfo = newAccountInfo(socialUserAccount);
		expireInfo.setKey("expiredTime");
		expireInfo.setValue(expireIn);
		expireInfo.setValueDate(expiredTime);
		expireInfo.setValueType(DictDef.dictInt("date"));
		socialUserAccountInfoService.createSocialUserAccountInfo(expireInfo);

		SocialUserAccountInfo oInfo = newAccountInfo(socialUserAccount);
		oInfo.setKey("openId");
		oInfo.setValue(oAuth.getOpenid());
		oInfo.setValueType(DictDef.dictInt("string"));
		socialUserAccountInfoService.createSocialUserAccountInfo(oInfo);

		oInfo = newAccountInfo(socialUserAccount);
		oInfo.setKey("openKey");
		oInfo.setValue(oAuth.getOpenkey());
		oInfo.setValueType(DictDef.dictInt("string"));
		socialUserAccountInfoService.createSocialUserAccountInfo(oInfo);
		
		//refresh_token
		oInfo = newAccountInfo(socialUserAccount);
		oInfo.setKey("refreshToken");
		oInfo.setValue(oAuth.getRefreshToken());
		oInfo.setValueType(DictDef.dictInt("string"));
		socialUserAccountInfoService.createSocialUserAccountInfo(oInfo);
	}



	public void updateSocialUserInfo(JSONObject userInfo,
			SocialUserAccount socialUserAccount) {
		SocialUserAccountInfo info = new SocialUserAccountInfo();
		info.setType(DictDef.dict("tencent"));
		info.setValue(userInfo.getString("fansnum"));
		info.setKey(DictDef.dict("followersCount"));
		info.setValueType(DictDef.dictInt("int"));
		info.setAccountId(socialUserAccount.getId());
		socialUserAccountInfoService.createSocialUserAccountInfo(info);
	}

	public SocialUserAccountInfo newAccountInfo(
			SocialUserAccount socialUserAccount) {
		SocialUserAccountInfo info = new SocialUserAccountInfo();
		info.setType(DictDef.dict("tencent"));
		info.setValueType(DictDef.dictInt("string"));
		info.setAccountId(socialUserAccount.getId());
		return info;
	}


	public SocialUserWeibo handlerNewSocialUserWeibo(String ownerUserId,
			SocialUserAccount ac, JSONObject source, Integer weiboType) {
		SocialUserWeibo soruceWeibo = currentNewSocialUserWeibo(ownerUserId, ac,weiboType);
		convertThirdToWeiboEntity(soruceWeibo,source);
		return soruceWeibo;
	}
	
	
	/**
	 * 把微博保存到数据库
	 * @param ac
	 * @param weiType
	 * @return
	 */
	@Override
	public int weiboToDb(SocialUserAccount ac) {
		String ownerUserId = ac.getOwnUser();
		int updateSize = 0;
		if (ac != null) {
			String accountId = ac.getId();
			OAuthV2 oAuth = queryAccountToken(accountId);
			
			//我的微博
			String webFetchTime = queryMyWeiboTime(ac);
			TimelineParameter pBroadcastTime = new TimelineParameter(oAuth);
			pBroadcastTime.pagetime = webFetchTime;
			//pBroadcastTime.reqnum = "2";
			myWeiboToDb(ac, oAuth,pBroadcastTime);
			
			//@我的微博
			TimelineParameter pBroadcastTime2 = new TimelineParameter(oAuth);
			String webFetchTime2 = queryMentionWeiboTime(ac);
			pBroadcastTime2.pagetime = webFetchTime2;
			mentionsWeiboToDb(ac, oAuth,pBroadcastTime2);
			
			//我关注的所有人的微博
			TimelineParameter pBroadcastTime3 = new TimelineParameter(oAuth);
			String webFetchTime3 = queryHomeWeiboTime(ac);
			pBroadcastTime3.pagetime = webFetchTime3;
			homeWeiboToDb(ac, oAuth,pBroadcastTime3,false);
		}
		return updateSize;
	}
	
	/**
	 * 最近的微博
	 * @param ac
	 * @param oAuth
	 * @param pBroadcastTime
	 */
	public void latestWeiboToDb(SocialUserAccount ac, OAuthV2 oAuth,TimelineParameter pBroadcastTime) {
			String response = "";
			try {
				response = new StatusesAPI(oAuth.getOauthVersion())
						.broadcastTimeline(pBroadcastTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringTool.judgeBlank(response)){
				WeiboHandleResult r = weiboSaveDb(response,ac,DictDef.dictInt("weibo_public_timeline"));
				if(r.isSuccess() && r.getInfoSize() >0){
					pBroadcastTime.lastid = r.getLastid();
					pBroadcastTime.pagetime = r.getPagetime();
					latestWeiboToDb(ac,oAuth,pBroadcastTime);
				}
			}
	}
	
	/**
	 * 我发表的微博
	 * @param ac
	 * @param oAuth
	 * @param pBroadcastTime
	 */
	public void myWeiboToDb(SocialUserAccount ac, OAuthV2 oAuth,TimelineParameter pBroadcastTime) {
			String response = "";
			try {
				response = new StatusesAPI(oAuth.getOauthVersion())
						.broadcastTimeline(pBroadcastTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringTool.judgeBlank(response)){
				WeiboHandleResult r = weiboSaveDb(response,ac,DictDef.dictInt("weibo_user_timeline"));
				if(r.isSuccess() && r.getInfoSize() >0){
					pBroadcastTime.lastid = r.getLastid();
					pBroadcastTime.pagetime = r.getPagetime();
					myWeiboToDb(ac,oAuth,pBroadcastTime);
				}
			}
	}
	
	/**
	 * @我的微博
	 * @param ac
	 * @param oAuth
	 * @param pBroadcastTime
	 */
	public void mentionsWeiboToDb(SocialUserAccount ac, OAuthV2 oAuth,TimelineParameter pBroadcastTime) {
			String response = "";
			try {
				response = new StatusesAPI(oAuth.getOauthVersion())
						.mentionsTimeline(pBroadcastTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringTool.judgeBlank(response)){
				WeiboHandleResult r = weiboSaveDb(response,ac,DictDef.dictInt("weibo_mentions_timeline"));
				if(r.isSuccess() && r.getInfoSize() >0){
					pBroadcastTime.lastid = r.getLastid();
					pBroadcastTime.pagetime = r.getPagetime();
					mentionsWeiboToDb(ac,oAuth,pBroadcastTime);
				}
			}
	}
	
	/**
	 * 我的所有微博
	 * @param ac
	 * @param oAuth
	 * @param pBroadcastTime
	 */
	public void homeWeiboToDb(SocialUserAccount ac, OAuthV2 oAuth,TimelineParameter pBroadcastTime,boolean sign) {
			String response = "";
			try {
				response = new StatusesAPI(oAuth.getOauthVersion())
						.homeTimeline(pBroadcastTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(StringTool.judgeBlank(response)){
				WeiboHandleResult r = weiboSaveDb(response,ac,DictDef.dictInt("weibo_public_timeline"));
				if(r.isSuccess() && r.getInfoSize() >0){
					pBroadcastTime.lastid = r.getLastid();
					pBroadcastTime.pagetime = r.getPagetime();
					if(!sign){//保存微博的最后时间
						sign = true;
						SocialUserAccountInfo info = new SocialUserAccountInfo();
						info.setKey("weibo_last_time");//微博最后更新时间
						Date d = new Date(Long.parseLong(r.getPagetime()) * 1000);
						info.setAccountId(ac.getId());
						info.setType(DictDef.dict("user_account_info_type"));//帐号类型
						info.setValue(StringDateUtil.parseString(d, 4));
						info.setValueType(DictDef.dictInt("date"));
						info.setValueDate(d);
						socialUserAccountInfoService.createSocialUserAccountInfo(info);
					}
					homeWeiboToDb(ac,oAuth,pBroadcastTime,sign);
				}
			}
	}
	
	public OAuthV2 queryAccountToken(String accountId) {
		//取数据库中的accesToken和openId
		SocialUserAccountInfo tok = socialUserAccountInfoService
				.findAccountOfInfoByKey(DictDef.dict("accessToken"),
						accountId);
		String assessToken = tok.getValue();
		SocialUserAccountInfo openIdInfo = socialUserAccountInfoService
				.findAccountOfInfoByKey(DictDef.dict("openId"), accountId);
		String openId = openIdInfo.getValue();
		
		
		OAuthV2 oAuth = TencentSociaTool.getQQAuthV2();
		oAuth.setAccessToken(assessToken);
		oAuth.setOpenid(openId);
		return oAuth;
	}
	
	/**
	 * 将微博内容放到数据库
	 * @param response
	 * @param ac
	 * @return
	 */
	private WeiboHandleResult weiboSaveDb(String response,SocialUserAccount ac,Integer weiboType) {
		WeiboHandleResult result = new WeiboHandleResult();
		JSONArray infoList = null;
		String ownerUserId = ac.getOwnUser();
		Object responseJsonObject = TencentSociaTool
				.getJsonDataObject(response);
		if (judgeNull(responseJsonObject) || ((JSONObject) responseJsonObject).get("info") == null) {
			result.setSuccess(false);
			return result;
		}
		infoList = ((JSONObject) responseJsonObject)
				.getJSONArray("info");
		boolean sign = false;
		for (int i = 0; i < infoList.size(); i++) {// 没一条的微博
			JSONObject infoObj = infoList.getJSONObject(i);
			SocialUserWeibo dbWeibo = handlerNewSocialUserWeibo(
					ownerUserId, ac, infoObj,weiboType);
			logger.info(i + "+++timestamp:" + infoObj.getString("timestamp") + "++++++++++++++++" +  "id:" + infoObj.getString("id"));
			if(i == 0 ){//
				result.setPagetime(infoObj.getString("timestamp"));
				result.setLastid(infoObj.getString("id"));
			}
			String type = infoObj.getString("type");
			SocialUserWeibo soruceWeibo = null;
			JSONObject source = infoObj.getJSONObject("source");
			if (source != null && !(source.toString().equals("null"))) {//有原帖内容
				soruceWeibo = handlerNewSocialUserWeibo(
						ownerUserId, ac, source,weiboType);
				soruceWeibo.setWeiboHandleType(1);//表明为原始贴
				if(saveWeiboUserEntity(soruceWeibo)){//插入原帖id
					dbWeibo.setRetweetedId(soruceWeibo.getId());
				}else {
					SocialUserWeibo queryWeibo = socialUserWeiboService.queryByAccountAndWeiboId(soruceWeibo.getUserAccountId(),soruceWeibo.getWeiboId());
					dbWeibo.setRetweetedId(queryWeibo.getId());
				}
			}
			
			if(type.equals("7")){//7-评论
				dbWeibo.setWeiboHandleType(2);//表明为品论贴
				SocialUserWeiboComment comment = tranlateWeiboToComment(dbWeibo,soruceWeibo,weiboType);
				initWeiboCommentByAccount(comment,ac);
				socialUserWeiboCommentService.createSocialUserWeiboComment(comment);
			}
			
			//插进本帖
			saveWeiboUserEntity(dbWeibo);
		}
		if(infoList != null){//记录数
			result.setInfoSize(infoList.size());
		}
		result.setSuccess(true);
		return result;
	}
	
	private void initWeiboCommentByAccount(SocialUserWeiboComment socialUserWeiboComment, SocialUserAccount ac) {
		socialUserWeiboComment.setUserAccountId(ac.getId());
		socialUserWeiboComment.setOwner(ac.getOwnUser());
		socialUserWeiboComment.setType(ac.getType());
	}
	
	private SocialUserWeiboComment tranlateWeiboToComment(
			SocialUserWeibo dbWeibo, SocialUserWeibo soruceWeibo, int weiboType) {
		SocialUserWeiboComment myComment = new SocialUserWeiboComment();
		myComment.setCommentId(dbWeibo.getWeiboId());
		int factWeiboType = -1;
		if(DictDef.dictInt("weibo_mentions_timeline") == weiboType){
			factWeiboType = DictDef.dictInt("comment_to_me");
		}
		if(DictDef.dictInt("weibo_user_timeline") == weiboType){//我的微博就是我发出的品论
			factWeiboType = DictDef.dictInt("comment_by_me");
		}
		myComment.setCommentType(factWeiboType);
		myComment.setCommentUid(dbWeibo.getWeiboUid());
		myComment.setCreateAt(dbWeibo.getCreateAt());
		myComment.setMid(dbWeibo.getMid());
		myComment.setSource(dbWeibo.getSource());
		myComment.setText(dbWeibo.getText());
		myComment.setUserDomain(dbWeibo.getUserDomain());
		myComment.setUserName(dbWeibo.getUserName());
		myComment.setUserProfileImageUrl(dbWeibo.getUserProfileImageUrl());
		myComment.setUserScreenName(dbWeibo.getUserScreenName());
		myComment.setUserVerified(dbWeibo.getUserVerified());
		if(soruceWeibo == null){
			return myComment;
		}
		myComment.setStatusCreatedAt(soruceWeibo.getCreateAt());
		myComment.setStatusMid(soruceWeibo.getMid());
		myComment.setStatusSource(soruceWeibo.getSource());
		//myComment.setStatusSourceUrl(comment.getStatus().getSource().getUrl());
		myComment.setStatusText(soruceWeibo.getText());
		myComment.setStatusUserDomain(soruceWeibo.getUserDomain());
		myComment.setStatusUserName(soruceWeibo.getUserName());
		myComment.setStatusUserScreenName(soruceWeibo.getUserScreenName());
		myComment.setStatusUserVerified(soruceWeibo.getUserVerified());
		myComment.setStatusWeiboId(soruceWeibo.getWeiboId());
		System.out.println(soruceWeibo.getWeiboUid()   + "----" + dbWeibo.getWeiboUid());
		myComment.setStatusUserUid(soruceWeibo.getWeiboUid());
		return myComment;
	}

	public boolean judgeNull(Object responseJsonObject) {
		if(responseJsonObject == null){
			return true;
		}
		if(responseJsonObject instanceof net.sf.json.JSONNull){
			return true;
		}
		if(responseJsonObject instanceof net.sf.json.JSONObject){
			return ((JSONObject)responseJsonObject).isNullObject();
		}
		return false;
	}
	
	@Override
	public <T> SocialUserWeibo convertThirdToWeiboEntity(SocialUserWeibo weibo,T thirdInfo
			) {
		JSONObject jsonInfo = (JSONObject)thirdInfo;
		if (weibo == null) {
			weibo = new SocialUserWeibo();
			weibo.setCreateAt(new Date());
		}

		String weiboId = jsonInfo.getString("id");
		System.out.println(" weibId " + weiboId);
		weibo.setWeiboId(weiboId);// 微博唯一id
		weibo.setCreateAt(new Date(jsonInfo.getLong("timestamp") * 1000));
		
		weibo.setSource(jsonInfo.getString("from"));// 来源
		weibo.setSourceUrl(jsonInfo.getString("fromurl"));
		weibo.setUserDomain(jsonInfo.getString("fromurl"));// 来源
		
		// 微博内容
		weibo.setText(jsonInfo.getString("origtext"));
		//System.out.println(jsonInfo.getString("text") + "&&*&&&&");
		//System.out.println(jsonInfo.getString("origtext"));
		weibo.setUserVerified(jsonInfo.getInt("isvip"));
		
		
	/*	System.out.println(jsonInfo.getInt("type")  + "--source---" + jsonInfo.getString("origtext"));
		if(jsonInfo.getString("source") != null){
			System.out.println("&&&&  99999----" + jsonInfo.getString("source"));
		}*/
		// 转发数
		weibo.setRepostsCount(jsonInfo.getInt("count"));
		// 评论数
		weibo.setCommentsCount(jsonInfo.getInt("mcount"));
		//微博类型 type : 微博类型，1-原创发表，2-转载，3-私信，4-回复，5-空回，6-提及，7-评论
		
		//weibo.setWeiboType(jsonInfo.getInt("type"));
		//用户粉丝说
		//weibo.setWeiboUidFollower(weiboUidFollower);
		// 微博userid
		weibo.setWeiboUid(jsonInfo.getString("openid"));
		weibo.setUserName(jsonInfo.getString("name"));//发表人账号
		weibo.setUserScreenName(jsonInfo.getString("nick"));
		weibo.setUserProfileImageUrl(jsonInfo.getString("head"));
		//图片
		Object t = jsonInfo.get("image");
		if(!judgeNull(t)){
		JSONArray arrays  = jsonInfo.getJSONArray("image");
			for (int i = 0; i < arrays.size(); i++) {// 没一条的微博
				Object infoObj = arrays.get(i);
				//System.out.println(infoObj);
				String thumbnailPic = infoObj.toString() + "/120";
				weibo.setThumbnailPic(thumbnailPic);
				if(StringTool.judgeBlank(thumbnailPic)){//取图片的大小
					String sizeStr = ImageUtil.queryURLImageSize(thumbnailPic);
					weibo.setThumbnailPicSize(sizeStr);
				}
				
				String bmiddlePic = infoObj.toString() + "/2000";
				weibo.setBmiddlePic(bmiddlePic);
				if(StringTool.judgeBlank(bmiddlePic)){//取图片的大小
					String sizeStr = ImageUtil.queryURLImageSize(bmiddlePic);
					weibo.setBmiddlePicSize(sizeStr);
				}
				
				String originalPic = infoObj.toString() + "/460";
				weibo.setOriginalPic(originalPic);
				if(StringTool.judgeBlank(originalPic)){//取图片的大小
					String sizeStr = ImageUtil.queryURLImageSize(originalPic);
					weibo.setOriginalPicSize(sizeStr);
				}
			}
		}
		
		//JSONObject source = jsonInfo.getJSONObject("source");
		/*
		 * if (source.toString().equals("null")) {
		 * weibo.setWeiboType(DictDef.dictInt("at_me_status"));//@我的 } else { if
		 * (jsonInfo.getString("text").indexOf("@") > -1) {
		 * weibo.setWeiboType(DictDef.dictInt("at_me_comment")); } else {
		 * weibo.setWeiboType(DictDef.dictInt("comment_to_me")); }
		 * SocialUserWeibo subWeibo =
		 * socialUserWeiboService.newSocialUserWeibo();
		 * subWeibo.setUserName(source.getString("name"));
		 * subWeibo.setWeiboId(source.getString("id"));
		 * subWeibo.setText(source.getString("text"));
		 * subWeibo.setRetweetedId(weibo.getId()); }
		 */

		return weibo;
	}
	
	@Override
	public User initUserByThirdUser(Map thirdUserMap) {
		JSONObject userInfo = (JSONObject) thirdUserMap.get("user");
		String userId = userInfo.getString("name");
		String nick = userInfo.getString("nick");
		org.activiti.engine.identity.User user = identityService
				.newUser(userId);
		user.setFirstName(nick);
		return user;
	}
	
	public void addTencentSocialInfo(org.activiti.engine.identity.User user, HashMap thirdUserMap) {
		
		OAuthV2 oAuth = (OAuthV2) thirdUserMap.get("oAuth");
		JSONObject userInfo = (JSONObject) thirdUserMap.get("user");
		
		SocialUserAccount socialUserAccount = addSocialUserAccountByThird(user,
				userInfo);
		addTokenInfos(oAuth, socialUserAccount);
		
	}
	public SocialUserAccount addSocialUserAccountByThird(
			org.activiti.engine.identity.User user, JSONObject userInfo) {
		String name = userInfo.getString("name");
		String nick = userInfo.getString("nick");
		String uid = userInfo.getString("openid");
		String url = userInfo.getString("homepage");
		
		SocialUserAccount socialUserAccount = new SocialUserAccount();
		socialUserAccount.setWeiboUid(uid);
		socialUserAccount.setName(name);
		socialUserAccount.setUrl(url);
		socialUserAccount.setUserScreenName(nick);
		socialUserAccount.setType(getSocialTypeInt());
		socialUserAccount.setOwnUser(user.getId());
		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		return socialUserAccount;
	}
	
	public void updateSocialUserAccountByThird(
			SocialUserAccount socialUserAccount, JSONObject userInfo) {
		String name = userInfo.getString("name");
		String nick = userInfo.getString("nick");
		String uid = userInfo.getString("openid");
		String url = userInfo.getString("homepage");
		socialUserAccount.setWeiboUid(uid);
		socialUserAccount.setName(name);
		socialUserAccount.setUrl(url);
		socialUserAccount.setUserScreenName(nick);
		socialUserAccount.setType(getSocialTypeInt());
		socialUserAccountService.updateSocialUserAccount(socialUserAccount);
	}
	
	@Override
	public String getSocialType() {
		return "1";
	}

	@Override
	public void deleteWeibo(SocialUserWeibo userWeibo) {
		
	}

	@Override
	public void sendWeibo(SocialUserAccount socialUserAccount, String text,
			String type) {
		String accountId = socialUserAccount.getId();
		OAuthV2 oAuth = queryAccountToken(accountId);
		int ret = 0;
		TAPI tAPI=new TAPI(oAuth.getOauthVersion());//根据oAuth配置对应的连接管理器
		try {
			String response=tAPI.add(new AddParameter(oAuth, text));
			 ret = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(ret == 1){
			saveSendWeiboRecord(socialUserAccount, text, type);
		}
	}

}