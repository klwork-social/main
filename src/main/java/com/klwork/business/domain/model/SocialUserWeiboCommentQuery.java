package com.klwork.business.domain.model;

import com.klwork.common.dao.QueryParameter;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserWeiboCommentQuery extends QueryParameter{
 	private static final long serialVersionUID = 1L;
 	
 	public SocialUserWeiboCommentQuery() {
 	
 	}
 	
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
	private String commentUid;
	
	/**
	 *  
	 */
	private String userName;
	
	/**
	 *  
	 */
	private String statusWeiboId;
	
	/**
	 *  
	 */
	private String statusUserUid;
	
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
	 * @param userAccountId
	 */
	public SocialUserWeiboCommentQuery setUserAccountId(String userAccountId){
		this.userAccountId = userAccountId;
		return this;
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
	public SocialUserWeiboCommentQuery setOwner(String owner){
		this.owner = owner;
		return this;
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
	 * @param commentUid
	 */
	public SocialUserWeiboCommentQuery setCommentUid(String commentUid){
		this.commentUid = commentUid;
		return this;
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
	 * @param userName
	 */
	public SocialUserWeiboCommentQuery setUserName(String userName){
		this.userName = userName;
		return this;
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
	 * @param statusWeiboId
	 */
	public SocialUserWeiboCommentQuery setStatusWeiboId(String statusWeiboId){
		this.statusWeiboId = statusWeiboId;
		return this;
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
	 * @param statusUserUid
	 */
	public SocialUserWeiboCommentQuery setStatusUserUid(String statusUserUid){
		this.statusUserUid = statusUserUid;
		return this;
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
	 * @param commentType
	 */
	public SocialUserWeiboCommentQuery setCommentType(Integer commentType){
		this.commentType = commentType;
		return this;
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
	public SocialUserWeiboCommentQuery setType(Integer type){
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
	

}