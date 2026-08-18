package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.shop.order.support.OrderParam;

@Slf4j
@Component
public class OrderParamEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderParam> {
	private final String USER_NAME = "USER_NAME";
	private final String RECEIVE_NAME = "RECEIVE_NAME";
	private final String BANK_IN_NAME = "BANK_IN_NAME";

	private Cryptor cryptor;

	public OrderParamEncryptor(Cryptor cryptor) {
		super(cryptor);
		this.cryptor = cryptor;
	}

	@Override
	public void encrypt(OrderParam criteria) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
			&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())
//					|| RECEIVE_NAME.equals(criteria.getWhere())
//					|| BANK_IN_NAME.equals(criteria.getWhere()) 
					) {

				criteria.setQuery(encrypt(criteria.getQuery(), 50));
			} else if (RECEIVE_NAME.equals(criteria.getWhere())
					|| BANK_IN_NAME.equals(criteria.getWhere()) ) {
				criteria.setQuery(encrypt(criteria.getQuery(), 200));
			}
		}

		if (StringUtils.isNotEmpty(criteria.getReturnBankInName())
		 		&& StringUtils.isNotEmpty(criteria.getReturnVirtualNo())) {

			criteria.setReturnBankInName(encrypt(criteria.getReturnBankInName(), 200));
			criteria.setReturnVirtualNo(encrypt(criteria.getReturnVirtualNo(), 200));
		}

	}


	@Override
	public void decrypt(OrderParam criteria, boolean needMasking) {

		if (criteria.getWhere() != null && !"".equals(criteria.getWhere().trim())
				&& criteria.getQuery() != null && !"".equals(criteria.getQuery().trim())) {

			if (USER_NAME.equals(criteria.getWhere())
					|| RECEIVE_NAME.equals(criteria.getWhere())
					|| BANK_IN_NAME.equals(criteria.getWhere()) ) {

				criteria.setQuery(decrypt(criteria.getQuery()));
			}
		}

		if (needMasking) masking(criteria);
	}

	@Override
	public void masking(OrderParam object) {

	}
}
