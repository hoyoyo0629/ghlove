package saleson.api.event.domain;

import org.springframework.util.ObjectUtils;

import saleson.common.configuration.SalesonProperty;
import saleson.common.utils.ShopUtils;
import saleson.shop.featured.domain.Featured;

public class EventInfo {

    public EventInfo() {}

    public EventInfo(Featured featured) {
        if (featured != null) {
            setFeaturedId(featured.getFeaturedId());
            setFeaturedUrl(featured.getFeaturedUrl());
            setFeaturedClass(featured.getFeaturedClass());
            setLink(featured.getLink());
            setFeaturedImageMobile(featured.getFeaturedImageMobile());
            setThumbnailImage(featured.getThumbnailImage());
            setThumbnailImageMobile(featured.getThumbnailImageMobile());
            setFeaturedName(featured.getFeaturedName());
            setFeaturedImage(featured.getFeaturedImage());
            setFeaturedContent(featured.getFeaturedContent());
            setFeaturedSimpleContent(featured.getFeaturedSimpleContent());
            setProdState(featured.getProdState());
            setStartDate(featured.getStartDate());
            setStartTime(featured.getStartTime());
            setEndDate(featured.getEndDate());
            setEndTime(featured.getEndTime());
            setEventViewUrl(featured.getEventViewUrl());
            setFeaturedPhoneNo1(featured.getFeaturedPhoneNo1());
            setFeaturedPhoneNo2(featured.getFeaturedPhoneNo2());
            setFeaturedPhoneNo3(featured.getFeaturedPhoneNo3());
            setFeaturedHost(featured.getFeaturedHost());
            setFeaturedListImage(featured.getFeaturedListImage());
            setFeaturedListImageSrc(featured.getFeaturedListImageSrc());
        }
    }

    private int featuredId;
    private String featuredUrl;
    private int featuredClass;
    private String link = "";
    private String featuredImageMobile;
    private String thumbnailImage;
    private String thumbnailImageMobile;
    private String featuredImage;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String featuredName;
    private String featuredContent;
    private String prodState;
    private String featuredSimpleContent;
    private String eventViewUrl;
    private String featuredPhoneNo1;
    private String featuredPhoneNo2;
    private String featuredPhoneNo3;
    private String featuredHost;
    private String featuredListImage;
    private String featuredListImageSrc;

    public String getPageLink() {
        String pagePrefix = ShopUtils.isMobilePage() ? "/m" : "";
        return ObjectUtils.isEmpty(this.link) ? pagePrefix + "/pages/" + this.featuredUrl : this.link;
    }

    public String getFeaturedImageMobileSrc() {
        if (ObjectUtils.isEmpty(this.featuredImageMobile)) {
            return "";
        }

        return returnImageSrc(this.featuredImageMobile);
    }

    private String returnImageSrc(String imageKinds) {

        String imageSrc = new StringBuilder()
                .append(SalesonProperty.getSalesonUrlCdn())
                .append(SalesonProperty.getUploadBaseFolder())
                .append("/featured/")
                .append(this.featuredId)
                .append("/featured/")
                .append(imageKinds)
                .toString();

        return imageSrc;

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

    public int getFeaturedId() {
        return featuredId;
    }

    public void setFeaturedId(int featuredId) {
        this.featuredId = featuredId;
    }

    public String getFeaturedUrl() {
        return featuredUrl;
    }

    public void setFeaturedUrl(String featuredUrl) {
        this.featuredUrl = featuredUrl;
    }

    public int getFeaturedClass() {
        return featuredClass;
    }

    public void setFeaturedClass(int featuredClass) {
        this.featuredClass = featuredClass;
    }

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public String getThumbnailImageMobile() {
        return thumbnailImageMobile;
    }

    public void setThumbnailImageMobile(String thumbnailImageMobile) {
        this.thumbnailImageMobile = thumbnailImageMobile;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getFeaturedName() {
        return featuredName;
    }

    public void setFeaturedName(String featuredName) {
        this.featuredName = featuredName;
    }

    public String getFeaturedContent() {
        return featuredContent;
    }

    public void setFeaturedContent(String featuredContent) {
        this.featuredContent = featuredContent;
    }

    public String getProdState() {
        return prodState;
    }

    public void setProdState(String prodState) {
        this.prodState = prodState;
    }

    public String getFeaturedSimpleContent() {
        return featuredSimpleContent;
    }

    public void setFeaturedSimpleContent(String featuredSimpleContent) {
        this.featuredSimpleContent = featuredSimpleContent;
    }

    public String getEventViewUrl() {
        return eventViewUrl;
    }

    public void setEventViewUrl(String eventViewUrl) {
        this.eventViewUrl = eventViewUrl;
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

	public String getFeaturedHost() {
		return featuredHost;
	}

	public void setFeaturedHost(String featuredHost) {
		this.featuredHost = featuredHost;
	}

	public String getFeaturedListImage() {
		return featuredListImage;
	}

	public void setFeaturedListImage(String featuredListImage) {
		this.featuredListImage = featuredListImage;
	}

	public String getFeaturedListImageSrc() {
		return featuredListImageSrc;
	}

	public void setFeaturedListImageSrc(String featuredListImageSrc) {
		this.featuredListImageSrc = featuredListImageSrc;
	}
}
