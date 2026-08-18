package saleson.seller.main.support;

import com.onlinepowers.framework.web.domain.SearchParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("serial")
public class SellerParam extends SearchParam {
	private String statusCode;
	private long sellerId;
	private long defaultOpmanagerSellerId;
	private String startDate;
	private String endDate;
	private String adminRole;
	private String itemApprovalType;
	private String locgov;
	private String shWdr;
	private String shLocgovCode;
	private String mberDn;

	public SellerParam(String conditionType) {
		setConditionType(conditionType);
	}

}
