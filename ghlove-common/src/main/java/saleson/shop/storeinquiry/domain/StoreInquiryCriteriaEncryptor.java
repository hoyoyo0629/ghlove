package saleson.shop.storeinquiry.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.storeinquiry.support.StoreInquiryParam;

@Slf4j
@Component
public class StoreInquiryCriteriaEncryptor extends BaseDataEncryptor implements DataEncryptor<StoreInquiryParam> {

	private final String USER_NAME = "USER_NAME";

	private Cryptor cryptor;

	public StoreInquiryCriteriaEncryptor(Cryptor cryptor) {
		super(cryptor);
		this.cryptor = cryptor;
	}

	@Override
	public void encrypt(StoreInquiryParam criteria) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {
				criteria.setQuery(encrypt(criteria.getQuery(), 100));
			}
		}
	}


	@Override
	public void decrypt(StoreInquiryParam criteria, boolean needMasking) {
		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {
				criteria.setQuery(decrypt(criteria.getQuery()));
			}
		}
	}

	@Override
	public void masking(StoreInquiryParam object) {

	}
}
