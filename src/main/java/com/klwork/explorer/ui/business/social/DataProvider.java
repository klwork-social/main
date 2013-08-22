/**
 * DISCLAIMER
 * 
 * The quality of the code is such that you should not copy any of it as best
 * practice how to build Vaadin applications.
 * 
 * @author jouni@vaadin.com
 * 
 */

package com.klwork.explorer.ui.business.social;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.vaadin.server.VaadinRequest;
import com.vaadin.util.CurrentInstance;

public class DataProvider {
	
	public static Random rand = new Random();
	static List<Map<String,String>> faces = new ArrayList<Map<String,String>>();
	/**
	 * Initialize the data for this application.
	 */
	public static List<Map<String,String>> getFacesList() {
		return faces;
	}
	static {
		loadData();
	}

	/**
	 * Initialize the list of movies playing in theaters currently. Uses the
	 * Rotten Tomatoes API to get the list. The result is cached to a local file
	 * for 24h (daily limit of API calls is 10,000).
	 */
	static private void loadData() {

		File cache;
		String pathname = "smail.txt";
		// TODO why does this sometimes return null?
		VaadinRequest vaadinRequest = CurrentInstance.get(VaadinRequest.class);
		if (vaadinRequest == null) {
			// PANIC!!!
			cache = new File(pathname);
		} else {
			File baseDirectory = vaadinRequest.getService().getBaseDirectory();
			cache = new File(baseDirectory + "/" + pathname);
		}
		JSONObject json = null;
		if (cache.exists()) {
			try {
				json = readJsonFromFile(cache);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				// json = getSmail();
				FileWriter fileWriter = new FileWriter(cache);
				fileWriter.write(getSmail());
				fileWriter.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (json != null) {
			JSONArray infoList = json.getJSONArray("data");
			for (int i = 0; i < infoList.size(); i++) {// 没一条的微博
				JSONObject infoObj = infoList.getJSONObject(i);
				HashMap map = new HashMap();
				map.put("phrase", infoObj.getString("phrase"));
				map.put("url", infoObj.getString("url"));
				faces.add(map);
			}
		}

	}

	public static String getSmail() throws IOException {
		VaadinRequest vaadinRequest = CurrentInstance.get(VaadinRequest.class);
		JSONObject o = readJsonFromUrl("http://"
				+ vaadinRequest.getRemoteAddr()
				+ vaadinRequest.getContextPath() + "/site/js/face.json");
		JSONObject data = getObject("data", o);
		JSONObject usual = getObject("usual", data);
		JSONArray infoList = (usual).getJSONArray("norm");
		HashMap retMap = new HashMap();
		List list = new ArrayList();
		for (int i = 0; i < infoList.size(); i++) {// 没一条的微博
			JSONObject infoObj = infoList.getJSONObject(i);
			HashMap map = new HashMap();
			map.put("phrase", infoObj.getString("phrase"));
			map.put("url", infoObj.getString("url"));
			list.add(map);
		}
		retMap.put("data", list);
		return getJSONString(retMap);

	}

	public static String getJSONString(Object object) throws IOException {
		String jsonString = null;
		// 日期值处理器
		JsonConfig jsonConfig = new JsonConfig();
		// jsonConfig.registerJsonValueProcessor(java.util.Date.class, new
		// JsonDateValueProcessor());
		if (object != null) {
			if (object instanceof Collection || object instanceof Object[]) {
				jsonString = JSONArray.fromObject(object, jsonConfig)
						.toString();
			} else {
				jsonString = JSONObject.fromObject(object, jsonConfig)
						.toString();
			}
		}
		return jsonString == null ? "{}" : jsonString;
	}

	public static <T> T getObject(String sign, Object o) {
		return (T) JSONObject.fromObject(o).get(sign);
	}

	/* JSON utility method */
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/* JSON utility method */
	private static JSONObject readJsonFromUrl(String url) throws IOException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			return JSONObject.fromObject(jsonText);
		} finally {
			is.close();
		}
	}

	/* JSON utility method */
	private static JSONObject readJsonFromFile(File path) throws IOException {
		BufferedReader rd = new BufferedReader(new FileReader(path));
		String jsonText = readAll(rd);
		return JSONObject.fromObject(jsonText);
	}
}
