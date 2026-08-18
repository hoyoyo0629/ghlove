package saleson.shop.usersns.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class UserSnsEncryptor extends BaseDataEncryptor implements DataEncryptor<UserSns> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public UserSnsEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(UserSns userSns) {
		userSns.setSnsName(encrypt(userSns.getSnsName(), 100));
		userSns.setEmail(encrypt(userSns.getEmail(), 100));
	}


	@Override
	public void decrypt(UserSns userSns, boolean needMasking) {

		userSns.setSnsName(decrypt(userSns.getSnsName()));
		userSns.setEmail(decrypt(userSns.getEmail()));

		if (needMasking) masking(userSns);
	}

	@Override
	public void masking(UserSns userSns) {
		userSns.setSnsName(dataMasking.mask(userSns.getSnsName(), Masking.NAME));
		userSns.setEmail(dataMasking.mask(userSns.getEmail(), Masking.EMAIL));
	}

}
