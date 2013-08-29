package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserWeiboSend implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public SocialUserWeiboSend() {
 	}
 

	/**
	 *  
	 */
	private String id;
	/**
	 *  
	 */
	private String userAccountId;
	/**
	 *  
	 */
	private String owner;
	/**
	 *  
	 */
	private java.util.Date createTime;
	/**
	 *  
	 */
	private String weiboId;
	/**
	 *  
	 */
	private String shortText;
	/**
	 *  
	 */
	private String text;
	/**
	 *  
	 */
	private Integer weiboType;
	/**
	 *  
	 */
	private Integer type;
	/**
	 *  
	 */
	private Integer status;
	/**
	 *  
	 */
	private java.util.Date planSendTime;
	/**
	 *  
	 */
	private java.util.Date sendTime;
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
	 * @param userAccountId
	 */
	public void setUserAccountId(String userAccountId){
		this.userAccountId = userAccountId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserAccountId(){
		return userAccountId;
	}
	/**
	 * 
	 *
	 * @param owner
	 */
	public void setOwner(String owner){
		this.owner = owner;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getOwner(){
		return owner;
	}
	/**
	 * 
	 *
	 * @param createTime
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getCreateTime(){
		return createTime;
	}
	/**
	 * 
	 *
	 * @param weiboId
	 */
	public void setWeiboId(String weiboId){
		this.weiboId = weiboId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getWeiboId(){
		return weiboId;
	}
	/**
	 * 
	 *
	 * @param shortText
	 */
	public void setShortText(String shortText){
		this.shortText = shortText;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getShortText(){
		return shortText;
	}
	/**
	 * 
	 *
	 * @param text
	 */
	public void setText(String text){
		this.text = text;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getText(){
		return text;
	}
	/**
	 * 
	 *
	 * @param weiboType
	 */
	public void setWeiboType(Integer weiboType){
		this.weiboType = weiboType;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getWeiboType(){
		return weiboType;
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
	 * @param planSendTime
	 */
	public void setPlanSendTime(java.util.Date planSendTime){
		this.planSendTime = planSendTime;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getPlanSendTime(){
		return planSendTime;
	}
	/**
	 * 
	 *
	 * @param sendTime
	 */
	public void setSendTime(java.util.Date sendTime){
		this.sendTime = sendTime;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getSendTime(){
		return sendTime;
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