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
public class SleepUserEncryptor extends BaseDataEncryptor implements DataEncryptor<SleepUser> {
	
	private Cryptor cryptor;
	private DataMasking dataMasking;
	
	public SleepUserEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(SleepUser sleepUser) {
//		sleepUser.setAddress(encrypt(sleepUser.getAddress(), 100));
//		sleepUser.setAddressDetail(encrypt(sleepUser.getAddressDetail(), 100));
	}
	
	@Override
	public void decrypt(SleepUser sleepUser, boolean needMasking) {
		sleepUser.setAddress(decrypt(sleepUser.getAddress()));
		sleepUser.setAddressDetail(decrypt(sleepUser.getAddressDetail()));
		
		if (needMasking) masking(sleepUser);
	}
	
	@Override
	public void masking(SleepUser sleepUser) {
		sleepUser.setAddress(dataMasking.mask(sleepUser.getAddress(), Masking.GH_ADDRESS));
		sleepUser.setAddressDetail(dataMasking.mask(sleepUser.getAddressDetail(), Masking.ADDRESS_DETAIL));
	}
}
