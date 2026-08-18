package saleson.api.representativebanner.domain;

import lombok.Getter;
import lombok.Setter;
import saleson.shop.representativebanner.domain.RepresentativeBanner;

@Getter
@Setter
public class RepresentativeBannerInfo {
	
	String imageSrcMobile;
	
	String imageSrcPc;
	
	String linkUrl;
	
	String title;
	
	String useYn;
	
	String bannerContent;
	
	
	public RepresentativeBannerInfo(RepresentativeBanner representativeBanner) {
		if(representativeBanner != null) {
			imageSrcMobile = representativeBanner.getImageSrcMobile();
			imageSrcPc = representativeBanner.getImageSrcPc();
			linkUrl = representativeBanner.getLinkUrl();
			title = representativeBanner.getTitle();
			useYn = representativeBanner.getUseYn();
			bannerContent = representativeBanner.getBannerContent();
		}
	}
}
