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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import weibo4j.Account;
import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.WeiboForwardSend;
import com.klwork.business.domain.service.infs.AbstractSocialService;
import com.klwork.business.utils.SinaSociaTool;
import com.klwork.business.utils.SocialConfig;
import com.klwork.common.exception.ThirdPlatformException;
import com.klwork.common.utils.ReflectionUtils;
import com.klwork.common.utils.StringTool;
import com.klwork.explorer.ui.util.ImageUtil;
import com.vdisk.net.ProgressListener;
import com.vdisk.net.VDiskAPI;
import com.vdisk.net.VDiskAPI.DeltaEntry;
import com.vdisk.net.VDiskAPI.DeltaPage;
import com.vdisk.net.VDiskAPI.Entry;
import com.vdisk.net.VDiskAPI.UploadRequest;
import com.vdisk.net.exception.VDiskException;
import com.vdisk.net.session.AppKeyPair;
import com.vdisk.net.session.MySession;
import com.vdisk.net.session.Session.AccessType;

/**
 * The Class SocialSinaService.
 */
@Service
public class SocialSinaService extends AbstractSocialService {

	

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
	
	@Override
	@SuppressWarnings("rawtypes")
	public Map queryUserInfoByCode(String code,String openid, String openkey) {
		HashMap map = new HashMap();
		String clientId = SocialConfig.getString("client_id");
		String clinetSecret = SocialConfig.getString("clinet_secret");
		String redirectUrl = SocialConfig.getString("go_back");
		AccessToken accessToken = null;
		try {
			accessToken = new Oauth().getAccessTokenByCode(code,
					clientId, clinetSecret, redirectUrl);
		} catch (WeiboException e) {
			e.printStackTrace();
			throw new ThirdPlatformException(e.getMessage());
		}
		
		String strUid = "";
		Account am = new Account();
		am.client.setToken(accessToken.getAccessToken());
		try {
			JSONObject uid = am.getUid();
			
			//Log.logInfo(uid.toString());
			strUid = uid.getString("uid");
		} catch (WeiboException e) {
			throw new ThirdPlatformException(e.getMessage());
		} catch (JSONException e) {
			throw new ThirdPlatformException(e.getMessage());
		}
		
		
		/*Weibo weibo = new Weibo();
		weibo.setToken(accessToken.getAccessToken());*/
		
		Users users = new Users();
		users.client.setToken(accessToken.getAccessToken());
		User sinaUser = null;
		try {
			sinaUser = users.showUserById(strUid);
		} catch (WeiboException e) {
			//e.printStackTrace();
			throw new ThirdPlatformException(e.getMessage());
		}
		
		map.put("user", sinaUser);
		map.put("token", accessToken);
		map.put("code", code);
		return map;
	}

