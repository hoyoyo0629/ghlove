package saleson.common.security.crypto;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 암복호화 처리하자 않음.
 */
@Slf4j
@NoArgsConstructor
public class DefaultCryptor implements Cryptor {
	@Override
	public String encrypt(String plainText) {
		return plainText;
	}

	@Override
	public String decrypt(String encrypted) {
		return encrypted;
	}
}
