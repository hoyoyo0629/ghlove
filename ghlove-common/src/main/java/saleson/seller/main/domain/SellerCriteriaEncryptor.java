package saleson.seller.main.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.seller.main.support.SellerParam;

@Slf4j
@Component
public class SellerCriteriaEncryptor extends BaseDataEncryptor implements DataEncryptor<SellerParam> {

	private final String USER_NAME = "USER_NAME";

	private Cryptor cryptor;

	public SellerCriteriaEncryptor(Cryptor cryptor) {
		super(cryptor);
		this.cryptor = cryptor;
	}

	@Override
	public void encrypt(SellerParam criteria) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {
				criteria.setQuery(encrypt(criteria.getQuery(), 100));
			}
		}
	}


	@Override
	public void decrypt(SellerParam criteria, boolean needMasking) {
		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {
				criteria.setQuery(decrypt(criteria.getQuery()));
			}
		}
	}

	@Override
	public void masking(SellerParam object) {

	}
}
