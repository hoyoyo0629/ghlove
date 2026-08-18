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
public class PersonInChargeEncryptor extends BaseDataEncryptor implements DataEncryptor<PersonInCharge> {

	private Cryptor cryptor;
	private DataMasking dataMasking;
	
	public PersonInChargeEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}
	
	@Override
	public void encrypt(PersonInCharge personInCharge) {
//		personInCharge.setUserName(encrypt(personInCharge.getUserName(), 100));
//		personInCharge.setPhoneNumber(encrypt(personInCharge.getPhoneNumber(), 50));
	}
	
	@Override
	public void decrypt(PersonInCharge personInCharge, boolean needMasking) {
		personInCharge.setUserName(decrypt(personInCharge.getUserName()));
		personInCharge.setPhoneNumber(decrypt(personInCharge.getPhoneNumber()));
		
		if (needMasking) masking(personInCharge);
	}
	
	@Override
	public void masking(PersonInCharge personInCharge) {
		personInCharge.setUserName(dataMasking.mask(personInCharge.getUserName(), Masking.NAME));
		personInCharge.setPhoneNumber(dataMasking.mask(personInCharge.getPhoneNumber(), Masking.GH_PHONE_NUMBER));
	}
}
