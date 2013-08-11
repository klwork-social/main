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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.utils.SinaSociaTool;
import com.klwork.business.utils.SocialConfig;
import com.klwork.common.DataBaseParameters;
import com.klwork.common.dto.vo.ViewPage;
import com.klwork.common.utils.ReflectionUtils;

/**
 * The Class SocialSinaService.
 */
@Service
public class SocialSinaService {

	

	@Autowired
	public SocialUserAccountService socialUserAccountService;

	@Autowired
	private SocialUserAccountInfoService socialUserAccountInfoService;

	@Autowired
	private SocialUserWeiboService socialUserWeiboService;

	@Autowired
	IdentityService identityService;

	@Autowired
	UserService userService;

	public Map queryUserInfoByCode(String code) throws WeiboException {
		HashMap map = new HashMap();
		String clientId = SocialConfig.getString("client_id");
		String clinetSecret = SocialConfig.getString("clinet_secret");
		String redirectUrl = SocialConfig.getString("go_back");
		AccessToken accessToken = new Oauth().getAccessTokenByCode(code,
				clientId, clinetSecret, redirectUrl);
		Weibo weibo = new Weibo();
		Users users = new Users();
		weibo.setToken(accessToken.getAccessToken());

		User sinaUser = users.showUserById(accessToken.getUid());
		map.put("user", sinaUser);
		map.put("token", accessToken);
		map.put("code", code);
		return map;
	}

