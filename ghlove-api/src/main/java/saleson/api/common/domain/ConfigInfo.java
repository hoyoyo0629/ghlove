package saleson.api.common.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.onlinepowers.framework.exception.OpRuntimeException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.api.mypage.domain.ItemReviewInfo;
import saleson.shop.config.domain.Config;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConfigInfo {

    private static Logger log = LoggerFactory.getLogger(ConfigInfo.class);

	public static final int SHOP_CONFIG_ID = 1;
	public static final String SHOP_CATEGORY_GROUP_KEY = "shop";

	private String shopName;
	private String companyName;
	private String companyNumber;
	private String bossName;
	private String categoryType;
	private String businessType;
	private String telNumber;
	private String faxNumber;
	private String counselTelNumber;
	private String adminTelNumber;
	private String adminName;
	private String email;
	private String adminEmail;
	private String post;
	private String address;
	private String addressDetail;
	private String mailOrderNumber;

	public ConfigInfo(Config config){
		if (config != null) {
			try {
				setShopName(config.getShopName());
				setCompanyName(config.getCompanyName());
				setCompanyNumber(config.getCompanyNumber());
				setBossName(config.getBossName());
				setCategoryType(config.getCategoryType());
				setBusinessType(config.getBusinessType());
				setTelNumber(config.getTelNumber());
				setFaxNumber(config.getFaxNumber());
				setCounselTelNumber(config.getCounselTelNumber());
				setAdminTelNumber(config.getAdminTelNumber());
				setAdminName(config.getAdminName());
				setEmail(config.getEmail());
				setAdminEmail(config.getAdminEmail());
				setPost(config.getPost());
				setAddress(config.getAddress());
				setAddressDetail(config.getAddressDetail());
				setMailOrderNumber(config.getMailOrderNumber());

			} catch(OpRuntimeException ignore) {
				log.error(getClass().getName() + " constructor error", ignore);
			}
		} // if E
	}// Constructor E

}