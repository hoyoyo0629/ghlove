package saleson.api.cart.domain;

import saleson.common.utils.CommonUtils;
import saleson.common.utils.UserUtils;
import saleson.shop.cart.domain.CartSet;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemOption;

import java.util.List;

public class CartInfo {

    private int cartId;
    private String sessionId;
    private long userId;
    private int itemId;
    private int quantity;
    private String options;
    private String shippingPaymentType = "1";
    private String shippingGroupCode = "";
    private List<ItemOption> optionList;
    private String additionItemFlag = "N";
    private String createdDate;
    private int parentItemId;
    private List<Integer> cartIds;
    // 필수 추가정보
    private String textOption;

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

    private String utmSourceL1;
    private String utmSourceL2;
    private String utmMedium;
    private String utmCampaign;

    private String campaignCode;

    // 비회원 약관동의 > 로그인 접근시 true
    private boolean isNoMemberLogin;

    public String getTextOption() {
		return textOption;
	}

	public void setTextOption(String textOption) {
		this.textOption = textOption;
	}

	public String getUtmSourceL1() {
        return utmSourceL1;
    }

    public void setUtmSourceL1(String utmSourceL1) {
        this.utmSourceL1 = utmSourceL1;
    }

    public String getUtmSourceL2() {
        return utmSourceL2;
    }

    public void setUtmSourceL2(String utmSourceL2) {
        this.utmSourceL2 = utmSourceL2;
    }

    public String getUtmMedium() {
        return utmMedium;
    }

    public void setUtmMedium(String utmMedium) {
        this.utmMedium = utmMedium;
    }

    public String getUtmCampaign() {
        return utmCampaign;
    }

    public void setUtmCampaign(String utmCampaign) {
        this.utmCampaign = utmCampaign;
    }

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

    public List<Integer> getCartIds() {
        return cartIds;
    }

    public void setCartIds(List<Integer> cartIds) {
        this.cartIds = cartIds;
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

    public List<CartSet> getItemSets() {
        return itemSets;
    }

    public void setItemSets(List<CartSet> itemSets) {
        this.itemSets = itemSets;
    }

    public boolean isNoMemberLogin() {
        return isNoMemberLogin;
    }

    public void setNoMemberLogin(boolean noMemberLogin) {
        isNoMemberLogin = noMemberLogin;
    }
}
