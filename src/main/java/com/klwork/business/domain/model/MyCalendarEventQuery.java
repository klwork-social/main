package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class MyCalendarEventQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public MyCalendarEventQuery() {
 	
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
	 *
	 * @param ownUser
	 */
	public MyCalendarEventQuery setOwnUser(String ownUser){
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
	public MyCalendarEventQuery setRelatedTodo(String relatedTodo){
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
	

}