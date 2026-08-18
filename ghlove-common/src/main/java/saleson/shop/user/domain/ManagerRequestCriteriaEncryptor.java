package saleson.shop.user.domain;

import org.springframework.stereotype.Component;

import com.onlinepowers.framework.security.DataEncryptor;

import lombok.extern.slf4j.Slf4j;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;
import saleson.shop.user.support.ManagerRequestSearchParam;

@Slf4j
@Component
public class ManagerRequestCriteriaEncryptor extends BaseDataEncryptor implements DataEncryptor<ManagerRequestSearchParam> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public ManagerRequestCriteriaEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(ManagerRequestSearchParam managerRequestSearchParam) {
//		managerRequestSearchParam.setCttpc(encrypt(managerRequestSearchParam.getCttpc(), 100));
	}

	@Override
	public void decrypt(ManagerRequestSearchParam managerRequestSearchParam, boolean needMasking) {
		managerRequestSearchParam.setCttpc(decrypt(managerRequestSearchParam.getCttpc()));
		if (needMasking) masking(managerRequestSearchParam);
	}

	@Override
	public void masking(ManagerRequestSearchParam managerRequestSearchParam) {
		managerRequestSearchParam.setCttpc(dataMasking.mask(managerRequestSearchParam.getCttpc(), Masking.PHONE_NUMBER));
	}
}