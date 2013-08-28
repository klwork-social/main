package com.vdisk.net.session;

import com.vdisk.net.VDiskAPI;
import com.vdisk.net.exception.VDiskException;


public class MySession extends AbstractSession{
	
	public static String URL_OAUTH2_ACCESS_AUTHORIZE = "https://auth.sina.com.cn/oauth2/authorize";
	public static String URL_OAUTH2_ACCESS_TOKEN = "https://auth.sina.com.cn/oauth2/access_token";
	
	public static final String OAUTH2_TOKEN = "oauth2_token";
	public static final String OAUTH2_PREFS = "prefs";
	public static final String OAUTH2_PREFS_ACCESS_TOKEN = "access_token";
	public static final String OAUTH2_PREFS_EXPIRES_IN = "expires_in";
	public static final String OAUTH2_PREFS_REFRESH_TOKEN = "refresh_token";
	public static final String OAUTH2_PREFS_WEIBO_UID = "uid";
	
	private VDiskAPI<Session> mApi = null;
	private String mRedirectUrl = "";
	
	public MySession(AppKeyPair appKeyPair, AccessType type) {
		super(appKeyPair, type);
	}

	public MySession(AppKeyPair appKeyPair, AccessType type,
			AccessToken accessTokenPair) {
		super(appKeyPair, type, accessTokenPair);
	}
	
	
	public void setRedirectUrl(String mRedirectUrl) {
		this.mRedirectUrl = mRedirectUrl;
	}

	public String getRedirectUrl() {
		return mRedirectUrl;
	}
	
	/**
	 * 访问接口，获取AccessToken.
	 * 
	 * Access the API and get the AccessToken
	 * 
	 * @param code
	 * @param context
	 * @return
	 * @throws VDiskException
	 */
	public AccessToken getOAuth2AccessToken(String code)
			throws VDiskException {
		AppKeyPair appKeyPair = getAppKeyPair();
		mApi = new VDiskAPI<Session>(this);
		String rlt = mApi.doOAuth2Authorization(appKeyPair, code, mRedirectUrl);
		AccessToken accessToken = new AccessToken(rlt);
		this.accessToken = accessToken;
		return accessToken;
	}
}
