package saleson.shop.brand.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class BrandParam extends SearchParam {
	private String brandName;
	private String displayFlag;
	
	private String brandId;
	private String locgovCode;
	private String upperLocgovCode;
	
	private Long userId;

	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getDisplayFlag() {
		return displayFlag;
	}
	public void setDisplayFlag(String displayFlag) {
		this.displayFlag = displayFlag;
	}
	public String getBrandId() {
		return brandId;
	}
	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getUpperLocgovCode() {
		return upperLocgovCode;
	}
	public void setUpperLocgovCode(String upperLocgovCode) {
		this.upperLocgovCode = upperLocgovCode;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
