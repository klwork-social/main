package com.klwork.business.utils;

import java.io.File;

import com.klwork.explorer.security.LoginHandler;

public class HtmlTranslateImageTool {
	/**
	 * 得到todolist的图片存储路径
	 * @param proId
	 * @return
	 */
	public static String currentTodoListImagePath(String proId) {
		String pathKey = "todolist";
		String factFilePath = currentFilePathByKey(pathKey,proId,"png");
		return factFilePath;
	}

	public static String currentFilePathByKey(String pathKey,String fileKey,String fileType) {
		String realUrl = SocialConfig.getString("weibo_temp_path");
		String path = realUrl  + System.getProperty("file.separator") + pathKey;
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		String factFilePath = path + System.getProperty("file.separator") + fileKey + "." + fileType;//
		return factFilePath;
	}
	
	/**
	 * 得到todolist的展示路径
	 * @param proId
	 * @return
	 */
	public static String getTodoListHtmlPath(String proId) {
		String urlstring = "http://klwork.com/ks/out/show-todolist?proId=" + proId;
		return urlstring;
	}
	
	/**
	 * 得到todolist的图片路径
	 * @param proId
	 * @return
	 */
	public static String getTodoListImagePath(String proId) {
		String urlstring = "http://klwork.com/ks/out/show-todolist-img?proId=" + proId;
		return urlstring;
	}
	
	
	/**
	 * 得到todolist的展示路径
	 * @param key
	 * @return
	 */
	public static String getWeiboHtmlPath(String key) {
		String urlstring = "http://klwork.com/ks/out/show-weibo-content?key=" + key;
		return urlstring;
	}
	
	/**
	 * 得到todolist的图片路径
	 * @param proId
	 * @return
	 */
	public static String getWeiboImagePath(String proId) {
		String urlstring = "http://klwork.com/ks/out/show-todolist-img?proId=" + proId;
		return urlstring;
	}
}
