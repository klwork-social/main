package com.klwork.common;

import com.klwork.common.utils.StringTool;

public final class SystemConstants {
	/**
	 * AJAX访问方式的Exception对象存放
	 */
	public final static String EXCEPTION_NAME = "ajax-exception";
	/**
	 * session中存放用户的attribute值
	 */
	public final static String SESSION_USER = "loginUser";
	/**
	 * cookie中存放的登录用户ID
	 */
	public final static String COOKIE_USER_ID = "cookieUserId";
	
	/**
	 * cookie中存放的登录用户ID
	 */
	public final static String COOKIE_USER_PWD = "cookieUserPwd";
	
	/**
	 * session中存放菜单组的attribute值
	 */
	public final static String SESSION_MENU_GROUP = "menuGroupList";
	/**
	 * session中存放的url权限列表的attribute值
	 */
	public final static String SESSION_FUNC_URL_LIST = "functionUrlList";

	/**
	 * 文件后缀
	 */
	public static final String FILE_SUFFIX = SystemProperties
			.getString("System.FILE_SUFFIX");

	/**
	 * 分页数
	 */
	public static int pageSize = StringTool.parseInt(SystemProperties
			.getString("System.PageSize"));
	
	/**
	 * 微立聚sina的APPKEY
	 */
	public static final String SINA_APP_KEY = "1497784661";
	
	/**
	 * 微立聚sina的APPSECRET
	 */
	public static final String SINA_APP_SECRET = "c528f98ce3f7cdd1145aaedef03a6f29";
	
	/**
	 * 微立聚腾讯的APPKEY
	 */
	public static final String QQ_APP_KEY = "801059539";
	
	/**
	 * 微立聚腾讯的APPSECRET
	 */
	public static final String QQ_APP_SECRET = "dcefa386b2b5cb5af4482b1f18ae26d4";
	
	/**
	 * 立即推sina的APPKEY
	 */
	public static final String SINA_APP_KEY_LJT = "1748365159";
	
	/**
	 * 立即推sina的APPSECRET
	 */
	public static final String SINA_APP_SECRET_LJT = "fba3a35eb132079895082b8cb3b09c0e";
	
	/**
	 * 立即推腾讯的APPKEY
	 */
	public static final String QQ_APP_KEY_LJT = "801149347";
	
	/**
	 * 立即推腾讯的APPSECRET
	 */
	public static final String QQ_APP_SECRET_LJT = "746b5537fc79cb5ab6cefa38dcbcebe0";

	/**
	 * 还未开始监控
	 */
	public final static int CHANNEL_NU_MONITOR = 0;
	/**
	 * 正常链接状态
	 */
	public final static int CHANNEL_NORMAL = 1;
	/**
	 * 监控中
	 */
	public final static int CHANNEL_MONITOR =2;
	/**
	 * 被删帖
	 */
	public final static int CHANNEL_DELETE = 3;
	/**
	 * 监控完毕
	 */
	public final static int CHANNEL_HAVE_MONITRO = 4;
	/**
	 * 信息不全，不进行监控
	 */
	public final static int CHANNEL_MSG_UNALL = 5;

}
