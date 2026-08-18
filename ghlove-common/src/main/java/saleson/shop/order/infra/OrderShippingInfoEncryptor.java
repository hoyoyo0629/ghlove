package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.domain.OrderShippingInfo;

@Slf4j
@Component
public class OrderShippingInfoEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderShippingInfo> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public OrderShippingInfoEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(OrderShippingInfo orderShippingInfo) {
		orderShippingInfo.setReceiveName(encrypt(orderShippingInfo.getReceiveName(), 100));
		orderShippingInfo.setReceivePhone(encrypt(orderShippingInfo.getReceivePhone(), 50));
		orderShippingInfo.setReceiveMobile(encrypt(orderShippingInfo.getReceiveMobile(), 50));
		orderShippingInfo.setReceiveAddressDetail(encrypt(orderShippingInfo.getReceiveAddressDetail(), 150));
		
		orderShippingInfo.setReceiveAddress(encrypt(orderShippingInfo.getReceiveAddress(), 150));
//		orderShippingInfo.setReceiveZipcode(encrypt(orderShippingInfo.getReceiveZipcode(), 10));
//		orderShippingInfo.setReceiveNewZipcode(encrypt(orderShippingInfo.getReceiveNewZipcode(), 10));
	}

	@Override
	public void decrypt(OrderShippingInfo orderShippingInfo, boolean needMasking) {
		orderShippingInfo.setReceiveName(decrypt(orderShippingInfo.getReceiveName()));
		orderShippingInfo.setReceivePhone(decrypt(orderShippingInfo.getReceivePhone()));
		orderShippingInfo.setReceiveMobile(decrypt(orderShippingInfo.getReceiveMobile()));
		orderShippingInfo.setReceiveAddressDetail(decrypt(orderShippingInfo.getReceiveAddressDetail()));
		
		orderShippingInfo.setReceiveAddress(decrypt(orderShippingInfo.getReceiveAddress()));
//		orderShippingInfo.setReceiveZipcode(decrypt(orderShippingInfo.getReceiveZipcode()));
//		orderShippingInfo.setReceiveNewZipcode(decrypt(orderShippingInfo.getReceiveNewZipcode()));

		if (needMasking) masking(orderShippingInfo);
	}

	@Override
	public void masking(OrderShippingInfo orderShippingInfo) {
		orderShippingInfo.setReceiveName(dataMasking.mask(orderShippingInfo.getReceiveName(), Masking.NAME));
		orderShippingInfo.setReceivePhone(dataMasking.mask(orderShippingInfo.getReceivePhone(), Masking.TEL_NUMBER));
		orderShippingInfo.setReceiveMobile(dataMasking.mask(orderShippingInfo.getReceiveMobile(), Masking.PHONE_NUMBER));
		orderShippingInfo.setReceiveAddressDetail(dataMasking.mask(orderShippingInfo.getReceiveAddressDetail(), Masking.ADDRESS_DETAIL));
	}
}
