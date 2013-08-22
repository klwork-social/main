package com.klwork.business.domain.model;


import java.io.Serializable;

/**
 * 
 * @version 1.0
 * @created ${plugin.now}
 * @author ww
 * 
 */
public class SocialUserWeibo implements Serializable 
 {
 	private static final long serialVersionUID = 1L;
 	public SocialUserWeibo() {
 	}
 

	/**
	 *  
	 */
	private String id;
	/**
	 *  
	 */
	private String retweetedId;
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
	private String weiboId;
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
	private Integer favorited;
	/**
	 *  
	 */
	private Integer truncated;
	/**
	 *  
	 */
	private String inReplyToStatusId;
	/**
	 *  
	 */
	private String inReplyToUserId;
	/**
	 *  
	 */
	private String inReplyToScreenName;
	/**
	 *  
	 */
	private String mid;
	/**
	 *  
	 */
	private Integer repostsCount;
	/**
	 *  
	 */
	private Integer commentsCount;
	/**
	 *  
	 */
	private Integer weiboUidFollower;
	/**
	 *  
	 */
	private String weiboUid;
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
	 *  缩略图
	 */
	private String thumbnailPic;
	/**
	 *  原始
	 */
	private String originalPic;
	/**
	 *  大
	 */
	private String bmiddlePic;
	/**
	 *  
	 */
	private Integer userVerified;
	/**
	 *  
	 */
	private Integer weiboType;
	/**
	 *  
	 */
	private Integer weiboHandleType;
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
	 * @param retweetedId
	 */
	public void setRetweetedId(String retweetedId){
		this.retweetedId = retweetedId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getRetweetedId(){
		return retweetedId;
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
	 * @param favorited
	 */
	public void setFavorited(Integer favorited){
		this.favorited = favorited;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getFavorited(){
		return favorited;
	}
	/**
	 * 
	 *
	 * @param truncated
	 */
	public void setTruncated(Integer truncated){
		this.truncated = truncated;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getTruncated(){
		return truncated;
	}
	/**
	 * 
	 *
	 * @param inReplyToStatusId
	 */
	public void setInReplyToStatusId(String inReplyToStatusId){
		this.inReplyToStatusId = inReplyToStatusId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getInReplyToStatusId(){
		return inReplyToStatusId;
	}
	/**
	 * 
	 *
	 * @param inReplyToUserId
	 */
	public void setInReplyToUserId(String inReplyToUserId){
		this.inReplyToUserId = inReplyToUserId;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getInReplyToUserId(){
		return inReplyToUserId;
	}
	/**
	 * 
	 *
	 * @param inReplyToScreenName
	 */
	public void setInReplyToScreenName(String inReplyToScreenName){
		this.inReplyToScreenName = inReplyToScreenName;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getInReplyToScreenName(){
		return inReplyToScreenName;
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
	 * @param repostsCount
	 */
	public void setRepostsCount(Integer repostsCount){
		this.repostsCount = repostsCount;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getRepostsCount(){
		return repostsCount;
	}
	/**
	 * 
	 *
	 * @param commentsCount
	 */
	public void setCommentsCount(Integer commentsCount){
		this.commentsCount = commentsCount;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getCommentsCount(){
		return commentsCount;
	}
	/**
	 * 
	 *
	 * @param weiboUidFollower
	 */
	public void setWeiboUidFollower(Integer weiboUidFollower){
		this.weiboUidFollower = weiboUidFollower;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getWeiboUidFollower(){
		return weiboUidFollower;
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
	 * @param thumbnailPic
	 */
	public void setThumbnailPic(String thumbnailPic){
		this.thumbnailPic = thumbnailPic;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getThumbnailPic(){
		return thumbnailPic;
	}
	/**
	 * 
	 *
	 * @param originalPic
	 */
	public void setOriginalPic(String originalPic){
		this.originalPic = originalPic;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getOriginalPic(){
		return originalPic;
	}
	/**
	 * 
	 *
	 * @param bmiddlePic
	 */
	public void setBmiddlePic(String bmiddlePic){
		this.bmiddlePic = bmiddlePic;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getBmiddlePic(){
		return bmiddlePic;
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
	 * @param weiboHandleType
	 */
	public void setWeiboHandleType(Integer weiboHandleType){
		this.weiboHandleType = weiboHandleType;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getWeiboHandleType(){
		return weiboHandleType;
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
}