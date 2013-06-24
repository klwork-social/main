package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class TodoQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public TodoQuery() {
 	
 	}
 	
 	/**
	 *  
	 */
	private String pid;
	
	/**
	 *  
	 */
	private Integer isContainer;
	
	/**
	 *  
	 */
	private Integer priority;
	
	/**
	 *  
	 */
	private String proId;
	
	/**
	 *  
	 */
	private String assignedUser;
	
	/**
	 *  
	 */
	private Integer status;
	
	/**
	 *  
	 */
	private Integer type;
	
	/**
	 *  
	 */
	private String relatedTask;
	
	/**
	 *  
	 */
	private String relatedCalendar;
	

	/**
	 * 
	 *
	 * @param pid
	 */
	public TodoQuery setPid(String pid){
		this.pid = pid;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getPid(){
		return pid;
	}
	
	/**
	 * 
	 *
	 * @param isContainer
	 */
	public TodoQuery setIsContainer(Integer isContainer){
		this.isContainer = isContainer;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getIsContainer(){
		return isContainer;
	}
	
	/**
	 * 
	 *
	 * @param priority
	 */
	public TodoQuery setPriority(Integer priority){
		this.priority = priority;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getPriority(){
		return priority;
	}
	
	/**
	 * 
	 *
	 * @param proId
	 */
	public TodoQuery setProId(String proId){
		this.proId = proId;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getProId(){
		return proId;
	}
	
	/**
	 * 
	 *
	 * @param assignedUser
	 */
	public TodoQuery setAssignedUser(String assignedUser){
		this.assignedUser = assignedUser;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getAssignedUser(){
		return assignedUser;
	}
	
	/**
	 * 
	 *
	 * @param status
	 */
	public TodoQuery setStatus(Integer status){
		this.status = status;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getStatus(){
		return status;
	}
	
	/**
	 * 
	 *
	 * @param type
	 */
	public TodoQuery setType(Integer type){
		this.type = type;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getType(){
		return type;
	}
	
	/**
	 * 
	 *
	 * @param relatedTask
	 */
	public TodoQuery setRelatedTask(String relatedTask){
		this.relatedTask = relatedTask;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getRelatedTask(){
		return relatedTask;
	}
	
	/**
	 * 
	 *
	 * @param relatedCalendar
	 */
	public TodoQuery setRelatedCalendar(String relatedCalendar){
		this.relatedCalendar = relatedCalendar;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getRelatedCalendar(){
		return relatedCalendar;
	}
	

}