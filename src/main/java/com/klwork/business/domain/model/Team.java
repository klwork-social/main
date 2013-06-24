package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class Team implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public Team() {
 	}
 

	/**
	 *  
	 */
	private String id;
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
	 * @param ownUser
	 */
	public void setOwnUser(String ownUser){
		this.ownUser = ownUser;
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
	public void setName(String name){
		this.name = name;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getName(){
		return name;
	}
}