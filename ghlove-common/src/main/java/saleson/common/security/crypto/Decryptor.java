package saleson.common.security.crypto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@RequiredArgsConstructor
@Component
public class Decryptor {
	private final Cryptor cryptor;
	private final DataMasking dataMasking;


	public String decrypt(String str) {
		return cryptor.decrypt(str);
	}

	public String decrypt(String str, Masking masking) {
		String result = cryptor.decrypt(str);
		return dataMasking.mask(result, masking);
	}

	public String decryptPhone(String str, boolean needMasking) {
		if (needMasking) return decrypt(str, Masking.PHONE_NUMBER);
		return decrypt(str);
	}

	public String decryptName(String str, boolean needMasking) {
		if (needMasking) return decrypt(str, Masking.NAME);
		return decrypt(str);
	}

	public String decryptBankAccount(String str, boolean needMasking) {
		if (needMasking) return decrypt(str, Masking.BANK_ACCOUNT);
		return decrypt(str);
	}

	public String decryptDate(String str, boolean needMasking) {
		if (needMasking) return decrypt(str, Masking.BIRTHDAY);
		return decrypt(str);
	}

	public String decryptEmail(String str, boolean needMasking) {
		if (needMasking) return decrypt(str, Masking.EMAIL);
		return decrypt(str);
	}

	public String decryptAddressDetail(String str, boolean needMasking) {
		if (needMasking) return decrypt(str, Masking.ADDRESS_DETAIL);
		return decrypt(str);
	}
}
