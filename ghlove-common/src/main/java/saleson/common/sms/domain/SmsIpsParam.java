package saleson.common.sms.domain;

import java.util.ArrayList;
import java.util.List;

import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.enumeration.SmsType;

public class SmsIpsParam extends SearchParam {
	
	private static final long serialVersionUID = 6946992353902536444L;

	private String searchStartDate;
	
	private String searchEndDate;
	
	private List<String> svcIds;
	
	private String smsTypeStr;

	public String getSearchStartDate() {
		return searchStartDate;
	}

	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}

	public String getSearchEndDate() {
		return searchEndDate;
	}

	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}

//	public List<String> getSvcIds() {
//		if (svcIds == null) {
//			return null;
//		} else {
//			List<String> list = new ArrayList<>();
//			for (String string : svcIds) {
//				list.add(string);
//			}
//			return list;
//		}
//	}
//
//	public void setSvcIds(String searchTxt) {
//		svcIds = new ArrayList<>();
//		if (StringUtils.hasLength(searchTxt)) {
//			SmsType[] smsTypes = SmsType.values();
//			for (SmsType smsType : smsTypes) {
//				if (smsType.getDescription().contains(searchTxt)) {
//					svcIds.add(smsType.getCode());
//				}
//			}
//		}
//		if (svcIds.isEmpty()) {
//			svcIds.add("");
//		}
//	}

	public void setSvcIdsAll() {
		svcIds = new ArrayList<>();
		SmsType[] smsTypes = SmsType.values();
		for (SmsType smsType : smsTypes) {
			svcIds.add(smsType.getCode());
		}
	}

	public String getSmsTypeStr() {
		return smsTypeStr;
	}

	public void setSmsTypeStr(String smsTypeStr) {
		SmsType[] smsTypes = SmsType.values();
		SmsType inputSmsType = null;
		for (SmsType smsType : smsTypes) {
			if (smsType.getCode().equals(smsTypeStr)) {
				inputSmsType = smsType;
			}
		}
		if (inputSmsType == null) {
			this.smsTypeStr = "";
		} else {
			this.smsTypeStr = inputSmsType.getCode();
		}
	}
	
}