	private void saveSocialUserWeiboList(SocialUserAccount ac,
			String ownerUserId, List<Status> list, Integer weiboType) {
		if (null == list) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			SocialUserWeibo weibo = currentNewSocialUserWeibo(ownerUserId, ac,weiboType);
			Status status = list.get(i);
			convertThirdToWeiboEntity(weibo, status);
			// 原创微博信息
			Status retweetedStatus = status.getRetweetedStatus();
			if (null != retweetedStatus) {
				SocialUserWeibo retWeibo = handlerRetweetedWeibo(ac,
						ownerUserId, weiboType,
						retweetedStatus);
				if(saveWeiboUserEntity(retWeibo)){
					weibo.setRetweetedId(retWeibo.getId());
				}
				
			}
			saveWeiboUserEntity(weibo);
		}

	}

	

	/**
	 * 微博对象转换为实体对象
	 * 
	 * @param weibo
	 * @param status
	 */
	@Override
	public <T> SocialUserWeibo convertThirdToWeiboEntity(SocialUserWeibo weibo, T thirdInfo) {
		Status status = (Status)thirdInfo;
		weibo.setCreateAt(status.getCreatedAt());
		weibo.setWeiboId(status.getId());
		weibo.setText(status.getText());
		weibo.setSource((status.getSource() == null) ? "" : status.getSource()
				.getName());
		weibo.setSourceUrl((status.getSource() == null) ? "" : status.getSource()
				.getUrl());

		weibo.setInReplyToStatusId(String.valueOf(status.getInReplyToStatusId()));
		weibo.setInReplyToUserId(String.valueOf(status.getInReplyToUserId()));
		weibo.setInReplyToScreenName(status.getInReplyToScreenName());
		weibo.setMid(status.getMid());
		weibo.setRepostsCount(status.getRepostsCount());
		weibo.setCommentsCount(status.getCommentsCount());
		
		//微博图片的处理
		String thumbnailPic = status.getThumbnailPic();
		weibo.setThumbnailPic(thumbnailPic);
		if(StringTool.judgeBlank(thumbnailPic)){//取图片的大小
			String sizeStr = ImageUtil.queryURLImageSize(thumbnailPic);
			weibo.setThumbnailPicSize(sizeStr);
		}
		String bmiddlePic = status.getBmiddlePic();
		weibo.setBmiddlePic(bmiddlePic);
		if(StringTool.judgeBlank(bmiddlePic)){//取图片的大小
			String sizeStr = ImageUtil.queryURLImageSize(bmiddlePic);
			weibo.setBmiddlePicSize(sizeStr);
		}
		String originalPic = status.getOriginalPic();
		weibo.setOriginalPic(originalPic);
		if(StringTool.judgeBlank(originalPic)){//取图片的大小
			String sizeStr = ImageUtil.queryURLImageSize(originalPic);
			weibo.setOriginalPicSize(sizeStr);
		}
		
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
		return weibo;
	}

	private SocialUserWeibo handlerRetweetedWeibo(SocialUserAccount ac,
			String ownerUserId, Integer weiboType,
			Status retweetedStatus) {
		SocialUserWeibo retWeibo = currentNewSocialUserWeibo(ownerUserId, ac,weiboType);
		convertThirdToWeiboEntity(retWeibo, retweetedStatus);
		//retWeibo.setWeiboType(weiboType);// 我的微博
		retWeibo.setWeiboHandleType(1);//表明为原始贴
		return retWeibo;
	}
	
	
	@Override
	public int weiboToDb(SocialUserAccount ac) {
		int type = 1;
		if (ac != null) {
			String accountId = ac.getId();
			String assessToken = findAccessTokenByAccout(accountId);
			
			//我的微博
			myWeiboToDb(ac, type, assessToken);
			
			//@我的微博
			mentionsWeiboToDb(ac, type, assessToken);
			
			//我关注的所有人的微博
			homeWeiboToDb(ac, type, assessToken);
		}
		return 0;
	}

	public String findAccessTokenByAccout(String accountId) {
		SocialUserAccountInfo tok = socialUserAccountInfoService
				.findAccountOfInfoByKey(DictDef.dict("accessToken"),
						accountId);
		String assessToken = tok.getValue();
		return assessToken;
	}
	
	/**
	 * 我的微博
	 * @param ac
	 * @param type
	 * @param assessToken
	 */
	public void myWeiboToDb(SocialUserAccount ac, int type, String assessToken) {
		Paging paging = new Paging();
		paging.setPage(1);// 当前页码
		paging.setCount(20); // 每页大小
		//paging.setSinceId(1);
		Integer baseAPP = 0;
		Integer feature = 0;
		
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,DictDef.dictInt("weibo_user_timeline"),ac.getName());
		if(lastWeibo != null){
			paging.setSinceId(StringTool.parseLong(lastWeibo.getWeiboId()));
		}
		int i = 0;
		String uid = ac.getWeiboUid();
		List<Status> list = null;
		do {
			paging.setPage(++i);
		// 获取用户发布的微博
		list = SinaSociaTool.findDsrmAccountWeiboInfo(assessToken, uid,
				paging, baseAPP, feature);
		saveSocialUserWeiboList(ac, ac.getOwnUser(), list, DictDef.dictInt("weibo_user_timeline"));
		} while (list != null && list.size() > 0
				&& paging.getPage() < 5);
	}
	
	/**
	 * 我的所有微博
	 * @param ac
	 * @param type
	 * @param assessToken
	 */
	public void homeWeiboToDb(SocialUserAccount ac, int type, String assessToken) {
		Paging paging = new Paging();
		paging.setPage(1);// 当前页码
		paging.setCount(20); // 每页大小
		//paging.setSinceId(1);
		Integer baseAPP = 0;
		Integer feature = 0;
		
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,DictDef.dictInt("weibo_public_timeline"),null);
		/*SocialUserAccountInfo lastTime = socialUserAccountInfoService
				.findAccountOfInfoByKey(DictDef.dict("weibo_last_time"), ac.getId());*/
		if(lastWeibo != null){
			paging.setSinceId(StringTool.parseLong(lastWeibo.getWeiboId()));
		}
		int i = 0;
		//String uid = ac.getWeiboUid();
		List<Status> list = null;
		do {
			paging.setPage(++i);
			list = SinaSociaTool.findDsrmAccountRelationWeiboInfo(
					assessToken, paging, baseAPP, feature);
			saveSocialUserWeiboList(ac, ac.getOwnUser(), list, DictDef.dictInt("weibo_public_timeline"));
		} while (list != null && list.size() > 0
				&& paging.getPage() < 5);
	}
	
	/**
	 *  @我的微博
	 * @param ac
	 * @param type
	 * @param assessToken
	 */
	public void mentionsWeiboToDb(SocialUserAccount ac, int type, String assessToken) {
		Paging paging = new Paging();
		paging.setPage(1);// 当前页码
		paging.setCount(20); // 每页大小
		//paging.setSinceId(1);
		Integer baseAPP = 0;
		Integer feature = 0;
		
		SocialUserWeibo lastWeibo = queryLastSpeWeibo(ac,DictDef.dictInt("weibo_mentions_timeline"),null);
		if(lastWeibo != null){
			paging.setSinceId(StringTool.parseLong(lastWeibo.getWeiboId()));
		}
		int i = 0;
		String uid = ac.getWeiboUid();
		List<Status> list = null;
		do {
			paging.setPage(++i);
			list = SinaSociaTool.findMentionsAccountWeiboInfo(
					assessToken, paging, 0,baseAPP, feature);
			saveSocialUserWeiboList(ac, ac.getOwnUser(), list, DictDef.dictInt("weibo_mentions_timeline"));
		} while (list != null && list.size() > 0
				&& paging.getPage() < 5);
	}


	/**
	 * 处理用户授权返回
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public org.activiti.engine.identity.User handlerUserAuthorize(Map map) {
		org.activiti.engine.identity.User user = null;
		weibo4j.model.User sinaUser = (weibo4j.model.User) map.get("user");
		AccessToken accessToken = (AccessToken) map.get("token");
		SocialUserAccountQuery query = new SocialUserAccountQuery();
		query.setWeiboUid(sinaUser.getId());
		query.setType(getSocialTypeInt());
		
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
		updateSocialUserAccountByThird(socialUserAccount,sinaUser);
		updateSocialInfos(sinaUser, socialUserAccount);
		addTokenInfos(accessToken, socialUserAccount);
		return la;
	}



	public void addSinaSocialInfo(org.activiti.engine.identity.User retUser,Map thirdUserMap) {
		weibo4j.model.User sinaUser = (weibo4j.model.User) thirdUserMap.get("user");
		AccessToken accessToken = (AccessToken) thirdUserMap.get("token");
		SocialUserAccount socialUserAccount = addSocialUserAccountByThird(
				retUser, sinaUser);
		updateSocialInfos(sinaUser, socialUserAccount);
		// accessToken
		addTokenInfos(accessToken, socialUserAccount);
	}
	
	private void updateSocialUserAccountByThird(
			SocialUserAccount socialUserAccount, User sinaUser) {
		initAccountBySinaUser(socialUserAccount, sinaUser);
		socialUserAccountService.updateSocialUserAccount(socialUserAccount);
		
	}

	public void initAccountBySinaUser(SocialUserAccount socialUserAccount,
			User sinaUser) {
		String name = sinaUser.getName();
		String nick = sinaUser.getScreenName();
		String uid = sinaUser.getId();
		String url = sinaUser.getUrl();
		socialUserAccount.setWeiboUid(uid);
		socialUserAccount.setName(name);
		socialUserAccount.setUrl(url);
		socialUserAccount.setUserScreenName(nick);
		socialUserAccount.setType(getSocialTypeInt());
	}
	
	public SocialUserAccount addSocialUserAccountByThird(
			org.activiti.engine.identity.User retUser,
			weibo4j.model.User sinaUser) {
		// 增加一个用户
		SocialUserAccount socialUserAccount = new SocialUserAccount();
		initAccountBySinaUser(socialUserAccount, sinaUser);
		socialUserAccount.setOwnUser(retUser.getId());
		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		return socialUserAccount;
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
	
	@Override
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

/*	public org.activiti.engine.identity.User addUerByThirdInfo(
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
		socialUserAccount.setType(getSocialTypeInt());
		socialUserAccount.setOwnUser(userId);

		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		updateSocialInfos(sinaUser, socialUserAccount);
		// accessToken
		addTokenInfos(accessToken, socialUserAccount);
		return retUser;
	}*/

	@Override
	public String getSocialType() {
		return "0";
	}

	public int forwardWeibo(WeiboForwardSend weiboForwardSend) {
		
		String assessToken = findAccessTokenByAccout(weiboForwardSend.getUserAccountId());
		return SinaSociaTool.forwardWeibo(weiboForwardSend, assessToken);
	}


	
	public void discussWeibo(WeiboForwardSend weiboForwardSend) {
		String assessToken = findAccessTokenByAccout(weiboForwardSend.getUserAccountId());
		SinaSociaTool.discussWeibo(weiboForwardSend, assessToken);
	}

	
	/**
	 * 删除微博
	 * @param userWeibo
	 */
	@Override
	public void deleteWeibo(SocialUserWeibo userWeibo) {
		String assessToken = findAccessTokenByAccout(userWeibo.getUserAccountId());
		if( 1 == SinaSociaTool.deleteWeibo(userWeibo.getWeiboId(), assessToken)){//成功删除
			socialUserWeiboService.deleteSocialUserWeibo(userWeibo);
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void queryVidiskInfoByCode(String code,String openid, String openkey) {
		HashMap map = new HashMap();
		String clientId = SocialConfig.getString("vidisk_client_id");
		String clinetSecret = SocialConfig.getString("vidisk_clinet_secret");
		String redirectUrl = SocialConfig.getString("vidisk_go_back");
		/*AccessToken accessToken = null;
		 String url = "https://auth.sina.com.cn/oauth2/access_token";
		try {
			accessToken = new Oauth().getAccessTokenByCode(url,code,
					clientId, clinetSecret, redirectUrl);
		} catch (WeiboException e) {
			e.printStackTrace();
			throw new ThirdPlatformException(e.getMessage());
		}*/
		
		File mFile = new File("F:/temp/a.jpg");
		java.io.FileInputStream fis = null;
		try {
			fis = new java.io.FileInputStream(mFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String mPath = "";
		if (!mPath.endsWith("/")) {
			mPath = mPath + "/";
		}
		
		AppKeyPair appKeyPair = new AppKeyPair(clientId,clinetSecret);
		MySession mySession = new MySession(appKeyPair,AccessType.APP_FOLDER);
		//MySession mySession = new MySession(appKeyPair,AccessType.VDISK);
		mySession.setRedirectUrl(redirectUrl);
		VDiskAPI<MySession> mApi = new VDiskAPI(mySession);
		
		
		String path = mPath + mFile.getName();
		UploadRequest mRequest = null;
		try {
			mySession.getOAuth2AccessToken(code);
			//com.vdisk.net.VDiskAPI.Account s =  mApi.accountInfo();
			/*mRequest = mApi.putFileOverwriteRequest(path, fis, mFile.length(),
					new ProgressListener() {
						@Override
						public long progressInterval() {
							// Update the progress bar every half-second or so
							return super.progressInterval();
						}

						@Override
						public void onProgress(long bytes, long total) {
							//publishProgress(bytes);
						}
					});
			if (mRequest != null) {
				Entry uploadBackEntry = mRequest.upload();
			}*/
			Entry cr = mApi.createFolder("good");
			String s = mApi.share("good");
			
			
			DeltaPage<Entry> deltaPage = mApi.delta(null);
			List<DeltaEntry<Entry>> entries = deltaPage.entries;

			if (entries != null) {
				for (DeltaEntry<Entry> entry : entries) {
					String entryInfo = entry.metadata == null ? "null"
							: entry.metadata.modified;
					System.out.println((entry.lcPath + ": " + entryInfo));
				}
			}
		} catch (VDiskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
}