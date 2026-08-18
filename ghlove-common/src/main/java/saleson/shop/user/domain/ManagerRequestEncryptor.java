package saleson.shop.user.domain;

import org.springframework.stereotype.Component;

import com.onlinepowers.framework.security.DataEncryptor;

import lombok.extern.slf4j.Slf4j;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class ManagerRequestEncryptor extends BaseDataEncryptor implements DataEncryptor<ManagerRequest> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public ManagerRequestEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(ManagerRequest managerRequest) {
//		managerRequest.setCttpc(encrypt(managerRequest.getCttpc(), 100));
	}

	@Override
	public void decrypt(ManagerRequest managerRequest, boolean needMasking) {
		managerRequest.setCttpc(decrypt(managerRequest.getCttpc()));
		if (needMasking) masking(managerRequest);
	}

	@Override
	public void masking(ManagerRequest managerRequest) {
		managerRequest.setCttpc(dataMasking.mask(managerRequest.getCttpc(), Masking.PHONE_NUMBER));
	}
}