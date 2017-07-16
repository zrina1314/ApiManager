package com.sf.encryption;


/**
 * 加密接口类
 * 
 * @author Yek Mobile
 *
 */
public interface IEncrypt {
	/**
	 * 对字符串加密
	 * @param str 加密前的字符串
	 * @return 加密后的字符串
	 */
	String encrypt(String str);

}
