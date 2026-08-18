package saleson.shop.order.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.common.utils.ShopUtils;
import saleson.shop.coupon.domain.OrderCoupon;

import com.onlinepowers.framework.util.StringUtils;

@Getter @Setter
@NoArgsConstructor
public class Receiver {
	
	private long userId;
	private String sessionId;
	private String orderCode;
	private int shippingIndex;
	private String receiveZipcode;
	private String receiveNewZipcode;
	private String receiveCompanyName;
	private String receiveSido;
	private String receiveSigungu;
	private String receiveEupmyeondong;
	private String receiveAddress;
	private String receiveAddressDetail;
	private String receiveName;
	private String receivePhone;
	private String receiveMobile;
	private String content;
	private String createdDate;
	private List<BuyQuantity> buyQuantitys;

	// 배송지별 배송될 상품들
	private List<Shipping> itemGroups;
	private List<BuyItem> items;

	private String receiveZipcode1;
	private String receiveZipcode2;

	private String receivePhone1;
	private String receivePhone2;
	private String receivePhone3;

	private String receiveMobile1;
	private String receiveMobile2;
	private String receiveMobile3;


	/**
	 * 상품쿠폰 할인금액을 적용
	 * @param isClean - POST로 넘어온 데이터를 적용할때는 쿠폰 사용 정보를 초기화해줘야함!!
	 */
	public void itemCouponUsed(boolean isClean, Buy buy, int shippingIndex) {
		
		if (this.items == null) {
			return;
		}
		if (buy == null) {
			return;
		}
		
		for(BuyItem buyItem : this.items) {
			if (buyItem != null) {
				// 초기화?? -- 중복 상품 쿠폰때문에 초기화 하면 안됨..
				if (isClean) {
					buyItem.setCouponUserId(0);
					buyItem.setAddCouponUserId(0);
				}
				
				// POST로 전송된 쿠폰 사용정보 셋팅
				buyItem.setCouponUserId(buy.getUseCouponKeys(), shippingIndex);
				
				ItemPrice itemPrice = buyItem.getItemPrice();
				
				if (itemPrice != null) {
					
					itemPrice.setCouponDiscountAmount(0);
					
					int couponDiscountPrice = 0;
					int couponDiscountAmount = 0;
					
					List<OrderCoupon> itemCoupons = buyItem.getItemCoupons();
					
					if (itemCoupons != null) {
						for(OrderCoupon itemCoupon : itemCoupons) {
							if (itemCoupon != null) {
								if (buyItem.getCouponUserId() == itemCoupon.getCouponUserId()) {
									
									couponDiscountPrice += itemCoupon.getDiscountPrice();
									couponDiscountAmount += itemCoupon.getDiscountAmount();
				
									String couponKey = "item-coupon-" + itemCoupon.getCouponUserId() + "-" + buyItem.getItemSequence() + "-" + shippingIndex;
									
									// step1에서 사용할 데이터 만들기
									List<String> makeUseCouponKeys = buy.getMakeUseCouponKeys();
									if (makeUseCouponKeys == null) {
										makeUseCouponKeys = new ArrayList<>();
									}
									
									makeUseCouponKeys.add(couponKey);
									buy.setMakeUseCouponKeys(makeUseCouponKeys);
								}
							}
						}
					}
					
					/* CJH 2017. 02. 10 중복 할인 쿠폰은 재거
					List<OrderCoupon> addItemCoupons = buyItem.getAddItemCoupons();
					
					if (addItemCoupons != null) {
						for(OrderCoupon itemCoupon : addItemCoupons) {
							
							// CJH 2016. 10. 27 쿠폰 할인금액 정의
							itemCoupon.setDiscountAmount(itemCoupon.getDiscountPrice() * itemPrice.getQuantity());
							if ("3".equals(itemCoupon.getCouponKind()) && buyItem.getAddCouponUserId() == itemCoupon.getCouponUserId()) {
								
								couponDiscountPrice += itemCoupon.getDiscountPrice();
								couponDiscountAmount += itemCoupon.getDiscountAmount();

								String couponKey = "add-item-coupon-" + itemCoupon.getCouponUserId() + "-" + buyItem.getItemSequence() + "-" + shippingIndex;
								
								// step1에서 사용할 데이터 만들기
								List<String> makeUseCouponKeys = buy.getMakeUseCouponKeys();
								if (makeUseCouponKeys == null) {
									makeUseCouponKeys = new ArrayList<>();
								}
								
								makeUseCouponKeys.add(couponKey);
								buy.setMakeUseCouponKeys(makeUseCouponKeys);
							}
						}
					}
					*/
					itemPrice.setCouponDiscountPrice(couponDiscountPrice);
					itemPrice.setCouponDiscountAmount(couponDiscountAmount);
				}
			}
		}
		
	}
	
	public void setShipping(String islandType) {
		Shipping shipping = new Shipping();
		
		if (this.items.isEmpty()) {
			return;
		}

		this.itemGroups = shipping.getShippingGroups(this.items, islandType);
	}

	public String getReceiveMobile1() {
		if (receiveMobile1 != null && !receiveMobile1.isEmpty()) return receiveMobile1;
		if (receiveMobile == null || receiveMobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receiveMobile)[0];
	}

	public String getReceiveMobile2() {
		if (receiveMobile2 != null && !receiveMobile2.isEmpty()) return receiveMobile2;
		if (receiveMobile == null || receiveMobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receiveMobile)[1];
	}

	public String getReceiveMobile3() {
		if (receiveMobile3 != null && !receiveMobile3.isEmpty()) return receiveMobile3;
		if (receiveMobile == null || receiveMobile.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receiveMobile)[2];
	}

	public String getReceivePhone1() {
		if (receivePhone1 != null && !receivePhone1.isEmpty()) return receivePhone1;
		if (receivePhone == null || receivePhone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receivePhone)[0];
	}

	public String getReceivePhone2() {
		if (receivePhone2 != null && !receivePhone2.isEmpty()) return receivePhone2;
		if (receivePhone == null || receivePhone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receivePhone)[1];
	}

	public String getReceivePhone3() {
		if (receivePhone3 != null && !receivePhone3.isEmpty()) return receivePhone3;
		if (receivePhone == null || receivePhone.isEmpty()) return "";
		return ShopUtils.phoneNumberForDelimitedToStringArray(receivePhone)[2];
	}

	public void processHyphen() {
		this.receiveMobile = StringUtils.hasText(this.receiveMobile1) ? this.receiveMobile1 + '-' + this.receiveMobile2 + '-' + this.receiveMobile3 : "";
		this.receivePhone = StringUtils.hasText(this.receivePhone1) ? this.receivePhone1 + '-' + this.receivePhone2 + '-' + this.receivePhone3 : "";
	}
}
