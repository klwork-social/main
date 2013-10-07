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
	
	//参与者
	private String participant;
	
	//参与的类型
	private String participantType;
	
	/**
	 *  
	 */
	private String flowType;
	

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

	public String getParticipant() {
		return participant;
	}

	public void setParticipant(String participant) {
		this.participant = participant;
	}

	public String getParticipantType() {
		return participantType;
	}

	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}
	
	/**
	 * 
	 *
	 * @param flowType
	 */
	public OutsourcingProjectQuery setFlowType(String flowType){
		this.flowType = flowType;
		return this;
	}
	
	/**
	 * 
	 *
	 * @return
	 */	
	public String getFlowType(){
		return flowType;
	}
}