package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class OutsourcingProject implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public OutsourcingProject() {
 	}
 

	/**
	 *  
	 */
	private String id;
	/**
	 *  
	 */
	private java.util.Date creationDate;
	/**
	 *  
	 */
	private String ownUser;
	/**
	 *  
	 */
	private java.util.Date lastUpdate;
	/**
	 *  
	 */
	private String description;
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
	private String companyName;
	/**
	 *  
	 */
	private Double bounty;
	/**
	 *  
	 */
	private String prgStatus;
	/**
	 *  
	 */
	private java.util.Date deadline;
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
	 */
	private String pictureId;

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
	 * @param creationDate
	 */
	public void setCreationDate(java.util.Date creationDate){
		this.creationDate = creationDate;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getCreationDate(){
		return creationDate;
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
	/**
	 * 
	 *
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getDescription(){
		return description;
	}
	/**
	 * 
	 *
	 * @param relatedTodo
	 */
	public void setRelatedTodo(String relatedTodo){
		this.relatedTodo = relatedTodo;
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
	/**
	 * 
	 *
	 * @param companyName
	 */
	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getCompanyName(){
		return companyName;
	}
	/**
	 * 
	 *
	 * @param bounty
	 */
	public void setBounty(Double bounty){
		this.bounty = bounty;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Double getBounty(){
		return bounty;
	}
	/**
	 * 
	 *
	 * @param prgStatus
	 */
	public void setPrgStatus(String prgStatus){
		this.prgStatus = prgStatus;
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
	 * @param deadline
	 */
	public void setDeadline(java.util.Date deadline){
		this.deadline = deadline;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getDeadline(){
		return deadline;
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
	 * @param procInstId
	 */
	public void setProcInstId(String procInstId){
		this.procInstId = procInstId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getProcInstId(){
		return procInstId;
	}
	/**
	 * 
	 *
	 * @param pictureId
	 */
	public void setPictureId(String pictureId){
		this.pictureId = pictureId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getPictureId(){
		return pictureId;
	}
}