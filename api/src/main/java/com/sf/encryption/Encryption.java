/*******************************************************************************
 *    系统名称   ：速运通App
 *    客户           ： 速运通研发人员
 *    文件名       ： Encryption.java
 *              (C) Copyright sf_Express Corporation 2014
 *               All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于顺丰速运资讯科技本部IT产品中心内部使用，禁止转发
 ******************************************************************************/
package com.sf.encryption;

/**
 * 外部调用封装加解密类
 * 
 * @author 479716
 * @see
 * @since
 * @date 2014-5-9下午4:16:03
 */
public class Encryption {

	static Base64 base;
	static TEAUtil teaUtil;

	private final static int[] key = new int[] {// 加密解密所用的KEY
	0xFF12345, 0x87654321, 0x89734832, 0xABCDEF02 };

	static {
		teaUtil = new TEAUtil();
		base = new Base64();
	}

	/**
	 * 加密
	 * 
	 * @param encodeStr
	 * @return
	 */
	public static String encryCode(String encodeStr) {
		try {
			byte[] teaEncode = teaUtil.encryptByTea(encodeStr, key);
			String tempResult = base.encryptBASE64(teaEncode);
			return tempResult.replaceAll("\r|\n", "");
		} catch (Exception e) {
			return encodeStr;
		}

	}

	/**
	 * 解密
	 * 
	 * @param decodeStr
	 *            encodeStr
	 * @return
	 */
	public static String decryCode(String decodeStr) {
		try {
			byte[] baseDecode = base.decryptBASE64(decodeStr);
			if (baseDecode == null) {
				return null;
			}
			return teaUtil.decryptByTea(baseDecode, key);
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String[] args) {
		String sse = "PCAEotqYkfxvDyp4W3WQ50Zcw+FiJ0mKRire9fYt8Ufe/BhUpWf3cdkO5zwy2s3RfG5KlAtMn3EPaZAcxE9N/5CO5DR67D/UWEDBHqfcv1mmxfYOH7552d7v0uTxu/l2V8aIg7xfeXA=";
		String eex = "{\"status\":\"0\",\"message\":\"您请求的数据，无法获取结果\",\"errCode\":\"decode.error\",\"result\":{}}";

		String wsw = decryCode(sse);
		String eexx = encryCode(eex);
		String ss = "";
	}
}
