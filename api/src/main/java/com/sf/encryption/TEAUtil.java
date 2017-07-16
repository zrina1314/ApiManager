/*******************************************************************************
 *    系统名称   ：速运通App
 *    客户           ： 速运通研发人员
 *    文件名       ： TEAUtil.java
 *              (C) Copyright sf_Express Corporation 2014
 *               All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于顺丰速运资讯科技本部IT产品中心内部使用，禁止转发
 ******************************************************************************/
package com.sf.encryption;

import java.io.UnsupportedEncodingException;

/**
 * TEA加解密工具类
 *
 * @author 479716
 * @see
 * @since
 * @date 2014-5-9下午4:15:26
 */
public class TEAUtil {
	// 加密
	public byte[] encrypt(byte[] content, int offset, int[] key, int times) {// times为加密轮数
		int[] tempInt = byteToInt(content, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];

		for (i = 0; i < times; i++) {
			sum += delta;
			y += ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			z += ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
		}
		tempInt[0] = y;
		tempInt[1] = z;
		return intToByte(tempInt, 0);
	}

	// 解密
	public byte[] decrypt(byte[] encryptContent, int offset, int[] key,
			int times) {
		int[] tempInt = byteToInt(encryptContent, offset);
		int y = tempInt[0], z = tempInt[1], sum = 0xC6EF3720, i;
		int delta = 0x9e3779b9; // 这是算法标准给的值
		int a = key[0], b = key[1], c = key[2], d = key[3];

		for (i = 0; i < times; i++) {
			z -= ((y << 4) + c) ^ (y + sum) ^ ((y >> 5) + d);
			y -= ((z << 4) + a) ^ (z + sum) ^ ((z >> 5) + b);
			sum -= delta;
		}
		tempInt[0] = y;
		tempInt[1] = z;

		return intToByte(tempInt, 0);
	}

	// byte[]型数据转成int[]型数据
	private int[] byteToInt(byte[] content, int offset) {

		int[] result = new int[2]; // 除以2的n次方 == 右移n位 即
									// content.length / 4 ==
									// content.length ＞＞ 2
		for (int i = 0, j = offset; j < content.length && i < 2; i++, j += 4) {
			result[i] = transform(content[j + 3])
					| transform(content[j + 2]) << 8
					| transform(content[j + 1]) << 16 | content[j] << 24;
		}
		return result;

	}

	// int[]型数据转成byte[]型数据
	private byte[] intToByte(int[] content, int offset) {
		byte[] result = new byte[8]; // 乘以2的n次方 == 左移n位 即
										// content.length * 4 ==
										// content.length ＜＜ 2
		for (int i = 0, j = offset; j < result.length && i < 2; i++, j += 4) {
			result[j + 3] = (byte) (content[i] & 0xff);
			result[j + 2] = (byte) ((content[i] >> 8) & 0xff);
			result[j + 1] = (byte) ((content[i] >> 16) & 0xff);
			result[j] = (byte) ((content[i] >> 24) & 0xff);
		}
		return result;
	}

	// 若某字节被解释成负的则需将其转成无符号正数
	private int transform(byte temp) {
		int tempInt = temp;
		if (tempInt < 0) {
			tempInt += 256;
		}
		return tempInt;
	}

	/**
	 * 通过TEA加密,返回byte加密串
	 *
	 * @param info
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public byte[] encryptByTea(String info, int[] key)
			throws UnsupportedEncodingException {
		byte[] temp = info.getBytes();

		int n = 8 - temp.length % 8; // 若temp的位数不足8的倍数,需要填充的位数
		byte[] encryptStr = new byte[temp.length + n];
		encryptStr[0] = (byte) n;
		System.arraycopy(temp, 0, encryptStr, n, temp.length);
		byte[] result = new byte[encryptStr.length];
		for (int offset = 0; offset < result.length; offset += 8) {
			byte[] tempEncrpt = encrypt(encryptStr, offset, key, 32);
			System.arraycopy(tempEncrpt, 0, result, offset, 8);
		}
		return result;
	}

	/**
	 * 通过TEA算法解密,返回byte
	 *
	 * @param secretInfo
	 * @param KEY
	 * @return
	 */
	public String decryptByTea(byte[] secretInfo, int[] KEY) {
		byte[] decryptStr = null;
		int len = secretInfo.length;
		byte[] tempDecrypt = new byte[len];
		for (int offset = 0; offset < secretInfo.length; offset += 8) {
			decryptStr = decrypt(secretInfo, offset, KEY, 32);
			System.arraycopy(decryptStr, 0, tempDecrypt, offset, 8);
		}
		int n = tempDecrypt[0];
		return new String(tempDecrypt, n, len - n);
	}

}
