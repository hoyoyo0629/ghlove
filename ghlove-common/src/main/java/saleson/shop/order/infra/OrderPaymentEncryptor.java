package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.domain.OrderPayment;

@Slf4j
@Component
public class OrderPaymentEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderPayment> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public OrderPaymentEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(OrderPayment orderPayment) {
		orderPayment.setUserName(encrypt(orderPayment.getUserName(), 100));
		orderPayment.setBankInName(encrypt(orderPayment.getBankInName(), 50));
		orderPayment.setBuyerName(encrypt(orderPayment.getBuyerName(), 100));
		orderPayment.setPaymentSummary(encrypt(orderPayment.getPaymentSummary(), 100));
		
		orderPayment.setBankVirtualNo(encrypt(orderPayment.getBankVirtualNo(), 100));
	}

	@Override
	public void decrypt(OrderPayment orderPayment, boolean needMasking) {
		orderPayment.setUserName(decrypt(orderPayment.getUserName()));
		orderPayment.setBankInName(decrypt(orderPayment.getBankInName()));
		orderPayment.setBuyerName(decrypt(orderPayment.getBuyerName()));
		orderPayment.setPaymentSummary(decrypt(orderPayment.getPaymentSummary()));
		
		orderPayment.setBankVirtualNo(decrypt(orderPayment.getBankVirtualNo()));

		if (needMasking) masking(orderPayment);
	}

	@Override
	public void masking(OrderPayment orderPayment) {
		orderPayment.setUserName(dataMasking.mask(orderPayment.getUserName(), Masking.NAME));
		orderPayment.setBankInName(dataMasking.mask(orderPayment.getBankInName(), Masking.NAME));
		orderPayment.setBuyerName(dataMasking.mask(orderPayment.getBuyerName(), Masking.NAME));
		
		orderPayment.setBankVirtualNo(dataMasking.mask(orderPayment.getBankVirtualNo(), Masking.BANK_ACCOUNT));
	}
}
