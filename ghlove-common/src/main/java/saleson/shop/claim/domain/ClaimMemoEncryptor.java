package saleson.shop.claim.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class ClaimMemoEncryptor extends BaseDataEncryptor implements DataEncryptor<ClaimMemo> {
	private Cryptor cryptor;
	private DataMasking dataMasking;

	public ClaimMemoEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(ClaimMemo claimMemo) {
		claimMemo.setUserName(encrypt(claimMemo.getUserName(), 100));
	}

	@Override
	public void decrypt(ClaimMemo claimMemo, boolean needMasking) {
		claimMemo.setUserName(decrypt(claimMemo.getUserName()));


		if (needMasking) masking(claimMemo);
	}

	@Override
	public void masking(ClaimMemo claimMemo) {
		claimMemo.setUserName(dataMasking.mask(claimMemo.getUserName(), Masking.NAME));
	}
}
