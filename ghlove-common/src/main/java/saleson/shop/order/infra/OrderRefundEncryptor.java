package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.refund.domain.OrderRefund;

@Slf4j
@Component
public class OrderRefundEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderRefund> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public OrderRefundEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(OrderRefund orderRefund) {
		orderRefund.setBuyerName(encrypt(orderRefund.getBuyerName(), 100));
		orderRefund.setUserName(encrypt(orderRefund.getUserName(), 100));
		orderRefund.setReturnBankInName(encrypt(orderRefund.getReturnBankInName(), 50));
		orderRefund.setReturnVirtualNo(encrypt(orderRefund.getReturnVirtualNo(), 50));
		
		orderRefund.setRequestManagerUserName(encrypt(orderRefund.getRequestManagerUserName(), 50));
		orderRefund.setProcessManagerUserName(encrypt(orderRefund.getProcessManagerUserName(), 50));
		orderRefund.setLoginId(encrypt(orderRefund.getLoginId(), 50));
	}

	@Override
	public void decrypt(OrderRefund orderRefund, boolean needMasking) {
		orderRefund.setBuyerName(decrypt(orderRefund.getBuyerName()));
		orderRefund.setUserName(decrypt(orderRefund.getUserName()));
		orderRefund.setReturnBankInName(decrypt(orderRefund.getReturnBankInName()));
		orderRefund.setReturnVirtualNo(decrypt(orderRefund.getReturnVirtualNo()));

		orderRefund.setRequestManagerUserName(decrypt(orderRefund.getRequestManagerUserName()));
		orderRefund.setProcessManagerUserName(decrypt(orderRefund.getProcessManagerUserName()));
		orderRefund.setLoginId(decrypt(orderRefund.getLoginId()));
		
		if (needMasking) masking(orderRefund);
	}

	@Override
	public void masking(OrderRefund orderRefund) {
		orderRefund.setBuyerName(dataMasking.mask(orderRefund.getBuyerName(), Masking.NAME));
		orderRefund.setUserName(dataMasking.mask(orderRefund.getUserName(), Masking.NAME));
		orderRefund.setReturnBankInName(dataMasking.mask(orderRefund.getReturnBankInName(), Masking.NAME));
		orderRefund.setReturnVirtualNo(dataMasking.mask(orderRefund.getReturnVirtualNo(), Masking.BANK_ACCOUNT));
	}
}
