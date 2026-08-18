package saleson.shop.banner.domain;

import java.io.File;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlinepowers.framework.util.ValidationUtils;

import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;

public class MainBannerManager {
	private Integer bannerId;				// 배너 ID
	private String title;					// 제목
	private String contents;				// 내용
	private String linkUrl;					// 이미지링크
	private Integer displayOrder;			// 배열순서
	private String createdDate;				// 생성일자
	private MultipartFile pcFile;			// PC 파일
	private MultipartFile mFile;			// 모바일 파일
	private String pcFileName;				// PC용 파일명
	private String mFileName;				// 모바일 파일명
	private String pcOrgFileName;			// PC 원본파일명
	private String mOrgFileName;			// 모바일 원본파일명
	private String displayFlag;				// 사용여부
	private List<String> displayOrderList;	// 배열순서 목록
	private String fileType;				// 파일 타입 (M: 모바일, PC: PC)
	private String pcFileSrc;
	private String mFileSrc;

	public Integer getBannerId() {
		return bannerId;
	}
	public void setBannerId(Integer bannerId) {
		this.bannerId = bannerId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	public Integer getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public MultipartFile getPcFile() {
		return pcFile;
	}
	public void setPcFile(MultipartFile pcFile) {
		this.pcFile = pcFile;
	}
	public MultipartFile getmFile() {
		return mFile;
	}
	public void setmFile(MultipartFile mFile) {
		this.mFile = mFile;
	}
	public String getPcFileName() {
		return pcFileName;
	}
	public void setPcFileName(String pcFileName) {
		this.pcFileName = pcFileName;
	}
	public String getmFileName() {
		return mFileName;
	}
	public void setmFileName(String mFileName) {
		this.mFileName = mFileName;
	}
	public String getPcOrgFileName() {
		return pcOrgFileName;
	}
	public void setPcOrgFileName(String pcOrgFileName) {
		this.pcOrgFileName = pcOrgFileName;
	}
	public String getmOrgFileName() {
		return mOrgFileName;
	}
	public void setmOrgFileName(String mOrgFileName) {
		this.mOrgFileName = mOrgFileName;
	}
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	public List<String> getDisplayOrderList() {
		return displayOrderList;
	}
	public void setDisplayOrderList(List<String> displayOrderList) {
		this.displayOrderList = displayOrderList;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getPcFileSrc() {
		return pcFileSrc;
	}
	public void setPcFileSrc() {
		createPcFileSrc();
	}
	public String getMFileSrc() {
		return mFileSrc;
	}
	public void setMFileSrc() {
		createMFileSrc();
	}
	public void createPcFileSrc() {
		if (ValidationUtils.isEmpty(this.pcFileName)) {
			this.pcFileSrc = ShopUtils.getNoImagePath();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getUploadBaseFolder());
		sb.append(File.separator);
		sb.append("main-banner");
		sb.append(File.separator);
		sb.append(this.pcFileName);
		this.pcFileSrc = sb.toString();
	}
	public void createMFileSrc() {
		if (ValidationUtils.isEmpty(this.mFileName)) {
			this.mFileSrc = ShopUtils.getNoImagePath();
		}

		StringBuilder sb = new StringBuilder();
		sb.append(SalesonProperty.getUploadBaseFolder());
		sb.append(File.separator);
		sb.append("main-banner");
		sb.append(File.separator);
		sb.append(this.mFileName);
		this.mFileSrc = sb.toString();
	}
	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("main-banner")
				.toString();
	}

	public String getPcFullFilePath() {
		return this.getUploadPath() + File.separator + this.pcFileName;
	}

	public String getmFullFilePath() {
		return this.getUploadPath() + File.separator + this.mFileName;
	}
}
