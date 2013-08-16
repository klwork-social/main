package com.klwork.business.utils;

import net.sf.json.JSONObject;

import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.oauthv2.OAuthV2Client;
import com.tencent.weibo.utils.TencentWeiboContentTransLate;

public class TencentSociaTool {
	public static OAuthV2 getQQAuthV2() {
		OAuthV2 oAuth = new OAuthV2();
		oAuth.setClientId(SocialConfig.getString("app_ID"));
		oAuth.setClientSecret(SocialConfig.getString("app_KEY"));
		oAuth.setRedirectUri(SocialConfig.getString("redirect_URI"));
		return oAuth;
	}
	
	public static Object getJsonDataObject(String reponseJsonStr) {
		return JSONObject.fromObject(reponseJsonStr).get("data");
	}
	
	public static String generateAuthorizationURL(){
		OAuthV2 oAuth = getQQAuthV2();
		String url = OAuthV2Client.generateAuthorizationURL(oAuth);
		return url;
	}

	public static String textTranslate(String text) {
		if(text == null || text.trim().length() == 0){
			return null;
		}
		return TencentWeiboContentTransLate.transLateall(text, "blue");
	}
}
