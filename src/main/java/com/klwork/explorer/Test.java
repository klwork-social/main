package com.klwork.explorer;

import com.klwork.common.utils.StringTool;

public class Test {
	public static void main(String args[]) {
		int size = (int)Math.round(100.0 + Math.random() * 100.0);
		System.out.println(size);
		int nWord = StringTool.totalChineseWords("生活里阳光灿烂也罢，asdfsdfsd我阿萨德");
		System.out.println(140-nWord);
	}
}
