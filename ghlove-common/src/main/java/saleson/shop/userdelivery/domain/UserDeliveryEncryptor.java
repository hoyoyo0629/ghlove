package saleson.shop.userdelivery.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class UserDeliveryEncryptor extends BaseDataEncryptor implements DataEncryptor<UserDelivery> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public UserDeliveryEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(UserDelivery userDelivery) {
		userDelivery.setUserName(encrypt(userDelivery.getUserName(), 100));
		userDelivery.setPhone(encrypt(userDelivery.getPhone(), 50));
		userDelivery.setMobile(encrypt(userDelivery.getMobile(), 50));
		userDelivery.setAddressDetail(encrypt(userDelivery.getAddressDetail(), 100));

//		userDelivery.setZipcode(encrypt(userDelivery.getZipcode(), 10));
		userDelivery.setAddress(encrypt(userDelivery.getAddress(), 100));
//		userDelivery.setSido(encrypt(userDelivery.getSido(), 10));
//		userDelivery.setSigungu(encrypt(userDelivery.getSigungu(), 10));
//		userDelivery.setEupmyeondong(encrypt(userDelivery.getEupmyeondong(), 10));
	}

	@Override
	public void decrypt(UserDelivery userDelivery, boolean needMasking) {
		userDelivery.setUserName(decrypt(userDelivery.getUserName()));
		userDelivery.setPhone(decrypt(userDelivery.getPhone()));
		userDelivery.setMobile(decrypt(userDelivery.getMobile()));
		userDelivery.setAddressDetail(decrypt(userDelivery.getAddressDetail()));

//		userDelivery.setZipcode(decrypt(userDelivery.getZipcode()));
		userDelivery.setAddress(decrypt(userDelivery.getAddress()));
//		userDelivery.setSido(decrypt(userDelivery.getSido()));
//		userDelivery.setSigungu(decrypt(userDelivery.getSigungu()));
//		userDelivery.setEupmyeondong(decrypt(userDelivery.getEupmyeondong()));

		if (needMasking) masking(userDelivery);
	}

	@Override
	public void masking(UserDelivery userDelivery) {
		userDelivery.setUserName(dataMasking.mask(userDelivery.getUserName(), Masking.NAME));
		userDelivery.setPhone(dataMasking.mask(userDelivery.getPhone(), Masking.TEL_NUMBER));
		userDelivery.setMobile(dataMasking.mask(userDelivery.getMobile(), Masking.PHONE_NUMBER));
		userDelivery.setAddressDetail(dataMasking.mask(userDelivery.getAddressDetail(), Masking.ADDRESS_DETAIL));
	}
}
