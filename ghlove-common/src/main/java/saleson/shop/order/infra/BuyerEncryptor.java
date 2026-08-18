package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.domain.Buyer;

@Slf4j
@Component
public class BuyerEncryptor extends BaseDataEncryptor implements DataEncryptor<Buyer> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public BuyerEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(Buyer buyer) {
		buyer.setUserName(encrypt(buyer.getUserName(), 100));
		buyer.setEmail(encrypt(buyer.getEmail(), 50));
		buyer.setPhone(encrypt(buyer.getPhone(), 50));
		buyer.setMobile(encrypt(buyer.getMobile(), 50));
		buyer.setAddressDetail(encrypt(buyer.getAddressDetail(), 150));

		buyer.setAddress(encrypt(buyer.getAddress(), 150));
//		buyer.setSido(encrypt(buyer.getSido(), 10));
//		buyer.setSigungu(encrypt(buyer.getSigungu(), 10));
//		buyer.setEupmyeondong(encrypt(buyer.getEupmyeondong(), 10));
		
		buyer.setIp(encrypt(buyer.getIp(), 31));
		buyer.setLoginId(encrypt(buyer.getLoginId(), 40));
	}

	@Override
	public void decrypt(Buyer buyer, boolean needMasking) {
		buyer.setUserName(decrypt(buyer.getUserName()));
		buyer.setEmail(decrypt(buyer.getEmail()));
		buyer.setPhone(decrypt(buyer.getPhone()));
		buyer.setMobile(decrypt(buyer.getMobile()));
		buyer.setAddressDetail(decrypt(buyer.getAddressDetail()));

		buyer.setAddress(decrypt(buyer.getAddress()));
//		buyer.setSido(decrypt(buyer.getSido()));
//		buyer.setSigungu(decrypt(buyer.getSigungu()));
//		buyer.setEupmyeondong(decrypt(buyer.getEupmyeondong()));
		
		buyer.setIp(decrypt(buyer.getIp()));
		buyer.setLoginId(decrypt(buyer.getLoginId()));

		if (needMasking) masking(buyer);
	}

	@Override
	public void masking(Buyer buyer) {
		buyer.setUserName(dataMasking.mask(buyer.getUserName(), Masking.NAME));
		buyer.setEmail(dataMasking.mask(buyer.getEmail(), Masking.EMAIL));
		buyer.setPhone(dataMasking.mask(buyer.getPhone(), Masking.TEL_NUMBER));
		buyer.setMobile(dataMasking.mask(buyer.getMobile(), Masking.PHONE_NUMBER));
		buyer.setAddressDetail(dataMasking.mask(buyer.getAddressDetail(), Masking.ADDRESS_DETAIL));

		buyer.setIp(dataMasking.mask(buyer.getIp(), Masking.IP));
	}
}
