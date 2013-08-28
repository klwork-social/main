package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class TeamQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public TeamQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String ownUser;
	
	/**
	 *  
	 */
	private String inUser;
	
	/**
	 *  
	 */
	private String type;
	
	
	private String name;
	

	/**
	 * 
	 *
	 * @param ownUser
	 */
	public TeamQuery setOwnUser(String ownUser){
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
	 * @param type
	 */
	public TeamQuery setType(String type){
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

	public String getName() {
		return name;
	}

	public TeamQuery setName(String name) {
		this.name = name;
		return this;
	}

	public String getInUser() {
		return inUser;
	}

	public void setInUser(String inUser) {
		this.inUser = inUser;
	}
}