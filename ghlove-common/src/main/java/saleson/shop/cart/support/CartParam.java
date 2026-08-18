package saleson.shop.cart.support;

import com.onlinepowers.framework.web.domain.ListParam;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class CartParam extends ListParam {
	private String sessionId;
	private long userId;
	private int cartId;
	private int quantity;
	private List<Integer> cartIds;
	private List<Integer> cartSetIds;
	private List<Integer> itemOptionIds;
	private int itemId;
	private List<Integer> itemIds;
	private String shippingGroupCode;
	private String shippingPaymentType;
	private String campaignCode;
	private String entryPage;
	private int parentCartId;

	public List<Integer> getItemIds() {
//		return itemIds;
		if (itemIds == null) {
			return null;
		} else {
			List<Integer> list = new ArrayList<>();
			for (Integer integer : itemIds) {
				list.add(integer);
			}
			return list;
		}
	}
	public void setItemIds(List<Integer> itemIds) {
//		this.itemIds = itemIds;
		if (itemIds == null) {
			this.itemIds = null;
		} else {
			this.itemIds = new ArrayList<>();
			for (Integer integer : itemIds) {
				this.itemIds.add(integer);
			}
		}
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public List<Integer> getItemOptionIds() {
//		return itemOptionIds;
		if (itemOptionIds == null) {
			return null;
		} else {
			List<Integer> list = new ArrayList<>();
			for (Integer integer : itemOptionIds) {
				list.add(integer);
			}
			return list;
		}
	}
	public void setItemOptionIds(List<Integer> itemOptionIds) {
//		this.itemOptionIds = itemOptionIds;
		if (itemOptionIds == null) {
			this.itemOptionIds = null;
		} else {
			this.itemOptionIds = new ArrayList<>();
			for (Integer integer : itemOptionIds) {
				this.itemOptionIds.add(integer);
			}
		}
	}
	public List<Integer> getCartIds() {
//		return cartIds;
		if (cartIds == null) {
			return null;
		} else {
			List<Integer> list = new ArrayList<>();
			for (Integer integer : cartIds) {
				list.add(integer);
			}
			return list;
		}
	}
	public void setCartIds(List<Integer> cartIds) {
//		this.cartIds = cartIds;
		if (cartIds == null) {
			this.cartIds = null;
		} else {
			this.cartIds = new ArrayList<>();
			for (Integer integer : cartIds) {
				this.cartIds.add(integer);
			}
		}
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public boolean getIsLogin() {
		
		if (this.userId > 0) {
			return true;
		}
		
		return UserUtils.isUserLogin();
	}
	
	public String getItemType() {
		
		String businessFlag = "N";
		/*if (this.userId > 0) {
			if (UserUtils.isUserLogin()) {
				businessFlag = UserUtils.getUserDetail().getBusinessFlag();
			}
		}*/
		
		if (ObjectUtils.isEmpty(businessFlag)) {
			businessFlag = "N";
		}
		
		return "N".equals(businessFlag) ? "1" : "";
	}
	
	public String getShippingGroupCode() {
		return shippingGroupCode;
	}
	public void setShippingGroupCode(String shippingGroupCode) {
		this.shippingGroupCode = shippingGroupCode;
	}
	public String getShippingPaymentType() {
		return shippingPaymentType;
	}
	public void setShippingPaymentType(String shippingPaymentType) {
		this.shippingPaymentType = shippingPaymentType;
	}
	public String getCampaignCode() {
		return campaignCode;
	}
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getEntryPage() {
		return entryPage;
	}

	public void setEntryPage(String entryPage) {
		this.entryPage = entryPage;
	}

	public List<Integer> getCartSetIds() {
//		return cartSetIds;
		if (cartSetIds == null) {
			return null;
		} else {
			List<Integer> list = new ArrayList<>();
			for (Integer integer : cartSetIds) {
				list.add(integer);
			}
			return list;
		}
	}

	public void setCartSetIds(List<Integer> cartSetIds) {
//		this.cartSetIds = cartSetIds;
		if (cartSetIds == null) {
			this.cartSetIds = null;
		} else {
			this.cartSetIds = new ArrayList<>();
			for (Integer integer : cartSetIds) {
				this.cartSetIds.add(integer);
			}
		}
	}

	public int getParentCartId() {
		return parentCartId;
	}

	public void setParentCartId(int parentCartId) {
		this.parentCartId = parentCartId;
	}
	
	public void addItemIds(int itemId) {
		if (itemIds == null) {
			itemIds = new ArrayList<>();
		}
		itemIds.add(Integer.valueOf(itemId));
	}
}
