package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class Todo implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public Todo() {
 	}
 

	/**
	 *  
	 */
	private String id;
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
	private java.util.Date creationDate;
	/**
	 *  
	 */
	private String description;
	/**
	 *  
	 */
	private Integer priority;
	/**
	 *  
	 */
	private Integer completed;
	/**
	 *  
	 */
	private java.util.Date completionDate;
	/**
	 *  
	 */
	private String proId;
	/**
	 *  
	 */
	private java.util.Date dueDate;
	/**
	 *  
	 */
	private String assignedUser;
	/**
	 *  
	 */
	private java.util.Date lastUpdate;
	/**
	 *  
	 */
	private Integer useup;
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
	private String tags;
	/**
	 *  
	 */
	private String relatedTaskId;
	/**
	 *  
	 */
	private boolean relatedTask;
	/**
	 *  
	 */
	private String name;
	/**
	 *  
	 */
	private java.util.Date startDate;
	/**
	 *  
	 */
	private Double estimate;
	/**
	 *  
	 */
	private Integer estimateUnit;
	/**
	 *  
	 */
	private boolean relatedCalendar;

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
	 * @param pid
	 */
	public void setPid(String pid){
		this.pid = pid;
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
	public void setIsContainer(Integer isContainer){
		this.isContainer = isContainer;
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
	 * @param priority
	 */
	public void setPriority(Integer priority){
		this.priority = priority;
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
	 * @param completed
	 */
	public void setCompleted(Integer completed){
		this.completed = completed;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getCompleted(){
		return completed;
	}
	/**
	 * 
	 *
	 * @param completionDate
	 */
	public void setCompletionDate(java.util.Date completionDate){
		this.completionDate = completionDate;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getCompletionDate(){
		return completionDate;
	}
	/**
	 * 
	 *
	 * @param proId
	 */
	public void setProId(String proId){
		this.proId = proId;
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
	 * @param dueDate
	 */
	public void setDueDate(java.util.Date dueDate){
		this.dueDate = dueDate;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getDueDate(){
		return dueDate;
	}
	/**
	 * 
	 *
	 * @param assignedUser
	 */
	public void setAssignedUser(String assignedUser){
		this.assignedUser = assignedUser;
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
	 * @param useup
	 */
	public void setUseup(Integer useup){
		this.useup = useup;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getUseup(){
		return useup;
	}
	/**
	 * 
	 *
	 * @param status
	 */
	public void setStatus(Integer status){
		this.status = status;
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
	public void setType(Integer type){
		this.type = type;
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
	 * @param tags
	 */
	public void setTags(String tags){
		this.tags = tags;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getTags(){
		return tags;
	}
	/**
	 * 
	 *
	 * @param relatedTaskId
	 */
	public void setRelatedTaskId(String relatedTaskId){
		this.relatedTaskId = relatedTaskId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getRelatedTaskId(){
		return relatedTaskId;
	}
	/**
	 * 
	 *
	 * @param relatedTask
	 */
	public void setRelatedTask(boolean relatedTask){
		this.relatedTask = relatedTask;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public boolean getRelatedTask(){
		return relatedTask;
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
	 * @param startDate
	 */
	public void setStartDate(java.util.Date startDate){
		this.startDate = startDate;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getStartDate(){
		return startDate;
	}
	/**
	 * 
	 *
	 * @param estimate
	 */
	public void setEstimate(Double estimate){
		this.estimate = estimate;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Double getEstimate(){
		return estimate;
	}
	/**
	 * 
	 *
	 * @param estimateUnit
	 */
	public void setEstimateUnit(Integer estimateUnit){
		this.estimateUnit = estimateUnit;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getEstimateUnit(){
		return estimateUnit;
	}
	/**
	 * 
	 *
	 * @param relatedCalendar
	 */
	public void setRelatedCalendar(boolean relatedCalendar){
		this.relatedCalendar = relatedCalendar;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public boolean getRelatedCalendar(){
		return relatedCalendar;
	}
}