package saleson.common.security.crypto;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@NoArgsConstructor
public class AesCryptor implements Cryptor {
	private final String ALGORITHM = "AES";
	private final String ENCODING = "UTF-8";
	private String key;
	private SecretKeySpec secretKeySpec;

	public AesCryptor(String key) {
		this.key = key;
		this.secretKeySpec = generateMySQLAESKeySpec(this.key);
	}

	@Override
	public String encrypt(String plainText) {
		if (plainText == null) return null;
		try {
			final Cipher encryptCipher = Cipher.getInstance(ALGORITHM);
			encryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			return new String(Hex.encodeHex(encryptCipher.doFinal(plainText.getBytes(ENCODING)))).toUpperCase();

		} catch (NoSuchAlgorithmException
				| NoSuchPaddingException
				| InvalidKeyException
				| BadPaddingException
				| IllegalBlockSizeException
				| UnsupportedEncodingException ignore) {
//			log.warn("[AesCryptor] 암호화 실패: {} ({})", plainText, ignore.getMessage(), ignore);
			log.warn("[AesCryptor] 암호화 실패: {} ({})", plainText, getClass().getName() + " :: encrypt Exception ==============");
			return plainText;
		}
	}

	@Override
	public String decrypt(String encrypted) {
		if (encrypted == null) return null;
		try {
			final Cipher decryptCipher = Cipher.getInstance("AES");
			decryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			return new String(decryptCipher.doFinal(Hex.decodeHex(encrypted.toCharArray())));

		} catch (NoSuchAlgorithmException
				| NoSuchPaddingException
				| InvalidKeyException
				| BadPaddingException
				| IllegalBlockSizeException
				| DecoderException ignore) {
//			log.warn("[AesCryptor] 복호화 실패: {} ({})", encrypted, ignore.getMessage(), ignore);
			log.warn("[AesCryptor] 복호화 실패: {} ({})", encrypted, getClass().getName() + " :: decrypt Exception ==============");
			return encrypted;
		}
	}


	private SecretKeySpec generateMySQLAESKeySpec(final String key) {
		try {
			final byte[] finalKey = new byte[16];
			int i = 0;
			for(byte b : key.getBytes(ENCODING))
				finalKey[i++%16] ^= b;
			return new SecretKeySpec(finalKey, ALGORITHM);
		} catch(UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
