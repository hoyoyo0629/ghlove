package saleson.seller.main.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.security.DataEncryptor;

import saleson.common.configuration.SalesonProperty;

import java.io.File;
import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("serial")
public class Seller implements Serializable {

	private static final long serialVersionUID = -5331657776259297299L;

	private long sellerId;
	private String sellerName;
	private String loginId;
	@JsonIgnore
	private String password;
	private String userName;
	private String telephoneNumber;
	private String telephoneNumber1;
	private String telephoneNumber2;
	private String telephoneNumber3;
	private String phoneNumber;
	private String phoneNumber1;
	private String phoneNumber2;
	private String phoneNumber3;
	private String faxNumber;
	private String faxNumber1;
	private String faxNumber2;
	private String faxNumber3;
	private String email;
	private String email1;
	private String email2;


	private String secondUserName;
	private String secondTelephoneNumber;
	private String secondTelephoneNumber1;
	private String secondTelephoneNumber2;
	private String secondTelephoneNumber3;
	private String secondPhoneNumber;
	private String secondPhoneNumber1;
	private String secondPhoneNumber2;
	private String secondPhoneNumber3;
	private String secondEmail;
	private String secondEmail1;
	private String secondEmail2;


	private String post;
	private String post1;
	private String post2;
	private String address;
	private String addressDetail;
	private String companyName;
	private String representativeName;
	private String businessNumber;
	private String businessNumber1;
	private String businessNumber2;
	private String businessNumber3;
	private String businessLocation;
	private String businessType;
	private String businessItems;
	@JsonIgnore
	private float commissionRate;
	@JsonIgnore
	private String remittanceType;
	@JsonIgnore
	private String remittanceDay;
	@JsonIgnore
	private String bankName;
	@JsonIgnore
	private String bankInName;
	@JsonIgnore
	private String bankAccountNumber;
	private String shippingFlag;
	private int shipping;
	private int shippingFreeAmount;
	private int shippingExtraCharge1;
	private int shippingExtraCharge2;
	private String headerContent;

	@JsonIgnore
	private String itemApprovalType;
	private String smsSendTime;
	@JsonIgnore
	private Integer mdId;
	@JsonIgnore
	private String mdName;

	@JsonIgnore
	private String statusCode;
	private String createdDate;
	private long createdUserId;
	private String updatedDate;
	private long updatedUserId;

	@JsonIgnore
	private Integer currentMdId;

	@JsonIgnore
	private int shadowLoginLogId;

	private String mailOrderNumber;
	private String buySafetyUseConfirmNumber;
	private String taxType;
	private String fileNameCertificate1;
	private String fileNameCertificate2;
	private String fileNameCertificate3;
	private String locgovCode;
	private String locgovNm;
	private String adultItemYn;
	private String emailBefore;
	private String emailAfter;
	private String userId;

	private MultipartFile uploadFile1;
	private MultipartFile uploadFile2;
	private MultipartFile uploadFile3;
	private String delelteFile;

	private String mberDn;
	private String mberFinDn;
	private String mberCi;
	private int plusSeq;
	private String receiveSms;

	private boolean manualNumberType;

	private String communityBusinessYn;

	public boolean getManualNumberType() {
		if(this.telephoneNumber != null && this.telephoneNumber != "") {
			String[] telephoneNumberList = this.telephoneNumber.split("-");
			if(telephoneNumberList.length == 3 && telephoneNumberList[0].length() == 4) {
				this.manualNumberType = "050".equals(telephoneNumberList[0].substring(0, 3));
			}else {
				this.manualNumberType = false;
			}
			return this.manualNumberType;
		}
		return false;
	}

