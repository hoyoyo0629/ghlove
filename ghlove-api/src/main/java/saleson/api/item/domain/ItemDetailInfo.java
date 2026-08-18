package saleson.api.item.domain;

import saleson.common.enumeration.eventcode.EventCodeType;
import saleson.common.utils.*;
import saleson.shop.giftitem.domain.GiftItemInfo;
import saleson.shop.item.domain.ItemInfo;
import saleson.shop.item.domain.*;

import java.util.ArrayList;
import java.util.List;

public class ItemDetailInfo extends ItemBase {

	public ItemDetailInfo() {}

	public ItemDetailInfo(Item item) {
		if (item != null) {

			setItemId(item.getItemId());
			setItemUserCode(item.getItemUserCode());
			setItemName(item.getItemName());
			setItemSummary(item.getItemSummary());
			setItemPrice(item.getItemPrice());
			setSalePrice(item.getSalePrice());

			setBrand(item.getBrand());
			setBrandId(item.getBrandId());

			setSellerDiscountFlag(item.getSellerDiscountFlag());
			setSellerDiscountType(item.getSellerDiscountType());
			setSellerDiscountAmount(item.getSellerDiscountAmount());

			setSize(item.getSize());

			// 세트상품
			if ("3".equals(item.getItemType())) {
				setSetDiscountType(item.getSetDiscountType());
				setSetDiscountAmount(item.getSetDiscountAmount());
				setItemSets(item.getItemSets());
			}

			setItemImage(item.getItemImage());
			setOrderMinQuantity(item.getOrderMinQuantity());
			setOrderMaxQuantity(item.getOrderMaxQuantity());
			setItemImages(item.getItemImages());
			setItemNoticeCode(item.getItemNoticeCode());
			setDeliveryCompanyName(item.getDeliveryCompanyName());
			setDeliveryType(item.getDeliveryType());
			setShipmentReturnId(item.getShipmentReturnId());
			setShippingGroupCode(item.getShippingGroupCode());
			setShippingType(item.getShippingType());
			setShipping(item.getShipping());
			setShippingItemCount(item.getShippingItemCount());
			setShippingExtraCharge1(item.getShippingExtraCharge1());
			setShippingExtraCharge2(item.getShippingExtraCharge2());
			setShippingReturn(item.getShippingReturn());
			setShippingFreeAmount(item.getShippingFreeAmount());
			setItemReturnFlag(item.getItemReturnFlag());
			setHits(item.getHits());
			setItemInfos(item.getItemInfos());
			setItemRelations(item.getItemRelations());
			setItemOptions(item.getItemOptions());

			setItemOptionFlag(item.getItemOptionFlag());
			setItemOptionType(item.getItemOptionType());
			setItemOptionTitle1(item.getItemOptionTitle1());
			setItemOptionTitle2(item.getItemOptionTitle2());
			setItemOptionTitle3(item.getItemOptionTitle3());

			// 필수 추가정보
			setItemTextOptionFlag(item.getItemTextOptionFlag());
			setItemTextOptionTitle1(item.getItemTextOptionTitle1());
			setItemTextOptionTitle2(item.getItemTextOptionTitle2());
			setItemTextOptionTitle3(item.getItemTextOptionTitle3());

			// pageURL 변경으로 인한 빈값 처리
			setDetailContent(UserUtils.removeIframe(item.getDetailContent()));
//			setDetailContent(item.getDetailContent());

			if (item.getSeller() != null) {
				setSellerName(item.getSeller().getSellerName());
			}

			setSpotDiscountAmount(item.getSpotDiscountAmount());
			setSpotStartDate(item.getSpotStartDate());
			setSpotEndDate(item.getSpotEndDate());
			setSpotStartTime(item.getSpotStartTime());
			setSpotEndTime(item.getSpotEndTime());
			setSpotWeekDay(item.getSpotWeekDay());
			setSpotDateType(item.getSpotDateType());
			setSpotApplyGroup(item.getSpotApplyGroup());
			setSpotFlag(item.getSpotFlag());

			setItemType(item.getItemType());	// 1: 일반상품, 3: 세트상품
			setItemLabel(item.getItemLabel());
			setItemType1(item.getItemType1());
			setItemType2(item.getItemType2());
			setItemType3(item.getItemType3());
			setItemType4(item.getItemType4());
			setItemType5(item.getItemType5());

			setItemSoldOutFlag(item.getItemSoldOutFlag());

			setItemNewFlag(item.getItemNewFlag());
			setDisplayFlag(item.getDisplayFlag());

			// 품절 관련 데이터 추가
			setStockFlag(item.getStockFlag());  // 재고 연동 유무 (Y : 연동 / N : 무제한)
			setStockQuantity(item.getStockQuantity()); //재고 수량

			// 사은품
			setFreeGiftFlag(item.getFreeGiftFlag());
			setFreeGiftName(item.getFreeGiftName());
			setFreeGiftItemList(ShopUtils.conventGiftItemInfoList(item.getFreeGiftItemList()));

			// 2020.01.20 sje detailInfo 생성중 회원할인률이 달라지는 현상 수정
			setUserLevelDiscountRate(item.getUserLevelDiscountRate());

			if (UserUtils.isUserLogin()) {
				setLevelName(UserUtils.getUserDetail().getLevelName());
			}

			setReviewScore(item.getReviewScore());
			setReviewCount(item.getReviewCount());

			setNaverPayFlag(item.isDisplayNaverPayFlag());
			setWishlistFlag(item.isWishlistFlag());

			setLocgovCode(item.getLocgovCode());
			setLocgovNm(item.getLocgovNm());

			setOriginContry(item.getOriginCountry());

			setMobileItemYn(item.getMobileItemYn());
		}
	}

