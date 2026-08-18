package saleson.common.security.crypto;

public interface Cryptor {

	/**
	 * 데이터 암호화
	 * @param plainText 평문텍스트
	 * @return
	 */
	String encrypt(String plainText);


	/**
	 * 데이터 복호화
	 * @param encrypted 암호화 문자열
	 * @return
	 */
	String decrypt(String encrypted);
}
