package saleson.shop.order.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.utils.ShopUtils;

@Getter @Setter
@NoArgsConstructor
public class Buyer {
	
	public Buyer(User user) {
		
		setUserId(user.getUserId());
		setUserName(user.getUserName());
		setLoginId(user.getLoginId());
		
	}
	
	private String orderCode;
	private int orderSequence;
	private String ip;
	private OrderPrice orderPrice;
	private long userId;
	private String loginId;
	private String userName;
	private String phone;
	private String mobile;
	private String email;
	private String email1;
	private String email2;
	private String zipcode;
	private String newZipcode;
	private String companyName;
	private String sido;
	private String sigungu;
	private String eupmyeondong;
	private String address;
	private String addressDetail;

	private String zipcode1;
	private String zipcode2;

	private String phone1;
	private String phone2;
	private String phone3;

	private String mobile1;
	private String mobile2;
	private String mobile3;
	
	private String locgovCode;
	private int orderPayAmountTotal;		// OrderPrice.orderPayAmountTotal 사용하지 않고 이값 사용
	private int payAmount;					// OrderPrice.payAmount 사용하지 않고 이값 사용

	public String getEmail1() {
		if (email1 != null && !email1.isEmpty()) return email1;
		if (email == null || email.isEmpty()) return "";

		return getEmailPart(0);
	}

	public String getEmail2() {
		if (email2 != null && !email2.isEmpty()) return email2;
		if (email == null || email.isEmpty()) return "";

		return getEmailPart(1);
	}

	private String getEmailPart(int index) {
		if (StringUtils.isNotEmpty(email) && email.contains("@")) {
			String[] temp = StringUtils.delimitedListToStringArray(email, "@");

			if (temp.length == 2) {
				return temp[index];
			}
		}
		return "";
	}

	public String getPhone1() {
		if (phone1 != null && !phone1.isEmpty()) return phone1;
		if (phone == null || phone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(phone)[0];
	}

	public String getPhone2() {
		if (phone2 != null && !phone2.isEmpty()) return phone2;
		if (phone == null || phone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(phone)[1];
	}

	public String getPhone3() {
		if (phone3 != null && !phone3.isEmpty()) return phone3;
		if (phone == null || phone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(phone)[2];
	}

	public String getMobile1() {
		if (mobile1 != null && !mobile1.isEmpty()) return mobile1;
		if (mobile == null || mobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(mobile)[0];
	}

	public String getMobile2() {
		if (mobile2 != null && !mobile2.isEmpty()) return mobile2;
		if (mobile == null || mobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(mobile)[1];
	}

	public String getMobile3() {
		if (mobile3 != null && !mobile3.isEmpty()) return mobile3;
		if (mobile == null || mobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(mobile)[2];
	}

	public void processHyphen() {
		this.phone = StringUtils.hasText(this.phone1) ? this.phone1 + '-' + this.phone2 + '-' + this.phone3 : "";
		this.mobile = StringUtils.hasText(this.mobile1) ? this.mobile1 + '-' + this.mobile2 + '-' + this.mobile3 : "";
	}

	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor encryptor) {
		encryptor.encrypt(this);
	}



	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}


}
