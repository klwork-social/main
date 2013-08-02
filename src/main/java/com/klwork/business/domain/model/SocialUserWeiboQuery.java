package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserWeiboQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public SocialUserWeiboQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String retweetedId;
	
	/**
	 *  
	 */
	private String userAccountId;
	
	/**
	 *  
	 */
	private String weiboId;
	
	/**
	 *  
	 */
	private Integer weiboType;
	
	/**
	 *  
	 */
	private Integer weiboHandleType;
	
	/**
	 *  
	 */
	private Integer type;
	
	
	String userName;

	/**
	 * 
	 *
	 * @param retweetedId
	 */
	public SocialUserWeiboQuery setRetweetedId(String retweetedId){
		this.retweetedId = retweetedId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getRetweetedId(){
		return retweetedId;
	}
	
	/**
	 * 
	 *
	 * @param userAccountId
	 */
	public SocialUserWeiboQuery setUserAccountId(String userAccountId){
		this.userAccountId = userAccountId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserAccountId(){
		return userAccountId;
	}
	
	/**
	 * 
	 *
	 * @param weiboId
	 */
	public SocialUserWeiboQuery setWeiboId(String weiboId){
		this.weiboId = weiboId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getWeiboId(){
		return weiboId;
	}
	
	/**
	 * 
	 *
	 * @param weiboType
	 */
	public SocialUserWeiboQuery setWeiboType(Integer weiboType){
		this.weiboType = weiboType;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getWeiboType(){
		return weiboType;
	}
	
	/**
	 * 
	 *
	 * @param weiboHandleType
	 */
	public SocialUserWeiboQuery setWeiboHandleType(Integer weiboHandleType){
		this.weiboHandleType = weiboHandleType;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getWeiboHandleType(){
		return weiboHandleType;
	}
	
	/**
	 * 
	 *
	 * @param type
	 */
	public SocialUserWeiboQuery setType(Integer type){
		this.type = type;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getType(){
		return type;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}