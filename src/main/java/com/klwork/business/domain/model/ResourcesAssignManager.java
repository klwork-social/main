package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class ResourcesAssignManager implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public ResourcesAssignManager() {
 	}
 

	/**
	 *  
	 */
	private String id;
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
	 */
	private java.util.Date lastUpdate;

	/**
	 * 
	 *
	 * @param id
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getId(){
		return id;
	}
	/**
	 * 
	 *
	 * @param entityId
	 */
	public void setEntityId(String entityId){
		this.entityId = entityId;
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
	public void setEntityType(String entityType){
		this.entityType = entityType;
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
	/**
	 * 
	 *
	 * @param type
	 */
	public void setType(String type){
		this.type = type;
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
	 * @param lastUpdate
	 */
	public void setLastUpdate(java.util.Date lastUpdate){
		this.lastUpdate = lastUpdate;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getLastUpdate(){
		return lastUpdate;
	}
	public ResourcesAssignManager(String entityId, String entityType,
			String teamId, String type) {
		super();
		this.entityId = entityId;
		this.entityType = entityType;
		this.teamId = teamId;
		this.type = type;
	}
}