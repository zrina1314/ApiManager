/*******************************************************************************
 *    系统名称   ：速运通App
 *    客户           ： 速运通研发人员
 *    文件名       ： Base64.java
 *              (C) Copyright sf_Express Corporation 2014
 *               All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于顺丰速运资讯科技本部IT产品中心内部使用，禁止转发
 ******************************************************************************/
package com.sf.encryption;


/**
 * Base64加解密工具类
 *
 * @author 479716
 * @see
 * @since
 * @date 2014-5-9下午4:13:14
 */
public class Base64 {
	/**
	 * BASE64解密
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);
	}

	/**
	 * BASE64加密
	 *
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String encryptBASE64(byte[] key) throws Exception {
		return (new BASE64Encoder()).encodeBuffer(key);
	}

}