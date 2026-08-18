package saleson.shop.openmarket.support;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.onlinepowers.framework.util.DateUtils;
import saleson.common.utils.CommonUtils;

public class OpenMarketConfigParam {
	private String dataStatusCode;

	private int[] ids;
	
	private String searchStartDate;
	private String searchEndDate;

	public String getSearchStartDate() {
		
		if (ObjectUtils.isEmpty(searchStartDate)) {
			return DateUtils.getToday();
		}
		
		return searchStartDate;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public String getSearchEndDate() {
		
		if (ObjectUtils.isEmpty(searchEndDate)) {
			return DateUtils.getToday();
		}
		
		return searchEndDate;
	}

	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

	public int[] getIds() {
		return CommonUtils.copy(ids);
	}

	public void setIds(int[] ids) {
		this.ids = CommonUtils.copy(ids);
	}

	public String getDataStatusCode() {
		return dataStatusCode;
	}

	public void setDataStatusCode(String dataStatusCode) {
		this.dataStatusCode = dataStatusCode;
	}
	
	
}
