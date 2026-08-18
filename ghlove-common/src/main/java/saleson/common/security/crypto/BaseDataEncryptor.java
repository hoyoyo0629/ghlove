package saleson.common.security.crypto;

import java.io.UnsupportedEncodingException;

//import com.onlinepowers.framework.exception.OpRuntimeException;
import com.privacy.pCrypto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseDataEncryptor {
//	private Cryptor cryptor;

	public BaseDataEncryptor(Cryptor cryptor) {
//		this.cryptor = cryptor;
	}

	private boolean encryptUse = true;

	public String encrypt(String data, int maxLength) {			// DB 컬럼 길이로 인해 일부만 저장되어 복호화 안되는 현상 방지 위해 길이 추가, 0 이하로 입력시 제한 없음 
//		try {
//			return cryptor.encrypt(data);
//		} catch (OpRuntimeException ignore) {
//			log.warn("[DataEncryptor] encrypt error: ({})", data);
//			return data;
//		}

		if (encryptUse) {
			try {
				if (maxLength > 0 && data != null && data.length() > maxLength) {			// db에 암호화 후 잘려서 들어가는 현상 방지하기 위해 글자수 체크한 후 암호화 진행
					data = data.substring(0, maxLength);
				}
				return pCrypto.Encrypt("normal", data, "");
			} catch (UnsupportedEncodingException e) {
				log.error("BaseDataEncryptor error", e);
				return data;
			}
		} else {
			return data;
		}
	}

	public String decrypt(String data) {
//		try {
//			if (data != null && !(cryptor instanceof DefaultCryptor)) {
//				data = data.replaceAll("-", "");
//			}
//			return cryptor.decrypt(data);
//		} catch (OpRuntimeException ignore) {
//			log.warn("[DataDecryptor] decrypt error: ({})", data);
//			return data;
//		}
		
		try {
//			if (data != null && !data.isEmpty()) {
//				data = data.replaceAll("-", "");
//			}
			return pCrypto.Decrypt("normal", data, "", 0);
		} catch (UnsupportedEncodingException e) {
			log.error("BaseDataEncryptor error", e);
			return data;
		}
	}
}
