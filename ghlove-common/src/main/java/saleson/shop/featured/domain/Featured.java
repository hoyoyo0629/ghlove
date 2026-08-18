package saleson.shop.featured.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.util.StringUtils;

import saleson.common.configuration.SalesonProperty;
import saleson.common.enumeration.eventcode.EventCodeType;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.EventViewUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.item.domain.Item;
import saleson.shop.seo.domain.Seo;

public class Featured {

	public static final String ITEM_TYPE_ID_KEY = "ITEM_TYPE_ID";
	public static final String ITEM_TYPE_NAME_KEY = "ITEM_TYPE_NAME_KEY";

	private int featuredId;
	private int featuredClass;
	private String featuredType;
	private String featuredUrl;
	private String featuredCode;
	private String[] featuredCodeChecked;
	private String featuredFlag;
	private String displayListFlag;
	private String featuredName;
	private String featuredSimpleContent = "";
	private String featuredContent;
	private String featuredImage;
	private String featuredImageMobile;
	private String thumbnailImage;
	private String thumbnailImageMobile;
	private String link = "";
	private String linkTargetFlag = "N";
	private String linkRelFlag = "N";
	private int ordering;
	private MultipartFile featuredFile;
	private MultipartFile featuredFileMobile;
	private MultipartFile thumbnailFile;
	private MultipartFile thumbnailFileMobile;
	private String createdDate;
	private Seo seo;
	private int fileType;
	private String listType;
	
	// 1: 기본, 2: 제품카테고리 정렬, 3: 사용자그룹 정렬
	private String prodState;
	private String accessAuth;
	private String startDate;
	private String startTime;
	private String endDate;
	private String endTime;
	
	private String progression;

	private String replyUsedFlag = "N"; // 댓글 사용 여부

	private String eventCode;
	
	private String featuredHost;		// 주최/주관
	private String featuredPhoneNo1;		// 대표연락처1
	private String featuredPhoneNo2;		// 대표연락처2
	private String featuredPhoneNo3;		// 대표연락처3
	private String featuredListImage;		// 목록용 이미지
	private String thumbnailListImage;			// 목록용 이미지 썸네일
	private MultipartFile featuredListFile;		// 목록용 이미지 파일
	private MultipartFile thumbnailListFile;	// 목록용 이미지 썸네일 파일
	private String locgovCode;			// 지자체 코드
	private String locgovName;			// 지자체 명

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	List<Item> featuredItems = new ArrayList<>();
	
	public List<Item> getFeaturedItems() {
		return featuredItems;
	}

	public void setFeaturedItems(List<Item> featuredItems) {
		this.featuredItems = featuredItems;
	}

	public Featured() {}
	
	public Featured(int featuredId, int ordering) {
		this.featuredId = featuredId;
		this.ordering = ordering;
	}
	
	public int getFeaturedId() {
		return featuredId;
	}
	public void setFeaturedId(int featuredId) {
		this.featuredId = featuredId;
	}
	public int getFeaturedClass() {
		return featuredClass;
	}
	public void setFeaturedClass(int featuredClass) {
		this.featuredClass = featuredClass;
	}
	public String getFeaturedUrl() {
		return featuredUrl;
	}
	public void setFeaturedUrl(String featuredUrl) {
		this.featuredUrl = featuredUrl;
	}
	public String getFeaturedCode() {
		return featuredCode;
	}
	public void setFeaturedCode(String featuredCode) {
		this.featuredCode = featuredCode;
	}
	public String getFeaturedName() {
		return featuredName;
	}
	public void setFeaturedName(String featuredName) {
		this.featuredName = featuredName;
	}
	public String getFeaturedSimpleContent() {
		return featuredSimpleContent;
	}
	public void setFeaturedSimpleContent(String featuredSimpleContent) {
		this.featuredSimpleContent = featuredSimpleContent;
	}
	public String getFeaturedContent() {
		return featuredContent;
	}
	public void setFeaturedContent(String featuredContent) {
		this.featuredContent = featuredContent;
	}
	public String getFeaturedImage() {
		return featuredImage;
	}
	public void setFeaturedImage(String featuredImage) {
		this.featuredImage = featuredImage;
	}
	public String getFeaturedImageMobile() {
		return featuredImageMobile;
	}

