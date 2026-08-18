package saleson.common.security.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RsaCryptor {

	private static final Logger log = LoggerFactory.getLogger(RsaCryptor.class);
	
	public static HashMap<String, String> createKeypairAsString() {
		HashMap<String, String> stringKeypair = new HashMap<>();
		
		try {
			SecureRandom secureRandom = new SecureRandom();
			
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048, secureRandom);
			KeyPair keyPair = kpg.genKeyPair();
			
			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();
			
			String stringPublickKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
			String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
			
			stringKeypair.put("publicKeyStr", stringPublickKey);
			stringKeypair.put("privateKeyStr", stringPrivateKey);
			
			return stringKeypair;
		} catch (NoSuchAlgorithmException e) {
			log.error("RsaCryptor createKeypairAsString error", e);
			return null;
		}
	}
	
	// 암호화
	public static String encrypt(String plainText, String publicKeyStr) {		
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] bytePublicKey = Base64.getDecoder().decode(publicKeyStr.getBytes());
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			
			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch(NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException 
				| InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
			log.error("RsaCryptor encrypt error", e);
			return "";
		}
	}
	
	// 복호화
	public static String decrypt(String encryptedText, String privateKeyStr) {
		try {
			// 평문으로 전달받은 공개키를 사용하기 위해 공개키 객체 생성
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] bytePrivateKey = Base64.getDecoder().decode(privateKeyStr.getBytes());
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

			// 만들어진 공개키 객체로 복호화 설정
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			// 암호문을 평문화하는 과정
			byte[] encryptedBytes =  Base64.getDecoder().decode(encryptedText.getBytes());
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
//			return Base64.getEncoder().encodeToString(decryptedBytes);
			return new String(decryptedBytes, "utf-8");
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException 
				| InvalidKeyException | BadPaddingException | IllegalBlockSizeException | UnsupportedEncodingException e) {
			log.error("RsaCryptor decrypt error", e);
			return "";
		}
	}
	
}
