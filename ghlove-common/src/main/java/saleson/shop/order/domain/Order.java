package saleson.shop.order.domain;

import com.onlinepowers.framework.security.DataEncryptor;
import com.onlinepowers.framework.util.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import saleson.shop.order.claimapply.domain.OrderCancelShipping;
import saleson.shop.order.claimapply.domain.OrderReturnApply;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @NoArgsConstructor
public class Order {

	private List<OrderReturnApply> returnApplys;
	private List<OrderCancelShipping> cancelShippingGroups;
	private String orderCode;
	private int orderSequence;
	private long userId;
	private String loginId;
	private String buyerName;

	private String userName;
	private String phone;
	private String mobile;
	private String email;

	private String zipcode;
	private String newZipcode;
	private String companyName;
	private String sido;
	private String sigungu;
	private String eupmyeondong;
	private String address;
	private String addressDetail;

	private int itemTotalAmount;
	private int shippingTotalAmount;
	private int orderTotalAmount;
	private int payAmount;
	private String ip;
	private String orderAdminMemo;
	private String messageTargetDeliveryCompanyName; 	// 배송 메시지 발송시 발송 택배사
	private String messageTargetDeliveryNumber; 		// 배송 메시지 발송시 발송 타겟 송장번호
	private String[] messageTargetItemSequences;		// 배송 메시지 발송 타겟 아이템 순번

	private String returnBankName;
	private String returnBankInName;
	private String returnVirtualNo;

	private String dataStatusCode;
	private String createdDate;

	private List<OrderPayment> orderPayments;
	private List<OrderShippingInfo> orderShippingInfos;

	private String zipcode1;
	private String zipcode2;
	private String locgovNm;
	private String locgovCode;
	private String mobileItemYn;
	private String payDate;
	private String paymentType;
	private int totalCancelAmount;
	private String itemName;
	private String options;
	private String textOption;			// 20260325 필수 추가정보.

	private String telephoneNumber;
	private String phoneNumber;

	public String getFullZipcode() {

		if (ObjectUtils.isEmpty(this.zipcode2)) {
			return this.zipcode;
		}

		return this.zipcode1 + "-" + this.zipcode2;
	}

	public void setZipcode(String zipcode) {

		String[] temp = StringUtils.delimitedListToStringArray(zipcode, "-");
		if (temp.length == 2) {
			this.zipcode1 = temp[0];
			this.zipcode2 = temp[1];
		}

		this.zipcode = zipcode;
	}

	/**
	 * 환불, 취소요청시 은행계좌 정보를 입력해야 되는가?
	 * @return
	 */
	public boolean isBankInfoWrite() {

		if (this.orderPayments == null) {
			return false;
		}

		for(OrderPayment orderPayment : orderPayments) {
			if ("bank".equals(orderPayment.getApprovalType()) || "vbank".equals(orderPayment.getApprovalType())) {
				//if (orderPayment.getRemainingAmount() > 0) {
					return true;
				//}
			}
		}

		return false;
	}

	/**
	 * 취소금액
	 * @return
	 */
	public int getCancelAmount() {

		if (this.orderPayments == null) {
			return 0;
		}

		int cancelAmount = 0;
		for (OrderPayment orderPayment : orderPayments) {
			if ("2".equals(orderPayment.getPaymentType())) {
				cancelAmount += orderPayment.getCancelAmount();
			}
		}
		return cancelAmount;
	}

	public boolean isAllItemsCanceled() {
		if (this.orderPayments == null) {
			return false;
		}

		int canceledCount = 0;
		int orderItemCount = 0;
		for (OrderShippingInfo orderShippingInfo : getOrderShippingInfos()) {
			if (orderShippingInfo.getOrderItems() == null) {
				return false;
			}

			for (OrderItem orderItem : orderShippingInfo.getOrderItems()) {
				if ("71".equals(orderItem.getOrderStatus())) {
					canceledCount++;
				}
				orderItemCount++;
			}
		}

		return orderItemCount > 0 && canceledCount == orderItemCount ? true : false;

	}

	/**
	 * 상품의 총 겟수
	 * @return
	 */
	public int getItemCount() {

		int count = 0;
		for(OrderShippingInfo info : getOrderShippingInfos()) {
			for(OrderItem item : info.getOrderItems()) {
				count++;
			}
		}

		return count;
	}

	/**
	 * 결제 예정금액
	 * @return
	 */
	public int getPostPayAmount() {

        return this.orderTotalAmount - this.payAmount - getCancelAmount();
	}


	/**
	 * 해당 주문에 에스크로 결제 유무 확인
	 *
	 * 에스크로 정보가 ORDER_ITEM에 저장 되서 ORDER_ITEM 정보에서 확인 후
	 * 결과를 리턴함.
	 *
	 * @author skc@onlinepowers.com
	 * @date 2017-08-23
	 *
	 * @return Y: 에스크로 결제 , N: 일반 결제.
	 */
	public String getEscrowStatus() {
		if (this.orderShippingInfos == null || this.orderShippingInfos.isEmpty()) {
			return "N";
		}
		for (OrderShippingInfo orderShippingInfo : this.orderShippingInfos) {
			List<OrderItem> orderItems = orderShippingInfo.getOrderItems();

			if (orderItems == null || orderItems.isEmpty()) {
				return "N";
			}

			for (OrderItem orderItem : orderItems) {
				if ("Y".equals(orderItem.getEscrowStatus())) {
					return "Y";
				} else {
					return "N";
				}

			}
		}
		return "N";
	}