	public String getMberFinDn() {
		return mberFinDn;
	}
	public void setMberFinDn(String mberFinDn) {
		this.mberFinDn = mberFinDn;
	}
	public String getMberDn() {
		return mberDn;
	}
	public void setMberDn(String mberDn) {
		this.mberDn = mberDn;
	}
	public int getShadowLoginLogId() {
		return shadowLoginLogId;
	}
	public void setShadowLoginLogId(int shadowLoginLogId) {
		this.shadowLoginLogId = shadowLoginLogId;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getTelephoneNumber1() {
		return telephoneNumber1;
	}
	public void setTelephoneNumber1(String telephoneNumber1) {
		this.telephoneNumber1 = telephoneNumber1;
	}
	public String getTelephoneNumber2() {
		return telephoneNumber2;
	}
	public void setTelephoneNumber2(String telephoneNumber2) {
		this.telephoneNumber2 = telephoneNumber2;
	}
	public String getTelephoneNumber3() {
		return telephoneNumber3;
	}
	public void setTelephoneNumber3(String telephoneNumber3) {
		this.telephoneNumber3 = telephoneNumber3;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPhoneNumber1() {
		return phoneNumber1;
	}
	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}
	public String getPhoneNumber2() {
		return phoneNumber2;
	}
	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}
	public String getPhoneNumber3() {
		return phoneNumber3;
	}
	public void setPhoneNumber3(String phoneNumber3) {
		this.phoneNumber3 = phoneNumber3;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getFaxNumber() {
		return faxNumber;
	}

	public String getFaxNumber1() {
		return faxNumber1;
	}
	public void setFaxNumber1(String faxNumber1) {
		this.faxNumber1 = faxNumber1;
	}
	public String getFaxNumber2() {
		return faxNumber2;
	}
	public void setFaxNumber2(String faxNumber2) {
		this.faxNumber2 = faxNumber2;
	}
	public String getFaxNumber3() {
		return faxNumber3;
	}
	public void setFaxNumber3(String faxNumber3) {
		this.faxNumber3 = faxNumber3;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getEmail() {
		return email;
	}

	public String getEmail1() {
		return email1;
	}
	public void setEmail1(String email1) {
		this.email1 = email1;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPost() {
		return post;
	}

	public String getPost1() {
		return post1;
	}
	public void setPost1(String post1) {
		this.post1 = post1;
	}
	public String getPost2() {
		return post2;
	}
	public void setPost2(String post2) {
		this.post2 = post2;
	}
	public void setPost(String post) {
		this.post = post;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressDetail() {
		return addressDetail;
	}
	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}



	public String getSecondUserName() {
		return secondUserName;
	}
	public void setSecondUserName(String secondUserName) {
		this.secondUserName = secondUserName;
	}
	public String getSecondTelephoneNumber() {
		return secondTelephoneNumber;
	}
	public void setSecondTelephoneNumber(String secondTelephoneNumber) {
		this.secondTelephoneNumber = secondTelephoneNumber;
	}
	public String getSecondTelephoneNumber1() {
		return secondTelephoneNumber1;
	}
	public void setSecondTelephoneNumber1(String secondTelephoneNumber1) {
		this.secondTelephoneNumber1 = secondTelephoneNumber1;
	}
	public String getSecondTelephoneNumber2() {
		return secondTelephoneNumber2;
	}
	public void setSecondTelephoneNumber2(String secondTelephoneNumber2) {
		this.secondTelephoneNumber2 = secondTelephoneNumber2;
	}
	public String getSecondTelephoneNumber3() {
		return secondTelephoneNumber3;
	}
	public void setSecondTelephoneNumber3(String secondTelephoneNumber3) {
		this.secondTelephoneNumber3 = secondTelephoneNumber3;
	}
	public String getSecondPhoneNumber() {
		return secondPhoneNumber;
	}
	public void setSecondPhoneNumber(String secondPhoneNumber) {
		this.secondPhoneNumber = secondPhoneNumber;
	}
	public String getSecondPhoneNumber1() {
		return secondPhoneNumber1;
	}
	public void setSecondPhoneNumber1(String secondPhoneNumber1) {
		this.secondPhoneNumber1 = secondPhoneNumber1;
	}
	public String getSecondPhoneNumber2() {
		return secondPhoneNumber2;
	}
	public void setSecondPhoneNumber2(String secondPhoneNumber2) {
		this.secondPhoneNumber2 = secondPhoneNumber2;
	}
	public String getSecondPhoneNumber3() {
		return secondPhoneNumber3;
	}
	public void setSecondPhoneNumber3(String secondPhoneNumber3) {
		this.secondPhoneNumber3 = secondPhoneNumber3;
	}
	public String getSecondEmail() {
		return secondEmail;
	}
	public void setSecondEmail(String secondEmail) {
		this.secondEmail = secondEmail;
	}
	public String getSecondEmail1() {
		return secondEmail1;
	}
	public void setSecondEmail1(String secondEmail1) {
		this.secondEmail1 = secondEmail1;
	}
	public String getSecondEmail2() {
		return secondEmail2;
	}
	public void setSecondEmail2(String secondEmail2) {
		this.secondEmail2 = secondEmail2;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getRepresentativeName() {
		return representativeName;
	}
	public void setRepresentativeName(String representativeName) {
		this.representativeName = representativeName;
	}
	public String getBusinessNumber() {
		return businessNumber;
	}

	public String getBusinessNumber1() {
		return businessNumber1;
	}
	public void setBusinessNumber1(String businessNumber1) {
		this.businessNumber1 = businessNumber1;
	}
	public String getBusinessNumber2() {
		return businessNumber2;
	}
	public void setBusinessNumber2(String businessNumber2) {
		this.businessNumber2 = businessNumber2;
	}
	public String getBusinessNumber3() {
		return businessNumber3;
	}
	public void setBusinessNumber3(String businessNumber3) {
		this.businessNumber3 = businessNumber3;
	}
	public void setBusinessNumber(String businessNumber) {
		this.businessNumber = businessNumber;
	}
	public String getBusinessLocation() {
		return businessLocation;
	}
	public void setBusinessLocation(String businessLocation) {
		this.businessLocation = businessLocation;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getBusinessItems() {
		return businessItems;
	}
	public void setBusinessItems(String businessItems) {
		this.businessItems = businessItems;
	}

	public float getCommissionRate() {
		return commissionRate;
	}
	public void setCommissionRate(float commissionRate) {
		this.commissionRate = commissionRate;
	}
	public String getRemittanceType() {
		return remittanceType;
	}
	public void setRemittanceType(String remittanceType) {
		this.remittanceType = remittanceType;
	}
	public String getRemittanceDay() {
		return remittanceDay;
	}
	public void setRemittanceDay(String remittanceDay) {
		this.remittanceDay = remittanceDay;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankInName() {
		return bankInName;
	}
	public void setBankInName(String bankInName) {
		this.bankInName = bankInName;
	}
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public String getShippingFlag() {
		return shippingFlag;
	}
	public void setShippingFlag(String shippingFlag) {
		this.shippingFlag = shippingFlag;
	}
	public int getShipping() {
		return shipping;
	}
	public void setShipping(int shipping) {
		this.shipping = shipping;
	}
	public int getShippingFreeAmount() {
		return shippingFreeAmount;
	}
	public void setShippingFreeAmount(int shippingFreeAmount) {
		this.shippingFreeAmount = shippingFreeAmount;
	}
	public int getShippingExtraCharge1() {
		return shippingExtraCharge1;
	}
	public void setShippingExtraCharge1(int shippingExtraCharge1) {
		this.shippingExtraCharge1 = shippingExtraCharge1;
	}
	public int getShippingExtraCharge2() {
		return shippingExtraCharge2;
	}
	public void setShippingExtraCharge2(int shippingExtraCharge2) {
		this.shippingExtraCharge2 = shippingExtraCharge2;
	}
	public String getHeaderContent() {
		return headerContent;
	}
	public void setHeaderContent(String headerContent) {
		this.headerContent = headerContent;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getItemApprovalType() {
		return itemApprovalType;
	}
	public void setItemApprovalType(String itemApprovalType) {
		this.itemApprovalType = itemApprovalType;
	}

	public String getSmsSendTime() {
		return smsSendTime;
	}
	public void setSmsSendTime(String smsSendTime) {
		this.smsSendTime = smsSendTime;
	}
	public Integer getMdId() {
		return mdId;
	}
	public void setMdId(Integer mdId) {
		this.mdId = mdId;
	}
	public String getMdName() {
		return mdName;
	}
	public void setMdName(String mdName) {
		this.mdName = mdName;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public long getUpdatedUserId() {
		return updatedUserId;
	}
	public void setUpdatedUserId(long updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
	public Integer getCurrentMdId() {
		return currentMdId;
	}
	public void setCurrentMdId(Integer currentMdId) {
		this.currentMdId = currentMdId;
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

	public String getMailOrderNumber() {
		return mailOrderNumber;
	}
	public void setMailOrderNumber(String mailOrderNumber) {
		this.mailOrderNumber = mailOrderNumber;
	}
	public String getBuySafetyUseConfirmNumber() {
		return buySafetyUseConfirmNumber;
	}
	public void setBuySafetyUseConfirmNumber(String buySafetyUseConfirmNumber) {
		this.buySafetyUseConfirmNumber = buySafetyUseConfirmNumber;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public String getFileNameCertificate1() {
		return fileNameCertificate1;
	}
	public void setFileNameCertificate1(String fileNameCertificate1) {
		this.fileNameCertificate1 = fileNameCertificate1;
	}
	public String getFileNameCertificate2() {
		return fileNameCertificate2;
	}
	public void setFileNameCertificate2(String fileNameCertificate2) {
		this.fileNameCertificate2 = fileNameCertificate2;
	}
	public String getFileNameCertificate3() {
		return fileNameCertificate3;
	}
	public void setFileNameCertificate3(String fileNameCertificate3) {
		this.fileNameCertificate3 = fileNameCertificate3;
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getLocgovNm() {
		return locgovNm;
	}
	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}
	public String getAdultItemYn() {
		return adultItemYn;
	}
	public void setAdultItemYn(String adultItemYn) {
		this.adultItemYn = adultItemYn;
	}
	public String getEmailBefore() {
		return emailBefore;
	}
	public void setEmailBefore(String emailBefore) {
		this.emailBefore = emailBefore;
	}
	public String getEmailAfter() {
		return emailAfter;
	}
	public void setEmailAfter(String emailAfter) {
		this.emailAfter = emailAfter;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public MultipartFile getUploadFile1() {
		return uploadFile1;
	}
	public void setUploadFile1(MultipartFile uploadFile1) {
		this.uploadFile1 = uploadFile1;
	}
	public MultipartFile getUploadFile2() {
		return uploadFile2;
	}
	public void setUploadFile2(MultipartFile uploadFile2) {
		this.uploadFile2 = uploadFile2;
	}
	public MultipartFile getUploadFile3() {
		return uploadFile3;
	}
	public void setUploadFile3(MultipartFile uploadFile3) {
		this.uploadFile3 = uploadFile3;
	}
	public String getDelelteFile() {
		return delelteFile;
	}
	public void setDelelteFile(String delelteFile) {
		this.delelteFile = delelteFile;
	}
	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("seller")
				.append(File.separator)
				.append(getSellerId())
				.toString();
	}
	public String getMberCi() {
		return mberCi;
	}
	public void setMberCi(String mberCi) {
		this.mberCi = mberCi;
	}
	public int getPlusSeq() {
		return plusSeq;
	}
	public void setPlusSeq(int plusSeq) {
		this.plusSeq = plusSeq;
	}
	public String getReceiveSms() {
		return receiveSms;
	}
	public void setReceiveSms(String receiveSms) {
		this.receiveSms = receiveSms;
	}

	public void parseTelephoneNumber() {
		if(this.telephoneNumber.split("-").length == 2)
			this.telephoneNumber = "미선택-" + this.telephoneNumber;
	}

	public String getCommunityBusinessYn() {
		return communityBusinessYn;
	}

	public void setCommunityBusinessYn(String communityBusinessYn) {
		this.communityBusinessYn = communityBusinessYn;
	}

}