	private String sellerName;
	private String itemLabel = "0";

	private String itemType1;
	private String itemType2;
	private String itemType3;
	private String itemType4;
	private String itemType5;
	private String itemNewFlag = "N";	// 신상품여부
	private String brand;
	private int brandId;

	private String stockFlag = "N";
	private int stockQuantity = -1;
	private int orderMinQuantity = -1;
	private int orderMaxQuantity = -1;
	private String itemOptionTitle1 = "";
	private String itemOptionTitle2 = "";
	private String itemOptionTitle3 = "";
	private String itemOptionType = "S";

	// 필수 추가정보
	private String itemTextOptionFlag;
	private String itemTextOptionTitle1;
	private String itemTextOptionTitle2;
	private String itemTextOptionTitle3;

	private String detailContent = "";

	private String itemNoticeCode;

	private String originContry;

	// 택배사 정보
	private String deliveryCompanyName;
	private String deliveryType;	// 배송구분 (1: 본사배송, 2: 업체배송)

	// 배송지 / 배송비
	private int shipmentReturnId;
	private String shippingType;
	private String shippingGroupCode;
	private int shipping;
	private int shippingFreeAmount;
	private int shippingItemCount = 1;		// 개당 배송비 부과 시 기준 상품 수.
	private int shippingExtraCharge1;
	private int shippingExtraCharge2;
	private int shippingReturn;			// 반품/교환 배송비 (편도 기준금액)
	private int hits;

	private List<ItemInfo> itemInfos = new ArrayList<>();
	private List<ItemImage> itemImages = new ArrayList<>();
	private List<ItemOption> itemOptions = new ArrayList<>();
	private List<ItemRelation> itemRelations = new ArrayList<>();

	// 관련상품
	private int[] relatedItemIds;

	// 상품옵션
	private String[] optionType;
	private String[] optionName1;		// 옵션 숨김 여부 (부모)
	private String[] optionName2;		// 옵션 세부항목 노출여
	private String[] optionName3;
	private String[] optionPrice;
	private String[] optionStockQuantity;

	// 반품신청 가능여부
	private String itemReturnFlag;

	// 사은품
	private String freeGiftFlag = "N";
	private String freeGiftName = "";
	private List<GiftItemInfo> freeGiftItemList = new ArrayList<>();

	// 세트상품
	private List<ItemSet> itemSets = new ArrayList<>();

	private String levelName;

	private String reviewCount;
	private String reviewScore;

	private boolean naverPayFlag;

	private boolean wishlistFlag;

	public String getItemReturnFlag() {
		return itemReturnFlag;
	}

