package saleson.shop.banner.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class MainBannerManagerSearchParam extends SearchParam {
	private Integer bannerId;		// 배너 ID
	private String title;			// 제목
	private String linkUrl;			// 이미지링크
	private Integer displayOrder;	// 배열순서
	private String createdDate;		// 생성일자
	private String pcFileName;		// PC용 파일명
	private String mFileName;		// 모바일 파일명
	private String pcOrgFileName;	// PC 원본파일명
	private String mOrgFileName;	// 모바일 원본파일명
	private String displayFlag;		// 사용여부
	
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
}
