package saleson.shop.order.domain;

import java.util.regex.Pattern;

import com.onlinepowers.framework.security.DataEncryptor;

import saleson.common.utils.ShopUtils;

public class OrderList extends OrderItem {
	private String loginId;
	private String userName;
	private String receiveName;
	private String memo;

	/* 엑셀 다운로드를 위해 복호화된 데이터 */
	private String receiveNameDec;
	private String mobileDec;
	private String receiveAddressDec;

	private String buyerName;

	private String receiveMobile;
	private String receiveZipcode;
	private String receiveNewZipcode;
	private String receiveAddress;
	private String receiveAddressDetail;
	private String mobile;

	private String isPoint;

	private String birthday;

	private String itemTextOptionFlag="N";			// 판매자입력 20260325 필수 추가정보 isb
	private String textOption="";					// 구매자입력 20260325 필수 추가정보 isb

	public String getTextOption() {
		return textOption;
	}

	public void setTextOption(String textOption) {
		this.textOption = textOption;
	}

	public String getItemTextOptionFlag() {
		return itemTextOptionFlag;
	}

	public void setItemTextOptionFlag(String itemTextOptionFlag) {
		this.itemTextOptionFlag = itemTextOptionFlag;
	}


    public String getIsPoint() {
        return isPoint;
    }

    public void setIsPoint(String isPoint) {
        this.isPoint = isPoint;
    }

    public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public String getReceiveMobile() {
		return receiveMobile;
	}
	public void setReceiveMobile(String receiveMobile) {
		this.receiveMobile = receiveMobile;
	}
	public String getReceiveZipcode() {
		return receiveZipcode;
	}
	public void setReceiveZipcode(String receiveZipcode) {
		this.receiveZipcode = receiveZipcode;
	}
	public String getReceiveAddress() {
		return receiveAddress;
	}
	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}
	public String getReceiveAddressDetail() {
		return receiveAddressDetail;
	}
	public void setReceiveAddressDetail(String receiveAddressDetail) {
		this.receiveAddressDetail = receiveAddressDetail;
	}

	public String getReceiveNewZipcode() {
		return receiveNewZipcode;
	}

	public void setReceiveNewZipcode(String receiveNewZipcode) {
		this.receiveNewZipcode = receiveNewZipcode;
	}

	/* 20260108 추가 */
	public String getReceiveNameDec() {
		return receiveNameDec;
	}
	public void setReceiveNameDec(String receiveNameDec) {
		this.receiveNameDec = receiveNameDec;
	}

    public String getMobileDec() {
		return mobileDec;
	}
	public void setMobileDec(String mobileDec) {
		this.mobileDec = mobileDec;
	}

	public String getReceiveAddressDec() {
		return receiveAddressDec;
	}
	public void setReceiveAddressDec(String receiveAddressDec) {
		this.receiveAddressDec = receiveAddressDec;
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

	public String getBirthday() {
		if(birthday == null)birthday = "";
		String birthdayPattern = "^(19|20)\\d{2}([-.]?)(0[1-9]|1[0-2])\\2(0[1-9]|[12]\\d|3[01])$";
		boolean reg = Pattern.matches(birthdayPattern, birthday);
		if (reg) {
			return ShopUtils.localDateToString(ShopUtils.getLocalDate("yyyyMMdd", birthday), "yyyy-MM-dd");
		}
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
}
