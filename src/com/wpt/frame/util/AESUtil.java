package com.wpt.frame.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class AESUtil {
	private static Cipher cipher;

	private AESUtil() {

	}

	static {
		try {
			cipher = Cipher.getInstance("AES/CBC/NoPadding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	public static String encryptText(String text, String key, String iv) {
		byte[] textBytes = text.getBytes();
		int textLength = textBytes.length;
		int blocksize = 16;
		int pad = blocksize - (textLength % blocksize);
		StringBuilder sb = new StringBuilder(text);
		for (int i = 0; i < pad; i++) {
			sb.append((char) pad);
		}
		byte[] textBytes2 = sb.toString().getBytes();
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
		try {
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
			byte[] encrypted = cipher.doFinal(textBytes2);
			return Base64.encodeToString(encrypted, Base64.NO_WRAP
					| Base64.DEFAULT | Base64.NO_CLOSE);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String decryptText(String cipherText, String key, String iv) {
		byte[] textBytes = Base64.decode(cipherText, Base64.DEFAULT);
		SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
		IvParameterSpec ivParamSpec = new IvParameterSpec(iv.getBytes());
		try {
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
			byte[] encrypted = cipher.doFinal(textBytes);
			String text = new String(encrypted);
			int textLenght = text.length();
			int pad = (int) text.charAt(textLenght - 1);
			return text.substring(0, textLenght - pad);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}

		return null;
	}
	/**
	 * @Description: 转unicode
	 * @author wpt
	 * @since 2014-8-8 上午11:10:18 
	 * @param ascii
	 * @return
	 */
	public static String toUnicode(String str){
        char[]arChar=str.toCharArray();
        int iValue=0;
        String uStr="";
        for(int i=0;i<arChar.length;i++){
            iValue=(int)str.charAt(i);          
            if(iValue<=256){
              // uStr+="& "+Integer.toHexString(iValue)+";";
                uStr+="\\"+Integer.toHexString(iValue);
            }else{
              // uStr+="&#x"+Integer.toHexString(iValue)+";";
                uStr+="\\u"+Integer.toHexString(iValue);
            }
        }
        return uStr;
    }
	/**
	 * @Description: unicode转为中文
	 * @author wpt
	 * @since 2014-8-8 上午11:10:18 
	 * @param ascii
	 * @return
	 */
	public static String ascii2native(String ascii) {
		int n = ascii.length() / 6;
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0, j = 2; i < n; i++, j += 6) {
			String code = ascii.substring(j, j + 4);
			char ch = (char) Integer.parseInt(code, 16);
			sb.append(ch);
		}
		return sb.toString();
	}
}
