package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserAccountQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public SocialUserAccountQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String weiboUid;
	
	/**
	 *  
	 */
	private Integer status;
	
	/**
	 *  
	 */
	private Integer accountSelected;
	
	/**
	 *  
	 */
	private String ownUser;
	
	/**
	 *  
	 */
	private String name;
	
	/**
	 *  
	 */
	private Integer type;
	

	/**
	 * 
	 *
	 * @param weiboUid
	 */
	public SocialUserAccountQuery setWeiboUid(String weiboUid){
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
	 * @param status
	 */
	public SocialUserAccountQuery setStatus(Integer status){
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
	
	/**
	 * 
	 *
	 * @param accountSelected
	 */
	public SocialUserAccountQuery setAccountSelected(Integer accountSelected){
		this.accountSelected = accountSelected;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getAccountSelected(){
		return accountSelected;
	}
	
	/**
	 * 
	 *
	 * @param ownUser
	 */
	public SocialUserAccountQuery setOwnUser(String ownUser){
		this.ownUser = ownUser;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getOwnUser(){
		return ownUser;
	}
	
	/**
	 * 
	 *
	 * @param name
	 */
	public SocialUserAccountQuery setName(String name){
		this.name = name;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 *
	 * @param type
	 */
	public SocialUserAccountQuery setType(Integer type){
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
	

}