package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.claimapply.domain.OrderCancelApply;

@Slf4j
@Component
public class OrderCancelApplyEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderCancelApply> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public OrderCancelApplyEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(OrderCancelApply crderCancelApply) {
		crderCancelApply.setBuyerName(encrypt(crderCancelApply.getBuyerName(), 100));
		crderCancelApply.setReceiveName(encrypt(crderCancelApply.getReceiveName(), 100));
	}

	@Override
	public void decrypt(OrderCancelApply crderCancelApply, boolean needMasking) {
		crderCancelApply.setBuyerName(decrypt(crderCancelApply.getBuyerName()));
		crderCancelApply.setReceiveName(decrypt(crderCancelApply.getReceiveName()));
		
		crderCancelApply.setLoginId(decrypt(crderCancelApply.getLoginId()));

		if (needMasking) masking(crderCancelApply);
	}

	@Override
	public void masking(OrderCancelApply crderCancelApply) {
		crderCancelApply.setBuyerName(dataMasking.mask(crderCancelApply.getBuyerName(), Masking.NAME));
		crderCancelApply.setReceiveName(dataMasking.mask(crderCancelApply.getReceiveName(), Masking.NAME));
	}
}
