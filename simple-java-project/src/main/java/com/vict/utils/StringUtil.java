package com.vict.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 工具类
 * @author chang
 *
 */
public class StringUtil {
	/**
	 * 判断字符数组是否为空
	 */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 *
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, String> params) {

		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		String prestr = "";

		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);

			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		return prestr;
	}

	/**
	 * 判断字符串是否为空
	 * <ul>
	 * <li>isEmpty(null) = true</li>
	 * <li>isEmpty("") = true</li>
	 * <li>isEmpty("   ") = true</li>
	 * <li>isEmpty("abc") = false</li>
	 * </ul>
	 *
	 * @param value
	 *            目标字符串
	 * @return true/false
	 */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	/**
     * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isBlank(null)      = true
     * StringUtils.isBlank("")        = true
     * StringUtils.isBlank(" ")       = true
     * StringUtils.isBlank("bob")     = false
     * StringUtils.isBlank("  bob  ") = false
     * </pre>
     *
     * @param cs  the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is null, empty or whitespace
     * @since 2.0
     * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
     */
	 public static boolean isBlank(CharSequence cs) {
	    int strLen;
	    if (cs == null || (strLen = cs.length()) == 0) {
	        return true;
	    }
	    for (int i = 0; i < strLen; i++) {
	        if (Character.isWhitespace(cs.charAt(i)) == false) {
	            return false;
	        }
	    }
	    return true;
	 }

	private static final char[] NORMAL_BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

	public static String long2string(Long value, char[] chars){
		if(chars == null){
			chars = NORMAL_BASE62;
		}

		int chars_length = chars.length;

		if (value == 0) {
			return "0";
		}

		StringBuilder sb = new StringBuilder();
		boolean negative = value < 0;
		if (negative) {
			value = -value; // 转为正数以处理
		}

		while (value > 0) {
			int remainder = (int) (value % chars_length);
			sb.append(chars[remainder]);
			value /= chars_length;
		}

		if (negative) {
			sb.append('-');
		}

		return sb.reverse().toString();
	 }

	public static long string2long(String str, char[] chars) {
		if(chars == null){
			chars = NORMAL_BASE62;
		}

		int chars_length = chars.length;

		if (str == null || str.isEmpty()) {
			throw new IllegalArgumentException("Invalid input string.");
		}

		boolean negative = str.charAt(0) == '-';
		long result = 0;

		for (int i = (negative ? 1 : 0); i < str.length(); i++) {
			char c = str.charAt(i);
			int index = new String(chars).indexOf(c);
			if (index < 0) {
				throw new IllegalArgumentException("Invalid character in input string.");
			}
			result = result * chars_length + index;
		}

		return negative ? -result : result;
	}

	public static void main(String[] args) {
		System.out.println(long2string(9223372036854775807L, null));
		System.out.println(string2long(long2string(Long.MAX_VALUE, null), null));
	}
}
