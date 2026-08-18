package saleson.shop.coupon.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class CouponUserEncryptor extends BaseDataEncryptor implements DataEncryptor<CouponUser> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public CouponUserEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(CouponUser couponUser) {
		couponUser.setUserName(encrypt(couponUser.getUserName(), 100));
		couponUser.setEmail(encrypt(couponUser.getEmail(), 150));
		couponUser.setPhoneNumber(encrypt(couponUser.getPhoneNumber(), 50));
	}

	@Override
	public void decrypt(CouponUser couponUser, boolean needMasking) {
		couponUser.setUserName(decrypt(couponUser.getUserName()));
		couponUser.setEmail(decrypt(couponUser.getEmail()));
		couponUser.setPhoneNumber(decrypt(couponUser.getPhoneNumber()));

		if (needMasking) masking(couponUser);
	}

	@Override
	public void masking(CouponUser couponUser) {
		couponUser.setUserName(dataMasking.mask(couponUser.getUserName(), Masking.NAME));
		couponUser.setEmail(dataMasking.mask(couponUser.getEmail(), Masking.EMAIL));
		couponUser.setPhoneNumber(dataMasking.mask(couponUser.getPhoneNumber(), Masking.PHONE_NUMBER));
	}
}
