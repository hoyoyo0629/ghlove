package saleson.shop.donation.support;

import com.onlinepowers.framework.web.domain.ListParam;

public class NtsParam extends ListParam{
	
	private String jijacheCd;		/* 지자체코드 */
	private String userId;			/* 사용자id */
	private String taxAmt;			/* 과세금액 */
	
	public String getJijacheCd() {
		return jijacheCd;
	}
	public void setJijacheCd(String jijacheCd) {
		this.jijacheCd = jijacheCd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(String taxAmt) {
		this.taxAmt = taxAmt;
	}
	
}

