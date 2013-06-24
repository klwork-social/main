package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class ProjectParticipant implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public ProjectParticipant() {
 	}
 

	/**
	 *  
	 */
	private String id;
	/**
	 *  
	 */
	private String userId;
	/**
	 *  
	 */
	private String outPrgId;
	/**
	 *  
	 */
	private String participantsType;
	/**
	 *  
	 */
	private String currentTaskId;
	/**
	 *  
	 */
	private Double score;
	/**
	 *  
	 */
	private String scoreUserId;
	/**
	 *  
	 */
	private Boolean isWinner;
	/**
	 *  
	 */
	private Double winningAmount;
	/**
	 *  
	 */
	private String handleStatus;
	/**
	 *  
	 */
	private String workAttachment;
	/**
	 *  
	 */
	private String procInstId;
	/**
	 *  
	 */
	private java.util.Date lastUpdate;
	/**
	 *  
	 */
	private String workComment;
	/**
	 *  
	 */
	private String winReson;
	/**
	 *  
	 */
	private String assessedTaskId;

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
	 * @param userId
	 */
	public void setUserId(String userId){
		this.userId = userId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserId(){
		return userId;
	}
	/**
	 * 
	 *
	 * @param outPrgId
	 */
	public void setOutPrgId(String outPrgId){
		this.outPrgId = outPrgId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getOutPrgId(){
		return outPrgId;
	}
	/**
	 * 
	 *
	 * @param participantsType
	 */
	public void setParticipantsType(String participantsType){
		this.participantsType = participantsType;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getParticipantsType(){
		return participantsType;
	}
	/**
	 * 
	 *
	 * @param currentTaskId
	 */
	public void setCurrentTaskId(String currentTaskId){
		this.currentTaskId = currentTaskId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getCurrentTaskId(){
		return currentTaskId;
	}
	/**
	 * 
	 *
	 * @param score
	 */
	public void setScore(Double score){
		this.score = score;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Double getScore(){
		return score;
	}
	/**
	 * 
	 *
	 * @param scoreUserId
	 */
	public void setScoreUserId(String scoreUserId){
		this.scoreUserId = scoreUserId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getScoreUserId(){
		return scoreUserId;
	}
	/**
	 * 
	 *
	 * @param isWinner
	 */
	public void setIsWinner(Boolean isWinner){
		this.isWinner = isWinner;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Boolean getIsWinner(){
		return isWinner;
	}
	/**
	 * 
	 *
	 * @param winningAmount
	 */
	public void setWinningAmount(Double winningAmount){
		this.winningAmount = winningAmount;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Double getWinningAmount(){
		return winningAmount;
	}
	/**
	 * 
	 *
	 * @param handleStatus
	 */
	public void setHandleStatus(String handleStatus){
		this.handleStatus = handleStatus;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getHandleStatus(){
		return handleStatus;
	}
	/**
	 * 
	 *
	 * @param workAttachment
	 */
	public void setWorkAttachment(String workAttachment){
		this.workAttachment = workAttachment;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getWorkAttachment(){
		return workAttachment;
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
	 * @param workComment
	 */
	public void setWorkComment(String workComment){
		this.workComment = workComment;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getWorkComment(){
		return workComment;
	}
	/**
	 * 
	 *
	 * @param winReson
	 */
	public void setWinReson(String winReson){
		this.winReson = winReson;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getWinReson(){
		return winReson;
	}
	/**
	 * 
	 *
	 * @param assessedTaskId
	 */
	public void setAssessedTaskId(String assessedTaskId){
		this.assessedTaskId = assessedTaskId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getAssessedTaskId(){
		return assessedTaskId;
	}
}