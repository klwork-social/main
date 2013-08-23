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
	private String sourceUrl;
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
	 *  
	 */
	private String thumbnailPic;
	/**
	 *  
	 */
	private String thumbnailPicSize;
	/**
	 *  
	 */
	private String originalPic;
	/**
	 *  
	 */
	private String originalPicSize;
	/**
	 *  
	 */
	private String bmiddlePic;
	/**
	 *  
	 */
	private String bmiddlePicSize;
	/**
	 *  
	 */
	private Integer userVerified;
	/**
	 *  微博分类，数据字典6（全部微博，我的微博，@我的微博......
	 */
	private Integer weiboType;
	/**
	 *  微博处理结果类型，原始贴，还是非原始贴
	 */
	private Integer weiboHandleType;
	/**
	 * 新浪微博还是腾讯微博
	 */
	private Integer type;
	/**
	 *  微博的状态
	 */
	private Integer weiboStatus;
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
	 * @param sourceUrl
	 */
	public void setSourceUrl(String sourceUrl){
		this.sourceUrl = sourceUrl;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getSourceUrl(){
		return sourceUrl;
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
	 * @param thumbnailPicSize
	 */
	public void setThumbnailPicSize(String thumbnailPicSize){
		this.thumbnailPicSize = thumbnailPicSize;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getThumbnailPicSize(){
		return thumbnailPicSize;
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
	 * @param originalPicSize
	 */
	public void setOriginalPicSize(String originalPicSize){
		this.originalPicSize = originalPicSize;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getOriginalPicSize(){
		return originalPicSize;
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
	 * @param bmiddlePicSize
	 */
	public void setBmiddlePicSize(String bmiddlePicSize){
		this.bmiddlePicSize = bmiddlePicSize;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public String getBmiddlePicSize(){
		return bmiddlePicSize;
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
	 * @param weiboStatus
	 */
	public void setWeiboStatus(Integer weiboStatus){
		this.weiboStatus = weiboStatus;
	}
	/**
	 * 
	 *
	 * @return
	 */	
	public Integer getWeiboStatus(){
		return weiboStatus;
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