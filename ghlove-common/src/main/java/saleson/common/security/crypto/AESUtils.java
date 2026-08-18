package saleson.common.security.crypto;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;


/**
 * AESUtils
 *
 * mysql 8.x 버전의 암복호화 함수(aes_encrypt, aes_decrypt)와 대응되는 Java AES 암복호화 유틸.
 *
 * <pre>
 *     암호화
 *     String encrypted = AESUtils.encrypt(name);
 *
 *     복호화
 *     String name = AESUtils.decrypt(encrypted);
 * </pre>
 *
 * <pre>
 *     mysql암호화
 *	   select hex(aes_encrypt(name, 'keykeykeykeykey'));
 *
 * 	   mysql 복호화
 * 	   select aes_decrypt(unhex(encrypted), 'keykeykeykeykey'));
 * </pre>
 */
public class AESUtils {
	private static final String KEY = "4646A6A7B2E0A5CE40F063A3347AF084";


	public static String encrypt(String plainText) throws Exception {
		final Cipher encryptCipher = Cipher.getInstance("AES");
		encryptCipher.init(Cipher.ENCRYPT_MODE, generateMySQLAESKey(KEY, "UTF-8"));
		return new String(Hex.encodeHex(encryptCipher.doFinal(plainText.getBytes("UTF-8")))).toUpperCase();
	}

	public static String decrypt(String encryptedText) throws Exception {
		// Decrypt
		final Cipher decryptCipher = Cipher.getInstance("AES");
		decryptCipher.init(Cipher.DECRYPT_MODE, generateMySQLAESKey(KEY, "UTF-8"));
		return new String(decryptCipher.doFinal(Hex.decodeHex(encryptedText.toCharArray())));
	}

	public static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
		try {
			final byte[] finalKey = new byte[16];
			int i = 0;
			for(byte b : key.getBytes(encoding))
				finalKey[i++%16] ^= b;
			return new SecretKeySpec(finalKey, "AES");
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
