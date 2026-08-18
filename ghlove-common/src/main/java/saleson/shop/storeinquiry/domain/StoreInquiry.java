package saleson.shop.storeinquiry.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.security.DataEncryptor;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.configuration.SalesonProperty;

import java.io.File;

public class StoreInquiry  {
	
	private int storeInquiryId;
	private String company;
	private String userName;
	private String phoneNumber;
	private String phoneNumber1;
	private String phoneNumber2;
	private String phoneNumber3;
	private String email;
	private String homepage;
	private String content;
	private String fileName;
	private String status;
	private String creationDate;
	private MultipartFile file;
	
	
	public int getStoreInquiryId() {
		return storeInquiryId;
	}
	public void setStoreInquiryId(int storeInquiryId) {
		this.storeInquiryId = storeInquiryId;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneNumber() {
		
		if (phoneNumber != null) {
			return phoneNumber;
		}
		
		return phoneNumber1+"-"+phoneNumber2+"-"+phoneNumber3;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
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
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
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

	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("store-inquiry")
				.toString();
	}
}