	public void setFeaturedImageMobile(String featuredImageMobile) {
		this.featuredImageMobile = featuredImageMobile;
	}

	public String getThumbnailImage() {
		return thumbnailImage;
	}
	public void setThumbnailImage(String thumbnailImage) {
		this.thumbnailImage = thumbnailImage;
	}
	public MultipartFile getFeaturedFile() {
		return featuredFile;
	}
	public void setFeaturedFile(MultipartFile featuredFile) {
		this.featuredFile = featuredFile;
	}
	
	public MultipartFile getFeaturedFileMobile() {
		return featuredFileMobile;
	}

	public void setFeaturedFileMobile(MultipartFile featuredFileMobile) {
		this.featuredFileMobile = featuredFileMobile;
	}

	public MultipartFile getThumbnailFile() {
		return thumbnailFile;
	}

	public void setThumbnailFile(MultipartFile thumbnailFile) {
		this.thumbnailFile = thumbnailFile;
	}

	public String getFeaturedFlag() {
		return featuredFlag;
	}
	public void setFeaturedFlag(String featuredFlag) {
		this.featuredFlag = featuredFlag;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Seo getSeo() {
		return seo;
	}
	public void setSeo(Seo seo) {
		this.seo = seo;
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	public String[] getFeaturedCodeChecked() {
		return CommonUtils.copy(featuredCodeChecked);
	}
	public void setFeaturedCodeChecked(String[] featuredCodeChecked) {
		this.featuredCodeChecked = CommonUtils.copy(featuredCodeChecked);
	}
	
	
	public String getFeaturedType() {
		return featuredType;
	}

	public void setFeaturedType(String featuredType) {
		this.featuredType = featuredType;
	}

	public String getDisplayListFlag() {
		return displayListFlag;
	}
	public void setDisplayListFlag(String displayListFlag) {
		this.displayListFlag = displayListFlag;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getLinkTargetFlag() {
		return linkTargetFlag;
	}
	
	public void setLinkTargetFlag(String linkTargetFlag) {
		this.linkTargetFlag = linkTargetFlag;
	}
	
	public String getLinkRelFlag() {
		return linkRelFlag;
	}
	
	public void setLinkRelFlag(String linkRelFlag) {
		this.linkRelFlag = linkRelFlag;
	}
	
	public int getOrdering() {
		return ordering;
	}
	
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	
	public String getListType() {
		return listType;
	}
	
	public void setListType(String listType) {
		this.listType = listType;
	}
	
	public String getProdState() {
		return prodState;
	}
	
	public void setProdState(String prodState) {
		this.prodState = prodState;
	}
	
	public String getAccessAuth() {
		return accessAuth;
	}
	
	public void setAccessAuth(String accessAuth) {
		this.accessAuth = accessAuth;
	}
	
	public List getFeaturedCodes(){
		
		String codes[] = StringUtils.delimitedListToStringArray(featuredCode, "|") ;
		List<String> list = new ArrayList<>();
		
		for(int i = 0; i < codes.length; i++){
			list.add(codes[i].trim());
		}
		return list;
	}
	
	//LCH-featured 기획전  <수정>

	public String getPageLink() {
		String pagePrefix = ShopUtils.isMobilePage() ? "/m" : "";
		return ObjectUtils.isEmpty(this.link) ? pagePrefix + "/pages/" + this.featuredUrl : this.link;
 	}
	
	public String getTarget() {
		if (ObjectUtils.isEmpty(this.link)) {
			return "";
		}
		return "Y".equals(this.linkTargetFlag) ? " target=\"_blank\"" : "";
 	}
	
	public String getRel() {
		if (ObjectUtils.isEmpty(this.link)) {
			return "";
		}
		return "Y".equals(this.linkRelFlag) ? " rel=\"nofollow\"" : "";
 	}

	public String getThumbnailImageMobile() {
		return thumbnailImageMobile;
	}

	public void setThumbnailImageMobile(String thumbnailImageMobile) {
		this.thumbnailImageMobile = thumbnailImageMobile;
	}

	public MultipartFile getThumbnailFileMobile() {
		return thumbnailFileMobile;
	}

	public void setThumbnailFileMobile(MultipartFile thumbnailFileMobile) {
		this.thumbnailFileMobile = thumbnailFileMobile;
	}

	public String getProgression() {
		return progression;
	}

	public void setProgression(String progression) {
		this.progression = progression;
	}

	public String getReplyUsedFlag() {
		return replyUsedFlag;
	}

	public void setReplyUsedFlag(String replyUsedFlag) {
		this.replyUsedFlag = replyUsedFlag;
	}


	public String getThumbnailImageSrc() {
		if (ObjectUtils.isEmpty(this.thumbnailImage)) {
			return "";
		}

		return returnImageSrc(this.thumbnailImage);
	}

	public String getThumbnailImageMobileSrc() {
		if (ObjectUtils.isEmpty(this.thumbnailImageMobile)) {
			return "";
		}

		return returnImageSrc(this.thumbnailImageMobile);
	}

	public String getFeaturedImageSrc() {
		if (ObjectUtils.isEmpty(this.featuredImage)) {
			return "";
		}

		return returnImageSrc(this.featuredImage);

	}
	public String getFeaturedImageMobileSrc() {
		if (ObjectUtils.isEmpty(this.featuredImageMobile)) {
			return "";
		}

		return returnImageSrc(this.featuredImageMobile);
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventViewUrl() {

		return EventViewUtils.getUrl(EventCodeType.FEATURED, getEventCode());
	}

	private String returnImageSrc(String imageKinds) {

		String imageSrc = new StringBuilder()
				.append(SalesonProperty.getSalesonUrlCdn())
				.append(SalesonProperty.getUploadBaseFolder())
				.append("/featured/")
				.append(String.valueOf(featuredId))
				.append(File.separator)
				.append(imageKinds)
				.toString();
		return imageSrc;

	}

	@JsonIgnore
	public String getUploadPath() {
		return new StringBuilder()
				.append(SalesonProperty.getUploadSaveFolder())
				.append(File.separator)
				.append("featured")
				.append(File.separator)
				.append(String.valueOf(featuredId))
				.append(File.separator)
				.toString();
	}

	public String getFeaturedHost() {
		return featuredHost;
	}

	public void setFeaturedHost(String featuredHost) {
		this.featuredHost = featuredHost;
	}

	public String getFeaturedPhoneNo1() {
		return featuredPhoneNo1;
	}

	public void setFeaturedPhoneNo1(String featuredPhoneNo1) {
		this.featuredPhoneNo1 = featuredPhoneNo1;
	}

	public String getFeaturedPhoneNo2() {
		return featuredPhoneNo2;
	}

	public void setFeaturedPhoneNo2(String featuredPhoneNo2) {
		this.featuredPhoneNo2 = featuredPhoneNo2;
	}

	public String getFeaturedPhoneNo3() {
		return featuredPhoneNo3;
	}

	public void setFeaturedPhoneNo3(String featuredPhoneNo3) {
		this.featuredPhoneNo3 = featuredPhoneNo3;
	}

	public String getFeaturedImageList() {
		return featuredListImage;
	}


	public String getFeaturedListImage() {
		return featuredListImage;
	}

	public void setFeaturedListImage(String featuredListImage) {
		this.featuredListImage = featuredListImage;
	}

	public String getThumbnailListImage() {
		return thumbnailListImage;
	}

	public void setThumbnailListImage(String thumbnailListImage) {
		this.thumbnailListImage = thumbnailListImage;
	}

	public MultipartFile getFeaturedListFile() {
		return featuredListFile;
	}

	public void setFeaturedListFile(MultipartFile featuredListFile) {
		this.featuredListFile = featuredListFile;
	}

	public MultipartFile getThumbnailListFile() {
		return thumbnailListFile;
	}

	public void setThumbnailListFile(MultipartFile thumbnailListFile) {
		this.thumbnailListFile = thumbnailListFile;
	}

	public String getThumbnailListImageSrc() {
		if (ObjectUtils.isEmpty(this.thumbnailListImage)) {
			return "";
		}

		return returnImageSrc(this.thumbnailListImage);
	}

	public String getFeaturedListImageSrc() {
		if (ObjectUtils.isEmpty(this.featuredListImage)) {
			return "";
		}
		return returnImageSrc(this.featuredListImage);
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