	public void saveSocialUserWeiboList(SocialUserAccount ac,
			String ownerUserId, List<Status> list, Integer weiboType,
			Integer weiboHandleType) {
		if (null == list) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			SocialUserWeibo weibo = currentSocialUserWeibo(ownerUserId, ac);
			Status status = list.get(i);
			statusToWeibo(weibo, status);
			// 原创微博信息
			Status retweetedStatus = status.getRetweetedStatus();
			if (null != retweetedStatus) {
				SocialUserWeibo retWeibo = handlerRetweetedWeibo(ac,
						ownerUserId, weiboType, weiboHandleType,
						retweetedStatus);
				try {
					socialUserWeiboService.createSocialUserWeibo(retWeibo);
					weibo.setRetweetedId(retWeibo.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			saveWeiboUserEntity(weibo);
		}

	}

	private void saveWeiboUserEntity(SocialUserWeibo weibo) {
		try {
			socialUserWeiboService.createSocialUserWeibo(weibo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 微博对象转换为实体对象
	 * 
	 * @param weibo
	 * @param status
	 */
	public void statusToWeibo(SocialUserWeibo weibo, Status status) {
		weibo.setCreateAt(status.getCreatedAt());
		weibo.setWeiboId(status.getId());
		weibo.setText(status.getText());
		weibo.setSource((status.getSource() == null) ? "" : status.getSource()
				.getName());

		weibo.setInReplyToStatusId(String.valueOf(status.getInReplyToStatusId()));
		weibo.setInReplyToUserId(String.valueOf(status.getInReplyToUserId()));
		weibo.setInReplyToScreenName(status.getInReplyToScreenName());
		weibo.setMid(status.getMid());
		weibo.setRepostsCount(status.getRepostsCount());
		weibo.setCommentsCount(status.getCommentsCount());
		weibo.setThumbnailPic(status.getThumbnailPic());
		weibo.setBmiddlePic(status.getBmiddlePic());
		weibo.setOriginalPic(status.getOriginalPic());
		if (null == status.getUser()) {
			weibo.setWeiboUidFollower(0);
			weibo.setWeiboUid("");
			weibo.setUserScreenName("");
			weibo.setUserName("");
			weibo.setUserDomain("");
			weibo.setUserVerified(0);
		} else {
			weibo.setUserProfileImageUrl(status.getUser().getProfileImageUrl());
			weibo.setWeiboUidFollower(status.getUser().getFollowersCount());
			weibo.setWeiboUid(status.getUser().getId());
			weibo.setUserScreenName(status.getUser().getScreenName());
			weibo.setUserName(status.getUser().getName());
			weibo.setUserDomain(status.getUser().getUserDomain());
			weibo.setUserVerified((status.getUser().isVerified()) ? 1 : 0);
		}
	}

	private SocialUserWeibo handlerRetweetedWeibo(SocialUserAccount ac,
			String ownerUserId, Integer weiboType, Integer weiboHandleType,
			Status retweetedStatus) {
		SocialUserWeibo retWeibo = currentSocialUserWeibo(ownerUserId, ac);
		statusToWeibo(retWeibo, retweetedStatus);
		retWeibo.setWeiboType(weiboType);// 我的微博
		retWeibo.setWeiboHandleType(weiboHandleType);// 全部微博
		return retWeibo;
	}

	private SocialUserWeibo currentSocialUserWeibo(String ownerUserId,
			SocialUserAccount ac) {
		SocialUserWeibo soruceWeibo = new SocialUserWeibo();
		soruceWeibo.setUserAccountId(ac.getId());
		soruceWeibo.setOwner(ownerUserId);
		soruceWeibo.setType(DataBaseParameters.SINA);
		return soruceWeibo;
	}

	public void myWeiboToDb(int type) {
		String ownerUserId = "fir5671";
		SocialUserAccount ac = socialUserAccountService.findSocialUserByType(
				ownerUserId, DataBaseParameters.SINA);
		if (ac != null) {
			String accountId = ac.getId();
			SocialUserAccountInfo tok = socialUserAccountInfoService
					.findAccountOfInfoByKey(DictDef.dict("accessToken"),
							accountId);
			String assessToken = tok.getValue();
			ViewPage<SocialUserWeibo> page = new ViewPage<SocialUserWeibo>();
			page.setStart(0);
			page.setPageSize(10);

			Paging paging = new Paging();
			paging.setPage(page.getCurrentPage() + 1);// 当前页码
			paging.setCount(page.getPageSize()); // 每页大小
			Integer baseAPP = 0;
			Integer feature = 0;
			// WW_TODO 新浪接口 全部微博
			List<Status> list = null;
			if (type == 0) {
				list = SinaSociaTool.findDsrmAccountRelationWeiboInfo(
						assessToken, paging, baseAPP, feature);
				saveSocialUserWeiboList(ac, ownerUserId, list, 1, 0);
			} else {
				String uid = ac.getWeiboUid();
				// 获取用户发布的微博
				list = SinaSociaTool.findDsrmAccountWeiboInfo(assessToken, uid,
						paging, baseAPP, feature);
				saveSocialUserWeiboList(ac, ownerUserId, list, 1, 1);
			}
		}
	}

	/**
	 * 处理用户授权返回
	 * 
	 * @param map
	 * @return
	 */
	public org.activiti.engine.identity.User handlerUserAuthorize(Map map) {
		org.activiti.engine.identity.User user = null;
		weibo4j.model.User sinaUser = (weibo4j.model.User) map.get("user");
		AccessToken accessToken = (AccessToken) map.get("token");
		SocialUserAccountQuery query = new SocialUserAccountQuery();
		query.setWeiboUid(sinaUser.getId());
		query.setType(DataBaseParameters.SINA);
		List<SocialUserAccount> list = socialUserAccountService
				.findSocialUserAccountByQueryCriteria(query, null);
		if (list != null && list.size() > 0) {// 帐号已经存在
			SocialUserAccount c = list.get(0);
			user = updateSinaSocialInfo(sinaUser, c, accessToken);
		} else {
			// user = addSinaSocialInfo(sinaUser, accessToken);
		}

		return user;
	}

	private org.activiti.engine.identity.User updateSinaSocialInfo(
			User sinaUser, SocialUserAccount socialUserAccount,
			AccessToken accessToken) {
		org.activiti.engine.identity.User la = identityService
				.createUserQuery().userId(socialUserAccount.getOwnUser())
				.singleResult();
		updateSocialInfos(sinaUser, socialUserAccount);
		addTokenInfos(accessToken, socialUserAccount);
		return la;
	}

	public void addSinaSocialInfo(org.activiti.engine.identity.User retUser,Map thirdUserMap) {
		weibo4j.model.User sinaUser = (weibo4j.model.User) thirdUserMap.get("user");
		AccessToken accessToken = (AccessToken) thirdUserMap.get("token");
		// 增加一个用户
		String userId = sinaUser.getName();
		/*org.activiti.engine.identity.User retUser = userService.createUser(
				userId, sinaUser.getScreenName(), null, "123456", null, null,
				Arrays.asList("user"), null);*/
		// 把密码怎么给
		SocialUserAccount socialUserAccount = new SocialUserAccount();
		socialUserAccount.setWeiboUid(sinaUser.getId());
		socialUserAccount.setName(userId);
		socialUserAccount.setType(DataBaseParameters.SINA);
		socialUserAccount.setOwnUser(retUser.getId());
		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		updateSocialInfos(sinaUser, socialUserAccount);
		// accessToken
		addTokenInfos(accessToken, socialUserAccount);
	}

	private void addTokenInfos(AccessToken accessToken,
			SocialUserAccount socialUserAccount) {
		SocialUserAccountInfo info = newAccountInfo(socialUserAccount);
		info.setKey("accessToken");
		info.setValue(accessToken.getAccessToken());
		info.setValueType(DictDef.dictInt("string"));
		socialUserAccountInfoService.createSocialUserAccountInfo(info);

		String expireIn = accessToken.getExpireIn();
		// 失效时间
		long timeMill = Long.parseLong(String.valueOf(Integer
				.parseInt(expireIn) * 1000));
		timeMill += new Date().getTime();

		SocialUserAccountInfo expireInfo = newAccountInfo(socialUserAccount);
		expireInfo.setKey("expiredTime");
		expireInfo.setValue(expireIn);
		expireInfo.setValueDate(new Date(timeMill));
		expireInfo.setValueType(DictDef.dictInt("date"));
		socialUserAccountInfoService.createSocialUserAccountInfo(expireInfo);

		SocialUserAccountInfo appKeyInfo = newAccountInfo(socialUserAccount);
		appKeyInfo.setKey("appkeyId");
		appKeyInfo.setValue(SocialConfig.getString("client_id"));
		appKeyInfo.setValueType(DictDef.dictInt("string"));
		socialUserAccountInfoService.createSocialUserAccountInfo(appKeyInfo);
	}

	private SocialUserAccountInfo newAccountInfo(
			SocialUserAccount socialUserAccount) {
		SocialUserAccountInfo info = new SocialUserAccountInfo();
		info.setType(DictDef.dict("sina"));
		info.setValueType(DictDef.dictInt("string"));
		info.setAccountId(socialUserAccount.getId());
		return info;
	}

	private void updateSocialInfos(User sinaUser,
			SocialUserAccount socialUserAccount) {
		List<DictDef> list = DictDef.queryDictsByType(DictDef
				.dict("social_account_info"));
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			DictDef dictDef = (DictDef) iterator.next();
			String key = dictDef.getValue();
			Object ret = null;
			try {
				ret = ReflectionUtils.invokeGetterMethod(sinaUser, key);
			} catch (Exception e) {
				// System.out.println("没有的-----------------------------------------------------------"
				// + key);
				continue;
			}
			SocialUserAccountInfo info = new SocialUserAccountInfo();
			info.setType(DictDef.dict("sina"));
			if (ret != null) {
				ReflectionUtils.setFieldValue(info, "value", ret.toString());
			} else {
				info.setValue(null);
			}
			info.setKey(key);
			if (ret instanceof Integer) {
				info.setValueType(DictDef.dictInt("int"));
			}
			if (ret instanceof String) {
				info.setValueType(DictDef.dictInt("string"));
			}
			if (ret instanceof Double) {
				info.setValueType(DictDef.dictInt("double"));
			}

			if (ret instanceof Date) {
				info.setValueType(DictDef.dictInt("date"));
			}
			// 设置关联账户
			info.setAccountId(socialUserAccount.getId());
			// System.out.println("key:" + key + " value:" + ret);
			socialUserAccountInfoService.createSocialUserAccountInfo(info);

		}
	}

	public org.activiti.engine.identity.User initUserByThirdUser(
			Map thirdUserMap) {
		weibo4j.model.User sinaUser = (weibo4j.model.User) thirdUserMap
				.get("user");
		org.activiti.engine.identity.User user = identityService
				.newUser(sinaUser.getName());
		user.setFirstName(sinaUser.getScreenName());
		// user.setEmail(sinaUser.)
		// user.setLastName(lastName);
		return user;
	}

	public org.activiti.engine.identity.User addUerByThirdInfo(
			org.activiti.engine.identity.User user, HashMap thirdUserMap) {
		weibo4j.model.User sinaUser = (weibo4j.model.User) thirdUserMap
				.get("user");
		AccessToken accessToken = (AccessToken) thirdUserMap.get("token");
		// 增加一个用户
		String userId = user.getId();
		org.activiti.engine.identity.User retUser = userService.createUser(
				userId, user.getFirstName(), null, user.getPassword(), null, null,
				Arrays.asList("user"), null);
		// 把密码怎么给
		SocialUserAccount socialUserAccount = new SocialUserAccount();
		socialUserAccount.setWeiboUid(sinaUser.getId());
		socialUserAccount.setName(userId);
		socialUserAccount.setType(DataBaseParameters.SINA);
		socialUserAccount.setOwnUser(userId);

		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		updateSocialInfos(sinaUser, socialUserAccount);
		// accessToken
		addTokenInfos(accessToken, socialUserAccount);
		return retUser;
	}
}