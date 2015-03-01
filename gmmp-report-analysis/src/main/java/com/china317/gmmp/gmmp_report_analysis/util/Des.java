/*
 * Created on 2006-6-2
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.china317.gmmp.gmmp_report_analysis.util;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


/**
 * @author JosephZhou
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Des {

	/**
	 * 
	 */
	private byte[] desKey; 
	public Des(byte[] desKey) 
	{ 
		this.desKey = desKey; 
	} 
	public byte[] doEncrypt(byte[] plainText) throws Exception 
	{ 
		SecureRandom sr = new SecureRandom(); 
		byte rawKeyData[] = desKey;
		DESKeySpec dks = new DESKeySpec(rawKeyData); 
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
		SecretKey key = keyFactory.generateSecret(dks); 
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding","SunJCE"); 
		cipher.init(Cipher.ENCRYPT_MODE, key); 
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data); 
		return encryptedData; 
	} 
	public byte[] doDecrypt(byte[] encryptText) throws Exception 
	{ 
		SecureRandom sr = new SecureRandom(); 
		byte rawKeyData[] = desKey;
		DESKeySpec dks = new DESKeySpec(rawKeyData); 
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES"); 
		SecretKey key = keyFactory.generateSecret(dks); 
		Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding","SunJCE"); 
		cipher.init(Cipher.DECRYPT_MODE, key,sr); 
		byte encryptedData[] = encryptText;
		byte decryptedData[] = cipher.doFinal(encryptedData); 
		return decryptedData; 
	} 
	public static void main(String[] args) throws Exception 
	{ 
		String key = "@tanway#"; 
		Des desEncrypt = new Des(key.getBytes("GBK")); 
		String plainText="1234567812345678";
		byte[] encryptText = desEncrypt.doEncrypt(plainText.getBytes("GBK")); 
		System.out.println(plainText.getBytes("GBK").length);
		System.out.println(encryptText.length);
		System.out.println(toHexString(encryptText));
		Des desDecrypt = new Des(key.getBytes()); 
		byte[] decryptText = desDecrypt.doDecrypt(encryptText); 
		System.out.println(new String(decryptText));
	} 

	public static String toHexString(byte[] value) 
	{ 
		String newString = ""; 
		for (int i = 0; i < value.length; i++) { 
		byte b = value[i]; 
		String str = Integer.toHexString(b); 
		if (str.length() > 2) { 
		str = str.substring(str.length() - 2); 
		} 
		if (str.length() < 2) { 
		str = "0" + str; 
		} 
		newString += str; 
		} 
		return newString.toUpperCase(); 


		} 



}
