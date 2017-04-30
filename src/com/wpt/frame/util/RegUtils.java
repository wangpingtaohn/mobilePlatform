package com.wpt.frame.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liujun
 */
public class RegUtils {
	
	private static final String NUMBER_REG_EXP = "^([0-9]+)*$";
	private static final String PHONE_REG_EXP = "^([0-9]{3}-?[0-9]{8})|([0-9]{4}-?[0-9]{7})$";
	private static final String MOBILE_REG_EXP = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
	private static final String EMAIL_REG_EXP = "^([a-z0-9A-Z]+[-|\\.|\\_]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	private static final String IP_REG_EXP = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
	
	
	/** 使用Pattern的compile方法和Matcher的matcher方法共同匹配 */
	public static boolean usePattern(String regex,String str) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	public static boolean isNumber(String str)
	{
		return usePattern(NUMBER_REG_EXP,str);
	}
	
	public static boolean isPhone(String str)
	{
		return usePattern(PHONE_REG_EXP,str);
	}
	
	public static boolean isMobile(String str)
	{
		return usePattern(MOBILE_REG_EXP,str);
	}
	
	public static boolean isEmail(String str)
	{
		return usePattern(EMAIL_REG_EXP,str);
	}
	
	public static boolean isIp(String str)
	{
		return usePattern(IP_REG_EXP,str);
	}
}
