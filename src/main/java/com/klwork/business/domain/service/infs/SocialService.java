package com.klwork.business.domain.service.infs;

import java.util.Map;

import com.klwork.business.domain.model.SocialUserAccount;
import com.klwork.business.domain.model.SocialUserWeibo;
import com.klwork.business.domain.model.WeiboForwardSend;

public interface SocialService {
	/**
	 * 社交类型
	 * @return
	 */
	public String getSocialType();
	
	/**
	 * 保存指定的微博数据到数据库
	 * @param ac
	 * @param weiType
	 * @return
	 */
	public int weiboToDb(SocialUserAccount ac);
	
	/**
	 * 查询用户信息通过第三方返回的值
	 * @param code
	 * @param openid
	 * @param openkey
	 * @return
	 */
	public Map queryUserInfoByCode(String code,
			String openid, String openkey);
	
	/**
	 * 用户处理授权返回
	 * @param map
	 * @return
	 */
	public org.activiti.engine.identity.User handlerUserAuthorize(Map map);
	
	/**
	 * 初始化用户对象，从第三方信息中
	 * @param thirdUserMap
	 * @return
	 */
	public org.activiti.engine.identity.User initUserByThirdUser(
			Map thirdUserMap);
	
	/**
	 * 把第三方微博对象转化为UserWeibo对象
	 * @param thirdInfo
	 * @param weibo
	 * @return
	 */
	public <T> SocialUserWeibo convertThirdToWeiboEntity(SocialUserWeibo weibo,T thirdInfo
			);
	
	/**
	 * 删除微博
	 * @param userWeibo
	 */
	public void deleteWeibo(SocialUserWeibo userWeibo);
	
	/**
	 * 发送微博
	 * @param socialUserAccount
	 * @param value
	 * @param type
	 */
	public void sendWeibo(SocialUserAccount socialUserAccount, String value,
			String type);
	
	
	/**
	 * 发送微博
	 * @param socialUserAccount
	 * @param value
	 * @param type
	 * @param type2 
	 */
	public void sendWeiboAndImage(SocialUserAccount socialUserAccount, String value, String imageUrl,
			String type);
	
	/**
	 * 转发微博
	 * @param weiboForwardSend
	 */
	public int forwardWeibo(WeiboForwardSend weiboForwardSend);
	
	/**
	 * 评论微博
	 * @param weiboForwardSend
	 * @return
	 */
	public int discussWeibo(WeiboForwardSend weiboForwardSend);
}
