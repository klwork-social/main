package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class UserDataStatisticQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public UserDataStatisticQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String userId;
	
	/**
	 *  
	 */
	private Boolean dirty;
	

	/**
	 * 
	 *
	 * @param userId
	 */
	public UserDataStatisticQuery setUserId(String userId){
		this.userId = userId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserId(){
		return userId;
	}
	
	/**
	 * 
	 *
	 * @param dirty
	 */
	public UserDataStatisticQuery setDirty(Boolean dirty){
		this.dirty = dirty;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Boolean getDirty(){
		return dirty;
	}
	

}