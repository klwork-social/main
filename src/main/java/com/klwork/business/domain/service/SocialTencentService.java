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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.SocialUserWeiboQuery;
import com.klwork.business.utils.TencentSociaTool;
import com.klwork.common.utils.StringDateUtil;
import com.klwork.explorer.security.LoginHandler;
import com.tencent.weibo.api.StatusesAPI;
import com.tencent.weibo.api.UserAPI;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;

/**
 * The Class SocialSinaService.
 */
@Service
public class SocialTencentService {
	@Autowired
	public SocialUserAccountService socialUserAccountService;

	@Autowired
	IdentityService identityService;

	@Autowired
	UserService userService;

	@Autowired
	private SocialUserAccountInfoService socialUserAccountInfoService;

	@Autowired
	private SocialUserWeiboService socialUserWeiboService;

	public org.activiti.engine.identity.User handlerUserAuthorize(String code,
			String openid, String openkey) {
		org.activiti.engine.identity.User retUser = null;
		try {
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
			String userInfoJson = userAPI.info(oAuth, "json");
			Object userInfoObject = TencentSociaTool
					.getJsonDataObject(userInfoJson);
			if (judgeNull(userInfoObject)) {
				return null;
			}
			JSONObject userInfo = ((JSONObject) userInfoObject);

			SocialUserAccountQuery query = getQuery(userInfo);
			List<SocialUserAccount> list = socialUserAccountService
					.findSocialUserAccountByQueryCriteria(query, null);
			if (list != null && list.size() > 0) {
				SocialUserAccount c = list.get(0);
				retUser = updateSocialInfo(c, userInfo, oAuth);
			} else {
				retUser = addSocialInfo(userInfo, oAuth);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retUser;
	}

	public SocialUserAccountQuery getQuery(JSONObject userInfo) {
		SocialUserAccountQuery query = new SocialUserAccountQuery();
		query.setWeiboUid(userInfo.getString("name"));
		query.setType(DictDef.dictInt("tencent"));
		return query;
	}

	private org.activiti.engine.identity.User updateSocialInfo(
			SocialUserAccount c, JSONObject userInfo, OAuthV2 oAuth) {
		org.activiti.engine.identity.User la = identityService
				.createUserQuery().userId(c.getName()).singleResult();
		// updateSocialInfos(c, accessTokenObj);
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

	private org.activiti.engine.identity.User addSocialInfo(
			JSONObject userInfo, OAuthV2 oAuth) {
		String userId = userInfo.getString("name");
		String nick = userInfo.getString("nick");
		org.activiti.engine.identity.User retUser = userService.createUser(
				userId, nick, null, "123456", null, null,
				Arrays.asList("user"), null);
		SocialUserAccount socialUserAccount = new SocialUserAccount();
		socialUserAccount.setWeiboUid(userId);
		socialUserAccount.setName(nick);
		socialUserAccount.setType(DictDef.dictInt("tencent"));
		socialUserAccount.setOwnUser(userId);
		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		//
		// updateSocialUserInfo(weiboUserInfoBean, socialUserAccount);
		addTokenInfos(oAuth, socialUserAccount);

		return retUser;
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



	public static void outInfo(String msg) {
		System.out.println(msg);
	}

	public int weiboToDb() {
		int updateSize = 0;
		String ownerUserId = LoginHandler.getLoggedInUser().getId();
		SocialUserAccount ac = socialUserAccountService.findSocialUserByType(
				ownerUserId,
				DictDef.dictInt("tencent"));
		if (ac != null) {
			String accountId = ac.getId();
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
			String format = "json";
			String pageflag = "2";// 向上翻页
			String reqnum = "50";

			String contenttype = "0";
			String type = "0";
			JSONArray infoList = null;
			Object responseJsonObject = null;
			String lastid = "1";
			SocialUserWeiboQuery query = new SocialUserWeiboQuery();
			query.setUserAccountId(ac.getId());
			String startTime = StringDateUtil.addDay(new Date(), -1).getTime()/1000 + "";//1347444000
			SocialUserWeibo lastWeibo = socialUserWeiboService.queryLastWeibo(query);
			String pagetime = lastWeibo == null ? startTime : String
					.valueOf(lastWeibo.getCreateAt().getTime() / 1000);
			do {
				String response = "";
				try {
					response = new StatusesAPI(oAuth.getOauthVersion())
							.homeTimeline(oAuth, format, pageflag, pagetime,
									reqnum, type, contenttype);
				} catch (Exception e) {
					e.printStackTrace();
				}
				responseJsonObject = TencentSociaTool
						.getJsonDataObject(response);
				if (judgeNull(responseJsonObject)) {
					break;
				}
				infoList = ((JSONObject) responseJsonObject)
						.getJSONArray("info");
				for (int i = 0; i < infoList.size(); i++) {// 没一条的微博
					JSONObject infoObj = infoList.getJSONObject(i);
					SocialUserWeibo dbWeibo = currentSocialUserWeibo(
							ownerUserId, ac, infoObj);
					
					JSONObject source = infoObj.getJSONObject("source");
					if (source != null && !source.toString().equals("null")) {//有原帖内容
						SocialUserWeibo soruceWeibo = currentSocialUserWeibo(
								ownerUserId, ac, source);
						socialUserWeiboService.createSocialUserWeibo(soruceWeibo);
						dbWeibo.setRetweetedId(soruceWeibo.getId());
					}
					socialUserWeiboService.createSocialUserWeibo(dbWeibo);
					
					
					if(i == 0 ){
						pagetime = infoObj.getString("timestamp");
					}
				}
				updateSize += infoList.size();
			} while (infoList != null && infoList.size() > 0
					&& !judgeNull(responseJsonObject));
		}
		return updateSize;

	}

	public SocialUserWeibo currentSocialUserWeibo(String ownerUserId,
			SocialUserAccount ac, JSONObject source) {
		SocialUserWeibo soruceWeibo =  new SocialUserWeibo();
		convertTecentJsonInfoToDbWb(source, soruceWeibo);
		soruceWeibo.setUserAccountId(ac.getId());
		soruceWeibo.setOwner(ownerUserId);
		soruceWeibo.setType(DictDef.dictInt("tencent"));
		return soruceWeibo;
	}
	
	public int myWeiboToDb() {
		int updateSize = 0;
		String ownerUserId = LoginHandler.getLoggedInUser().getId();
		SocialUserAccount ac = socialUserAccountService.findSocialUserByType(
				ownerUserId,
				DictDef.dictInt("tencent"));
		if (ac != null) {
			String accountId = ac.getId();
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
			String format = "json";
			String pageflag = "2";// 0：第一页，1：向下翻页，2向上翻页
			String reqnum = "50";

			String contenttype = "0";//内容过滤。0-表示所有类型，1-带文本，2-带链接，4-带图片，8-带视频，0x10-带音频
			String type = "0";//0：所有类型，0x1：原创发表，0x2：转播
			JSONArray infoList = null;
			Object responseJsonObject = null;
			String lastid = "0";//和pagetime配合使用（第一页：填0，向上翻页：填上一次请求返回的第一条记录id，向下翻页：填上一次请求返回的最后一条记录id）
			String pagetime = "0";//本页起始时间（第一页：填0，向上翻页：填上一次请求返回的第一条记录时间，向下翻页：填上一次请求返回的最后一条记录时间）
			SocialUserWeiboQuery query = new SocialUserWeiboQuery();
			query.setUserAccountId(ac.getId());
			String startTime = StringDateUtil.addDay(new Date(), -3).getTime()/1000 + "";//1347444000
			SocialUserWeibo lastWeibo = socialUserWeiboService.queryLastWeibo(query);
			if(lastWeibo != null){
				pagetime = lastWeibo.getCreateAt().getTime()/1000 + "";//
				lastid = lastWeibo.getWeiboId();
			}else {
				pagetime = startTime;
			}
			System.out.println(pagetime + "&&&&&&&&" + lastid);
			do {
				String response = "";
				try {
					response = new StatusesAPI(oAuth.getOauthVersion())
							.broadcastTimeline(oAuth, format, pageflag, pagetime,
									reqnum, lastid,type, contenttype);
				} catch (Exception e) {
					e.printStackTrace();
				}
				responseJsonObject = TencentSociaTool
						.getJsonDataObject(response);
				if (judgeNull(responseJsonObject) || ((JSONObject) responseJsonObject).get("info") == null) {
					break;
				}
				infoList = ((JSONObject) responseJsonObject)
						.getJSONArray("info");
				for (int i = 0; i < infoList.size(); i++) {// 没一条的微博
					JSONObject infoObj = infoList.getJSONObject(i);
					SocialUserWeibo dbWeibo = currentSocialUserWeibo(
							ownerUserId, ac, infoObj);
					if(i == 0 ){//
						pagetime = infoObj.getString("timestamp");
						lastid = infoObj.getString("id");
					}
					JSONObject source = infoObj.getJSONObject("source");
					if (source != null && !(source.toString().equals("null"))) {//有原帖内容
						SocialUserWeibo soruceWeibo = currentSocialUserWeibo(
								ownerUserId, ac, source);
						try {
							socialUserWeiboService.createSocialUserWeibo(soruceWeibo);
						}catch(Exception e){
							e.printStackTrace();
							System.out.println("sub:" + soruceWeibo.getUserAccountId() + "--" +  soruceWeibo.getWeiboId());
							continue;
						}
						dbWeibo.setRetweetedId(soruceWeibo.getId());
					}
					
					try{
					socialUserWeiboService.createSocialUserWeibo(dbWeibo);
					}catch(Exception e){
						e.printStackTrace();
						System.out.println("" +dbWeibo.getUserAccountId() + "--" +  dbWeibo.getWeiboId());
						continue;
					}
					
					//System.out.println(infoObj.getString("timestamp") + "&&&&&&&&" + infoObj.getString("id"));
				
				}
				updateSize += infoList.size();
			} while (infoList != null && infoList.size() > 0
					&& !judgeNull(responseJsonObject));
		}
		return updateSize;

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

	private SocialUserWeibo convertTecentJsonInfoToDbWb(JSONObject jsonInfo,
			SocialUserWeibo weibo) {
		if (weibo == null) {
			weibo = new SocialUserWeibo();
			weibo.setCreateAt(new Date());
		}

		String weiboId = jsonInfo.getString("id");
		System.out.println(" weibId " + weiboId);
		weibo.setWeiboId(weiboId);// 微博唯一id
		weibo.setCreateAt(new Date(jsonInfo.getLong("timestamp") * 1000));
		
		weibo.setSource(jsonInfo.getString("from"));// 来源
		weibo.setUserDomain(jsonInfo.getString("fromurl"));// 来源
		
		// 微博内容
		weibo.setText(jsonInfo.getString("text"));
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
		
		weibo.setWeiboType(jsonInfo.getInt("type"));
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
				System.out.println(infoObj);
				weibo.setOriginalPic(infoObj.toString());
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

}