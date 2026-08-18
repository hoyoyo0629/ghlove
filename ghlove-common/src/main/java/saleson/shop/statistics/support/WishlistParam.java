package saleson.shop.statistics.support;



import org.springframework.security.core.SpringSecurityCoreVersion;

import com.onlinepowers.framework.web.domain.SearchParam;

public class WishlistParam extends SearchParam {

	private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

	private String locgovCode;
	private String upperLocgovCode;
	private String itemName;

	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getUpperLocgovCode() {
		return upperLocgovCode;
	}
	public void setUpperLocgovCode(String upperLocgovCode) {
		this.upperLocgovCode = upperLocgovCode;
	}

}
