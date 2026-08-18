package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.order.refund.support.OrderRefundParam;

@Slf4j
@Component
public class OrderRefundParamEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderRefundParam> {
	private final String USER_NAME = "USER_NAME";

	private Cryptor cryptor;

	public OrderRefundParamEncryptor(Cryptor cryptor) {
		super(cryptor);
		this.cryptor = cryptor;
	}

	@Override
	public void encrypt(OrderRefundParam criteria) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
			&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {

				criteria.setQuery(encrypt(criteria.getQuery(), 100));
			}
		}

	}


	@Override
	public void decrypt(OrderRefundParam criteria, boolean needMasking) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())) {

				criteria.setQuery(decrypt(criteria.getQuery()));
			}
		}

		if (needMasking) masking(criteria);
	}

	@Override
	public void masking(OrderRefundParam object) {

	}
}
