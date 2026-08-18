package saleson.shop.order.domain;

import com.onlinepowers.framework.util.ValidationUtils;
import org.springframework.util.StringUtils;

import saleson.common.utils.CommonUtils;
import saleson.common.utils.ShopUtils;
import saleson.shop.cart.domain.OrderQuantity;
import saleson.shop.coupon.domain.CouponItem;
import saleson.shop.coupon.domain.OrderCoupon;
import saleson.shop.giftitem.domain.GiftItemInfo;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemOption;
import saleson.shop.point.domain.PointPolicy;
import saleson.shop.userlevel.domain.UserLevel;

import java.util.ArrayList;
import java.util.List;

public class BuyItem implements Cloneable {


	@Override
    public Object clone() throws CloneNotSupportedException {

		BuyItem cloneObject = (BuyItem) super.clone();

		if (!ValidationUtils.isNull(itemPrice)) {
			cloneObject.setItemPrice((ItemPrice) itemPrice.clone());
		}

		// CJH 2016. 10. 27 쿠폰 할인금액 이슈로 상품 쿠폰 정보도 복사해서 새로 생성
		if (itemCoupons != null) {

			List<OrderCoupon> newList = new ArrayList<>();
			for(OrderCoupon coupon : itemCoupons) {
				newList.add(copyOrderCoupon(coupon));
			}

			cloneObject.setItemCoupons(newList);
		}

		if (addItemCoupons != null) {

			List<OrderCoupon> newList = new ArrayList<>();
			for(OrderCoupon coupon : addItemCoupons) {
				newList.add(copyOrderCoupon(coupon));
			}

			cloneObject.setAddItemCoupons(newList);
		}

        return cloneObject;
    }

	private UserLevel userLevel; // 회원 등급
	public UserLevel getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(UserLevel userLevel) {
		this.userLevel = userLevel;
	}

