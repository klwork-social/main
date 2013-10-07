package com.klwork.flow;

import java.util.Date;

import com.klwork.explorer.ui.util.ImageUtil;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new Date(2012,1,1).getTime()/1000);
		System.out.println("\u53c2\u6570\u4e0d\u6b63\u786e");
		String url = "http://app.qpic.cn/mblogpic/3d3e91bb3f12e68aa154/120";
		//ImageUtil.queryURLImageSize(url);
		String parameter = "myTask?taskId=46512";
		 String[] tokenArray = parameter.split("\\?");
		 System.out.println(tokenArray[1]);
		
	}

}
