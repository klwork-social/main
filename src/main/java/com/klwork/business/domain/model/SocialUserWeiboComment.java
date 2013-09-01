package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserWeiboComment implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public SocialUserWeiboComment() {
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
	private java.util.Date createAt;
	/**
	 *  
	 */
	private String commentId;
	/**
	 *  
	 */
	private String text;
	/**
	 *  
	 */
	private String source;
	/**
	 *  
	 */
	private String mid;
	/**
	 *  
	 */
	private String commentUid;
	/**
	 *  
	 */
	private String userScreenName;
	/**
	 *  
	 */
	private String userName;
	/**
	 *  
	 */
	private String userProfileImageUrl;
	/**
	 *  
	 */
	private String userDomain;
	/**
	 *  
	 */
	private Integer userVerified;
	/**
	 *  
	 */
	private String relUserWeiboId;
	/**
	 *  
	 */
	private String statusWeiboId;
	/**
	 *  
	 */
	private java.util.Date statusCreatedAt;
	/**
	 *  
	 */
	private String statusText;
	/**
	 *  
	 */
	private String statusSource;
	/**
	 *  
	 */
	private String statusMid;
	/**
	 *  
	 */
	private String statusUserUid;
	/**
	 *  
	 */
	private String statusUserScreenName;
	/**
	 *  
	 */
	private String statusUserName;
	/**
	 *  
	 */
	private String statusUserDomain;
	/**
	 *  
	 */
	private Integer statusUserVerified;
	/**
	 *  
	 */
	private Integer commentType;
	/**
	 *  
	 */
	private Integer type;

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
	 * @param createAt
	 */
	public void setCreateAt(java.util.Date createAt){
		this.createAt = createAt;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getCreateAt(){
		return createAt;
	}
	/**
	 * 
	 *
	 * @param commentId
	 */
	public void setCommentId(String commentId){
		this.commentId = commentId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getCommentId(){
		return commentId;
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
	 * @param source
	 */
	public void setSource(String source){
		this.source = source;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getSource(){
		return source;
	}
	/**
	 * 
	 *
	 * @param mid
	 */
	public void setMid(String mid){
		this.mid = mid;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getMid(){
		return mid;
	}
	/**
	 * 
	 *
	 * @param commentUid
	 */
	public void setCommentUid(String commentUid){
		this.commentUid = commentUid;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getCommentUid(){
		return commentUid;
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
	 * @param userName
	 */
	public void setUserName(String userName){
		this.userName = userName;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserName(){
		return userName;
	}
	/**
	 * 
	 *
	 * @param userProfileImageUrl
	 */
	public void setUserProfileImageUrl(String userProfileImageUrl){
		this.userProfileImageUrl = userProfileImageUrl;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserProfileImageUrl(){
		return userProfileImageUrl;
	}
	/**
	 * 
	 *
	 * @param userDomain
	 */
	public void setUserDomain(String userDomain){
		this.userDomain = userDomain;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getUserDomain(){
		return userDomain;
	}
	/**
	 * 
	 *
	 * @param userVerified
	 */
	public void setUserVerified(Integer userVerified){
		this.userVerified = userVerified;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getUserVerified(){
		return userVerified;
	}
	/**
	 * 
	 *
	 * @param relUserWeiboId
	 */
	public void setRelUserWeiboId(String relUserWeiboId){
		this.relUserWeiboId = relUserWeiboId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getRelUserWeiboId(){
		return relUserWeiboId;
	}
	/**
	 * 
	 *
	 * @param statusWeiboId
	 */
	public void setStatusWeiboId(String statusWeiboId){
		this.statusWeiboId = statusWeiboId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getStatusWeiboId(){
		return statusWeiboId;
	}
	/**
	 * 
	 *
	 * @param statusCreatedAt
	 */
	public void setStatusCreatedAt(java.util.Date statusCreatedAt){
		this.statusCreatedAt = statusCreatedAt;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public java.util.Date getStatusCreatedAt(){
		return statusCreatedAt;
	}
	/**
	 * 
	 *
	 * @param statusText
	 */
	public void setStatusText(String statusText){
		this.statusText = statusText;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getStatusText(){
		return statusText;
	}
	/**
	 * 
	 *
	 * @param statusSource
	 */
	public void setStatusSource(String statusSource){
		this.statusSource = statusSource;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getStatusSource(){
		return statusSource;
	}
	/**
	 * 
	 *
	 * @param statusMid
	 */
	public void setStatusMid(String statusMid){
		this.statusMid = statusMid;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getStatusMid(){
		return statusMid;
	}
	/**
	 * 
	 *
	 * @param statusUserUid
	 */
	public void setStatusUserUid(String statusUserUid){
		this.statusUserUid = statusUserUid;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getStatusUserUid(){
		return statusUserUid;
	}
	/**
	 * 
	 *
	 * @param statusUserScreenName
	 */
	public void setStatusUserScreenName(String statusUserScreenName){
		this.statusUserScreenName = statusUserScreenName;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getStatusUserScreenName(){
		return statusUserScreenName;
	}
	/**
	 * 
	 *
	 * @param statusUserName
	 */
	public void setStatusUserName(String statusUserName){
		this.statusUserName = statusUserName;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getStatusUserName(){
		return statusUserName;
	}
	/**
	 * 
	 *
	 * @param statusUserDomain
	 */
	public void setStatusUserDomain(String statusUserDomain){
		this.statusUserDomain = statusUserDomain;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getStatusUserDomain(){
		return statusUserDomain;
	}
	/**
	 * 
	 *
	 * @param statusUserVerified
	 */
	public void setStatusUserVerified(Integer statusUserVerified){
		this.statusUserVerified = statusUserVerified;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getStatusUserVerified(){
		return statusUserVerified;
	}
	/**
	 * 
	 *
	 * @param commentType
	 */
	public void setCommentType(Integer commentType){
		this.commentType = commentType;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getCommentType(){
		return commentType;
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
}