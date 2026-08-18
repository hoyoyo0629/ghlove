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
public class QnaAnswerEncryptor extends BaseDataEncryptor implements DataEncryptor<QnaAnswer> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public QnaAnswerEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}


	@Override
	public void encrypt(QnaAnswer qnaAnswer) {
//		qnaAnswer.setUserNm(encrypt(qnaAnswer.getUserNm(), 100));
	}

	@Override
	public void decrypt(QnaAnswer qnaAnswer, boolean needMasking) {
		qnaAnswer.setUserNm(decrypt(qnaAnswer.getUserNm()));

		if (needMasking) masking(qnaAnswer);
	}

	@Override
	public void masking(QnaAnswer qnaAnswer) {
		qnaAnswer.setUserNm(dataMasking.mask(qnaAnswer.getUserNm(), Masking.NAME));
	}


}
