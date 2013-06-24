package middlegen;

public class CodeGenUtil {
	
	/**
	 * 首字母大写
	 * @param word
	 * @return
	 */
	public static String firstWordToLower(String word) {
		if (word == null || word.trim().equals(""))
			return word;
		String first = word.substring(0, 1).toLowerCase();
		String res = first + word.substring(1);
		return res;
	}
}
