package saleson.shop.userdelivery.domain;

import com.onlinepowers.framework.repository.CodeInfo;
import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.CodeUtils;
import com.onlinepowers.framework.util.ValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import saleson.common.utils.ShopUtils;

import java.util.List;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
@NoArgsConstructor
public class UserDelivery {

	private long userDeliveryId;
	private long userId;
	@NotEmpty
	private String defaultFlag;
	private String title;
	@NotEmpty
	private String userName;
	private String phone;

	private String phone1;
	private String phone2;
	private String phone3;

	private String mobile;

	private String mobile1;
	private String mobile2;
	private String mobile3;

	private String newZipcode;
	@NotEmpty
	private String zipcode;

	private String zipcode1;
	private String zipcode2;

	private String sido;
	private String sigungu;
	private String eupmyeondong;

	@NotEmpty
	private String address;
	@NotEmpty
	private String addressDetail;
	private String createdDate;

//	@NotEmpty
	private String frontMobile;
//	@NotEmpty
	private String backMobile;


	public String getFrontMobile() {
		return StringUtils.hasText(frontMobile) ? frontMobile : getMobile1();
	}
	public String getBackMobile() {
		return StringUtils.hasText(backMobile) ? backMobile : getMobile2() + getMobile3();
	}

	public String getPhone1() {
		if (phone1 != null && !phone1.isEmpty()) return phone1;
		if (phone == null || phone.isEmpty()) return "";
//		return ShopUtils.phoneNumberForDelimitedToStringArray(phone)[0];
		return getPhoneNoWithHyphen(phone, false)[0];
	}

	public String getPhone2() {
		if (phone2 != null && !phone2.isEmpty()) return phone2;
		if (phone == null || phone.isEmpty()) return "";
//		return ShopUtils.phoneNumberForDelimitedToStringArray(phone)[1];
		return getPhoneNoWithHyphen(phone, false)[1];
	}

	public String getPhone3() {
		if (phone3 != null && !phone3.isEmpty()) return phone3;
		if (phone == null || phone.isEmpty()) return "";
//		return ShopUtils.phoneNumberForDelimitedToStringArray(phone)[2];
		return getPhoneNoWithHyphen(phone, false)[2];
	}

	public String getMobile1() {
		if (mobile1 != null && !mobile1.isEmpty()) return mobile1;
		if (mobile == null || mobile.isEmpty()) return "";
//		return ShopUtils.phoneNumberForDelimitedToStringArray(mobile)[0];
		return getPhoneNoWithHyphen(mobile, true)[0];
	}

	public String getMobile2() {
		if (mobile2 != null && !mobile2.isEmpty()) return mobile2;
		if (mobile == null || mobile.isEmpty()) return "";
//		return ShopUtils.phoneNumberForDelimitedToStringArray(mobile)[1];
		return getPhoneNoWithHyphen(mobile, true)[1];
	}

	public String getMobile3() {
		if (mobile3 != null && !mobile3.isEmpty()) return mobile3;
		if (mobile == null || mobile.isEmpty()) return "";
//		return ShopUtils.phoneNumberForDelimitedToStringArray(mobile)[2];
		return getPhoneNoWithHyphen(mobile, true)[2];
	}

	public void processHyphen() {
		if (StringUtils.hasText(mobile1)) {
			this.mobile = this.mobile1 + '-' + this.mobile2 + '-' + this.mobile3;
		} else {
			if (StringUtils.hasText(getFrontMobile())) {
//				String[] arr = ShopUtils.phoneNumberForDelimitedToStringArray(getFrontMobile() +  getBackMobile());
				String[] arr = getPhoneNoWithHyphen(getFrontMobile() +  getBackMobile(), true);
				this.mobile = arr[0] + '-' + arr[1] + '-' + arr[2];
			} else {
				this.mobile = "";
			}
		}

		if (!StringUtils.hasText(phone)) {
			if (StringUtils.hasText(phone1)) {
				this.phone = this.phone1 + '-' + this.phone2 + '-' + this.phone3;
			} else {
				this.phone ="";
			}
		}
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
	
	private String[] getPhoneNoWithHyphen(String phoneNo, boolean isMobile) {
		List<CodeInfo> phoneCodeList;
		if (isMobile) {
			phoneCodeList = CodeUtils.getCodeInfoList("PHONE");
		} else {
			phoneCodeList = CodeUtils.getCodeInfoList("TEL");
		}
		String[] result = new String[3];
		if (StringUtils.hasLength(phoneNo)) {
			if (phoneNo.contains("-")) {
				return phoneNo.split("-");
			}
			boolean hasPhoneCode = false;
			String frstNo = "";
			String scndNo = "";
			String thrdNo = "";
			for (CodeInfo codeInfo : phoneCodeList) {
				if (phoneNo.indexOf(codeInfo.getLabel()) == 0) {
					hasPhoneCode = true;
					frstNo = codeInfo.getLabel();
					break;
				}
			}
			
			if (hasPhoneCode) {
				try {
					scndNo = phoneNo.substring(frstNo.length(), phoneNo.length() - 4);
					thrdNo = phoneNo.substring(phoneNo.length() - 4);
					
					result[0] = frstNo;
					result[1] = scndNo;
					result[2] = thrdNo;
				} catch (IndexOutOfBoundsException e) {
					result = new String[3];
				}
			}
		}
		
		return result;
	}

}