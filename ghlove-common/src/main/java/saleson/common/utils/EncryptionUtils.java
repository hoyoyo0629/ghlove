package saleson.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;

public class EncryptionUtils {
	private static final Logger logger = LoggerFactory.getLogger(EncryptionUtils.class);
	
	private EncryptionUtils() {
	}

	/**
     * HMAC SHA256 문자열 생성
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public static String getHmacSHA256(String key, String value) throws IllegalArgumentException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {

        Base64.Encoder encoder = Base64.getEncoder();
        String algorithm = "HmacSHA256";

        Mac sha256HMAC = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"),algorithm);

        sha256HMAC.init(secretKey);

        String hash = new String (encoder.encode((sha256HMAC.doFinal(value.getBytes("UTF-8")))));


        return hash;
    }

    /**
     * HMAC SHA256 생성된 문자열 비교
     * @param hexString
     * @param key
     * @param value
     * @return
     */
    public static boolean isMatchesHmacSha256Hex(String hexString, String key, String value) {
        boolean flag = false;

        if (!ObjectUtils.isEmpty(hexString)) {
            try {
                flag = hexString.equals(getHmacSHA256(key, value));
            } catch (IllegalArgumentException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
                flag = false;
            }
        }

        return flag;
    }
    
    public static String  getRSA(String plainData, String stringPublicKey) {
		String encryptedData = null;
        try {
            //평문으로 전달받은 공개키를 공개키객체로 만드는 과정
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            //만들어진 공개키객체를 기반으로 암호화모드로 설정하는 과정
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            //평문을 암호화하는 과정
            byte[] byteEncryptedData = cipher.doFinal(plainData.getBytes());
            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);

        } catch (NoSuchAlgorithmException e) {
        	logger.error("RSA 암호화 : NoSuchAlgorithmException ERROR-35: 암호 알고리즘 사용불가 오류", e);
        } catch (InvalidKeySpecException e) {
        	logger.error("RSA 암호화 : InvalidKeySpecException ERROR-36: Key 표준 부적합 오류", e);
        } catch (NoSuchPaddingException e) {
        	logger.error("RSA 암호화 : NoSuchPaddingException ERROR-37: 패딩 사용불가 오류", e);
        } catch (InvalidKeyException e) {
        	logger.error("RSA 암호화 : InvalidKeyException ERROR-38: Key 길이 초과 오류", e);
		} catch (IllegalBlockSizeException e) {
			logger.error("RSA 암호화 : IllegalBlockSizeException ERROR-39: 암호화 데이터 Size 오류", e);
			System.err.println("IllegalBlockSizeException : " + "ERROR-39: 암호화 데이터 Size 오류");
		} catch (BadPaddingException e) {
			logger.error("RSA 암호화 : BadPaddingException ERROR-40: 복호화 Key 불일치 오류", e);
		}
        return encryptedData;
	}

}
