package saleson.shop.qna.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class QnaEncryptor extends BaseDataEncryptor implements DataEncryptor<Qna> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public QnaEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}



	@Override
	public void encrypt(Qna qna) {
//		qna.setUserName(encrypt(qna.getUserName(), 100));
//		qna.setEmail(encrypt(qna.getEmail(), 200));
	}

	@Override
	public void decrypt(Qna qna, boolean needMasking) {
//		qna.setUserName(decrypt(qna.getUserName()));
//		qna.setEmail(decrypt(qna.getEmail()));

		if (needMasking) masking(qna);
	}

	@Override
	public void masking(Qna qna) {
		qna.setUserName(dataMasking.mask(qna.getUserName(), Masking.NAME));
	}


}
