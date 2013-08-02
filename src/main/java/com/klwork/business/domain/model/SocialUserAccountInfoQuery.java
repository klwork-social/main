package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserAccountInfoQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public SocialUserAccountInfoQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String accountId;
	
	/**
	 *  
	 */
	private String type;
	
	/**
	 *  
	 */
	private String key;
	

	/**
	 * 
	 *
	 * @param accountId
	 */
	public SocialUserAccountInfoQuery setAccountId(String accountId){
		this.accountId = accountId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getAccountId(){
		return accountId;
	}
	
	/**
	 * 
	 *
	 * @param type
	 */
	public SocialUserAccountInfoQuery setType(String type){
		this.type = type;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getType(){
		return type;
	}
	
	/**
	 * 
	 *
	 * @param key
	 */
	public SocialUserAccountInfoQuery setKey(String key){
		this.key = key;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getKey(){
		return key;
	}
	

}