package saleson.shop.cart.domain;

import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.model.GiftItem;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemOption;

import java.util.List;

public class Cart {
	private int cartId;
	private String sessionId;
	private long userId;
	private int itemId;
	private String itemName;
	private int quantity;
	private String options;
	private String shippingPaymentType = "1";
	private String shippingGroupCode = "";
	private List<ItemOption> optionList;
	private String additionItemFlag = "N";
	private String setItemFlag = "N";
	private String createdDate;
	private int parentItemId;
	private int parentItemSequence;
	private int parentCartId;
	private String campaignCode;

	// 필수 추가정보
	private String textOption;


	public String getTextOption() {
		return textOption;
	}
	public void setTextOption(String textOption) {
		this.textOption = textOption;
	}

	public int getParentItemId() {
		return parentItemId;
	}
	public void setParentItemId(int parentItemId) {
		this.parentItemId = parentItemId;
	}
	private Item item;

	/* 장바구니 등록용 */
	private String[] arrayRequiredItems;
	private String[] arrayAdditionItems;

	// 세트상품용
	private List<String[]> arrayItemSets;
	private List<CartSet> itemSets;

	public String getAdditionItemFlag() {
		return additionItemFlag;
	}
	public void setAdditionItemFlag(String additionItemFlag) {
		this.additionItemFlag = additionItemFlag;
	}
	public String getShippingPaymentType() {
		return shippingPaymentType;
	}
	public void setShippingPaymentType(String shippingPaymentType) {
		this.shippingPaymentType = shippingPaymentType;
	}
	public String getShippingGroupCode() {
		return shippingGroupCode;
	}
	public void setShippingGroupCode(String shippingGroupCode) {
		this.shippingGroupCode = shippingGroupCode;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public List<ItemOption> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<ItemOption> optionList) {
		this.optionList = optionList;
	}
	
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public String[] getArrayRequiredItems() {
		return CommonUtils.copy(arrayRequiredItems);
	}
	public void setArrayRequiredItems(String[] arrayRequiredItems) {
		this.arrayRequiredItems = CommonUtils.copy(arrayRequiredItems);
	}
	public String[] getArrayAdditionItems() {
		return CommonUtils.copy(arrayAdditionItems);
	}
	public void setArrayAdditionItems(String[] arrayAdditionItems) {
		this.arrayAdditionItems = CommonUtils.copy(arrayAdditionItems);
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
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public boolean getIsLogin() {
		return UserUtils.isUserLogin();
	}

	private List<GiftItem> giftItemList;

	public List<GiftItem> getGiftItemList() {
		return giftItemList;
	}

	public void setGiftItemList(List<GiftItem> giftItemList) {
		this.giftItemList = giftItemList;
	}

	public String getCampaignCode() {
		return campaignCode;
	}
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public List<String[]> getArrayItemSets() {
		return arrayItemSets;
	}

	public void setArrayItemSets(List<String[]> arrayItemSets) {
		this.arrayItemSets = arrayItemSets;
	}

	public int getParentItemSequence() {
		return parentItemSequence;
	}

	public void setParentItemSequence(int parentItemSequence) {
		this.parentItemSequence = parentItemSequence;
	}

	public int getParentCartId() {
		return parentCartId;
	}

	public void setParentCartId(int parentCartId) {
		this.parentCartId = parentCartId;
	}

	public List<CartSet> getItemSets() {
		return itemSets;
	}

	public void setItemSets(List<CartSet> itemSets) {
		this.itemSets = itemSets;
	}

	public String getSetItemFlag() {
		return setItemFlag;
	}

	public void setSetItemFlag(String setItemFlag) {
		this.setItemFlag = setItemFlag;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
