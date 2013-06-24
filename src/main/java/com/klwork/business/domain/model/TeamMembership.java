package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class TeamMembership implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public TeamMembership() {
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
	public void setUserId(String userId){
		this.userId = userId;
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
	public void setTeamId(String teamId){
		this.teamId = teamId;
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