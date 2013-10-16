package com.klwork.business.utils;

import java.util.Vector;

import cz.vutbr.web.css.CSSProperty.Font;

public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "从武侠看人才 好的杀手应该分成两种,一种是顶级的,一种是优秀的。什么是顶级的？这种人武功已经达到化境，这是一个什么的境界？也就是说对于他来说，已经没有招式可言,你可以想想张三丰、扫地僧、风清扬的样子。";
		double b = 16;
		
		System.out.println(getSplitString(str, b));
	}

	private static StringBuilder getSplitString(String str, double b) {
		StringBuilder buffer =  new StringBuilder(str.length() + (int)b);
		double a = str.length();
		double times = Math.ceil(a / b);
		for (int i = 0; i < (int) times; i++) {
			int last = (int) times - 1;

			if (i < last) {
				int start = (int) (b * i);
				int end = (int) (b * (i + 1));
				buffer.append((str.substring(start, end)));
				buffer.append("\n");
			} else {
				int start = (int) (b * i);
				buffer.append((str.substring(start)));
				buffer.append("\n");
			}
		}
		return buffer;
	}
}