	public void setItemReturnFlag(String itemReturnFlag) {
		this.itemReturnFlag = itemReturnFlag;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getItemLabel() {
		return itemLabel;
	}
	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public String getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public String getItemType1() {
		return itemType1;
	}
	public void setItemType1(String itemType1) {
		this.itemType1 = itemType1;
	}
	public String getItemType2() {
		return itemType2;
	}
	public void setItemType2(String itemType2) {
		this.itemType2 = itemType2;
	}
	public String getItemType3() {
		return itemType3;
	}
	public void setItemType3(String itemType3) {
		this.itemType3 = itemType3;
	}
	public String getItemType4() {
		return itemType4;
	}
	public void setItemType4(String itemType4) {
		this.itemType4 = itemType4;
	}
	public String getItemType5() {
		return itemType5;
	}
	public void setItemType5(String itemType5) {
		this.itemType5 = itemType5;
	}

	public String getStockFlag() {
		return stockFlag;
	}

	public void setStockFlag(String stockFlag) {
		this.stockFlag = stockFlag;
	}
	public int getStockQuantity() {
		return stockQuantity;
	}
	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public int getOrderMinQuantity() {
		return orderMinQuantity;
	}

	public void setOrderMinQuantity(int orderMinQuantity) {
		this.orderMinQuantity = orderMinQuantity;
	}

	public int getOrderMaxQuantity() {
		return orderMaxQuantity;
	}

	public void setOrderMaxQuantity(int orderMaxQuantity) {
		this.orderMaxQuantity = orderMaxQuantity;
	}

	public String getItemOptionType() {
		return itemOptionType;
	}

	public void setItemOptionType(String itemOptionType) {
		this.itemOptionType = itemOptionType;
	}

	public String getDetailContent() {
		return detailContent;
	}
	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
	}

	public String getItemNoticeCode() {
		return itemNoticeCode;
	}
	public void setItemNoticeCode(String itemNoticeCode) {
		this.itemNoticeCode = itemNoticeCode;
	}

	public String getDeliveryCompanyName() {
		return deliveryCompanyName;
	}

	public void setDeliveryCompanyName(String deliveryCompanyName) {
		this.deliveryCompanyName = deliveryCompanyName;
	}

	public int getShipmentReturnId() {
		return shipmentReturnId;
	}

