package saleson.shop.claim.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.claim.support.ClaimMemoParam;

@Slf4j
@Component
public class ClaimCriteriaEncryptor extends BaseDataEncryptor implements DataEncryptor<ClaimMemoParam> {

	private final String USER_NAME = "USER_NAME";

	private Cryptor cryptor;

	public ClaimCriteriaEncryptor(Cryptor cryptor) {
		super(cryptor);
		this.cryptor = cryptor;
	}

	@Override
	public void encrypt(ClaimMemoParam criteria) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
			&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere()) ) {
				criteria.setQuery(encrypt(criteria.getQuery(), 100));
			}
		}
	}


	@Override
	public void decrypt(ClaimMemoParam criteria, boolean needMasking) {
		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {
				criteria.setQuery(decrypt(criteria.getQuery()));
			}
		}

		if (needMasking) masking(criteria);
	}

	@Override
	public void masking(ClaimMemoParam object) {

	}

}
