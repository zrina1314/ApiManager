package com.sf.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * MD5加密<br>
 * 使用方法:<br>
 * new MD5Encrypt().encrypt(paramter);
 * 
 * @author Yek Mobile
 * 
 */
public class MD5Encrypt implements IEncrypt {

	/**
	 * 对字符串MD5加密
	 *
	 * @param str 加密前的字符串
	 * @return 加密后的字符串
	 */
	public String encrypt(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(str.getBytes());
			byte[] mess = digest.digest();
			return EncryptHelper.toHexString(mess);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return str;
	}

	/**
	 * 对集合字符MD5加密
	 * 
	 * @param strs
	 *            加密前的集合
	 * @return 加密后的字符串
	 */
	public static String encrypt(ArrayList<String> strs) {
		StringBuffer str = new StringBuffer();
		for (String string : strs) {
			str.append(string);
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(str.toString().getBytes());
			byte[] mess = digest.digest();
			return EncryptHelper.toHexString(mess);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toString();
	}

}
