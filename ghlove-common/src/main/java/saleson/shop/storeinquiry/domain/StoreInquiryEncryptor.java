package saleson.shop.storeinquiry.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class StoreInquiryEncryptor extends BaseDataEncryptor implements DataEncryptor<StoreInquiry> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public StoreInquiryEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(StoreInquiry storeInquiry) {
		storeInquiry.setUserName(encrypt(storeInquiry.getUserName(), 100));
		storeInquiry.setPhoneNumber(encrypt(storeInquiry.getPhoneNumber(), 50));
		storeInquiry.setEmail(encrypt(storeInquiry.getEmail(), 150));
	}

	@Override
	public void decrypt(StoreInquiry storeInquiry, boolean needMasking) {
		storeInquiry.setUserName(decrypt(storeInquiry.getUserName()));
		storeInquiry.setPhoneNumber(decrypt(storeInquiry.getPhoneNumber()));
		storeInquiry.setEmail(decrypt(storeInquiry.getEmail()));

		if (needMasking) masking(storeInquiry);
	}

	@Override
	public void masking(StoreInquiry storeInquiry) {
		storeInquiry.setUserName(dataMasking.mask(storeInquiry.getUserName(), Masking.NAME));
		storeInquiry.setPhoneNumber(dataMasking.mask(storeInquiry.getPhoneNumber(), Masking.PHONE_NUMBER));
		storeInquiry.setEmail(dataMasking.mask(storeInquiry.getEmail(), Masking.EMAIL));
	}
}