	// 보안코딩 적용
	public void setItemCoupons(List<OrderCoupon> itemCoupons) {
//		this.itemCoupons = itemCoupons;
		if (itemCoupons == null) {
			itemCoupons = null;
		} else {
			this.itemCoupons = new ArrayList<>();
			for (OrderCoupon orderCoupon : itemCoupons) {
				try {
					itemCoupons.add((OrderCoupon) orderCoupon.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
		}
	}

	private Shipping buyShipping;

	// 장바구니 번호
	private int cartId;
	private String deviceType = "WEB";
	private String sessionId;


	private int shippingIndex; // 복수 배송지때 사용

	private String orderCode;
	private int orderSequence;
	private int itemSequence;
	private int shippingSequence;
	private int shippingInfoSequence;

	private long userId;
	private String guestFlag;
	private long sellerId;
	private String sellerName;
	private String companyName;
	private int categoryTeamId;
	private int categoryGroupId;
	private int categoryId;
	private int shipmentId;
	private int shipmentReturnId;
	private int couponUserId;
	private int addCouponUserId;

	private int itemId;
	private String itemCode;
	private String itemUserCode;
	private String itemName;
	private String freeGiftName;
	private String freeGiftItemText;
	private List<GiftItemInfo> freeGiftItemList;

	private String brand;

	private String options;
	// 필수 추가정보
	private String textOption;
	private String cartCreatedDate;

	private String optionsOriginal;
	private String optionsDisplay;
	private List<ItemOption> optionList;

	private String orderStatus;

	private String deliveryType;

	private int deliveryCompanyId;
	private String deliveryCompanyName;
	private String deliveryNumber;

	private String createdDate;
	private String updatedDate;
	private String revenueSalesStatus;
	private String salesDate;
	private String salesCancelDate;
	private String deliveryDate;
	private String confirmDate;
	private String returnRequestDate;
	private String returnRequestFinishDate;
	private String exchangeRequestDate;
	private String returnPointFlag = "N";

	private String orderItemStatus;
	private String islandType;
	private String payDate;
	private int shippingReturn;

	private String additionItemFlag = "N";
	private int parentItemId;
	private int parentItemSequence;
	private int setItemSequence;
	private String setItemFlag = "N";
	private String filler9;
	private String updatedAdminUserName;



	public String getTextOption() {
		return textOption;
	}
	public void setTextOption(String textOption) {
		this.textOption = textOption;
	}
	public String getCartCreatedDate() {
		return cartCreatedDate;
	}
	public void setCartCreatedDate(String cartCreatedDate) {
		this.cartCreatedDate = cartCreatedDate;
	}
	public Shipping getBuyShipping() {
		return buyShipping;
	}
	public void setBuyShipping(Shipping buyShipping) {
		this.buyShipping = buyShipping;
	}
	public int getShippingIndex() {
		return shippingIndex;
	}
	public void setShippingIndex(int shippingIndex) {
		this.shippingIndex = shippingIndex;
	}
	public int getShippingInfoSequence() {
		return shippingInfoSequence;
	}
	public void setShippingInfoSequence(int shippingInfoSequence) {
		this.shippingInfoSequence = shippingInfoSequence;
	}
	public int getShippingSequence() {
		return shippingSequence;
	}
	public void setShippingSequence(int shippingSequence) {
		this.shippingSequence = shippingSequence;
	}

	public String getDeliveryType() {
		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	public int getParentItemId() {
		return parentItemId;
	}
	public void setParentItemId(int parentItemId) {
		this.parentItemId = parentItemId;
	}
	public int getParentItemSequence() {
		return parentItemSequence;
	}
	public void setParentItemSequence(int parentItemSequence) {
		this.parentItemSequence = parentItemSequence;
	}
	public int getSetItemSequence() {
		return setItemSequence;
	}
	public void setSetItemSequence(int setItemSequence) {
		this.setItemSequence = setItemSequence;
	}
	public int getShippingReturn() {
		return shippingReturn;
	}
	public void setShippingReturn(int shippingReturn) {
		this.shippingReturn = shippingReturn;
	}

	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public String getIslandType() {
		return islandType;
	}
	public void setIslandType(String islandType) {
		this.islandType = islandType;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeliveryCompanyName() {
		return deliveryCompanyName;
	}
	public void setDeliveryCompanyName(String deliveryCompanyName) {
		this.deliveryCompanyName = deliveryCompanyName;
	}
	public String getRevenueSalesStatus() {
		return revenueSalesStatus;
	}
	public void setRevenueSalesStatus(String revenueSalesStatus) {
		this.revenueSalesStatus = revenueSalesStatus;
	}
	public String getOrderItemStatus() {
		return orderItemStatus;
	}
	public void setOrderItemStatus(String orderItemStatus) {
		this.orderItemStatus = orderItemStatus;
	}
	// 에스크로 상태
	private String escrowStatus;

	// 구매시 적립금
	private PointPolicy pointPolicy;

	// 상품 정보
	private Item item;

	// 구매 수량 정보
	private OrderQuantity orderQuantity;

	// 구매 가능 상태 여부(Y: 판매 가능, N: 판매 불가)
	private String availableForSaleFlag= "Y";

	// 사용자에게 보여줄 메시지
	private String systemComment;

	// 해당 상품에서 사용가능한 쿠폰 - 일반 쿠폰
	private List<OrderCoupon> itemCoupons;

	// 해당 상품에서 사용가능한 쿠폰 - 중복 쿠폰
	private List<OrderCoupon> addItemCoupons;

	// 상품 가격 정보
	private ItemPrice itemPrice;

	private String campaignCode;

	private String shippingPaymentType = "1";
	private String shipmentGroupCode;

	// 지자체 정보
	private String locgovCode;
	private String locgovNm;

	// 19세 이상 구매가능 답례품 여부
	private String adultItemYn;

	// 모바일 상품 여부
	private String mobileItemYn;

	// 면세 여부
	private String taxFreeYn;

	// 모바일 상품 분할 개수
	private long copyMobileItemCnt;

	// 세트상품이 있는 경우
	private List<BuyItem> itemSets = new ArrayList<>();

	public String getShipmentGroupCode() {
		return shipmentGroupCode;
	}
	public void setShipmentGroupCode(String shipmentGroupCode) {
		this.shipmentGroupCode = shipmentGroupCode;
	}
	public int getShipmentReturnId() {
		return shipmentReturnId;
	}
	public void setShipmentReturnId(int shipmentReturnId) {
		this.shipmentReturnId = shipmentReturnId;
	}
	public String getFreeGiftName() {
		return freeGiftName;
	}
	public void setFreeGiftName(String freeGiftName) {
		this.freeGiftName = freeGiftName;
	}

	public String getFreeGiftItemText() {
		return freeGiftItemText;
	}

	public void setFreeGiftItemText(String freeGiftItemText) {
		this.freeGiftItemText = freeGiftItemText;
	}

	// 보안코딩 적용
	public List<GiftItemInfo> getFreeGiftItemList() {
//		return freeGiftItemList;
		if (freeGiftItemList == null) {
			return null;
		} else {
			List<GiftItemInfo> list = new ArrayList<>();
			for (GiftItemInfo giftItemInfo : freeGiftItemList) {
				try {
					list.add((GiftItemInfo) giftItemInfo.clone());
				} catch(CloneNotSupportedException e) {
					continue;
				}
			}
			return list;
		}
	}

	// 보안코딩 적용
	public void setFreeGiftItemList(List<GiftItemInfo> freeGiftItemList) {
//		this.freeGiftItemList = freeGiftItemList;
		if (freeGiftItemList == null) {
			this.freeGiftItemList = null;
		} else {
			this.freeGiftItemList = new ArrayList<>();
			for (GiftItemInfo giftItemInfo : freeGiftItemList) {
				try {
					this.freeGiftItemList.add((GiftItemInfo) giftItemInfo.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
		}
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

	public int getItemSequence() {
		return itemSequence;
	}
	public void setItemSequence(int itemSequence) {
		this.itemSequence = itemSequence;
	}

	// 보안코딩 적용
	public List<OrderCoupon> getItemCoupons() {
//		return itemCoupons;
		if (itemCoupons == null) {
			return null;
		} else {
			List<OrderCoupon> list = new ArrayList<>();
			for (OrderCoupon orderCoupon : itemCoupons) {
				try {
					list.add((OrderCoupon) orderCoupon.clone());
				} catch(CloneNotSupportedException e) {
					continue;
				}
			}
			return list;
		}
	}

	public void setItemCoupons(List<OrderCoupon> itemCoupons, boolean isAll) {

		if (itemCoupons == null) {
			return;
		}

		Item item = this.getItem();

		if ("N".equals(item.getCouponUseFlag())) {
			return;
		}

		// 스팟할인 상품의 경우 쿠폰을 사용하지 못하게 한다
		//if (item.isSpotItem()) {
		//	return;
		//}

		// CJH 2016.11.07 추가구성상품 쿠폰 적용 안됨
		if ("Y".equals(getAdditionItemFlag())) {
			return;
		}

		int itemId = item.getItemId();
		for(OrderCoupon itemCoupon : itemCoupons) {

			if (isAll == false) {
				boolean isSet = false;
				for(CouponItem cItem : itemCoupon.getCouponItems()) {
					if (itemId == cItem.getItemId()) {
						isSet = true;
					}

				}

				if (isSet == false) {
					continue;
				}
			}



			int discountPrice = ShopUtils.getCouponDiscountPriceForItemCoupon(itemCoupon, this);
			OrderCoupon coupon = this.newOrderCoupon(itemCoupon, discountPrice);

			if (discountPrice > 0 && coupon != null) {
				if (this.itemCoupons == null) {
					this.itemCoupons = new ArrayList<>();
				} else {

					// CJH 2016.4.20 같은쿠폰이 이미 담겨있는경우 무시한다. 같은쿠폰이 여러개 노출됨.
					for(OrderCoupon c : this.itemCoupons) {
						if (coupon.getCouponUserId() == c.getCouponUserId()) {
							coupon = null;
							break;
						}
					}

				}
				if (coupon != null) {
					this.itemCoupons.add(coupon);
				}
			}
		}
	}

	/**
	 * 사용한 쿠폰정보를 반환
	 * @return
	 */
	public OrderCoupon getUsedCoupon() {
		if (this.couponUserId == 0) {
			return null;
		}

		if (this.itemCoupons == null) {
			return null;
		}

		for(OrderCoupon coupon : this.itemCoupons) {
			if (coupon.getCouponUserId() == this.couponUserId) {
				return coupon;
			}
		}

		return null;
	}

	/**
	 * 사용한 쿠폰정보를 반환
	 * @return
	 */
	public OrderCoupon getUsedAddCoupon() {

		if (this.addCouponUserId == 0) {
			return null;
		}

		if (this.addItemCoupons == null) {
			return null;
		}

		for(OrderCoupon coupon : this.addItemCoupons) {
			if (coupon.getCouponUserId() == this.addCouponUserId) {
				return coupon;
			}
		}

		return null;
	}

	/**
	 * Post로 전송된 사용 쿠폰 적용
	 * @param useCouponKeys
	 */
	public void setCouponUserId(List<String> useCouponKeys, int shippingIndex) {
		if (useCouponKeys == null) {
			return;
		}

		for(String coupon : useCouponKeys) {
			if (coupon.startsWith("item-coupon")) {
				String[] tmp = StringUtils.delimitedListToStringArray(coupon, "-");
				if (tmp.length == 5) {
					int couponUserId = Integer.parseInt(tmp[2]);
					int itemSequence = Integer.parseInt(tmp[3]);
					int sIndex = Integer.parseInt(tmp[4]);

					if (this.getItemSequence() == itemSequence && shippingIndex == sIndex) {
						this.setCouponUserId(couponUserId);
						break;
					}
				}
			}
		}

		for(String coupon : useCouponKeys) {
			if (coupon.startsWith("add-item-coupon")) {
				String[] tmp = StringUtils.delimitedListToStringArray(coupon, "-");
				if (tmp.length == 6) {
					int couponUserId = Integer.parseInt(tmp[3]);
					int itemSequence = Integer.parseInt(tmp[4]);
					int sIndex = Integer.parseInt(tmp[5]);

					if (this.getItemSequence() == itemSequence && shippingIndex == sIndex) {
						this.setAddCouponUserId(couponUserId);
						break;
					}
				}
			}
		}
	}

	/**
	 *
	 * @param orderCoupon
	 * @return
	 */
	private OrderCoupon copyOrderCoupon(OrderCoupon orderCoupon) {
		OrderCoupon r = new OrderCoupon();
		r.setCouponUserId(orderCoupon.getCouponUserId());
		r.setCouponName(orderCoupon.getCouponName());
		r.setCouponComment(orderCoupon.getCouponComment());
		r.setCouponApplyStartDate(orderCoupon.getCouponApplyStartDate());
		r.setCouponApplyEndDate(orderCoupon.getCouponApplyEndDate());

		r.setCouponPayRestriction(orderCoupon.getCouponPayRestriction());
		r.setCouponConcurrently(orderCoupon.getCouponConcurrently());
		r.setCouponPay(orderCoupon.getCouponPay());
		r.setCouponPayType(orderCoupon.getCouponPayType());
		r.setUserId(orderCoupon.getUserId());
		r.setDiscountPrice(orderCoupon.getDiscountPrice());

		r.setDataStatusCode(orderCoupon.getDataStatusCode());
		r.setOrderCode(orderCoupon.getOrderCode());

		r.setCouponDiscountLimitPrice(orderCoupon.getCouponDiscountLimitPrice());
		r.setDiscountAmount(orderCoupon.getDiscountAmount());
		return r;
	}

	/**
	 * 쿠폰 새로 생성
	 * @param orderCoupon
	 * @param discountPrice
	 * @return
	 */
	private OrderCoupon newOrderCoupon(OrderCoupon orderCoupon, int discountPrice) {
		OrderCoupon r = null;
		if (orderCoupon == null) {
			return null;
		}

		r = new OrderCoupon();
		r.setCouponUserId(orderCoupon.getCouponUserId());
		r.setCouponName(orderCoupon.getCouponName());
		r.setCouponComment(orderCoupon.getCouponComment());
		r.setCouponApplyStartDate(orderCoupon.getCouponApplyStartDate());
		r.setCouponApplyEndDate(orderCoupon.getCouponApplyEndDate());

		r.setCouponPayRestriction(orderCoupon.getCouponPayRestriction());
		r.setCouponConcurrently(orderCoupon.getCouponConcurrently());
		r.setCouponPay(orderCoupon.getCouponPay());
		r.setCouponPayType(orderCoupon.getCouponPayType());
		r.setUserId(orderCoupon.getUserId());
		r.setDiscountPrice(discountPrice);

		int discountAmount = discountPrice;

		// 구매 수량만큼 중복 할인 쿠폰
		if ("2".equals(orderCoupon.getCouponConcurrently())) {
			discountAmount = discountPrice * CommonUtils.intNvl(getItemPrice().getQuantity()) ;
		}

		r.setDataStatusCode(orderCoupon.getDataStatusCode());
		r.setOrderCode(orderCoupon.getOrderCode());

		r.setCouponDiscountLimitPrice(orderCoupon.getCouponDiscountLimitPrice());
		r.setDiscountAmount(discountAmount);

		return r;
	}

	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public PointPolicy getPointPolicy() {
		return pointPolicy;
	}
	public void setPointPolicy(PointPolicy pointPolicy) {
		this.pointPolicy = pointPolicy;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}

	// 보안코딩 적용
	public List<ItemOption> getOptionList() {
//		return optionList;
		if (optionList == null) {
			return null;
		} else {
			List<ItemOption> list = new ArrayList<>();
			for (ItemOption itemOption : optionList) {
				try {
					list.add((ItemOption) itemOption.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
			return list;
		}
	}

	// 보안코딩 적용
	public void setOptionList(List<ItemOption> optionList) {
//		this.optionList = optionList;
		if (optionList == null) {
			this.optionList = null;
		} else {
			this.optionList = new ArrayList<>();
			for (ItemOption itemOption : optionList) {
				try {
					this.optionList.add((ItemOption) itemOption.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
		}
	}
	public int getCartId() {
		return cartId;
	}
	public void setCartId(int cartId) {
		this.cartId = cartId;
	}
	public ItemPrice getItemPrice() {
		if (itemPrice == null) {
			return null;
		} else {
			try {
				return (ItemPrice) itemPrice.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}
//		return itemPrice;
	}
	public void setItemPrice(ItemPrice itemPrice) {
//		this.itemPrice = itemPrice;
		try {
			this.itemPrice = (ItemPrice) itemPrice.clone();
		} catch (CloneNotSupportedException e) {
			this.itemPrice = null;
		}
	}
	public String getSystemComment() {
		return systemComment;
	}
	public void setSystemComment(String systemComment) {
		this.systemComment = systemComment;
	}
	public String getAvailableForSaleFlag() {
		return availableForSaleFlag;
	}
	public void setAvailableForSaleFlag(String availableForSaleFlag) {
		this.availableForSaleFlag = availableForSaleFlag;
	}
	public OrderQuantity getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(OrderQuantity orderQuantity) {
		this.orderQuantity = orderQuantity;
	}

	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}

	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getGuestFlag() {
		return guestFlag;
	}
	public void setGuestFlag(String guestFlag) {
		this.guestFlag = guestFlag;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public int getCategoryTeamId() {
		return categoryTeamId;
	}
	public void setCategoryTeamId(int categoryTeamId) {
		this.categoryTeamId = categoryTeamId;
	}
	public int getCategoryGroupId() {
		return categoryGroupId;
	}
	public void setCategoryGroupId(int categoryGroupId) {
		this.categoryGroupId = categoryGroupId;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getShipmentId() {
		return shipmentId;
	}
	public void setShipmentId(int shipmentId) {
		this.shipmentId = shipmentId;
	}
	public int getItemId() {
		return itemId;
	}
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	public int getCouponUserId() {
		return couponUserId;
	}
	public void setCouponUserId(int couponUserId) {
		this.couponUserId = couponUserId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemUserCode() {
		return itemUserCode;
	}
	public void setItemUserCode(String itemUserCode) {
		this.itemUserCode = itemUserCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getDeliveryCompanyId() {
		return deliveryCompanyId;
	}
	public void setDeliveryCompanyId(int deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}
	public String getDeliveryNumber() {
		return deliveryNumber;
	}
	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}
	public String getSalesDate() {
		return salesDate;
	}
	public void setSalesDate(String salesDate) {
		this.salesDate = salesDate;
	}
	public String getSalesCancelDate() {
		return salesCancelDate;
	}
	public void setSalesCancelDate(String salesCancelDate) {
		this.salesCancelDate = salesCancelDate;
	}
	public String getReturnRequestDate() {
		return returnRequestDate;
	}
	public void setReturnRequestDate(String returnRequestDate) {
		this.returnRequestDate = returnRequestDate;
	}
	public String getReturnRequestFinishDate() {
		return returnRequestFinishDate;
	}
	public void setReturnRequestFinishDate(String returnRequestFinishDate) {
		this.returnRequestFinishDate = returnRequestFinishDate;
	}
	public String getExchangeRequestDate() {
		return exchangeRequestDate;
	}
	public void setExchangeRequestDate(String exchangeRequestDate) {
		this.exchangeRequestDate = exchangeRequestDate;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}
	public String getReturnPointFlag() {
		return returnPointFlag;
	}
	public void setReturnPointFlag(String returnPointFlag) {
		this.returnPointFlag = returnPointFlag;
	}
	public int getOrderSequence() {
		return orderSequence;
	}
	public void setOrderSequence(int orderSequence) {
		this.orderSequence = orderSequence;
	}
	public int getAddCouponUserId() {
		return addCouponUserId;
	}
	public void setAddCouponUserId(int addCouponUserId) {
		this.addCouponUserId = addCouponUserId;
	}

	// 시큐어 코딩 적용
	public List<OrderCoupon> getAddItemCoupons() {
//		return addItemCoupons;
		if (addItemCoupons == null) {
			return null;
		} else {
			List<OrderCoupon> list = new ArrayList<>();
			for (OrderCoupon orderCoupon : addItemCoupons) {
				try {
					list.add((OrderCoupon) orderCoupon.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
			return list;
		}
	}

	// 시큐어 코딩 적용
	public void setAddItemCoupons(List<OrderCoupon> addItemCoupons) {
//		this.addItemCoupons = addItemCoupons;
		if (addItemCoupons == null) {
			this.addItemCoupons = null;
		} else {
			this.addItemCoupons = new ArrayList<>();
			for (OrderCoupon orderCoupon : addItemCoupons) {
				try {
					this.addItemCoupons.add((OrderCoupon) orderCoupon.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
		}
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getEscrowStatus() {
		return escrowStatus;
	}
	public void setEscrowStatus(String escrowStatus) {
		this.escrowStatus = escrowStatus;
	}
	public String getCampaignCode() {
//		return campaignCode;
		if ("Y".equalsIgnoreCase(mobileItemYn)) {
			return "MOBILE";
		} else {
			return null;
		}
	}
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	// 보안코딩 적용
	public List<BuyItem> getItemSets() {
		//return itemSets;
		if (itemSets == null) {
			return null;
		} else {
			List<BuyItem> list = new ArrayList<>();
			for (BuyItem buyItem : itemSets) {
				try {
					list.add((BuyItem) buyItem.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
			return list;
		}
	}

	// 보안코딩 적용
	public void setItemSets(List<BuyItem> itemSets) {
		if (itemSets == null) {
			this.itemSets = null;
		} else {
			this.itemSets = new ArrayList<>();
			for (BuyItem buyItem : itemSets) {
				try {
					this.itemSets.add((BuyItem) buyItem.clone());
				} catch (CloneNotSupportedException e) {
					continue;
				}
			}
		}
//		this.itemSets = itemSets;
	}

	public String getSetItemFlag() {
		return setItemFlag;
	}

	public void setSetItemFlag(String setItemFlag) {
		this.setItemFlag = setItemFlag;
	}
	public String getLocgovCode() {
		return locgovCode;
	}
	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}
	public String getLocgovNm() {
		return locgovNm;
	}
	public void setLocgovNm(String locgovNm) {
		this.locgovNm = locgovNm;
	}
	public String getOptionsOriginal() {
		return optionsOriginal;
	}
	public void setOptionsOriginal(String optionsOriginal) {
		this.optionsOriginal = optionsOriginal;
	}
	public String getOptionsDisplay() {
		return optionsDisplay;
	}
	public void setOptionsDisplay(String optionsDisplay) {
		this.optionsDisplay = optionsDisplay;
	}
	public String getAdultItemYn() {
		return adultItemYn;
	}
	public void setAdultItemYn(String adultItemYn) {
		this.adultItemYn = adultItemYn;
	}
	public String getMobileItemYn() {
		return mobileItemYn;
	}
	public void setMobileItemYn(String mobileItemYn) {
		this.mobileItemYn = mobileItemYn;
	}
	public String getTaxFreeYn() {
		return taxFreeYn;
	}
	public void setTaxFreeYn(String taxFreeYn) {
		this.taxFreeYn = taxFreeYn;
	}
	public long getCopyMobileItemCnt() {
		return copyMobileItemCnt;
	}
	public void setCopyMobileItemCnt(long copyMobileItemCnt) {
		this.copyMobileItemCnt = copyMobileItemCnt;
	}

	public void setItemPriceQuantity(int quantity) {
		if (itemPrice != null) {
			itemPrice.setQuantity(quantity);
		}
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getFiller9() {
		return filler9;
	}
	public void setFiller9(String filler9) {
		this.filler9 = filler9;
	}
	public String getUpdatedAdminUserName() {
		return updatedAdminUserName;
	}
	public void setUpdatedAdminUserName(String updatedAdminUserName) {
		this.updatedAdminUserName = updatedAdminUserName;
	}
}
