package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class TeamMembershipQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public TeamMembershipQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String userId;
	
	/**
	 *  
	 */
	private String teamId;
	

	/**
	 * 
	 *
	 * @param userId
	 */
	public TeamMembershipQuery setUserId(String userId){
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
	 * @param teamId
	 */
	public TeamMembershipQuery setTeamId(String teamId){
		this.teamId = teamId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getTeamId(){
		return teamId;
	}
	

}