package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class OutsourcingProjectQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public OutsourcingProjectQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String ownUser;
	
	/**
	 *  
	 */
	private String relatedTodo;
	
	/**
	 *  
	 */
	private String name;
	
	/**
	 *  
	 */
	private String prgStatus;
	
	/**
	 *  
	 */
	private String type;
	
	/**
	 *  
	 */
	private String procInstId;
	

	/**
	 * 
	 *
	 * @param ownUser
	 */
	public OutsourcingProjectQuery setOwnUser(String ownUser){
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
	 * @param relatedTodo
	 */
	public OutsourcingProjectQuery setRelatedTodo(String relatedTodo){
		this.relatedTodo = relatedTodo;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getRelatedTodo(){
		return relatedTodo;
	}
	
	/**
	 * 
	 *
	 * @param name
	 */
	public OutsourcingProjectQuery setName(String name){
		this.name = name;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 *
	 * @param prgStatus
	 */
	public OutsourcingProjectQuery setPrgStatus(String prgStatus){
		this.prgStatus = prgStatus;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getPrgStatus(){
		return prgStatus;
	}
	
	/**
	 * 
	 *
	 * @param type
	 */
	public OutsourcingProjectQuery setType(String type){
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
	 * @param procInstId
	 */
	public OutsourcingProjectQuery setProcInstId(String procInstId){
		this.procInstId = procInstId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getProcInstId(){
		return procInstId;
	}
	

}