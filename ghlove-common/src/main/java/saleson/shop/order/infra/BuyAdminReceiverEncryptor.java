package saleson.shop.order.infra;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.order.admin.domain.BuyAdminReceiver;

@Slf4j
@Component
public class BuyAdminReceiverEncryptor extends BaseDataEncryptor implements DataEncryptor<BuyAdminReceiver> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public BuyAdminReceiverEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(BuyAdminReceiver buyAdminReceiver) {
		buyAdminReceiver.setReceiveName(encrypt(buyAdminReceiver.getReceiveName(), 100));
		buyAdminReceiver.setReceivePhone(encrypt(buyAdminReceiver.getReceivePhone(), 50));
		buyAdminReceiver.setReceiveMobile(encrypt(buyAdminReceiver.getReceiveMobile(), 50));
		buyAdminReceiver.setReceiveAddressDetail(encrypt(buyAdminReceiver.getReceiveAddressDetail(), 150));

		buyAdminReceiver.setReceiveAddress(encrypt(buyAdminReceiver.getReceiveAddress(), 150));
		buyAdminReceiver.setReceiveSido(encrypt(buyAdminReceiver.getReceiveSido(), 10));
		buyAdminReceiver.setReceiveSigungu(encrypt(buyAdminReceiver.getReceiveSigungu(), 10));
		buyAdminReceiver.setReceiveEupmyeondong(encrypt(buyAdminReceiver.getReceiveEupmyeondong(), 10));
	}

	@Override
	public void decrypt(BuyAdminReceiver buyAdminReceiver, boolean needMasking) {
		buyAdminReceiver.setReceiveName(decrypt(buyAdminReceiver.getReceiveName()));
		buyAdminReceiver.setReceivePhone(decrypt(buyAdminReceiver.getReceivePhone()));
		buyAdminReceiver.setReceiveMobile(decrypt(buyAdminReceiver.getReceiveMobile()));
		buyAdminReceiver.setReceiveAddressDetail(decrypt(buyAdminReceiver.getReceiveAddressDetail()));

		buyAdminReceiver.setReceiveAddress(decrypt(buyAdminReceiver.getReceiveAddress()));
		buyAdminReceiver.setReceiveSido(decrypt(buyAdminReceiver.getReceiveSido()));
		buyAdminReceiver.setReceiveSigungu(decrypt(buyAdminReceiver.getReceiveSigungu()));
		buyAdminReceiver.setReceiveEupmyeondong(decrypt(buyAdminReceiver.getReceiveEupmyeondong()));
		if (needMasking) masking(buyAdminReceiver);
	}

	@Override
	public void masking(BuyAdminReceiver buyAdminReceiver) {
		buyAdminReceiver.setReceiveName(dataMasking.mask(buyAdminReceiver.getReceiveName(), Masking.NAME));
		buyAdminReceiver.setReceivePhone(dataMasking.mask(buyAdminReceiver.getReceivePhone(), Masking.TEL_NUMBER));
		buyAdminReceiver.setReceiveMobile(dataMasking.mask(buyAdminReceiver.getReceiveMobile(), Masking.PHONE_NUMBER));
		buyAdminReceiver.setReceiveAddressDetail(dataMasking.mask(buyAdminReceiver.getReceiveAddressDetail(), Masking.ADDRESS_DETAIL));
	}
}
