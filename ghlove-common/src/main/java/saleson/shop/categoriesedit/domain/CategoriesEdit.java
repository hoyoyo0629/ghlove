package saleson.shop.categoriesedit.domain;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;
import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;


public class CategoriesEdit {
	
	private int categoryEditId;
	private String code;
	private String editKind;
	private String editPosition;
	private String editContent;
	private String editImage;
	private String editUrl;
	private String createdDate;
	private String updatedDate;
	
	private List<MultipartFile> editImages;
	private List<String> editUrls;
	private List<String> fileNames;
	private List<String> editContents;
	
	public int getCategoryEditId() {
		return categoryEditId;
	}
	public void setCategoryEditId(int categoryEditId) {
		this.categoryEditId = categoryEditId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getEditKind() {
		return editKind;
	}
	public void setEditKind(String editKind) {
		this.editKind = editKind;
	}
	public String getEditPosition() {
		return editPosition;
	}
	public void setEditPosition(String editPosition) {
		this.editPosition = editPosition;
	}
	public String getEditContent() {
		return editContent;
	}
	public void setEditContent(String editContent) {
		this.editContent = editContent;
	}
	public String getEditImage() {
		return editImage;
	}
	public void setEditImage(String editImage) {
		this.editImage = editImage;
	}
	public String getEditUrl() {
		return editUrl;
	}
	public void setEditUrl(String editUrl) {
		this.editUrl = editUrl;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public List<String> getEditUrls() {
		return editUrls;
	}
	public void setEditUrls(List<String> editUrls) {
		this.editUrls = editUrls;
	}
	public List<String> getFileNames() {
		return fileNames;
	}
	public void setFileNames(List<String> fileNames) {
		this.fileNames = fileNames;
	}
	public List<MultipartFile> getEditImages() {
		return editImages;
	}
	public void setEditImages(List<MultipartFile> editImages) {
		this.editImages = editImages;
	}
	public List<String> getEditContents() {
		return editContents;
	}
	public void setEditContents(List<String> editContents) {
		this.editContents = editContents;
	}


	private String getUploadPath(String parent, String type) {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append(parent)
				.append(File.separator)
				.append(getCode())
				.append(File.separator)
				.append(type)
				.toString();
	}

	@JsonIgnore
	public String getUploadPath() {
		return getUploadPath("category-edit","default");
	}


	@JsonIgnore
	public String getThumbUploadPath() {
		return getUploadPath("category-edit", "thumb");
	}

	@JsonIgnore
	public String getLoginBannerPath(String bannerType) {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("loginbanner")
				.append(File.separator)
				.append(getCategoryEditId())
				.append(File.separator)
				.append(bannerType)
				.toString();
	}

	@JsonIgnore
	public String getMobileUploadPath() {
		return getUploadPath("mobile-category-edit", "default");
	}
}

