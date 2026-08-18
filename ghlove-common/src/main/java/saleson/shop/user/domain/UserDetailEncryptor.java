package saleson.shop.user.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class UserDetailEncryptor extends BaseDataEncryptor implements DataEncryptor<UserDetail> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public UserDetailEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(UserDetail userDetail) {
//		userDetail.setAddressDetail(encrypt(userDetail.getAddressDetail(), 100));
//		userDetail.setPhoneNumber(encrypt(userDetail.getPhoneNumber(), 50));
//		userDetail.setTelNumber(encrypt(userDetail.getTelNumber(), 50));
//		userDetail.setBirthday(encrypt(userDetail.getBirthday(), 50));
	}


	@Override
	public void decrypt(UserDetail userDetail, boolean needMasking) {
		userDetail.setAddressDetail(decrypt(userDetail.getAddressDetail()));
		userDetail.setPhoneNumber(decrypt(userDetail.getPhoneNumber()));
		userDetail.setTelNumber(decrypt(userDetail.getTelNumber()));
		userDetail.setBirthday(decrypt(userDetail.getBirthday()));

		if (needMasking) masking(userDetail);
	}

	@Override
	public void masking(UserDetail userDetail) {
		userDetail.setAddressDetail(dataMasking.mask(userDetail.getAddressDetail(), Masking.ADDRESS_DETAIL));
		userDetail.setPhoneNumber(dataMasking.mask(userDetail.getPhoneNumber(), Masking.PHONE_NUMBER));
		userDetail.setTelNumber(dataMasking.mask(userDetail.getTelNumber(), Masking.TEL_NUMBER));
		userDetail.setBirthday(dataMasking.mask(userDetail.getBirthday(), Masking.BIRTHDAY));
	}

}
