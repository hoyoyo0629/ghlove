package saleson.shop.qna.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.qna.support.QnaParam;

@Slf4j
@Component
public class QnaCriteriaEncryptor extends BaseDataEncryptor implements DataEncryptor<QnaParam> {

	private final String USER_NAME = "USER_NAME";

	private Cryptor cryptor;

	public QnaCriteriaEncryptor(Cryptor cryptor) {
		super(cryptor);
		this.cryptor = cryptor;
	}

	@Override
	public void encrypt(QnaParam criteria) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere()) ) {

//				criteria.setQuery(encrypt(criteria.getQuery(), 100));
			}
		}

	}


	@Override
	public void decrypt(QnaParam criteria, boolean needMasking) {
		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {
				criteria.setQuery(decrypt(criteria.getQuery()));
			}
		}
	}

	@Override
	public void masking(QnaParam object) {

	}
}
