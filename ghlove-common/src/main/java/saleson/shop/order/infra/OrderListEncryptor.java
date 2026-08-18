package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.domain.OrderList;

@Slf4j
@Component
public class OrderListEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderList> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public OrderListEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(OrderList order) {
		order.setUserName(encrypt(order.getUserName(), 100));
		order.setBuyerName(encrypt(order.getBuyerName(), 100));
		order.setMobile(encrypt(order.getMobile(), 50));
		order.setReceiveName(encrypt(order.getReceiveName(), 100));
		order.setReceiveMobile(encrypt(order.getReceiveMobile(), 50));
		order.setReceiveAddressDetail(encrypt(order.getReceiveAddressDetail(), 100));

		order.setReceiveAddress(encrypt(order.getReceiveAddress(), 100));
		order.setLoginId(encrypt(order.getLoginId(), 50));

		order.setBirthday(encrypt(order.getBirthday(), 50));

	}

	@Override
	public void decrypt(OrderList order, boolean needMasking) {
		order.setUserName(decrypt(order.getUserName()));
		order.setBuyerName(decrypt(order.getBuyerName()));
		order.setMobile(decrypt(order.getMobile()));
		order.setReceiveName(decrypt(order.getReceiveName()));
		order.setReceiveMobile(decrypt(order.getReceiveMobile()));
		order.setReceiveAddressDetail(decrypt(order.getReceiveAddressDetail()));

		order.setReceiveAddress(decrypt(order.getReceiveAddress()));
		order.setLoginId(decrypt(order.getLoginId()));

		order.setBirthday(decrypt(order.getBirthday()));

		if (needMasking) masking(order);
	}

	@Override
	public void masking(OrderList order) {
		order.setUserName(dataMasking.mask(order.getUserName(), Masking.NAME));
		order.setBuyerName(dataMasking.mask(order.getBuyerName(), Masking.NAME));
		order.setMobile(dataMasking.mask(order.getMobile(), Masking.PHONE_NUMBER));
		order.setReceiveName(dataMasking.mask(order.getReceiveName(), Masking.NAME));
		order.setMobile(dataMasking.mask(order.getReceiveMobile(), Masking.PHONE_NUMBER));
		order.setReceiveAddressDetail(dataMasking.mask(order.getReceiveAddressDetail(), Masking.ADDRESS_DETAIL));
		order.setBirthday(dataMasking.mask(order.getBirthday(), Masking.BIRTHDAY));
	}
}
