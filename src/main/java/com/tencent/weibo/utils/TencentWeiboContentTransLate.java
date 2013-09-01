package com.tencent.weibo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TencentWeiboContentTransLate {
	public static String translateEmotion(String s,
			Map<String, String> emotionMap) {
		String[] es = "惊讶,撇嘴,色,发呆,得意,流泪,害羞,闭嘴,睡,大哭,尴尬,发怒,调皮,呲牙,微笑,难过,酷,非典,抓狂,吐,偷笑,可爱,白眼,傲慢,饥饿,困,惊恐,流汗,憨笑,大兵,奋斗,咒骂,疑问,嘘...,晕,折磨,衰,骷髅,敲打,再见,闪人,发抖,爱情,跳跳,找,美眉,猪头,小狗,钱,拥抱,灯泡,酒杯,音乐,蛋糕,闪电,炸弹,刀,足球,猫咪,便便,咖啡,饭,女,玫瑰,凋谢,男,爱心,心碎,药丸,礼物,吻,会议,电话,时间,太阳,月亮,强,弱,握手,胜利,邮件,电视,多多,美女,汉良,飞吻,怄火,毛毛,Q仔,西瓜,白酒,汽水,下雨,多云,雪人,星星,冷汗,擦汗,抠鼻,鼓掌,糗大了,坏笑,左哼哼,右哼哼,哈欠,鄙视,委屈,快哭了,阴险,亲亲,吓,可怜,菜刀,啤酒,篮球,乒乓,示爱,瓢虫,抱拳,勾引,拳头,差劲,爱你,NO,OK,转圈,磕头,回头,跳绳,挥手,激动,街舞,献吻,左太极,右太极,喜糖,红包"
				.split(",");
		for (int i = 0; i < es.length; i++) {
			int ii = i;
			if (ii == 135) {
				ii = 150;
			} else if (ii == 136) {
				ii = 151;
			}
			String e = es[i];
			String replacement = "<img src=\"http://mat1.gtimg.com/www/mb/images/face/"
					+ ii
					+ ".gif\" title=\""
					+ e
					+ "\" class=\"weibo_emotion\"/>";
			if (s.indexOf("/" + e) > -1) {
				s = s.replaceAll("/" + e, replacement);
			}
		}
		return s;
	}

	public static String translateAT(String c, String className) {

		String emotionPattern = "(@[\\u4e00-\\u9fa5\\w\\-]+)";
		Matcher m = Pattern.compile(emotionPattern).matcher(c);
		while (m.find()) {
			String r = m.group(0);
			/*for (int i = 1; i <= m.groupCount(); i++) {
				System.out.println("#: " + m.group(i));
			}*/
			String replacement = "";
			try {
				if ((className == null) || (className.trim().length() == 0)) {
					replacement = "<a href='http://t.qq.com/"
							+ URLEncoder.encode(r.substring(1), "utf-8")
							+ "' target='_blank'>" + r + "</a>";
				} else {
					replacement = "<a class='" + className
							+ "' href='http://t.qq.com/"
							+ URLEncoder.encode(r.substring(1), "utf-8")
							+ "' target='_blank'>" + r + "</a>";
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c = c.replaceAll(r, replacement);
			// c = content.replace("@" + htContent, "<a href='http://t.qq.com/"
			// + htContent + "' target='_blank'>@" +
			// URLEncoder.encode(htContent, "utf-8") + "</a>");

		}
		return c;

	}

	public static String translateLink(String content, String className) {
		if ((content == null) || (content.trim().length() == 0)) {
			return content;
		}

		String reg = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&amp;=]*)?";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(content);
		while (m.find()) {
			System.out.println(m.group(0));
			if ((className == null) || (className.trim().length() == 0)) {
				content = content.replace(m.group(0), "<a href='" + m.group(0)
						+ "' target='_blank'/>" + m.group(0) + "</a>");
			} else {
				content = content.replace(m.group(0),
						"<a href='" + m.group(0) + "' target='_blank' class='"
								+ className + "'/>" + m.group(0) + "</a>");
			}
		}
		return content;
	}

	public static String translateHT(String content, String className) {
		String htStr = "[#]";
		Matcher matcher = Pattern.compile(htStr).matcher(content);

		int first = -1;

		while (matcher.find()) {
			String htContent = null;

			if (first < 0) {
				first = matcher.start();
			} else if (matcher.start() - 1 == first) {
				first = matcher.start();
			} else {
				htContent = content.substring(first + 1, matcher.start());
				if ((className == null) || (className.trim().length() == 0)) {
					content = content.replace("#" + htContent + "#",
							"<a href='http://huati.weibo.com/k/" + htContent
									+ "' target='_blank'>#" + htContent
									+ "#</a>");
				} else
					content = content.replace("#" + htContent + "#",
							"<a class='" + className
									+ "' href='http://huati.weibo.com/k/"
									+ htContent + "' target='_blank'>#"
									+ htContent + "#</a>");

				first = -1;
			}
		}
		return content;
	}

	public static String transLateall(String content, String className) {
		content = translateLink(content, className);
		content = translateHT(content, className);
		content = translateAT(content, className);
		content = translateEmotion(content, EmotionUtil.getEmotionMap());
		return content;
	}
	
	public static List<Map<String,String>> getFacesList() {
		String[] es = "惊讶,撇嘴,色,发呆,得意,流泪,害羞,闭嘴,睡,大哭,尴尬,发怒,调皮,呲牙,微笑,难过,酷,非典,抓狂,吐,偷笑,可爱,白眼,傲慢,饥饿,困,惊恐,流汗,憨笑,大兵,奋斗,咒骂,疑问,嘘...,晕,折磨,衰,骷髅,敲打,再见,闪人,发抖,爱情,跳跳,找,美眉,猪头,小狗,钱,拥抱,灯泡,酒杯,音乐,蛋糕,闪电,炸弹,刀,足球,猫咪,便便,咖啡,饭,女,玫瑰,凋谢,男,爱心,心碎,药丸,礼物,吻,会议,电话,时间,太阳,月亮,强,弱,握手,胜利,邮件,电视,多多,美女,汉良,飞吻,怄火,毛毛,Q仔,西瓜,白酒,汽水,下雨,多云,雪人,星星,冷汗,擦汗,抠鼻,鼓掌,糗大了,坏笑,左哼哼,右哼哼,哈欠,鄙视,委屈,快哭了,阴险,亲亲,吓,可怜,菜刀,啤酒,篮球,乒乓,示爱,瓢虫,抱拳,勾引,拳头,差劲,爱你,NO,OK,转圈,磕头,回头,跳绳,挥手,激动,街舞,献吻,左太极,右太极,喜糖,红包"
				.split(",");
		List<Map<String,String>> faces = new ArrayList<Map<String,String>>(es.length);
		for (int i = 0; i < es.length; i++) {
			int ii = i;
			if (ii == 135) {
				ii = 150;
			} else if (ii == 136) {
				ii = 151;
			}
			String e = es[i];
			String url = "http://mat1.gtimg.com/www/mb/images/face/" + ii + ".gif";
			HashMap map = new HashMap();
			map.put("phrase", "/" + e);
			map.put("url", url);
			faces.add(map);
		}
		return faces;
	}

	public static void main(String[] args) {

		// System.out.println(transLateall(content, "blue"));

		String c = "一起来为这张照片起名吧！！我先起两个：1.七夕夜后难相聚； 2.天热来求 @xiaojingteng。 /大兵怎木样，本精灵是不是很有才捏？ /偷笑";
		c = "//@fir5671: //@薛蛮子:转发微博";
		System.out.println(transLateall(c, "blue"));
		getFacesList();

		// System.out.println(c);

	}
}