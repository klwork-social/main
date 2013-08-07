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
	


}
