package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

@SuppressWarnings("serial")
public class GeneralCustomerCntrSearchParam extends SearchParam {
	private Long userId;	// 회원 ID

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
