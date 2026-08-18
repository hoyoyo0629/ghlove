package saleson.shop.donation.support;

import com.onlinepowers.framework.util.StringUtils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ForeignStatusParam {
	
	private long userId;

	private String foreignStatusCode; /* 내/외국인 구분 코드 */
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getForeignStatusCode() {
		if (StringUtils.hasLength(foreignStatusCode)) {
			return foreignStatusCode;
		} else {
			return "0";
		}
	}
	public void setForeignStatusCode(String foreignStatusCode) {
		this.foreignStatusCode = foreignStatusCode;
	}
	
}

