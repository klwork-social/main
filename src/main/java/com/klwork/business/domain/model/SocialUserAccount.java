package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserAccount implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public SocialUserAccount() {
 	}
 

	/**
	 *  
	 */
	private String id;
	/**
	 *  
	 */
	private String weiboUid;
	/**
	 *  
	 */
	private Integer status;
	/**
	 *  
	 */
	private Integer accountSelected;
	/**
	 *  
	 */
	private java.util.Date expiredTime;
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
	 */
	private java.util.Date lastUpdate;
	/**
	 *  
	 */
	private Integer type;
	/**
	 *  
	 */
	private String userScreenName;
	/**
	 *  
	 */
	private String url;

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
	 * @param weiboUid
	 */
	public void setWeiboUid(String weiboUid){
		this.weiboUid = weiboUid;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getWeiboUid(){
		return weiboUid;
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
	 * @param accountSelected
	 */
	public void setAccountSelected(Integer accountSelected){
		this.accountSelected = accountSelected;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getAccountSelected(){
		return accountSelected;
	}
	/**
	 * 
	 *
	 * @param expiredTime
	 */
	public void setExpiredTime(java.util.Date expiredTime){
		this.expiredTime = expiredTime;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getExpiredTime(){
		return expiredTime;
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
	 * @param userScreenName
	 */
	public void setUserScreenName(String userScreenName){
		this.userScreenName = userScreenName;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserScreenName(){
		return userScreenName;
	}
	/**
	 * 
	 *
	 * @param url
	 */
	public void setUrl(String url){
		this.url = url;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUrl(){
		return url;
	}
}