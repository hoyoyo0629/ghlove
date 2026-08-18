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
public class QnaAdminAnswerEncryptor extends BaseDataEncryptor implements DataEncryptor<QnaAdminAnswer> {

//	private Cryptor cryptor;
	private DataMasking dataMasking;

	public QnaAdminAnswerEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
//		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}


	@Override
	public void encrypt(QnaAdminAnswer qnaAnswer) {
		qnaAnswer.setUserNm(encrypt(qnaAnswer.getUserNm(), 100));
	}

	@Override
	public void decrypt(QnaAdminAnswer qnaAnswer, boolean needMasking) {
		qnaAnswer.setUserNm(decrypt(qnaAnswer.getUserNm()));

		if (needMasking) masking(qnaAnswer);
	}

	@Override
	public void masking(QnaAdminAnswer qnaAnswer) {
		qnaAnswer.setUserNm(dataMasking.mask(qnaAnswer.getUserNm(), Masking.NAME));
	}


}
