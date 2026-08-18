package saleson.shop.user.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.user.support.UserSearchParam;

@Slf4j
@Component
public class UserCriteriaEncryptor extends BaseDataEncryptor implements DataEncryptor<UserSearchParam> {
	private final String EMAIL = "EMAIL";
	private final String USER_NAME = "USER_NAME";
	private final String PHONE_NUMBER = "PHONE_NUMBER";

	private Cryptor cryptor;

	public UserCriteriaEncryptor(Cryptor cryptor) {
		super(cryptor);
		this.cryptor = cryptor;
	}

	@Override
	public void encrypt(UserSearchParam criteria) {
//		criteria.setUserName(encrypt(criteria.getUserName(), 100));
//		criteria.setEmail(encrypt(criteria.getEmail(), 100));
//		criteria.setPhoneNumber(encrypt(criteria.getPhoneNumber(), 50));
//
//
//		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
//			&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {
//
//			if (EMAIL.equals(criteria.getWhere())
//					|| USER_NAME.equals(criteria.getWhere())
//					|| PHONE_NUMBER.equals(criteria.getWhere()) ) {
//
//				criteria.setQuery(encrypt(criteria.getQuery(), 50));
//			}
//		}

	}


	@Override
	public void decrypt(UserSearchParam criteria, boolean needMasking) {
		criteria.setUserName(decrypt(criteria.getUserName()));
		criteria.setEmail(decrypt(criteria.getEmail()));
		criteria.setPhoneNumber(decrypt(criteria.getPhoneNumber()));


		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (EMAIL.equals(criteria.getWhere())
					|| USER_NAME.equals(criteria.getWhere())
					|| PHONE_NUMBER.equals(criteria.getWhere()) ) {

				criteria.setQuery(decrypt(criteria.getQuery()));
			}
		}

		if (needMasking) masking(criteria);
	}

	@Override
	public void masking(UserSearchParam object) {

	}
}
