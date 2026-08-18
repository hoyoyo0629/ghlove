package saleson.shop.sendmaillog.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class SendMailLogEncryptor extends BaseDataEncryptor implements DataEncryptor<SendMailLog> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public SendMailLogEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(SendMailLog sendMailLog) {
		sendMailLog.setReceiveName(encrypt(sendMailLog.getReceiveName(), 100));
		sendMailLog.setReceiveEmail(encrypt(sendMailLog.getReceiveEmail(), 150));

		sendMailLog.setSendName(encrypt(sendMailLog.getSendName(), 15));
		sendMailLog.setSendEmail(encrypt(sendMailLog.getSendEmail(), 100));
		sendMailLog.setReceiveLoginId(encrypt(sendMailLog.getReceiveLoginId(), 31));
	}

	@Override
	public void decrypt(SendMailLog sendMailLog, boolean needMasking) {
		sendMailLog.setReceiveName(decrypt(sendMailLog.getReceiveName()));
		sendMailLog.setReceiveEmail(decrypt(sendMailLog.getReceiveEmail()));

		sendMailLog.setSendName(decrypt(sendMailLog.getSendName()));
		sendMailLog.setSendEmail(decrypt(sendMailLog.getSendEmail()));
		sendMailLog.setReceiveLoginId(decrypt(sendMailLog.getReceiveLoginId()));

		if (needMasking) masking(sendMailLog);
	}

	@Override
	public void masking(SendMailLog sendMailLog) {
		sendMailLog.setReceiveName(dataMasking.mask(sendMailLog.getReceiveName(), Masking.NAME));
		sendMailLog.setReceiveEmail(dataMasking.mask(sendMailLog.getReceiveEmail(), Masking.EMAIL));
	}
}
