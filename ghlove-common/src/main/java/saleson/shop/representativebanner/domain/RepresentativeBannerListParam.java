package saleson.shop.representativebanner.domain;

import com.onlinepowers.framework.web.domain.ListParam;
import saleson.common.utils.CommonUtils;

public class RepresentativeBannerListParam extends ListParam {
	private String[] ordering;

	public String[] getOrdering() {
		return CommonUtils.copy(ordering);
	}

	public void setOrdering(String[] ordering) {
		this.ordering = CommonUtils.copy(ordering);
	}
}
