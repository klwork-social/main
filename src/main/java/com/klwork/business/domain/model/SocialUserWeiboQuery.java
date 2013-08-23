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
	private String owner;
	
	/**
	 *  
	 */
	private String weiboId;
	
	/**
	 *  
	 */
	private String weiboUid;
	
	/**
	 *  
	 */
	private String userName;
	
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
	
	/**
	 *  
	 */
	private Integer weiboStatus;
	

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
	 * @param owner
	 */
	public SocialUserWeiboQuery setOwner(String owner){
		this.owner = owner;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getOwner(){
		return owner;
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
	 * @param weiboUid
	 */
	public SocialUserWeiboQuery setWeiboUid(String weiboUid){
		this.weiboUid = weiboUid;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getWeiboUid(){
		return weiboUid;
	}
	
	/**
	 * 
	 *
	 * @param userName
	 */
	public SocialUserWeiboQuery setUserName(String userName){
		this.userName = userName;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserName(){
		return userName;
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
	
	/**
	 * 
	 *
	 * @param weiboStatus
	 */
	public SocialUserWeiboQuery setWeiboStatus(Integer weiboStatus){
		this.weiboStatus = weiboStatus;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getWeiboStatus(){
		return weiboStatus;
	}
	

}