package saleson.shop.item.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.item.support.ItemParam;

@Slf4j
@Component
public class ItemCriteriaEncryptor extends BaseDataEncryptor implements DataEncryptor<ItemParam> {

	private final String USER_NAME = "USER_NAME";

	private Cryptor cryptor;

	public ItemCriteriaEncryptor(Cryptor cryptor) {
		super(cryptor);
		this.cryptor = cryptor;
	}

	@Override
	public void encrypt(ItemParam criteria) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
			&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere()) ) {

				criteria.setQuery(encrypt(criteria.getQuery(), 100));
			}
		}

	}


	@Override
	public void decrypt(ItemParam criteria, boolean needMasking) {
		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {
				criteria.setQuery(decrypt(criteria.getQuery()));
			}
		}

		if (needMasking) masking(criteria);
	}

	@Override
	public void masking(ItemParam object) {

	}
}
