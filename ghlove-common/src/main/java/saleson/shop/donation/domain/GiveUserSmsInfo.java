package saleson.shop.donation.domain;

import saleson.common.enumeration.SmsType;
import saleson.common.sms.domain.ReceiverInfo;

public class GiveUserSmsInfo {
	private long userId;
	private String mberCi;
	private String userName;
	private String locgovNm;
	private String cntrAmt;
	private String receiveSms;
	private String phoneNumber;
	 
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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
	public String getLocgovNm() {
		return locgovNm;
	}
	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}
	public String getCntrAmt() {
		return cntrAmt;
	}
	public void setCntrAmt(String cntrAmt) {
		this.cntrAmt = cntrAmt;
	}
	/* 2023-05-25 추가 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getReceiveSms() {
		return receiveSms;
	}
	public void setReceiveSms(String receiveSms) {
		this.receiveSms = receiveSms;
	}
	public String getCntrAmtByComma() {
		if (this.cntrAmt == null || "".equals(this.cntrAmt)) {
			return null;
		}
		
		return this.cntrAmt.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
	}
	
	public ReceiverInfo getReceiverInfo(SmsType smsType) {
		
		ReceiverInfo receiverInfo = null;
		
		
		if (!"".equals(this.mberCi) && this.mberCi != null) {
			receiverInfo = new ReceiverInfo();
			receiverInfo.setPrvcIdntfcInfo(this.mberCi);
			receiverInfo.setSmsType(smsType);
			
			if (SmsType.DONATION.equals(smsType) || SmsType.OVERPAYMENT.equals(smsType) || SmsType.OVERPAYMENT_CANCEL.equals(smsType)) {
				receiverInfo.setSndngCntnts(this.userName + "|" + this.locgovNm + "|" + this.getCntrAmtByComma() + "|" + this.phoneNumber);			
			} else if (SmsType.QNA.equals(smsType) || SmsType.JOIN_MEMBERSHIP.equals(smsType) || SmsType.SECESSION.equals(smsType) || SmsType.PASSWORD_CHANGE_COMPLETE.equals(smsType) || SmsType.MANAGER_LOGIN.equals(smsType)) {
				receiverInfo.setSndngCntnts(this.userName+ "|" + this.phoneNumber);
			} else if (SmsType.HONOR_DONATION.equals(smsType)) {
				receiverInfo.setSndngCntnts(this.userName + "|" + this.locgovNm+ "|" + this.phoneNumber);						
			}			
		}
		
		
		return receiverInfo;
		
	}
}
