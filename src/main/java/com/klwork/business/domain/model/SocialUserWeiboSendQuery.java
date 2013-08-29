package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserWeiboSendQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public SocialUserWeiboSendQuery() {
 	
 	}
 	
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
	private Integer weiboType;
	
	/**
	 *  
	 */
	private Integer type;
	
	/**
	 *  
	 */
	private Integer status;
	

	/**
	 * 
	 *
	 * @param userAccountId
	 */
	public SocialUserWeiboSendQuery setUserAccountId(String userAccountId){
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
	public SocialUserWeiboSendQuery setOwner(String owner){
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
	public SocialUserWeiboSendQuery setWeiboId(String weiboId){
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
	public SocialUserWeiboSendQuery setWeiboType(Integer weiboType){
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
	 * @param type
	 */
	public SocialUserWeiboSendQuery setType(Integer type){
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
	 * @param status
	 */
	public SocialUserWeiboSendQuery setStatus(Integer status){
		this.status = status;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getStatus(){
		return status;
	}
	

}