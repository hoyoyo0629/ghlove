package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.claimapply.domain.OrderReturnApply;

@Slf4j
@Component
public class OrderReturnApplyEncryptor extends BaseDataEncryptor implements DataEncryptor<OrderReturnApply> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public OrderReturnApplyEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(OrderReturnApply orderReturnApply) {
		orderReturnApply.setBuyerName(encrypt(orderReturnApply.getBuyerName(), 100));
		orderReturnApply.setReceiveName(encrypt(orderReturnApply.getReceiveName(), 100));
		orderReturnApply.setReturnReserveName(encrypt(orderReturnApply.getReturnReserveName(), 100));
		orderReturnApply.setReturnReservePhone(encrypt(orderReturnApply.getReturnReservePhone(), 50));
		orderReturnApply.setReturnReserveMobile(encrypt(orderReturnApply.getReturnReserveMobile(), 50));
		orderReturnApply.setReturnReserveAddress2(encrypt(orderReturnApply.getReturnReserveAddress2(), 100));
		
		orderReturnApply.setReturnReserveAddress(encrypt(orderReturnApply.getReturnReserveAddress(), 63));
//		orderReturnApply.setReturnReserveZipcode(encrypt(orderReturnApply.getReturnReserveZipcode(), 10));
	}

	@Override
	public void decrypt(OrderReturnApply orderReturnApply, boolean needMasking) {
		orderReturnApply.setBuyerName(decrypt(orderReturnApply.getBuyerName()));
		orderReturnApply.setReceiveName(decrypt(orderReturnApply.getReceiveName()));
		orderReturnApply.setReturnReserveName(decrypt(orderReturnApply.getReturnReserveName()));
		orderReturnApply.setReturnReservePhone(decrypt(orderReturnApply.getReturnReservePhone()));
		orderReturnApply.setReturnReserveMobile(decrypt(orderReturnApply.getReturnReserveMobile()));
		orderReturnApply.setReturnReserveAddress2(decrypt(orderReturnApply.getReturnReserveAddress2()));
		
		orderReturnApply.setReturnReserveAddress(decrypt(orderReturnApply.getReturnReserveAddress()));
//		orderReturnApply.setReturnReserveZipcode(decrypt(orderReturnApply.getReturnReserveZipcode()));

		orderReturnApply.setLoginId(decrypt(orderReturnApply.getLoginId()));
		
		if (needMasking) masking(orderReturnApply);
	}

	@Override
	public void masking(OrderReturnApply orderReturnApply) {
		orderReturnApply.setBuyerName(dataMasking.mask(orderReturnApply.getBuyerName(), Masking.NAME));
		orderReturnApply.setReceiveName(dataMasking.mask(orderReturnApply.getReceiveName(), Masking.NAME));
		orderReturnApply.setReturnReserveName(dataMasking.mask(orderReturnApply.getReturnReserveName(), Masking.NAME));
		orderReturnApply.setReturnReservePhone(dataMasking.mask(orderReturnApply.getReturnReservePhone(), Masking.TEL_NUMBER));
		orderReturnApply.setReturnReserveMobile(dataMasking.mask(orderReturnApply.getReturnReserveMobile(), Masking.PHONE_NUMBER));
		orderReturnApply.setReturnReserveAddress2(dataMasking.mask(orderReturnApply.getReturnReserveAddress2(), Masking.ADDRESS_DETAIL));
	}
}
