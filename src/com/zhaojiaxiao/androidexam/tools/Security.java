package com.jiaogui.androidexam.tools;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Security {
	private static final String KEY = "1234567812345678";
	
	public static String encrypt(){
		return Security.encrypt(System.currentTimeMillis()/1000+"", KEY);
	}
	public static String encrypt(String input, String key){
	  byte[] crypted = null;
	  try{
		  SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	      cipher.init(Cipher.ENCRYPT_MODE, skey);
	      crypted = cipher.doFinal(input.getBytes());
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	    return new String(Base64.encodeBase64(crypted));
	}

//	public static String decrypt(String input, String key){
//	    byte[] output = null;
//	    try{
//	      SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
//	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//	      cipher.init(Cipher.DECRYPT_MODE, skey);
//	      output = cipher.doFinal(Base64.decodeBase64(input));
//	    }catch(Exception e){
//	      System.out.println(e.toString());
//	    }
//	    return new String(output);
//	}
//	public static String decrypt(String input){
//		byte[] output = null;
//	    try{
//	      SecretKeySpec skey = new SecretKeySpec(KEY.getBytes(), "AES");
//	      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//	      cipher.init(Cipher.DECRYPT_MODE, skey);
//	      output = cipher.doFinal(Base64.decodeBase64(input));
//	    }catch(Exception e){
//	      System.out.println(e.toString());
//	    }
//	    return new String(output);
//	}
}
