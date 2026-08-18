package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.domain.Order;

@Slf4j
@Component
public class OrderEncryptor extends BaseDataEncryptor implements DataEncryptor<Order> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public OrderEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(Order order) {
		order.setLoginId(encrypt(order.getLoginId(), 31));
		order.setBuyerName(encrypt(order.getBuyerName(), 100));
		order.setPhone(encrypt(order.getPhone(), 50));
		order.setMobile(encrypt(order.getMobile(), 50));
		order.setEmail(encrypt(order.getEmail(), 100));
//		order.setUserName(encrypt(order.getUserName(), 100));
		order.setAddress(encrypt(order.getAddress(), 63));
		order.setAddressDetail(encrypt(order.getAddressDetail(), 500));
		
		order.setIp(encrypt(order.getIp(), 31));
		order.setReturnBankInName(encrypt(order.getReturnBankInName(), 200));
		order.setReturnVirtualNo(encrypt(order.getReturnVirtualNo(), 200));
//		order.setZipcode(encrypt(order.getZipcode(), 10));
//		order.setNewZipcode(encrypt(order.getNewZipcode(), 10));
	}

	@Override
	public void decrypt(Order order, boolean needMasking) {
		order.setLoginId(decrypt(order.getLoginId()));
		order.setBuyerName(decrypt(order.getBuyerName()));
		order.setPhone(decrypt(order.getPhone()));
		order.setMobile(decrypt(order.getMobile()));
		order.setEmail(decrypt(order.getEmail()));
//		order.setUserName(decrypt(order.getUserName()));
		order.setAddressDetail(decrypt(order.getAddressDetail()));

		order.setIp(decrypt(order.getIp()));
		order.setReturnBankInName(decrypt(order.getReturnBankInName()));
		order.setReturnVirtualNo(decrypt(order.getReturnVirtualNo()));
//		order.setZipcode(decrypt(order.getZipcode()));
//		order.setNewZipcode(decrypt(order.getNewZipcode()));
		
		if (needMasking) masking(order);
	}

	@Override
	public void masking(Order order) {
		order.setBuyerName(dataMasking.mask(order.getBuyerName(), Masking.NAME));
		order.setUserName(dataMasking.mask(order.getUserName(), Masking.NAME));
		order.setEmail(dataMasking.mask(order.getEmail(), Masking.EMAIL));
		order.setPhone(dataMasking.mask(order.getPhone(), Masking.TEL_NUMBER));
		order.setMobile(dataMasking.mask(order.getMobile(), Masking.PHONE_NUMBER));
		order.setAddressDetail(dataMasking.mask(order.getAddressDetail(), Masking.ADDRESS_DETAIL));
		
		order.setIp(dataMasking.mask(order.getIp(), Masking.IP));
		order.setReturnBankInName(dataMasking.mask(order.getReturnBankInName(), Masking.BANK_ACCOUNT));
		order.setReturnVirtualNo(dataMasking.mask(order.getReturnVirtualNo(), Masking.BANK_ACCOUNT));
	}
}
