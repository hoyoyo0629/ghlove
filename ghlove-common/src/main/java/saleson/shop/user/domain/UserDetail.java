/*
 * Copyright(c) 2009-2011 Onlinepowers Development Team
 * http://www.onlinepowers.com
 *
 * @file com.onlinepowers.web.security.UserDetail.java
 * @date 2011. 10. 5.
 */
package saleson.shop.user.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.SpringSecurityCoreVersion;
import saleson.common.utils.ShopUtils;
import saleson.shop.policy.PolicyService;
import saleson.shop.policy.PolicyServiceImpl;
import saleson.shop.user.support.AgreeDto;
import saleson.shop.userlevel.domain.UserLevel;

import com.onlinepowers.framework.util.MessageUtils;
import com.onlinepowers.framework.util.StringUtils;
import com.onlinepowers.framework.util.ValidationUtils;

import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.RtnpsntInfo;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDetail implements Serializable {
	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private int shadowLoginLogId;
	private String conditionType;

	private String levelName;
	private String groupName;

	private long userId;
	private String groupCode;
	private String newPost;
	private String post;
	private String address;
	private String addressDetail;
	private String telNumber;
	private String phoneNumber;
	private String faxNumber;
	private String receiveEmail;
	private String receiveSms;
	private String receivePush;
	private String receivePbanc;
	/* 20260120 추가 */
	private String receiveKakao;

	private String gender;
	private String age;
	private int point;
	private int buyCount;
	private int buyPrice;
	private String lastBuyDate;
	private String leaveReason;
	private String siteFlag;
	private String useFlag = "Y";
	private String birthdayType;
	private String birthday;
	private int levelId;
	private String levelLabel;
	private String userLevelExpirationDate;
	private float userLevelDiscountRate;
	private float userLevelPointRate;
	private String terms;
	private String privacy;
	private String marketing;

	private String birthdayYear;
	private String birthdayMonth;
	private String birthdayDay;

	private String post1;
	private String post2;

	private String telNumber1;
	private String telNumber2;
	private String telNumber3;

	private String faxNumber1;
	private String faxNumber2;
	private String faxNumber3;

	private String phoneNumber1;
	private String phoneNumber2;
	private String phoneNumber3;

	private UserLevel userlevel;

	private String[] locGovList;
    private String[] rtnpsntList;
    private String locgovCode;
    private String rtnpsnt;

    private String mberCi;
    private String mberDi;
    private String mberDn;
    private String mberFinDn;
    private String sbscrbSeCode;
    private String birthdayFull;
    private String userKey;
    private String loginPathCode;
    private String mberCiYn;

    private String kakaoUserKey;

    private String naverUserKey;

    private String roleCheck;

    private String createdDate;

	public UserDetail(long userId, String phoneNumber, String post,
			String address, String addressDetail, String telNumber, String faxNumber, String receiveEmail, String receiveSms, String receivePush, String receivePbanc,
			String gender,String age, int point, int buyCount, int buyPrice, String leaveReason, String siteFlag, String useFlag, String mberCi, String birthday, String receiveKakao
			) {
		this.userId = userId;
		this.phoneNumber = phoneNumber;
		this.post = post;
		this.address = address;
		this.addressDetail = addressDetail;
		this.telNumber = telNumber;
		this.faxNumber = faxNumber;
		this.receiveEmail = receiveEmail;
		this.receiveSms = receiveSms;
		this.receivePush = receivePush;
		this.receivePbanc = receivePbanc;
		this.gender = gender;
		this.age = age;
		this.point = point;
		this.buyCount = buyCount;
		this.buyPrice = buyPrice;
		this.leaveReason = leaveReason;
		this.siteFlag = siteFlag;
		this.useFlag = useFlag;
		this.mberCi = mberCi;
		this.birthday = birthday;
		this.receiveKakao = receiveKakao;
	}

	public String getLevelLabel(){
		String label = "";
		if (this.levelId > 0){
			String level = String.valueOf(this.levelId);
			if("1".equals(level)){
				label = "일반";
			}else if("2".equals(level)){
				label = "우수";
			}else if("3".equals(level)){
				label = "프리미엄";
			}else if("4".equals(level)){
				label = "VIP";
			}
		} else {
			label = "-";
		}
		return label;
	}

	public String getFrontPhoneNumber() {
		return getPhoneNumber1();
	}

	public String getBackPhoneNumber() {
		return getPhoneNumber2() + getPhoneNumber3();
	}

	public String getPhoneNumber1() {
		if (phoneNumber1 != null && !phoneNumber1.isEmpty()) return phoneNumber1;
		if (phoneNumber == null || phoneNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(phoneNumber)[0];
	}

	public String getPhoneNumber2() {
		if (phoneNumber2 != null && !phoneNumber2.isEmpty()) return phoneNumber2;
		if (phoneNumber == null || phoneNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(phoneNumber)[1];
	}

	public String getPhoneNumber3() {
		if (phoneNumber3 != null && !phoneNumber3.isEmpty()) return phoneNumber3;
		if (phoneNumber == null || phoneNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(phoneNumber)[2];
	}

	public String getTelNumber1() {
		if (telNumber1 != null && !telNumber1.isEmpty()) return telNumber1;
		if (telNumber == null || telNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(telNumber)[0];
	}

	public String getTelNumber2() {
		if (telNumber2 != null && !telNumber2.isEmpty()) return telNumber2;
		if (telNumber == null || telNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(telNumber)[1];
	}

	public String getTelNumber3() {
		if (telNumber3 != null && !telNumber3.isEmpty()) return telNumber3;
		if (telNumber == null || telNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(telNumber)[2];
	}

	public String getFaxNumber1() {
		if (faxNumber1 != null && !faxNumber1.isEmpty()) return faxNumber1;
		if (faxNumber == null || faxNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(faxNumber)[0];
	}

	public String getFaxNumber2() {
		if (faxNumber2 != null && !faxNumber2.isEmpty()) return faxNumber2;
		if (faxNumber == null || faxNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(faxNumber)[1];
	}

	public String getFaxNumber3() {
		if (faxNumber3 != null && !faxNumber3.isEmpty()) return faxNumber3;
		if (faxNumber == null || faxNumber.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(faxNumber)[2];
	}

	public String getBirthdayYear() {
		if (birthdayYear != null && !birthdayYear.isEmpty()) return birthdayYear;
		if (birthday == null || birthday.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(birthday)[0];
	}

	public String getBirthdayMonth() {
		if (birthdayMonth != null && !birthdayMonth.isEmpty()) return birthdayMonth;
		if (birthday == null || birthday.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(birthday)[1];
	}

	public String getBirthdayDay() {
		if (birthdayDay != null && !birthdayDay.isEmpty()) return birthdayDay;
		if (birthday == null || birthday.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(birthday)[2];
	}

	public void processHyphen() {

		if (org.springframework.util.StringUtils.hasText(phoneNumber1)) {
			this.phoneNumber = this.phoneNumber1 + '-' + this.phoneNumber2 + '-' + this.phoneNumber3;
		} else {
			if (org.springframework.util.StringUtils.hasText(getFrontPhoneNumber())) {
				String[] arr = ShopUtils.phoneNumberForDelimitedToStringArray(getFrontPhoneNumber() +  getBackPhoneNumber());
				this.phoneNumber = arr[0] + '-' + arr[1] + '-' + arr[2];
			} else {
				this.phoneNumber = "";
			}
		}

		 this.phoneNumber = StringUtils.hasText(this.phoneNumber1) ? this.phoneNumber1 + '-' + this.phoneNumber2 + '-' + this.phoneNumber3 : "";
		this.telNumber = StringUtils.hasText(this.telNumber1) ? this.telNumber1 + '-' + this.telNumber2 + '-' + this.telNumber3 : "";
		this.faxNumber = StringUtils.hasText(this.faxNumber1) ? this.faxNumber1 + '-' + this.faxNumber2 + '-' + this.faxNumber3 : "";
		this.birthday = StringUtils.hasText(this.birthdayYear) ? this.birthdayYear + '-' + this.birthdayMonth + '-' + this.birthdayDay : "";
	}

	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(UserDetailEncryptor encryptor) {
		encryptor.encrypt(this);
	}



	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(UserDetailEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}

	public String getTerms() {
		return terms;
	}

	public void setTerms(String terms) {
		this.terms = terms;
	}

	public String getPrivacy() {
		return privacy;
	}

	public void setPrivacy(String privacy) {
		this.privacy = privacy;
	}

	public String getMarketing() {
		return marketing;
	}

	public void setMarketing(String marketing) {
		this.marketing = marketing;
	}

	public List<AgreeDto> getAgreeTypeDtos() {
		List<AgreeDto> dtos = new ArrayList<>();
		PolicyService policyService = new PolicyServiceImpl();
		dtos.add(new AgreeDto(policyService.getPolicyType("terms"), "1".equals(getTerms())));
		dtos.add(new AgreeDto(policyService.getPolicyType("privacy"), "1".equals(getPrivacy())));
		dtos.add(new AgreeDto(policyService.getPolicyType("marketing"), "1".equals(getMarketing())));
		return dtos;
	}

	public List<AgreeDto> getAgreeNameDtos() {
		List<AgreeDto> dtos = new ArrayList<>();
		dtos.add(new AgreeDto("terms", "1".equals(getTerms())));
		dtos.add(new AgreeDto("privacy", "1".equals(getPrivacy())));
		dtos.add(new AgreeDto("marketing", "1".equals(getMarketing())));
		return dtos;
	}

	public String[] getLocGovList() {
		if (locGovList == null) {
			return null;
		} else {
	    	int length = locGovList.length;
	    	String[] array = new String[length];
	    	for (int i = 0; i < length; i++) {
				array[i] = locGovList[i];
	    	}
		return array;
		}
	}

	public void setLocGovList(String[] locGovList) {
	    if (locGovList == null) {
	        this.locGovList = null;
	    } else {
	        int length = locGovList.length;
	        this.locGovList = new String[length];
	        for (int i = 0; i < length; i++) {
	            this.locGovList[i] = locGovList[i];
	        }
	    }
	}

	public String getLocGovCode() {
		return locgovCode;
	}
	public void setLocGovCode(String locGovCode) {
		this.locgovCode = locgovCode;
	}

	public String[] getRtnpsntList() {
		if (rtnpsntList == null) {
			return null;
		} else {
	    	int length = rtnpsntList.length;
	    	String[] array = new String[length];
	    	for (int i = 0; i < length; i++) {
				array[i] = rtnpsntList[i];
	    	}
		return array;
		}
	}

	public void setRtnpsntList(String[] rtnpsntList) {
	    if (rtnpsntList == null) {
	        this.rtnpsntList = null;
	    } else {
	        int length = rtnpsntList.length;
	        this.rtnpsntList = new String[length];
	        for (int i = 0; i < length; i++) {
	            this.rtnpsntList[i] = rtnpsntList[i];
	        }
	    }
	}

	public void setRtnpsnt(String rtnpsnt) {
		this.rtnpsnt = rtnpsnt;
	}

	public String getRtnpsnt() {
		return rtnpsnt;
	}

	public String getMberCi() {
		return mberCi;
	}
	public void setMberCi(String mberCi) {
		this.mberCi = mberCi;
	}
	public String getMberDi() {
		return mberDi;
	}
	public void setMberDi(String mberDi) {
		this.mberDi = mberDi;
	}
	public String getMberDn() {
		return mberDn;
	}
	public void setMberDn(String mberDn) {
		this.mberDn = mberDn;
	}
	public String getMberFinDn() {
		return mberFinDn;
	}
	public void setMberFinDn(String mberFinDn) {
		this.mberFinDn = mberFinDn;
	}
	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

}
