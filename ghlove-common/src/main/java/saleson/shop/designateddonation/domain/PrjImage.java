package saleson.shop.designateddonation.domain;

import java.sql.Timestamp;

import com.onlinepowers.framework.util.StringUtils;

import saleson.common.utils.ShopUtils;

public class PrjImage {
	private long dsgncntrPrjImageId;
	private long prjId;
	private String imageName;
	private int ordering;
	private String createdDate;
	private String prjUserCode;
	// 최초등록자 ID
	private long frstRegisterId;
	// 최초 등록 시점
	private Timestamp frstRegistPnttm;
	public PrjImage() {}
	
	public PrjImage(int dsgncntrPrjImageId, int ordering) {
		this.dsgncntrPrjImageId = dsgncntrPrjImageId;
		this.ordering = ordering;
	}
	
	
	public long getFrstRegisterId() {
		return frstRegisterId;
	}

	public void setFrstRegisterId(long frstRegisterId) {
		this.frstRegisterId = frstRegisterId;
	}

	public Timestamp getFrstRegistPnttm() {
		return frstRegistPnttm;
	}

	public void setFrstRegistPnttm(Timestamp frstRegistPnttm) {
		this.frstRegistPnttm = frstRegistPnttm;
	}

	public long getDsgncntrPrjImageId() {
		return dsgncntrPrjImageId;
	}
	public void setDsgncntrPrjImageId(long dsgncntrPrjImageId) {
		this.dsgncntrPrjImageId = dsgncntrPrjImageId;
	}
	public long getPrjId() {
		return prjId;
	}
	public void setPrjId(long prjId) {
		this.prjId = prjId;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public int getOrdering() {
		return ordering;
	}
	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	
	/**
	 * 상세 이미지 경로 
	 * @return
	 */
	public String getImageSrc() {
		return ShopUtils.detailsImage(this.prjUserCode, this.imageName);
	}
	
	
	/**
	 * 상세 섬네일 이미지 경로 
	 * @return
	 */
	public String getThumbnailImageSrc() {
		return ShopUtils.detailsThumbnail(this.prjUserCode, this.imageName);
	}
	
	
	/**
	 * 상세 확대 이미지 경로 
	 * @return
	 */
	public String getBigImageSrc() {
		return ShopUtils.detailsBigImage(this.prjUserCode, this.imageName);
	}

	public String getPrjUserCode() {
		return prjUserCode;
	}

	public void setPrjUserCode(String prjUserCode) {
		this.prjUserCode = prjUserCode;
	}
	
	public String getImageNameL() {
		String imageNameL = imageName;
		if (StringUtils.hasLength(imageNameL)) {
			int index = imageNameL.lastIndexOf("_");
			int pointIndex = imageNameL.lastIndexOf(".");
			String ext = "";
			if (pointIndex >= 0) {
				ext = imageNameL.substring(pointIndex);
			}
			if (index >= 0) {
				imageNameL = imageNameL.substring(0, index) + "_L" + ext;
			}
		} else {
			imageNameL = "";
		}
		return imageNameL;
	}
}
