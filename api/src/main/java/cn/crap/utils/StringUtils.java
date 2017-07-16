package cn.crap.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Descript:字符串工具类
 * @author sfit0198
 * @version 1.0
 * @date 2014-4-24 下午6:55:45
 */
public final class StringUtils {

	private static final int USERID_LEN_32 = 32;
	private static final int WAYBILLID_LEN_20 = 20;
	// 正则表达式
	/**** 非0的数字 */
	public static final String REX_INTEGER = "^[1-9]\\d*$";
	private StringUtils() {
	}

	/**
	 * 把字符串的回车转换成"\\n"
	 * @param s 待转换的字符串
	 * @return 转换后的字符串
	 */
	public static String convert(final String s) {
		String str;
		str = s.replace("\r\n", "\n").replace("\r", "\n");
		String[] split = str.split("\n");
		str = "";
		for (int i = 0; i < split.length; i++) {
			str += split[i] + (i == split.length - 1 ? "" : "\\\\n");
		}
		return str;
	}

	/**
	 * 判断是否不为空串
	 * @param s 待判断的字符串
	 * @return 是否不为空串
	 */
	public static boolean isNotEmpty(final String s) {
		return (s != null) && (!"".equals(s.trim()));
	}

	/**
	 * 判断是否为空串
	 * @param s 待判断的字符串
	 * @return 是否为空串
	 */
	public static boolean isEmpty(final String s) {
		return (s == null) || ("".equals(s.trim()));
	}

	/**
	 * 首字母大写
	 * @param name
	 * @return
	 */
	public static String initialString(final String name) {
		if (name == null) {
			return "";
		}
		String s = name.trim();
		if (!"".equals(s)) {
			String i = s.substring(0, 1);
			s = i.toUpperCase() + s.substring(1);
		}
		return s;
	}

	/**
	 * 逗号分割字符串
	 * @param name
	 * @return
	 */
	public static List<Long> getListIds(final String listIds) {
		List<Long> list = new ArrayList<Long>();
		String[] stringList = listIds.split(",");
		for (int i = 0; i < stringList.length; i++) {
			list.add(i, Long.parseLong(stringList[i]));
		}
		return list;
	}

	/**
	 * 判断字符是否为空
	 * @param str
	 * @return
	 */
	public static boolean checkedIsEmpty(final String str) {
		return str == null || str.equals("");
	}

	/**
	 * 判断字符串是否相同，null == ""
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean checkedEqualString(final String str1, final String str2) {
		boolean result = false;
		if ((str1 == null && str2 == null) || (str1 == null && str2.equals("")) || (str2 == null && str1.equals(""))
				|| str1.equals(str2)) {
			result = true;
		}
		return result;
	}

	// 判断电话
	public static boolean isTelephone(final String phonenumber) {
		if (isEmpty(phonenumber)) {
			return false;
		}
		String phone = "0\\d{2,3}-\\d{7,8}";
		Pattern p = Pattern.compile(phone);
		Matcher m = p.matcher(phonenumber);
		return m.matches();
	}

	// 判断手机号
	public static boolean isMobile(final String mobile) {
		if (isEmpty(mobile)) {
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobile);
		return m.matches();
	}

	//不足32位前面补 0
	public static String fillBeginWith0(String str) {
		if (null != str && !"".equals(str)) {
			int len = str.length();
			for (int i = 0; i < USERID_LEN_32 - len; i++) {
				str = "0" + str;
			}
		}
		return str;
	}

	/**
	 * <p>Description: 不足32位前面补 0</p>
	 * @author 050940
	 * @return String
	 * @2013-11-9 下午3:46:54
	 */
	public static String fillLeftWith0(final String str) {
		if (isNotEmpty(str) && str.length() < USERID_LEN_32) {
			StringBuilder sb = new StringBuilder();
			int len = str.length();
			for (int i = 0; i < USERID_LEN_32 - len; i++) {
				sb.append("0");
			}
			return sb + str;
		}
		return str;
	}

	//截掉左边起所有为0的字串,如：截取0000000002343023434后，结果为：2343023434
	public static String getOrigiSubStr(final String str) {
		String origStr = null;
		if (!isEmpty(str)) {
			int omitNum = 0;
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == '0') {
					omitNum++;
				} else {
					break;
				}
			}
			origStr = str.substring(omitNum, str.length());
		}
		return origStr;
	}

	//不足32位前面补 0
	public static String fillBeginWith0ForwaybillId(String str) {
		if (null != str && !"".equals(str)) {
			int len = str.length();
			for (int i = 0; i < WAYBILLID_LEN_20 - len; i++) {
				str = "0" + str;
			}
		}
		return str;
	}

	//判断是否是允许的字符串（过滤特殊字符）
	public static boolean isPermitChars(final String str) {
		boolean flag = true;
		if (isNotEmpty(str)) {
			String regex = "[` ~!#$%^&*()+=|{}':;',\\[\\]<>/?~！#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			if (m.find()) {
				flag = false;
			}
		}
		return flag;
	}

	public static boolean isNaN(final String obj) {
		boolean result = false;
		if (isNotEmpty(obj)) {
			result = Pattern.matches("^\\d{1,}+$", obj);
		}
		return result;
	}

	public static boolean isNumber(final String obj) {
		boolean result = false;
		if (isNotEmpty(obj)) {
			result = Pattern.matches(REX_INTEGER, obj.trim());
		}
		return result;
	}

//	public static void main(final String[] args) {
//		String te = "2 ";
//		System.out.println(isNumber(te));
//	}
	/**
	 * 判断字符串是否存在数组
	 * @param val
	 * @param array
	 * @return
	 */
	public static boolean isStringInArray(String val,String[] array){
		boolean result = false;
		if(array != null && array.length != 0){
			for(String str:array){
				if(org.apache.commons.lang.StringUtils.equals(val, str)){
					result = true;
				}
			}
		}
		return result;
	}
	
	// 是否是[float,double]类型数据
	public static boolean isInt(String str) {
		if (isEmpty(str)) {
			return false;
		} else {
			return Pattern.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?", str.toString());
		}
	}
}
