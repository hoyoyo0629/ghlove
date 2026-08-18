package saleson.seller.main.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import saleson.common.security.crypto.BaseDataEncryptor;
import saleson.common.security.crypto.Cryptor;
import saleson.common.security.masking.DataMasking;
import saleson.common.security.masking.Masking;

@Slf4j
@Component
public class SellerEncryptor extends BaseDataEncryptor implements DataEncryptor<Seller> {

	private Cryptor cryptor;
	private DataMasking dataMasking;

	public SellerEncryptor(Cryptor cryptor, DataMasking dataMasking) {
		super(cryptor);
		this.cryptor = cryptor;
		this.dataMasking = dataMasking;
	}

	@Override
	public void encrypt(Seller seller) {
		seller.setSellerName(encrypt(seller.getSellerName(), 100));
		seller.setUserName(encrypt(seller.getUserName(), 100));
		seller.setTelephoneNumber(encrypt(seller.getTelephoneNumber(), 50));
		seller.setPhoneNumber(encrypt(seller.getPhoneNumber(), 50));
		seller.setFaxNumber(encrypt(seller.getFaxNumber(), 50));
		seller.setEmail(encrypt(seller.getEmail(), 100));
		seller.setAddress(encrypt(seller.getAddress(), 100));
		seller.setAddressDetail(encrypt(seller.getAddressDetail(), 100));
		seller.setSecondUserName(encrypt(seller.getSecondUserName(), 100));
		seller.setSecondTelephoneNumber(encrypt(seller.getSecondTelephoneNumber(), 50));
		seller.setSecondPhoneNumber(encrypt(seller.getSecondPhoneNumber(), 50));
		seller.setSecondEmail(encrypt(seller.getSecondEmail(), 100));
		seller.setRepresentativeName(encrypt(seller.getRepresentativeName(), 100));
		seller.setBankInName(encrypt(seller.getBankInName(), 100));
		seller.setBankAccountNumber(encrypt(seller.getBankAccountNumber(), 100));
		seller.setMdName(encrypt(seller.getMdName(), 100));
		
//		seller.setPost(encrypt(seller.getPost(), 10));
		seller.setBusinessLocation(encrypt(seller.getBusinessLocation(), 100));
	}

	@Override
	public void decrypt(Seller seller, boolean needMasking) {
		String email = "";
		seller.setSellerName(decrypt(seller.getSellerName()));
		seller.setUserName(decrypt(seller.getUserName()));
		seller.setTelephoneNumber(decrypt(seller.getTelephoneNumber()));
		seller.setPhoneNumber(decrypt(seller.getPhoneNumber()));
		seller.setFaxNumber(decrypt(seller.getFaxNumber()));
//		seller.setEmail(decrypt(seller.getEmail()));
		seller.setAddressDetail(decrypt(seller.getAddressDetail()));
		seller.setSecondUserName(decrypt(seller.getSecondUserName()));
		seller.setSecondTelephoneNumber(decrypt(seller.getSecondTelephoneNumber()));
		seller.setSecondPhoneNumber(decrypt(seller.getSecondPhoneNumber()));
		seller.setSecondEmail(decrypt(seller.getSecondEmail()));
		seller.setRepresentativeName(decrypt(seller.getRepresentativeName()));
		seller.setBankInName(decrypt(seller.getBankInName()));
		seller.setBankAccountNumber(decrypt(seller.getBankAccountNumber()));
		seller.setMdName(decrypt(seller.getMdName()));
		
		seller.setBusinessLocation(decrypt(seller.getBusinessLocation()));
		
		email = decrypt(seller.getEmail());
		if (email != null && email.contains("@")) {
			String[] emails = email.split("@");
			if (emails.length == 2) {
				seller.setEmail(email);
				seller.setEmailBefore(emails[0]);
				seller.setEmailAfter(emails[1]);
			}
		}
		
//		seller.setPost(decrypt(seller.getPost()));

		if (needMasking) masking(seller);
	}

	@Override
	public void masking(Seller seller) {
		seller.setUserName(dataMasking.mask(seller.getUserName(), Masking.NAME));
		seller.setTelephoneNumber(dataMasking.mask(seller.getTelephoneNumber(), Masking.PHONE_NUMBER));
		seller.setPhoneNumber(dataMasking.mask(seller.getPhoneNumber(), Masking.PHONE_NUMBER));
		seller.setFaxNumber(dataMasking.mask(seller.getFaxNumber(), Masking.PHONE_NUMBER));
		seller.setEmail(dataMasking.mask(seller.getEmail(), Masking.EMAIL));
		seller.setAddressDetail(dataMasking.mask(seller.getAddressDetail(), Masking.ADDRESS_DETAIL));

		seller.setSecondUserName(dataMasking.mask(seller.getSecondUserName(), Masking.NAME));
		seller.setSecondTelephoneNumber(dataMasking.mask(seller.getSecondTelephoneNumber(), Masking.PHONE_NUMBER));
		seller.setSecondPhoneNumber(dataMasking.mask(seller.getSecondPhoneNumber(), Masking.PHONE_NUMBER));
		seller.setSecondEmail(dataMasking.mask(seller.getSecondEmail(), Masking.EMAIL));
		seller.setRepresentativeName(dataMasking.mask(seller.getRepresentativeName(), Masking.NAME));
		seller.setBankInName(dataMasking.mask(seller.getBankInName(), Masking.NAME));
		seller.setBankAccountNumber(dataMasking.mask(seller.getBankAccountNumber(), Masking.BANK_ACCOUNT));
		seller.setMdName(dataMasking.mask(seller.getMdName(), Masking.NAME));
	}
}
