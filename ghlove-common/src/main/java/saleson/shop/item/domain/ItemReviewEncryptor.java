package saleson.shop.item.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class ItemReviewEncryptor extends BaseDataEncryptor implements DataEncryptor<ItemReview> {
	private Cryptor cryptor;
	private DataMasking dataMasking;

	public ItemReviewEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}


	@Override
	public void encrypt(ItemReview itemReview) {
		itemReview.setUserName(encrypt(itemReview.getUserName(), 100));
	}

	@Override
	public void decrypt(ItemReview itemReview, boolean needMasking) {
		itemReview.setUserName(decrypt(itemReview.getUserName()));


		if (needMasking) masking(itemReview);
	}

	@Override
	public void masking(ItemReview itemReview) {
		itemReview.setUserName(dataMasking.mask(itemReview.getUserName(), Masking.NAME));
	}

}