	public void setShipmentReturnId(int shipmentReturnId) {
		this.shipmentReturnId = shipmentReturnId;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public String getShippingGroupCode() {
		return shippingGroupCode;
	}

	public void setShippingGroupCode(String shippingGroupCode) {
		this.shippingGroupCode = shippingGroupCode;
	}

	public int getShipping() {
		return shipping;
	}

	public void setShipping(int shipping) {
		this.shipping = shipping;
	}

	public int getShippingFreeAmount() {
		return shippingFreeAmount;
	}

	public void setShippingFreeAmount(int shippingFreeAmount) {
		this.shippingFreeAmount = shippingFreeAmount;
	}

	public int getShippingItemCount() {
		return shippingItemCount;
	}

	public void setShippingItemCount(int shippingItemCount) {
		this.shippingItemCount = shippingItemCount;
	}

	public int getShippingExtraCharge1() {
		return shippingExtraCharge1;
	}

	public void setShippingExtraCharge1(int shippingExtraCharge1) {
		this.shippingExtraCharge1 = shippingExtraCharge1;
	}

	public int getShippingExtraCharge2() {
		return shippingExtraCharge2;
	}

	public void setShippingExtraCharge2(int shippingExtraCharge2) {
		this.shippingExtraCharge2 = shippingExtraCharge2;
	}

	public int getShippingReturn() {
		return shippingReturn;
	}

	public void setShippingReturn(int shippingReturn) {
		this.shippingReturn = shippingReturn;
	}

	public int getHits() {
		return hits;
	}
	public void setHits(int hits) {
		this.hits = hits;
	}

	public List<ItemOption> getItemOptions() {
		return itemOptions;
	}

	public void setItemOptions(List<ItemOption> itemOptions) {
		this.itemOptions = itemOptions;
	}

	public int[] getRelatedItemIds() {
		return CommonUtils.copy(relatedItemIds);
	}
	public void setRelatedItemIds(int[] relatedItemIds) {
		this.relatedItemIds = CommonUtils.copy(relatedItemIds);
	}
	public String[] getOptionName1() {
		return CommonUtils.copy(optionName1);
	}
	public void setOptionName1(String[] optionName1) {
		this.optionName1 = CommonUtils.copy(optionName1);
	}
	public String[] getOptionName2() {
		return CommonUtils.copy(optionName2);
	}
	public void setOptionName2(String[] optionName2) {
		this.optionName2 = CommonUtils.copy(optionName2);
	}

	public String[] getOptionType() {
		return CommonUtils.copy(optionType);
	}

	public void setOptionType(String[] optionType) {
		this.optionType = CommonUtils.copy(optionType);
	}

	public String[] getOptionName3() {
		return CommonUtils.copy(optionName3);
	}

	public void setOptionName3(String[] optionName3) {
		this.optionName3 = CommonUtils.copy(optionName3);
	}

	public String[] getOptionPrice() {
		return CommonUtils.copy(optionPrice);
	}

	public void setOptionPrice(String[] optionPrice) {
		this.optionPrice = CommonUtils.copy(optionPrice);
	}

	public String[] getOptionStockQuantity() {
		return CommonUtils.copy(optionStockQuantity);
	}

	public void setOptionStockQuantity(String[] optionStockQuantity) {
		this.optionStockQuantity = CommonUtils.copy(optionStockQuantity);
	}

	public String getItemNewFlag() {
		return itemNewFlag;
	}
	public void setItemNewFlag(String itemNewFlag) {
		this.itemNewFlag = itemNewFlag;
	}

	public List<ItemImage> getItemImages() {
		return itemImages;
	}
	public void setItemImages(List<ItemImage> itemImages) {
		this.itemImages = itemImages;
	}
	public List<ItemRelation> getItemRelations() {
		return itemRelations;
	}
	public void setItemRelations(List<ItemRelation> itemRelations) {
		this.itemRelations = itemRelations;
	}

	public List<ItemInfo> getItemInfos() {
		return itemInfos;
	}

	public void setItemInfos(List<ItemInfo> itemInfos) {
		this.itemInfos = itemInfos;
	}

	public String getItemOptionTitle1() {
		return itemOptionTitle1;
	}

	public void setItemOptionTitle1(String itemOptionTitle1) {
		this.itemOptionTitle1 = itemOptionTitle1;
	}

	public String getItemOptionTitle2() {
		return itemOptionTitle2;
	}

	public void setItemOptionTitle2(String itemOptionTitle2) {
		this.itemOptionTitle2 = itemOptionTitle2;
	}

	public String getItemOptionTitle3() {
		return itemOptionTitle3;
	}

	public void setItemOptionTitle3(String itemOptionTitle3) {
		this.itemOptionTitle3 = itemOptionTitle3;
	}

	public String getFreeGiftFlag() {
		return freeGiftFlag;
	}

	public void setFreeGiftFlag(String freeGiftFlag) {
		this.freeGiftFlag = freeGiftFlag;
	}

	public String getFreeGiftName() {
		return freeGiftName;
	}

	public void setFreeGiftName(String freeGiftName) {
		this.freeGiftName = freeGiftName;
	}

	public List<GiftItemInfo> getFreeGiftItemList() {
		return freeGiftItemList;
	}

	public void setFreeGiftItemList(List<GiftItemInfo> freeGiftItemList) {
		this.freeGiftItemList = freeGiftItemList;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public String getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(String reviewCount) {
		this.reviewCount = reviewCount;
	}

	public String getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(String reviewScore) {
		this.reviewScore = reviewScore;
	}

	public List<ItemSet> getItemSets() {
		return itemSets;
	}

	public void setItemSets(List<ItemSet> itemSets) {
		this.itemSets = itemSets;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getEventViewUrl() {
		return EventViewUtils.getUrl(EventCodeType.SHARE, getItemUserCode());
	}

	public String getEpItemUrl(String channel) {
		return EventViewUtils.getEpItemUrl(channel, getItemUserCode());
	}

	public boolean isNaverPayFlag() {
		return naverPayFlag;
	}

	public void setNaverPayFlag(boolean naverPayFlag) {
		this.naverPayFlag = naverPayFlag;
	}

	public boolean isWishlistFlag() {
		return wishlistFlag;
	}

	public void setWishlistFlag(boolean wishlistFlag) {
		this.wishlistFlag = wishlistFlag;
	}

	public String getOriginContry() {
		return originContry;
	}

	public void setOriginContry(String originContry) {
		this.originContry = originContry;
	}

	public String getItemTextOptionFlag() {
		return itemTextOptionFlag;
	}

	public void setItemTextOptionFlag(String itemTextOptionFlag) {
		this.itemTextOptionFlag = itemTextOptionFlag;
	}

	public String getItemTextOptionTitle1() {
		return itemTextOptionTitle1;
	}

	public void setItemTextOptionTitle1(String itemTextOptionTitle1) {
		this.itemTextOptionTitle1 = itemTextOptionTitle1;
	}

	public String getItemTextOptionTitle2() {
		return itemTextOptionTitle2;
	}

	public void setItemTextOptionTitle2(String itemTextOptionTitle2) {
		this.itemTextOptionTitle2 = itemTextOptionTitle2;
	}

	public String getItemTextOptionTitle3() {
		return itemTextOptionTitle3;
	}

	public void setItemTextOptionTitle3(String itemTextOptionTitle3) {
		this.itemTextOptionTitle3 = itemTextOptionTitle3;
	}

}
