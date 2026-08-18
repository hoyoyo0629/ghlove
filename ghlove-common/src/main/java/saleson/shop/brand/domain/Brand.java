package saleson.shop.brand.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.configuration.SalesonProperty;

import java.io.File;

public class Brand {
	private int brandId;
	private String brandName;
	private String brandImage;
	private String brandContent;
	private String displayFlag;
	private int updatedUserId;
	private String updatedDate;
	private int createdUserId;
	private String createdDate;
	
	private String brandImageDeleteFlag;
	private MultipartFile file;
	
	private String locgovCode;
	private String locgovName;
	
	public int getBrandId() {
		return brandId;
	}
	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandImage() {
		return brandImage;
	}
	public void setBrandImage(String brandImage) {
		this.brandImage = brandImage;
	}
	public String getBrandContent() {
		return brandContent;
	}
	public void setBrandContent(String brandContent) {
		this.brandContent = brandContent;
	}
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	public int getUpdatedUserId() {
		return updatedUserId;
	}
	public void setUpdatedUserId(int updatedUserId) {
		this.updatedUserId = updatedUserId;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(int createdUserId) {
		this.createdUserId = createdUserId;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getBrandImageDeleteFlag() {
		return brandImageDeleteFlag;
	}
	public void setBrandImageDeleteFlag(String brandImageDeleteFlag) {
		this.brandImageDeleteFlag = brandImageDeleteFlag;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	public String getBrandImageSrc() {
		if (ObjectUtils.isEmpty(this.brandImage)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();

		sb.append(SalesonProperty.getSalesonUrlCdn());
		sb.append(SalesonProperty.getUploadBaseFolder());
		sb.append("/brand/");
		sb.append(this.brandId);
		sb.append("/");
		sb.append(this.brandImage);

		return sb.toString();
	}

	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("brand")
				.append(File.separator)
				.append(getBrandId())
				.toString();
	}
	
	public String getLocgovCode() {
		return locgovCode;
	}
	
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getLocgovName() {
		return locgovName;
	}
	public void setLocgovName(String locgovName) {
		this.locgovName = locgovName;
	}
	
	
}
