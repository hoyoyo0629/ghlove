package saleson.shop.coupon.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.coupon.support.CouponTargetUser;

@Slf4j
@Component
public class CouponEncryptor extends BaseDataEncryptor implements DataEncryptor<Coupon> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public CouponEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(Coupon couponUser) {
		CouponTargetUser targetUser = couponUser.getSearchCouponTargetUser();
		if (targetUser != null) {
			String userName = targetUser.getUserName();
			String email = targetUser.getEmail();

			if (!ObjectUtils.isEmpty(userName)) targetUser.setUserName(encrypt(targetUser.getUserName(), 100));
			if (!ObjectUtils.isEmpty(email)) targetUser.setEmail(encrypt(targetUser.getEmail(), 150));
		}
	}

	@Override
	public void decrypt(Coupon couponUser, boolean needMasking) {
		CouponTargetUser targetUser = couponUser.getSearchCouponTargetUser();
		if (targetUser != null) {
			String userName = targetUser.getUserName();
			String email = targetUser.getEmail();

			if (!ObjectUtils.isEmpty(userName)) targetUser.setUserName(decrypt(targetUser.getUserName()));
			if (!ObjectUtils.isEmpty(email)) targetUser.setEmail(decrypt(targetUser.getEmail()));
		}

		if (needMasking) masking(couponUser);
	}

	@Override
	public void masking(Coupon couponUser) {
		CouponTargetUser targetUser = couponUser.getSearchCouponTargetUser();
		if (targetUser != null) {
			targetUser.setUserName(dataMasking.mask(targetUser.getUserName(), Masking.NAME));
			targetUser.setEmail(dataMasking.mask(targetUser.getEmail(), Masking.EMAIL));
		}
	}
}