	/**
	 * 주문 - 총 배송비
	 * 네이밍 변경.
	 * @return
	 */
	public int getTotalShippingAmount() {
		return getShippingTotalAmount();
	}

	/**
	 * 주문 - 총 주문금액
	 * 네이밍 변경.
	 * @return
	 */
	public int getTotalOrderAmount() {
		return getOrderTotalAmount();
	}

	/**
	 * 주문 - 총 상품금액
	 * @return
	 */
	public int getTotalItemAmount() {
//		return getTotal(OrderTotal.ITEM_AMOUNT);
		return getTotal(OrderTotal.SALE_AMOUNT);
	}

	/**
	 * 주문 - 총 할인금액 (전체)
	 * @return
	 */
	public int getTotalDiscountAmount() {
		return getTotal(OrderTotal.DISCOUNT_AMOUNT);
	}

	/**
	 * 주문 - 총 할인금액 (세트)
	 * @return
	 */
	public int getTotalSetDiscountAmount() {
		return getTotal(OrderTotal.SET_DISCOUNT_AMOUNT);
	}

	/**
	 * 주문 - 총 상품할인금액 (즉시 + 스팟)
	 * @return
	 */
	public int getTotalItemDiscountAmount() {
		return getTotal(OrderTotal.ITEM_DISCOUNT_AMOUNT);
	}

	/**
	 * 주문 - 총 할인금액 (쿠폰)
	 * @return
	 */
	public int getTotalCouponDiscountAmount() {
		return getTotal(OrderTotal.COUPON_DISCOUNT_AMOUNT);
	}

	/**
	 * 주문 - 총 할인금액 (회원등급)
	 * @return
	 */
	public int getTotalUserLevelDiscountAmount() {
		return getTotal(OrderTotal.USER_LEVEL_DISCOUNT_AMOUNT);
	}

	/**
	 * 주문 - 총 구입금액 (상품금액 - 할인금액)
	 * 할인 적용금액, 실제 결제 금액 (상품 기준)
	 * @return
	 */
	public int getTotalSaleAmount() {
		return getTotal(OrderTotal.SALE_AMOUNT);
	}


	/**
	 * 주문 항목별 Total 금액
	 * @param orderTotal
	 * @return
	 */
	private int getTotal(OrderTotal orderTotal) {
		if (this.orderShippingInfos == null || this.orderShippingInfos.isEmpty()) {
			return 0;
		}

		int totalAmount = 0;
		for (OrderShippingInfo orderShippingInfo : this.orderShippingInfos) {
			List<OrderItem> orderItems = orderShippingInfo.getOrderItems();

			if (orderItems == null || orderItems.isEmpty()) {
				continue;
			}

			for (OrderItem orderItem : orderItems) {
				int amount = 0;

				switch (orderTotal) {
					case ITEM_AMOUNT:
						amount = orderItem.getItemAmount();
						break;
					case DISCOUNT_AMOUNT:
						amount = orderItem.getDiscountAmount();
						break;
					case ITEM_DISCOUNT_AMOUNT:
						amount = orderItem.getItemDiscountAmount();
						break;
					case COUPON_DISCOUNT_AMOUNT:
						amount = orderItem.getCouponDiscountAmount();
						break;
					case USER_LEVEL_DISCOUNT_AMOUNT:
						amount = orderItem.getUserLevelDiscountAmount();
						break;
					case SALE_AMOUNT:
						amount = orderItem.getSaleAmount();
						break;
					case SET_DISCOUNT_AMOUNT:
						amount = orderItem.getSetDiscountAmount();
						break;

				}

				totalAmount += amount;
			}
		}

		return totalAmount;
	}


	public String getMessageTargetDeliveryCompanyName() {
		return messageTargetDeliveryCompanyName;
	}

	public void setMessageTargetDeliveryCompanyName(String messageTargetDeliveryCompanyName) {
		this.messageTargetDeliveryCompanyName = messageTargetDeliveryCompanyName;
	}

	public List<String> getOrderItemUserCodes() {

		List<String> list = new ArrayList<>();

		List<OrderShippingInfo> shippingInfos = getOrderShippingInfos();

		if (shippingInfos != null && !shippingInfos.isEmpty()) {

			for (OrderShippingInfo shippingInfo : shippingInfos) {
				List<OrderItem> orderItems = shippingInfo.getOrderItems();

				if (orderItems != null && !orderItems.isEmpty()) {
					for (OrderItem orderItem: orderItems) {
						list.add(orderItem.getItemUserCode());
					}
				}
			}
		}

		return list;
	}

	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor encryptor) {
		encryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}
}

/**
 * 주문 항목별 TOTAL
 */
enum OrderTotal {
	ITEM_AMOUNT,				// 상품금액
	DISCOUNT_AMOUNT,			// 전체 할인금액
	SET_DISCOUNT_AMOUNT,			// 세트 할인금액
	ITEM_DISCOUNT_AMOUNT,		// 상품할인금액
	COUPON_DISCOUNT_AMOUNT,		// 쿠폰사용금액
	USER_LEVEL_DISCOUNT_AMOUNT,		// 등급할인금액
	SALE_AMOUNT					// 구매금액 (할인적용금액)
}
