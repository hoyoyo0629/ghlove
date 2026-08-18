package saleson.shop.remittance.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class RemittanceExpectedEncryptor extends BaseDataEncryptor implements DataEncryptor<RemittanceExpected> {
	private Cryptor cryptor;
	private DataMasking dataMasking;

	public RemittanceExpectedEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}


	@Override
	public void encrypt(RemittanceExpected remittanceExpected) {
		remittanceExpected.setUserName(encrypt(remittanceExpected.getUserName(), 100));
		remittanceExpected.setTelephoneNumber(encrypt(remittanceExpected.getTelephoneNumber(), 50));
		remittanceExpected.setPhoneNumber(encrypt(remittanceExpected.getPhoneNumber(), 50));
		remittanceExpected.setBankAccountNumber(encrypt(remittanceExpected.getBankAccountNumber(), 100));
		remittanceExpected.setBankInName(encrypt(remittanceExpected.getBankInName(), 50));
		
		remittanceExpected.setBankName(encrypt(remittanceExpected.getBankName(), 50));
		remittanceExpected.setRepresentativeName(encrypt(remittanceExpected.getRepresentativeName(), 50));
	}

	@Override
	public void decrypt(RemittanceExpected remittanceExpected, boolean needMasking) {
		remittanceExpected.setUserName(decrypt(remittanceExpected.getUserName()));
		remittanceExpected.setTelephoneNumber(decrypt(remittanceExpected.getTelephoneNumber()));
		remittanceExpected.setPhoneNumber(decrypt(remittanceExpected.getPhoneNumber()));
		remittanceExpected.setBankAccountNumber(decrypt(remittanceExpected.getBankAccountNumber()));
		remittanceExpected.setBankInName(decrypt(remittanceExpected.getBankInName()));
		
		remittanceExpected.setBankName(decrypt(remittanceExpected.getBankName()));
		remittanceExpected.setRepresentativeName(decrypt(remittanceExpected.getRepresentativeName()));

		if (needMasking) masking(remittanceExpected);
	}

	@Override
	public void masking(RemittanceExpected remittanceExpected) {
		remittanceExpected.setUserName(dataMasking.mask(remittanceExpected.getUserName(), Masking.NAME));
		remittanceExpected.setTelephoneNumber(dataMasking.mask(remittanceExpected.getTelephoneNumber(), Masking.TEL_NUMBER));
		remittanceExpected.setPhoneNumber(dataMasking.mask(remittanceExpected.getPhoneNumber(), Masking.PHONE_NUMBER));
		remittanceExpected.setBankAccountNumber(dataMasking.mask(remittanceExpected.getBankAccountNumber(), Masking.BANK_ACCOUNT));
		remittanceExpected.setBankInName(dataMasking.mask(remittanceExpected.getBankInName(), Masking.NAME));
	}

}
