package weibo4j.util;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboContentTransLate
{
  public static String translateEmotion(String content, Map<String, String> emotionMap)
  {
    if ((content == null) || (content.trim().length() == 0)) {
      return content;
    }

    String emotionPattern = "(\\[)[^\\]/]*(\\])";
    Matcher strongBM = Pattern.compile(emotionPattern).matcher(content);
    while (strongBM.find())
      if (emotionMap.get(strongBM.group(0)) != null)
      {
        content = content.replace(strongBM.group(0), "<img src='" + (String)emotionMap.get(strongBM.group(0)) + "'/>");
      }
    return content;
  }
  
  public static String translateAT(String c, String className) {

		String emotionPattern = "(@[\\u4e00-\\u9fa5\\w\\-]+)";
		Matcher m = Pattern.compile(emotionPattern).matcher(c);
		while (m.find()) {
			String r = m.group(0);
			String replacement = "";
			try {
				if ((className == null) || (className.trim().length() == 0)) {
					replacement = "<a href='http://weibo.com/n/"
							+ URLEncoder.encode(r.substring(1), "utf-8")
							+ "' target='_blank'>" + r + "</a>";
				} else {
					replacement = "<a class='" + className
							+ "' href='http://weibo.com/n/"
							+ URLEncoder.encode(r.substring(1), "utf-8")
							+ "' target='_blank'>" + r + "</a>";
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			c = c.replaceAll(r, replacement);
			// c = content.replace("@" + htContent, "<a href='http://weibo.com/n/"
			// + htContent + "' target='_blank'>@" +
			// URLEncoder.encode(htContent, "utf-8") + "</a>");

		}
		return c;

	}
  

  public static String translateLink(String content, String className)
  {
    if ((content == null) || (content.trim().length() == 0)) {
      return content;
    }

    String reg = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&amp;=]*)?";
    Pattern p = Pattern.compile(reg);
    Matcher m = p.matcher(content);
    while (m.find()) {
      if ((className == null) || (className.trim().length() == 0))
      {
        content = content.replace(m.group(0), "<a href='" + m.group(0) + "' target='_blank'/>" + m.group(0) + "</a>");
      }
      else {
        content = content.replace(m.group(0), "<a href='" + m.group(0) + "' target='_blank' class='" + className + "'/>" + m.group(0) + "</a>");
      }
    }
    return content;
  }

  public static String translateHT(String content, String className)
  {
    String htStr = "[#]";
    Matcher matcher = Pattern.compile(htStr).matcher(content);

    int first = -1;

    while (matcher.find()) {
      String htContent = null;

      if (first < 0) {
        first = matcher.start();
      }
      else if (matcher.start() - 1 == first) {
        first = matcher.start();
      }
      else {
        htContent = content.substring(first + 1, matcher.start());
        if ((className == null) || (className.trim().length() == 0))
        {
          content = content.replace("#" + htContent + "#", "<a href='http://huati.weibo.com/k/" + htContent + "' target='_blank'>#" + htContent + "#</a>");
        }
        else content = content.replace("#" + htContent + "#", "<a class='" + className + "' href='http://huati.weibo.com/k/" + htContent + "' target='_blank'>#" + htContent + "#</a>");

        first = -1;
      }
    }
    return content;
  }

  public static String transLateall(String content, String className)
  {
    content = translateLink(content, className);
    content = translateHT(content, className);
    content = translateAT(content, className);
    content = translateEmotion(content, EmotionUtil.getEmotionMap());
    return content;
  }

  public static void main(String[] args)
  {
    String content = "【国内宽带各项指标完成超预期】自3月实施“宽带普及提速工程”，目前全国固定宽带用户数量达1.66亿户，2M、4M以上用户比重分别达92.6%、54%，新增光纤到户覆盖家庭数量超过2300万，全国平均单位带宽价格比2011年底下降了18.7%。国内宽带各项指标完成超预期。（吉利）http://t.cn/zjzBb9L";

    //System.out.println(transLateall(content, "blue"));
    
    String c = "一起来为这张照片起名吧！！我先起两个：1.七夕夜后难相聚； 2.天热来求 @xiaojingteng。 /大兵怎木样，本精灵是不是很有才捏？ /偷笑";
    System.out.println(transLateall(c, "blue"));
  }
}