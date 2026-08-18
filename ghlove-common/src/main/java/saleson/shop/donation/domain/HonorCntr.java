package saleson.shop.donation.domain;

import saleson.common.enumeration.SmsType;
import saleson.common.sms.domain.ReceiverInfo;

public class HonorCntr {
	
	private long userId;
	private String locgovCode;
	private String code;
	private String honorCntrbtrLevelCode;
	
	/* SMS 전송 정보용 */
	private String upperLocgovNm;
	private String locgovNm;
	private String mberCi;
	private String userName;
	private String receiveSms;
	private String phoneNumber;
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getHonorCntrbtrLevelCode() {
		return honorCntrbtrLevelCode;
	}
	public void setHonorCntrbtrLevelCode(String honorCntrbtrLevelCode) {
		this.honorCntrbtrLevelCode = honorCntrbtrLevelCode;
	}
	public String getUpperLocgovNm() {
		return upperLocgovNm;
	}
	public void setUpperLocgovNm(String upperLocgovNm) {
		this.upperLocgovNm = upperLocgovNm;
	}
	public String getLocgovNm() {
		return locgovNm;
	}
	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}
	public String getMberCi() {
		return mberCi;
	}
	public void setMberCi(String mberCi) {
		this.mberCi = mberCi;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getReceiveSms() {
		return receiveSms;
	}
	public void setReceiveSms(String receiveSms) {
		this.receiveSms = receiveSms;
	}
	/* 2023-05-25 추가 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public GiveUserSmsInfo getSmsInfo() {
		GiveUserSmsInfo info = new GiveUserSmsInfo();
		info.setUserId(this.userId);
		info.setUserName(this.userName);
		info.setMberCi(this.mberCi);
		info.setReceiveSms(this.receiveSms);
		info.setLocgovNm(this.upperLocgovNm + " " + this.locgovNm);
		info.setPhoneNumber(this.phoneNumber);
		return info;
	}
	
	public boolean isSame() {
		if (this.code != null && this.honorCntrbtrLevelCode != null) 
			return this.code.equals(this.honorCntrbtrLevelCode);
		else 
			return false;
	}
	
}
