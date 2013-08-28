package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class ResourcesAssignManagerQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public ResourcesAssignManagerQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String entityId;
	
	/**
	 *  
	 */
	private String entityType;
	
	/**
	 *  
	 */
	private String teamId;
	
	/**
	 *  
	 */
	private String type;
	

	/**
	 * 
	 *
	 * @param entityId
	 */
	public ResourcesAssignManagerQuery setEntityId(String entityId){
		this.entityId = entityId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getEntityId(){
		return entityId;
	}
	
	/**
	 * 
	 *
	 * @param entityType
	 */
	public ResourcesAssignManagerQuery setEntityType(String entityType){
		this.entityType = entityType;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getEntityType(){
		return entityType;
	}
	
	/**
	 * 
	 *
	 * @param teamId
	 */
	public ResourcesAssignManagerQuery setTeamId(String teamId){
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
	
	/**
	 * 
	 *
	 * @param type
	 */
	public ResourcesAssignManagerQuery setType(String type){
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
	

}