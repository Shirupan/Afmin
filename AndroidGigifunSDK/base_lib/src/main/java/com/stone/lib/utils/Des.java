package com.stone.lib.utils;

import com.stone.commons.codec.binary.Base64;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class Des {  
    private static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    
    private final static String ivKey = "alwaysbe";
    
    public static String encode(String key, String src) {
    	byte[] input = encode(key, src.getBytes());
		Base64 base = new Base64();
        return base.encodeToString(input);  
    }
    
    public static String decode(String key, String src) {
    	Base64 base = new Base64();
    	byte[] s = base.decode(src.getBytes());
    	byte[] input = decode(key, s);
        return new String(input);
    }
  
    private static byte[] encode(String key, byte[] data) {
        try {  
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
  
            byte[] bytes = cipher.doFinal(data);  
            return bytes;  
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    private static byte[] decode(String key, byte[] data) {
        try {  
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
  
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes());
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
            return cipher.doFinal(data);  
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        return null;  
    }  
}  