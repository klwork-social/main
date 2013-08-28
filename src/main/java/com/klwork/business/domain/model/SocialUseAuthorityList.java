package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUseAuthorityList implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public SocialUseAuthorityList() {
 	}
 

	/**
	 *  
	 */
	private String id;
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
	 * @param managerGroupId
	 */
	public void setManagerGroupId(String managerGroupId){
		this.managerGroupId = managerGroupId;
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
	public void setKey(String key){
		this.key = key;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getKey(){
		return key;
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
}