package saleson.shop.qnaadmin.support;

import com.onlinepowers.framework.web.domain.ListParam;

public class QnaAdminListParam extends ListParam {
	
	private long sellerId;
	private String locgovCode;
	
	public QnaAdminListParam() {}

	public long getSellerId() {
		return sellerId;
	}

	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	
}
