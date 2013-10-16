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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.IdentityService;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.EvernoteApi;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evernote.auth.EvernoteAuth;
import com.evernote.auth.EvernoteService;
import com.evernote.clients.ClientFactory;
import com.evernote.clients.NoteStoreClient;
import com.evernote.clients.UserStoreClient;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.User;
import com.evernote.thrift.TException;
import com.klwork.business.domain.model.DictDef;
import com.klwork.business.domain.model.NoteSendEntity;
import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserAccountInfo;
import com.klwork.business.domain.model.SocialUserAccountQuery;
import com.klwork.business.utils.SocialConfig;
import com.klwork.common.SystemConstants;
import com.klwork.common.utils.logging.Logger;
import com.klwork.common.utils.logging.LoggerFactory;

/**
 * The Class SocialSinaService.
 */
@Service
public class SocialEvernoteService {

	private Logger logger = LoggerFactory.getLogger(getClass());
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

	@Autowired
	SocialUserWeiboCommentService socialUserWeiboCommentService;

	/**
	 * 请求信息的证书
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map queryEvernoteRequestTokenInfo() {
		EvernoteService EVERNOTE_SERVICE = EvernoteService.SANDBOX;// 沙箱模式
		OAuthService service = queryEvernoteService(EVERNOTE_SERVICE);

		Map retMap = new HashMap();
		// 请求印象笔记的服务器来得到一个临时的证书(请求 token)
		Token scribeRequestToken = service.getRequestToken();

		// 响应结果将包含临时证书：oauth_token 和 oauth_token_secret
		// 当你得到临时证书后，就可以把用户（资源拥有者）转到印象笔记的网站来让用户对你的应用授权
		Object rawResponse = scribeRequestToken.getRawResponse();
		retMap.put("rawResponse", rawResponse);
		String requestToken = scribeRequestToken.getToken();
		retMap.put("requestToken", requestToken);
		String requestTokenSecret = scribeRequestToken.getSecret();
		retMap.put("requestTokenSecret", requestTokenSecret);

		// 得到授权的地址
		String authorizationUrl = EVERNOTE_SERVICE
				.getAuthorizationUrl(requestToken);
		retMap.put("authorizationUrl", authorizationUrl);
		logger.info(authorizationUrl);
		return retMap;
	}

	/**
	 * 得到OAuthService
	 * 
	 * @param EVERNOTE_SERVICE
	 * @return
	 */
	public OAuthService queryEvernoteService(EvernoteService EVERNOTE_SERVICE) {
		String CONSUMER_KEY = SocialConfig.getString("evernote_consumer_key");
		String CONSUMER_SECRET = SocialConfig
				.getString("evernote_consumer_secret");
		// 回调
		String CALLBACK_URL = SocialConfig.getString("evernote_go_back");

		Class<? extends org.scribe.builder.api.EvernoteApi> providerClass = EvernoteApi.Sandbox.class;
		if (EVERNOTE_SERVICE == EvernoteService.PRODUCTION) {
			providerClass = org.scribe.builder.api.EvernoteApi.class;
		}
		OAuthService service = new ServiceBuilder().provider(providerClass)
				.apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET)
				.callback(CALLBACK_URL).build();
		return service;
	}

	/**
	 * 用户授权完成后进行回调 的处理
	 * 
	 * @param request
	 */
	public void handlerEvernoteAccessToken(HttpServletRequest request) {
		EvernoteService EVERNOTE_SERVICE = EvernoteService.SANDBOX;// 沙箱模式
		OAuthService service = queryEvernoteService(EVERNOTE_SERVICE);
		String localUserId = (String) request.getSession().getAttribute(SystemConstants.COOKIE_USER_ID);
		String requestToken = request.getParameter("oauth_token");
		String verifier = request.getParameter("oauth_verifier");

		HashMap map = (HashMap) request.getSession().getAttribute(
				SystemConstants.SESSION_THIRD_USER_MAP);
		String requestTokenSecret = (String) map.get("requestTokenSecret");

		Token scribeRequestTokenObj = new Token(requestToken,
				requestTokenSecret);
		Verifier scribeVerifier = new Verifier(verifier);

		// 用临时证书来交换一个认证 token （也叫 token证书或访问 token）
		Token accessTokenObj = service.getAccessToken(scribeRequestTokenObj,
				scribeVerifier);

		// 响应（evernoteAuth）将包含必选参数 oauth_token 和 oauth_token_secrect (URL 编码的），
		// 并且还包含为后续的云 API 调用提供所需要的正确 URL 的 edam_noteStoreUrl，
		// 指明了认证用户的印象笔记用户 ID 的 edam_userId， 和指明了认证 token 过期时间的 edam_epires 入口。
		EvernoteAuth evernoteAuth = EvernoteAuth.parseOAuthResponse(
				EVERNOTE_SERVICE, accessTokenObj.getRawResponse());
		int uid = evernoteAuth.getUserId();
		String accessToken = evernoteAuth.getToken();
		logger.debug(accessToken);

		ClientFactory factory = new ClientFactory(evernoteAuth);
		try {
			UserStoreClient userStore = factory.createUserStoreClient();
			User u = userStore.getUser();
			saveUserToDb(u, accessToken, localUserId);
			logger.debug(u.toString());
		} catch (EDAMUserException e) {
			e.printStackTrace();
		} catch (EDAMSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 循环book
		/*
		 * EvernoteAuth evernoteAuth2 = new EvernoteAuth(EVERNOTE_SERVICE,
		 * accessToken); NoteStoreClient noteStoreClient;
		 * 
		 * try { noteStoreClient = new ClientFactory(evernoteAuth2)
		 * .createNoteStoreClient(); List<Notebook> notebooks =
		 * noteStoreClient.listNotebooks(); for (Notebook notebook : notebooks)
		 * { System.out.println("Notebook: " + notebook.getName()); } } catch
		 * (EDAMUserException | EDAMSystemException | TException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

	}

	private void saveUserToDb(User u, String accessToken, String localUserId) {
		SocialUserAccountQuery query = new SocialUserAccountQuery();
		query.setWeiboUid(u.getId() + "");
		query.setType(getSocialTypeInt());
		List<SocialUserAccount> list = socialUserAccountService
				.findSocialUserAccountByQueryCriteria(query, null);
		if (list != null && list.size() > 0) {// 帐号已经存在
			SocialUserAccount c = list.get(0);
			updateSocialInfo(u, accessToken, localUserId);
		} else {
			addSocialInfo(u, accessToken, localUserId);
		}
	}

	private SocialUserAccount addSocialInfo(User u, String accessToken,
			String localUserId) {
		SocialUserAccount socialUserAccount = initAccountByThirdEntity(u,
				localUserId);
		socialUserAccountService.createSocialUserAccount(socialUserAccount);
		addToken(accessToken, socialUserAccount);
		return socialUserAccount;
	}

	public void addToken(String accessToken, SocialUserAccount socialUserAccount) {
		SocialUserAccountInfo info = newAccountInfo(socialUserAccount);
		info.setKey("accessToken");
		info.setValue(accessToken);
		info.setValueType(DictDef.dictInt("string"));
		socialUserAccountInfoService.createSocialUserAccountInfo(info);
	}

	public SocialUserAccount initAccountByThirdEntity(User u, String localUserId) {
		SocialUserAccount socialUserAccount = new SocialUserAccount();
		socialUserAccount.setWeiboUid(u.getId() + "");
		socialUserAccount.setOwnUser(localUserId);
		socialUserAccount.setType(getSocialTypeInt());
		socialUserAccount.setName(u.getName());
		return socialUserAccount;
	}

	private SocialUserAccountInfo newAccountInfo(
			SocialUserAccount socialUserAccount) {
		SocialUserAccountInfo info = new SocialUserAccountInfo();
		info.setType(getSocialTypeInt().toString());
		info.setValueType(DictDef.dictInt("string"));
		info.setAccountId(socialUserAccount.getId());
		return info;
	}

	private void updateSocialInfo(User u, String accessToken, String localUserId) {
		SocialUserAccount socialUserAccount = initAccountByThirdEntity(u,
				localUserId);
		socialUserAccountService.updateSocialUserAccount(socialUserAccount);
		addToken(accessToken, socialUserAccount);
	}

	private Integer getSocialTypeInt() {
		return new Integer(2);
	}

	/**
	 * 查询记事本列表
	 * 
	 * @param socialUserAccount
	 * @return
	 */
	public Map<String, String> queryNotebook(SocialUserAccount socialUserAccount) {
		Map<String, String> retMap = new HashMap<String, String>();
		String accessToken = findAccessTokenByAccout(socialUserAccount.getId());
		EvernoteAuth evernoteAuth2 = queryEvernoteAuth(accessToken);

		NoteStoreClient noteStoreClient;
		try {
			noteStoreClient = new ClientFactory(evernoteAuth2)
					.createNoteStoreClient();
			List<Notebook> notebooks = noteStoreClient.listNotebooks();
			for (Notebook notebook : notebooks) {
				retMap.put(notebook.getGuid(), notebook.getName());
			}
		} catch (Exception e) {
		}
		return retMap;
	}

	public EvernoteAuth queryEvernoteAuth(String accessToken) {
		EvernoteService EVERNOTE_SERVICE = EvernoteService.SANDBOX;// 沙箱模式
		EvernoteAuth evernoteAuth2 = new EvernoteAuth(EVERNOTE_SERVICE,
				accessToken);
		return evernoteAuth2;
	}

	/**
	 * 将一个微博保存到一个新的日记中
	 * 
	 * @param noteEntity
	 * @return
	 */
	public int saveWeiboToNotes(NoteSendEntity noteEntity) {
		String assessToken = findAccessTokenByAccout(noteEntity
				.getUserAccountId());
		EvernoteAuth evernoteAuth = queryEvernoteAuth(assessToken);
		NoteStoreClient noteStoreClient;
		try {
			noteStoreClient = new ClientFactory(evernoteAuth)
					.createNoteStoreClient();
			Note note = new Note();
			//note.set
			note.setTitle(noteEntity.getTitle());
			String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			        + "<!DOCTYPE en-note SYSTEM \"http://xml.evernote.com/pub/enml2.dtd\">"
			        + "<en-note><pre>"
			        + noteEntity.getContent()
			        + "</pre></en-note>";
			note.setContent(content);
			Note createdNote = noteStoreClient.createNote(note);
			String newNoteGuid = createdNote.getGuid();
			logger.debug(newNoteGuid);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public String findAccessTokenByAccout(String accountId) {
		SocialUserAccountInfo tok = socialUserAccountInfoService
				.findAccountOfInfoByKey(DictDef.dict("accessToken"), accountId);
		String assessToken = tok.getValue();
		return assessToken;
	}
}