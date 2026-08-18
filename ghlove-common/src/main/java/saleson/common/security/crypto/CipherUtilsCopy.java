package saleson.common.security.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.onlinepowers.framework.util.Base64Utils;

public class CipherUtilsCopy {
	
    private static final java.lang.String IV_PARAM = "onlinepowers.com";
    
    private static final java.lang.String ALGORITHM = "AES";
    
    private static final java.lang.String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    
    private static final java.lang.String ENCRYPT_KEY = "68616e736f6c31323334353637383930";

    public static String encrypt(String plainStr) {
    	try {
        	Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(hexStringToBytes(ENCRYPT_KEY), ALGORITHM);
            IvParameterSpec ivParamSpec = new IvParameterSpec(IV_PARAM.substring(0, 16).getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec);
            
            byte[] encrypted = cipher.doFinal(plainStr.getBytes("UTF-8"));
            String hex = bytesToHex(encrypted);
            return Base64Utils.encode(hex);	
    	} catch (NullPointerException
    			| NoSuchAlgorithmException
    			| NoSuchPaddingException
    			| InvalidKeyException
    			| InvalidAlgorithmParameterException
    			| IllegalBlockSizeException
    			| BadPaddingException
    			| UnsupportedEncodingException e) {
    		return plainStr;
    	}
    }
    
    public static String decrypt(String encStr) {
    	try {
        	Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            SecretKeySpec keySpec = new SecretKeySpec(hexStringToBytes(ENCRYPT_KEY), ALGORITHM);
            IvParameterSpec ivParamSpec = new IvParameterSpec(IV_PARAM.substring(0, 16).getBytes());
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec);
            
            String base64Decode = Base64Utils.decode(encStr);
            byte[] decodeByte = hexStringToBytes(base64Decode);
            byte[] decodedByte =  cipher.doFinal(decodeByte);
            return new String(decodedByte);	
    	} catch (NullPointerException
    			| NoSuchAlgorithmException
    			| NoSuchPaddingException
    			| InvalidKeyException
    			| InvalidAlgorithmParameterException
    			| IllegalBlockSizeException
    			| BadPaddingException e) {
    		return encStr;
    	}
    }
    
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        
        Formatter formatter = new Formatter(sb);
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        formatter.close();
     
        return sb.toString();
    }
    
    private static byte[] hexStringToBytes(String s) {
    	int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
    }
}
