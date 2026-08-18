package saleson.shop.representativebanner.domain;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.onlinepowers.framework.util.FileUtils;

import saleson.common.utils.UserUtils;
import saleson.shop.representativebanner.support.RepresentativeBannerManagerException;

public class RepresentativeBanner {

	private final String DEFAULT_FILE_PATH = FileUtils.getDefaultUploadPath() + File.separator + "representativeBanner";
	
	private final int thumbnailWidthPc = 1280;	// PC 권장사이즈(넓이)
	private final int thumbnailHeightPc = 400;	// PC 권장사이즈(높이)
	
	private final int thumbnailWidthMobile = 768;	// 모바일 권장사이즈(넓이)
	private final int thumbnailHeightMobile = 280;	// 모바일 권장사이즈(높이)	
	
	private final int uploadFileMaxSize = 2;
	private final int uploadFileMinSize = 0;
	
	private int representativeBannerId;
	private String title;
	private String fileNamePc;
	private String fileNameMobile;
	private String linkUrl;
	private String useYn;
	private int displayOrder;
	private String createdDate;
	private MultipartFile uploadFilePc;
	private MultipartFile uploadFileMobile;
	private String deleteFlag;
	private String processType;
	
	private String bannerContent;
	
	private List<RepresentativeBanner> writeDatas;

	public int getThumbnailWidthPc() {
		return this.thumbnailWidthPc;
	}
	
	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	public int getThumbnailHeightPc() {
		return this.thumbnailHeightPc;
	}
	
	public String getDefaultFilePath() {
		return DEFAULT_FILE_PATH + File.separator + this.representativeBannerId;
	}
	
	public String getImageSrcPc() {
		return "/upload/representativeBanner/" + this.representativeBannerId + "/" + this.fileNamePc;
	}
	
	public String getImageSrcMobile() {
		return "/upload/representativeBanner/" + this.representativeBannerId + "/" + this.fileNameMobile;
	}	
	
	public MultipartFile getUploadFilePc() {
		return uploadFilePc;
	}

	public void setUploadFilePc(MultipartFile uploadFilePc) {
		this.uploadFilePc = uploadFilePc;
	}

	public MultipartFile getUploadFileMobile() {
		return uploadFileMobile;
	}

	public void setUploadFileMobile(MultipartFile uploadFileMobile) {
		this.uploadFileMobile = uploadFileMobile;
	}

	public List<RepresentativeBanner> getWriteDatas() {
		return writeDatas;
	}

	public void setWriteDatas(List<RepresentativeBanner> writeDatas) {
		this.writeDatas = writeDatas;
	}

	public int getUploadFileMaxSize() {
		return this.uploadFileMaxSize;
	}
	
	public int getUploadFileMinSize() {
		return this.uploadFileMinSize;
	}
	
	public int getRepresentativeBannerId() {
		return representativeBannerId;
	}
	
	public void setRepresentativeBannerId(int representativeBannerId) {
		this.representativeBannerId = representativeBannerId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getFileNamePc() {
		return fileNamePc;
	}
	
	public void setFileNamePc(String fileNamePc) {
		this.fileNamePc = fileNamePc;
	}
	
	public String getFileNameMobile() {
		return fileNameMobile;
	}
	
	public void setFileNameMobile(String fileNameMobile) {
		this.fileNameMobile = fileNameMobile;
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}
	
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	
	public int getDisplayOrder() {
		return displayOrder;
	}
	
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}	

	public String getCreatedDate() {
		return createdDate;
	}
	
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getThumbnailWidthMobile() {
		return thumbnailWidthMobile;
	}

	public int getThumbnailHeightMobile() {
		return thumbnailHeightMobile;
	}

	public String getBannerContent() {
		return bannerContent;
	}

	public void setBannerContent(String bannerContent) {
		this.bannerContent = bannerContent;
	}
	
	private long getUserId() {
		long userId = UserUtils.getUser().getUserId();
		if (userId > 0) {
			return userId;
		} else {
			throw new RepresentativeBannerManagerException("사용자 정보가 없습니다.");
		}
	}

	public String getProcessType() {
		return processType;
	}

	public void setProcessType(String processType) {
		this.processType = processType;
	}
	
//	private Timestamp getNowDate() {
//		return Timestamp.valueOf(LocalDateTime.now());
//	}
	
}
