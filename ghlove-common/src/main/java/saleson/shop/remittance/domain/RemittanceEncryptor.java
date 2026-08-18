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
public class RemittanceEncryptor extends BaseDataEncryptor implements DataEncryptor<Remittance> {
	private Cryptor cryptor;
	private DataMasking dataMasking;

	public RemittanceEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}


	@Override
	public void encrypt(Remittance remittance) {
		remittance.setUserName(encrypt(remittance.getUserName(), 100));
		remittance.setSellerName(encrypt(remittance.getSellerName(), 100));
		remittance.setTelephoneNumber(encrypt(remittance.getTelephoneNumber(), 50));
		remittance.setPhoneNumber(encrypt(remittance.getPhoneNumber(), 50));
		remittance.setBankAccountNumber(encrypt(remittance.getBankAccountNumber(), 100));
		remittance.setBankInName(encrypt(remittance.getBankInName(), 50));
		
		remittance.setFinishingManagerName(encrypt(remittance.getFinishingManagerName(), 31));
		remittance.setBankName(encrypt(remittance.getBankName(), 15));
		remittance.setRepresentativeName(encrypt(remittance.getRepresentativeName(), 31));
	}

	@Override
	public void decrypt(Remittance remittance, boolean needMasking) {
		remittance.setUserName(decrypt(remittance.getUserName()));
		remittance.setSellerName(decrypt(remittance.getSellerName()));
		remittance.setTelephoneNumber(decrypt(remittance.getTelephoneNumber()));
		remittance.setPhoneNumber(decrypt(remittance.getPhoneNumber()));
		remittance.setBankAccountNumber(decrypt(remittance.getBankAccountNumber()));
		remittance.setBankInName(decrypt(remittance.getBankInName()));
		
		remittance.setFinishingManagerName(decrypt(remittance.getFinishingManagerName()));
		remittance.setBankName(decrypt(remittance.getBankName()));
		remittance.setRepresentativeName(decrypt(remittance.getRepresentativeName()));

		if (needMasking) masking(remittance);
	}

	@Override
	public void masking(Remittance remittance) {
		remittance.setUserName(dataMasking.mask(remittance.getUserName(), Masking.NAME));
		remittance.setSellerName(dataMasking.mask(remittance.getSellerName(), Masking.NAME));
		remittance.setTelephoneNumber(dataMasking.mask(remittance.getTelephoneNumber(), Masking.TEL_NUMBER));
		remittance.setPhoneNumber(dataMasking.mask(remittance.getPhoneNumber(), Masking.PHONE_NUMBER));
		remittance.setBankAccountNumber(dataMasking.mask(remittance.getBankAccountNumber(), Masking.BANK_ACCOUNT));
		remittance.setBankInName(dataMasking.mask(remittance.getBankInName(), Masking.NAME));
	}

}
