package com.klwork.business.utils;

import java.util.List;

import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.util.WeiboContentTransLate;

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
		List<Status> list = null;
		try {

			Weibo weibo = new Weibo();
			weibo.setToken(accessToken);
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
		List<Status> list = null;
		try {

			Weibo weibo = new Weibo();
			weibo.setToken(accessToken);
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
		if(text == null || text.trim().length() == 0){
			return null;
		}
		return WeiboContentTransLate.transLateall(text, "blue");
	}
}
