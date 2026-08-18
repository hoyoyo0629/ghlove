package saleson.shop.qnaadmin.domain;

import com.onlinepowers.framework.security.DataEncryptor;
//import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

//@Slf4j
@Component
public class QnaAdminEncryptor extends BaseDataEncryptor implements DataEncryptor<QnaAdmin> {

//	private Cryptor cryptor;
	private DataMasking dataMasking;

	public QnaAdminEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
//		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}



	@Override
	public void encrypt(QnaAdmin qnaAdmin) {
		qnaAdmin.setUserName(encrypt(qnaAdmin.getUserName(), 100));
		qnaAdmin.setEmail(encrypt(qnaAdmin.getEmail(), 200));
	}

	@Override
	public void decrypt(QnaAdmin qnaAdmin, boolean needMasking) {
		qnaAdmin.setUserName(decrypt(qnaAdmin.getUserName()));
		qnaAdmin.setEmail(decrypt(qnaAdmin.getEmail()));
		qnaAdmin.setLoginId(decrypt(qnaAdmin.getLoginId()));

		if (needMasking) masking(qnaAdmin);
	}

	@Override
	public void masking(QnaAdmin qnaAdmin) {
		qnaAdmin.setUserName(dataMasking.mask(qnaAdmin.getUserName(), Masking.NAME));
	}


}
