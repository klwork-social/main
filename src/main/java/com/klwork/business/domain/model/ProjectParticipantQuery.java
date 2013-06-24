package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class ProjectParticipantQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public ProjectParticipantQuery() {
 	
 	}
 	
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
	private String scoreUserId;
	
	/**
	 *  
	 */
	private Boolean isWinner;
	
	/**
	 *  
	 */
	private String handleStatus;
	
	/**
	 *  
	 */
	private String procInstId;
	
	/**
	 *  
	 */
	private String assessedTaskId;
	

	/**
	 * 
	 *
	 * @param userId
	 */
	public ProjectParticipantQuery setUserId(String userId){
		this.userId = userId;
		return this;
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
	public ProjectParticipantQuery setOutPrgId(String outPrgId){
		this.outPrgId = outPrgId;
		return this;
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
	public ProjectParticipantQuery setParticipantsType(String participantsType){
		this.participantsType = participantsType;
		return this;
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
	 * @param scoreUserId
	 */
	public ProjectParticipantQuery setScoreUserId(String scoreUserId){
		this.scoreUserId = scoreUserId;
		return this;
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
	public ProjectParticipantQuery setIsWinner(Boolean isWinner){
		this.isWinner = isWinner;
		return this;
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
	 * @param handleStatus
	 */
	public ProjectParticipantQuery setHandleStatus(String handleStatus){
		this.handleStatus = handleStatus;
		return this;
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
	 * @param procInstId
	 */
	public ProjectParticipantQuery setProcInstId(String procInstId){
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
	
	/**
	 * 
	 *
	 * @param assessedTaskId
	 */
	public ProjectParticipantQuery setAssessedTaskId(String assessedTaskId){
		this.assessedTaskId = assessedTaskId;
		return this;
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