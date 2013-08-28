package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUseAuthorityListQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public SocialUseAuthorityListQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String managerGroupId;
	
	/**
	 *  
	 */
	private String key;
	

	/**
	 * 
	 *
	 * @param managerGroupId
	 */
	public SocialUseAuthorityListQuery setManagerGroupId(String managerGroupId){
		this.managerGroupId = managerGroupId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getManagerGroupId(){
		return managerGroupId;
	}
	
	/**
	 * 
	 *
	 * @param key
	 */
	public SocialUseAuthorityListQuery setKey(String key){
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