package saleson.shop.user.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.security.userdetails.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class UserEncryptor extends BaseDataEncryptor implements DataEncryptor<User> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public UserEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}



	@Override
	public void encrypt(User user) {
//		user.setUserName(encrypt(user.getUserName(), 100));
//		user.setEmail(encrypt(user.getEmail(), 100));
//		user.setPhoneNumber(encrypt(user.getPhoneNumber(), 50));
	}

	@Override
	public void decrypt(User user, boolean needMasking) {
		user.setUserName(decrypt(user.getUserName()));
		user.setEmail(decrypt(user.getEmail()));
		user.setPhoneNumber(decrypt(user.getPhoneNumber()));

		if (needMasking) masking(user);
	}

	@Override
	public void masking(User user) {
		user.setUserName(dataMasking.mask(user.getUserName(), Masking.NAME));
		user.setEmail(dataMasking.mask(user.getEmail(), Masking.EMAIL));
		user.setPhoneNumber(dataMasking.mask(user.getPhoneNumber(), Masking.PHONE_NUMBER));
	}
}
