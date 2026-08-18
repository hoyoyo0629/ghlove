package saleson.shop.user.domain;

import org.springframework.stereotype.Component;

import com.onlinepowers.framework.security.DataEncryptor;

import lombok.extern.slf4j.Slf4j;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class GeneralCustomerEncryptor extends BaseDataEncryptor implements DataEncryptor<GeneralCustomer> {

	private Cryptor cryptor;
	private DataMasking dataMasking;
	
	public GeneralCustomerEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}
	
	@Override
	public void encrypt(GeneralCustomer generalCustomer) {
//		generalCustomer.setPhoneNumber(encrypt(generalCustomer.getPhoneNumber(), 50));
//		generalCustomer.setEmail(encrypt(generalCustomer.getEmail(), 150));
//		generalCustomer.setAddress(encrypt(generalCustomer.getAddress(), 100));
//		generalCustomer.setAddressDetail(encrypt(generalCustomer.getAddressDetail(), 100));
	}
	
	@Override
	public void decrypt(GeneralCustomer generalCustomer, boolean needMasking) {
		generalCustomer.setPhoneNumber(decrypt(generalCustomer.getPhoneNumber()));
		generalCustomer.setEmail(decrypt(generalCustomer.getEmail()));
		generalCustomer.setAddress(decrypt(generalCustomer.getAddress()));
		generalCustomer.setAddressDetail(decrypt(generalCustomer.getAddressDetail()));

		if (needMasking) masking(generalCustomer);
	}
	
	@Override
	public void masking(GeneralCustomer generalCustomer) {
		generalCustomer.setPhoneNumber(dataMasking.mask(generalCustomer.getPhoneNumber(), Masking.GH_PHONE_NUMBER));
		generalCustomer.setEmail(dataMasking.mask(generalCustomer.getEmail(), Masking.GH_EMAIL));
		generalCustomer.setAddress(dataMasking.mask(generalCustomer.getAddress(), Masking.GH_ADDRESS));
		generalCustomer.setAddressDetail(dataMasking.mask(generalCustomer.getAddressDetail(), Masking.ADDRESS_DETAIL));
	}
}
