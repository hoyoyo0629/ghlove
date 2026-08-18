package saleson.common.security.crypto;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.privacy.pCrypto;

@Component
public class SellerPwSalt {

	private static final Logger log = LoggerFactory.getLogger(SellerPwSalt.class);

	@Value("${salt.strForSellerSalt}")
	private String strForSellerSalt;

	// salt 랜덤 생성..DB에 저장 필요
	public static String getRandomSalt() {
		byte[] randomByte = null;
		try {
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			randomByte = new byte[16];
			random.nextBytes(randomByte);
		} catch (NoSuchAlgorithmException e) {
			log.error("SellerPwSalt :: getRandomSalt error", e);
		}
		return new String(Base64.getEncoder().encode(randomByte));
	}
	
	// 로그인 아이디로 salt 생성..쿼리에서 사용할 수 없어서 미사용...
	public String makeLoginIdSalt(String loginId) {
		byte[] saltByte = new byte[16];
		String makeSaltStr = strForSellerSalt + loginId;
		
		try {
			makeSaltStr = pCrypto.Encrypt("normal", makeSaltStr, "");		// 최소 26자리
			
			byte[] temp = makeSaltStr.getBytes("UTF-8");
			
			for(int i = 0 ; i < 16 ; i++) {
				saltByte[i] = temp[i+2];		// 암호화 후 ^` 로 시작하여 3번째 자리부터 처리 
			}
			
			return new String(Base64.getEncoder().encode(saltByte)); 
		} catch (UnsupportedEncodingException | ArrayIndexOutOfBoundsException e) {
			log.error("SellerPwSalt :: makeLoginIdSalt error", e);
			return null;
		}
	}
	
	
	public String getStringForSalt() {
		try {
			byte[] temp = strForSellerSalt.getBytes("UTF-8");
			return new String(Base64.getEncoder().encode(temp)); 
		} catch (UnsupportedEncodingException e) {
			log.error("SellerPwSalt :: getStringForSalt error", e);
			return null;
		}
	}
	
}
