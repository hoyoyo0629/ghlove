package saleson.shop.wishlist.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import java.util.List;

@SuppressWarnings("serial")
public class WishlistParam extends SearchParam {
	private long userId;
	private List<String> privateTypes;
	
	public WishlistParam() {}
	
	public WishlistParam(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<String> getPrivateTypes() {
		return privateTypes;
	}

	public void setPrivateTypes(List<String> privateTypes) {
		this.privateTypes = privateTypes;
	}
}
