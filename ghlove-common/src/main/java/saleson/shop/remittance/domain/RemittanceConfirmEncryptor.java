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
public class RemittanceConfirmEncryptor extends BaseDataEncryptor implements DataEncryptor<RemittanceConfirm> {
	private Cryptor cryptor;
	private DataMasking dataMasking;

	public RemittanceConfirmEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}


	@Override
	public void encrypt(RemittanceConfirm remittanceConfirm) {
		remittanceConfirm.setUserName(encrypt(remittanceConfirm.getUserName(), 100));
		remittanceConfirm.setTelephoneNumber(encrypt(remittanceConfirm.getTelephoneNumber(), 50));
		remittanceConfirm.setPhoneNumber(encrypt(remittanceConfirm.getPhoneNumber(), 50));
		remittanceConfirm.setBankAccountNumber(encrypt(remittanceConfirm.getBankAccountNumber(), 100));
		remittanceConfirm.setBankInName(encrypt(remittanceConfirm.getBankInName(), 50));
		
		remittanceConfirm.setBankName(encrypt(remittanceConfirm.getBankName(), 15));
		remittanceConfirm.setRepresentativeName(encrypt(remittanceConfirm.getRepresentativeName(), 50));
	}

	@Override
	public void decrypt(RemittanceConfirm remittanceConfirm, boolean needMasking) {
		remittanceConfirm.setUserName(decrypt(remittanceConfirm.getUserName()));
		remittanceConfirm.setTelephoneNumber(decrypt(remittanceConfirm.getTelephoneNumber()));
		remittanceConfirm.setPhoneNumber(decrypt(remittanceConfirm.getPhoneNumber()));
		remittanceConfirm.setBankAccountNumber(decrypt(remittanceConfirm.getBankAccountNumber()));
		remittanceConfirm.setBankInName(decrypt(remittanceConfirm.getBankInName()));
		
		remittanceConfirm.setBankName(decrypt(remittanceConfirm.getBankName()));
		remittanceConfirm.setRepresentativeName(decrypt(remittanceConfirm.getRepresentativeName()));

		if (needMasking) masking(remittanceConfirm);
	}

	@Override
	public void masking(RemittanceConfirm remittanceConfirm) {
		remittanceConfirm.setUserName(dataMasking.mask(remittanceConfirm.getUserName(), Masking.NAME));
		remittanceConfirm.setTelephoneNumber(dataMasking.mask(remittanceConfirm.getTelephoneNumber(), Masking.TEL_NUMBER));
		remittanceConfirm.setPhoneNumber(dataMasking.mask(remittanceConfirm.getPhoneNumber(), Masking.PHONE_NUMBER));
		remittanceConfirm.setBankAccountNumber(dataMasking.mask(remittanceConfirm.getBankAccountNumber(), Masking.BANK_ACCOUNT));
		remittanceConfirm.setBankInName(dataMasking.mask(remittanceConfirm.getBankInName(), Masking.NAME));
	}

}
