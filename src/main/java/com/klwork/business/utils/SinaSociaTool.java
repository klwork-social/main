package com.klwork.business.utils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import weibo4j.Comments;
import weibo4j.Timeline;
import weibo4j.http.ImageItem;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;
import weibo4j.util.WeiboContentTransLate;

import com.klwork.business.domain.model.WeiboForwardSend;
import com.klwork.common.utils.FileUtil;
import com.klwork.common.utils.UriUtility;

public class SinaSociaTool {

	/**
	 * 获取当前登录用户及其所关注用户的最新微博
	 * 
	 * @param accessToken
	 * @param baseAPP
	 *            是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @param paging
	 *            相关分页参数
	 * 
	 **/
	public static List<Status> findDsrmAccountRelationWeiboInfo(
			String accessToken, Paging paging, Integer baseAPP, Integer feature) {
		Timeline timeline = new Timeline();
		timeline.client.setToken(accessToken);
		List<Status> list = null;
		try {
			StatusWapper statusWapper = timeline.getHomeTimeline(baseAPP,
					feature, paging);
			if (null != statusWapper) {
				list = statusWapper.getStatuses();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * @Description : 获取最新的提到登录用户的微博列表，即@我的微博
	 * @param accessToken
	 * @param page
	 *            返回结果的页码，默认为1。
	 * @param filter_by_author
	 *            作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 * @param filter_by_source
	 *            来源筛选类型，0：全部、1：来自微博、2：来自微群，默认为0。
	 * @param filter_by_type
	 *            原创筛选类型，0：全部微博、1：原创的微博，默认为0。
	 * @return
	 **/
	public static List<Status> findMentionsAccountWeiboInfo(String accessToken,
			Paging page, Integer filter_by_author, Integer filter_by_source,
			Integer filter_by_type) {

		Timeline timeline = new Timeline();
		timeline.client.setToken(accessToken);
		List<Status> list = null;
		try {

			StatusWapper statusWapper = timeline.getMentions(page,
					filter_by_author, filter_by_source, filter_by_type);
			if (null != statusWapper) {
				list = statusWapper.getStatuses();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取用户发布的微博(原创)
	 * 
	 * @param accessToken
	 * @param uid
	 *            需要查询的用户ID。
	 * @param page
	 *            返回结果的页码，默认为1。
	 * @param base_app
	 *            是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
	 * @param feature
	 *            过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
	 * @return
	 **/
	public static List<Status> findDsrmAccountWeiboInfo(String accessToken,
			String uid, Paging page, Integer base_app, Integer feature) {

		Timeline timeline = new Timeline();
		timeline.client.setToken(accessToken);
		List<Status> list = null;
		try {
			StatusWapper statusWapper = timeline.getUserTimelineByUid(uid,
					page, base_app, feature);
			if (null != statusWapper) {
				list = statusWapper.getStatuses();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String textTranslate(String text) {
		if (text == null || text.trim().length() == 0) {
			return null;
		}
		return WeiboContentTransLate.transLateall(text, "blue");
	}

	public static String generateAuthorizationURL(String state) {
		String clientId = SocialConfig.getString("client_id");
		String clinetSecret = SocialConfig.getString("clinet_secret");
		String redirectUrl = SocialConfig.getString("go_back");
		//
		// String r = "http%3a%2f%2f127.0.0.1%2fks%2fuser%2fweibo-login";
		// r = "http://127.0.0.1/ks/user/weibo-login";
		String r = UriUtility.encode(redirectUrl, "utf-8");
		String url = "https://api.weibo.com/oauth2/authorize?response_type=code&client_id="
				+ clientId + "&redirect_uri=" + r + "&state=" + state;
		if("reset-oauth".equals(state)){
			url += "&forcelogin=true";
		}
		return url;
	}

	/**
	 * 微博转发
	 * 
	 * @param weiboForwardSend
	 * @param assessToken
	 * @return
	 */
	public static int forwardWeibo(WeiboForwardSend weiboForwardSend,
			String assessToken) {
		Timeline timeline = new Timeline();
		timeline.client.setToken(assessToken);
		try {
			timeline.Repost(weiboForwardSend.getWeibId(),
					weiboForwardSend.getContent(),
					weiboForwardSend.getCommentType());
		} catch (WeiboException e) {

			e.printStackTrace();
			return -1;
		}
		return 0;
	}

	/**
	 * 评论微博
	 * 
	 * @param weiboForwardSend
	 * @param assessToken
	 * @return
	 */
	public static int discussWeibo(WeiboForwardSend weiboForwardSend,
			String assessToken) {
		try {
			Comments comments = new Comments();
			comments.client.setToken(assessToken);
			comments.createComment(weiboForwardSend.getContent(),
					weiboForwardSend.getWeibId(),
					weiboForwardSend.getCommentType());// 是否评论给原微博
			if ("1".equals(weiboForwardSend.getRepostType() + "")) {// 同时转发一条微博
				Timeline timeline = new Timeline();
				timeline.client.setToken(assessToken);
				timeline.Repost(weiboForwardSend.getWeibId(),
						weiboForwardSend.getContent(), 0);
			}
		} catch (WeiboException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	/**
	 * 删除微博
	 * @param weiboId
	 * @param assessToken
	 * @return
	 */
	public static int deleteWeibo(String weiboId, String assessToken) {
		try {
			Timeline timeline = new Timeline();
			timeline.client.setToken(assessToken);
			timeline.Destroy(weiboId);
		} catch (WeiboException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	/**
	 * 发送微博
	 * @param text
	 * @param assessToken
	 * @return
	 */
	public static int sendWeibo(String text, String assessToken) {
		try {
			Timeline timeline = new Timeline();
			timeline.client.setToken(assessToken);
			Status status = timeline.UpdateStatus(text);
		} catch (WeiboException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
		
	}
	
	
	/**
	 * 发送微博
	 * @param text
	 * @param assessToken
	 * @return
	 */
	public static int sendWeiboAndImage(String text, String imageUrl, String assessToken) {
		try {
			Timeline timeline = new Timeline();
			timeline.client.setToken(assessToken);
			byte[] content= FileUtil.readFileImage(imageUrl);
			String factText = URLEncoder.encode(text, "UTF-8");
			ImageItem pic=new ImageItem("pic",content);
			Status status = timeline.UploadStatus(factText,pic);
		} catch (WeiboException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 1;
		
	}
	/**
	 * 
	 *  获取当前登录用户所接收到的评论列表
	 *  @param accessToken
	 *  @param page
	 *            返回结果的页码，默认为1。
	 *  @param filter_by_author
	 *            作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 *  @param filter_by_source
	 *            来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0。
	 *  @return
	 **/
	public static List<Comment> findCommentToMeWeiboInfo(String accessToken,Paging page, Integer filter_by_source,
			Integer filter_by_author){
		
		Comments comments = new Comments();
		comments.client.setToken(accessToken);
		List<Comment> list = null;
		try {
			CommentWapper commentWapper = comments.getCommentToMe(page, filter_by_source, filter_by_author);
			if (null != commentWapper) {
				list = commentWapper.getComments();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 *  获取当前登录用户所发出的评论列表
	 *  @param accessToken
	 *  @param page
	 *            返回结果的页码，默认为1
	 *  @param filter_by_source
	 *            来源筛选类型，0：全部、1：来自微博的评论、2：来自微群的评论，默认为0
	 *  @return
	 **/
	public static List<Comment> findCommentByMeWeiboInfo(String accessToken,Paging page, Integer filter_by_source){
		
		Comments comments = new Comments();
		comments.client.setToken(accessToken);
		List<Comment> list = null;
		try {
			CommentWapper commentWapper = comments.getCommentByMe(page, filter_by_source);
			if (null != commentWapper) {
				list = commentWapper.getComments();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 *  根据微博ID返回某条微博的评论列表
	 *  @param accessToken
	 *  @param weiboId
	 *            需要查询的微博ID
	 *  @param count
	 *            单页返回的记录条数，默认为50。
	 *  @param page
	 *            返回结果的页码，默认为1。
	 *  @param filter_by_author
	 *            作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
	 *  @return
	 **/
	public static List<Comment> findCommentByWeiboId(String accessToken,String weiboId, Paging page,
			Integer filter_by_author){
		
		Comments comments = new Comments();
		comments.client.setToken(accessToken);
		List<Comment> list = null;
		try {
			CommentWapper commentWapper = comments.getCommentById(weiboId, page, filter_by_author);
			if (null != commentWapper) {
				list = commentWapper.getComments();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
	/**
	 *  对一条微博进行评论
	 *  @param accessToken
	 *  @param comment
	 *  @param weiboId
	 *  @return
	 **/
	public static Boolean CreateDsrmAccountWeibo(String accessToken,String content, String weiboId){
		
		Boolean result = false;
		Comments comments = new Comments();
		comments.client.setToken(accessToken);
		try {
			
			Comment comment = comments.createComment(content,weiboId);
			if (null == comment) {
				System.out.println("新浪评论微博失败");
			}else {
				System.out.println("新浪评论微博成功");
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
