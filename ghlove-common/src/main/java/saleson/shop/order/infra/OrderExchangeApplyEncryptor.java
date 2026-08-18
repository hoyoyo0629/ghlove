package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.claimapply.domain.OrderExchangeApply;

@Slf4j
@Component
public class OrderExchangeApplyEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderExchangeApply> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public OrderExchangeApplyEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(OrderExchangeApply orderExchangeApply) {
		orderExchangeApply.setBuyerName(encrypt(orderExchangeApply.getBuyerName(), 100));
		orderExchangeApply.setReceiveName(encrypt(orderExchangeApply.getReceiveName(), 100));
		orderExchangeApply.setExchangeReceiveName(encrypt(orderExchangeApply.getExchangeReceiveName(), 100));
		orderExchangeApply.setExchangeReceivePhone(encrypt(orderExchangeApply.getExchangeReceivePhone(), 50));
		orderExchangeApply.setExchangeReceiveMobile(encrypt(orderExchangeApply.getExchangeReceiveMobile(), 50));
		orderExchangeApply.setExchangeReceiveAddress2(encrypt(orderExchangeApply.getExchangeReceiveAddress2(), 100));

//		orderExchangeApply.setExchangeReceiveZipcode(encrypt(orderExchangeApply.getExchangeReceiveZipcode(), 10));
		orderExchangeApply.setExchangeReceiveAddress(encrypt(orderExchangeApply.getExchangeReceiveAddress(), 100));
	}

	@Override
	public void decrypt(OrderExchangeApply orderExchangeApply, boolean needMasking) {
		orderExchangeApply.setBuyerName(decrypt(orderExchangeApply.getBuyerName()));
		orderExchangeApply.setReceiveName(decrypt(orderExchangeApply.getReceiveName()));
		orderExchangeApply.setExchangeReceiveName(decrypt(orderExchangeApply.getExchangeReceiveName()));
		orderExchangeApply.setExchangeReceivePhone(decrypt(orderExchangeApply.getExchangeReceivePhone()));
		orderExchangeApply.setExchangeReceiveMobile(decrypt(orderExchangeApply.getExchangeReceiveMobile()));
		orderExchangeApply.setExchangeReceiveAddress2(decrypt(orderExchangeApply.getExchangeReceiveAddress2()));

//		orderExchangeApply.setExchangeReceiveZipcode(decrypt(orderExchangeApply.getExchangeReceiveZipcode()));
		orderExchangeApply.setExchangeReceiveAddress(decrypt(orderExchangeApply.getExchangeReceiveAddress()));
		
		orderExchangeApply.setLoginId(decrypt(orderExchangeApply.getLoginId()));

		if (needMasking) masking(orderExchangeApply);
	}

	@Override
	public void masking(OrderExchangeApply orderExchangeApply) {
		orderExchangeApply.setBuyerName(dataMasking.mask(orderExchangeApply.getBuyerName(), Masking.NAME));
		orderExchangeApply.setReceiveName(dataMasking.mask(orderExchangeApply.getReceiveName(), Masking.NAME));
		orderExchangeApply.setExchangeReceiveName(dataMasking.mask(orderExchangeApply.getExchangeReceiveName(), Masking.NAME));
		orderExchangeApply.setExchangeReceivePhone(dataMasking.mask(orderExchangeApply.getExchangeReceivePhone(), Masking.TEL_NUMBER));
		orderExchangeApply.setExchangeReceiveMobile(dataMasking.mask(orderExchangeApply.getExchangeReceiveMobile(), Masking.PHONE_NUMBER));
		orderExchangeApply.setExchangeReceiveAddress2(dataMasking.mask(orderExchangeApply.getExchangeReceiveAddress2(), Masking.ADDRESS_DETAIL));
	}
}
