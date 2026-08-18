package saleson.shop.order.support;

import com.onlinepowers.framework.web.domain.SearchParam;
import org.springframework.util.ObjectUtils;
import saleson.common.utils.CommonUtils;
import saleson.common.utils.SellerUtils;
import saleson.common.utils.UserUtils;
import saleson.common.web.Paging;
import saleson.shop.order.claimapply.domain.ClaimApply;
import saleson.shop.order.claimapply.domain.ClaimApplyItem;
import saleson.shop.order.claimapply.support.ExchangeApply;
import saleson.shop.order.claimapply.support.ReturnApply;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.shipping.support.ShippingParam;
import saleson.shop.order.shipping.support.ShippingReadyParam;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class OrderParam extends SearchParam {

	private String payMode;

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	private String searchDate;
	public String getSearchDate() {
		return searchDate;
	}
	public void setSearchDate(String searchDate) {
		this.searchDate = searchDate;
	}

	public OrderParam() {}
	public OrderParam(ClaimApply apply) {
		setOrderCode(apply.getOrderCode());
		setOrderSequence(apply.getOrderSequence());
	}

	public OrderParam(ExchangeApply apply) {
		setOrderCode(apply.getOrderCode());
		setOrderSequence(apply.getOrderSequence());
		setItemSequence(apply.getItemSequence());
	}

	public OrderParam(ReturnApply apply) {
		setOrderCode(apply.getOrderCode());
		setOrderSequence(apply.getOrderSequence());
		setItemSequence(apply.getItemSequence());
		setReturnBankName(apply.getReturnBankName());
		setReturnBankInName(apply.getReturnBankInName());
		setReturnVirtualNo(apply.getReturnVirtualNo());
	}

	public OrderParam(ClaimApplyItem apply) {
		setOrderCode(apply.getOrderCode());
		setOrderSequence(apply.getOrderSequence());
		setItemSequence(apply.getItemSequence());
	}

	private int shippingIndex;
	public int getShippingIndex() {
		return shippingIndex;
	}
	public void setShippingIndex(int shippingIndex) {
		this.shippingIndex = shippingIndex;
	}

	private int shippingSequence;
	public int getShippingSequence() {
		return shippingSequence;
	}
	public void setShippingSequence(int shippingSequence) {
		this.shippingSequence = shippingSequence;
	}

	private int shippingInfoSequence;
	public int getShippingInfoSequence() {
		return shippingInfoSequence;
	}
	public void setShippingInfoSequence(int shippingInfoSequence) {
		this.shippingInfoSequence = shippingInfoSequence;
	}

	private String viewTarget;
	private String confirmPurchaseDate;
	public String getConfirmPurchaseDate() {
		return confirmPurchaseDate;
	}
	public void setConfirmPurchaseDate(String confirmPurchaseDate) {
		this.confirmPurchaseDate = confirmPurchaseDate;
	}

	private String shippingCompleteDate;
	public String getShippingCompleteDate() {
		return shippingCompleteDate;
	}
	public void setShippingCompleteDate(String shippingCompleteDate) {
		this.shippingCompleteDate = shippingCompleteDate;
	}

	public String getViewTarget() {
		return viewTarget;
	}
	public void setViewTarget(String viewTarget) {
		this.viewTarget = viewTarget;
	}

	private String deliveryType;
	private String shipmentReturnType;

	public String getShipmentReturnType() {

		if (ObjectUtils.isEmpty(shipmentReturnType)) {
			return "";
		}

		return shipmentReturnType;
	}
	public void setShipmentReturnType(String shipmentReturnType) {
		this.shipmentReturnType = shipmentReturnType;
	}
	public String getDeliveryType() {

		if (ObjectUtils.isEmpty(deliveryType)) {
			return "";
		}

		return deliveryType;
	}
	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}

	private String sessionId;
	private long userId;
	private String adminUserName;
	private long sellerId;
	private String orderCode;
	private int orderSequence;
	private int itemSequence;
	private int paymentSequence;
	private int parentItemId;
	private Integer parentItemSequence;
	private Integer setItemSequence;
	public int getPaymentSequence() {
		return paymentSequence;
	}
	public void setPaymentSequence(int paymentSequence) {
		this.paymentSequence = paymentSequence;
	}
	public int getOrderSequence() {
		return orderSequence;
	}

	public void setOrderSequence(int orderSequence) {
		this.orderSequence = orderSequence;
	}

	public int getItemSequence() {
		return itemSequence;
	}

	public void setItemSequence(int itemSequence) {
		this.itemSequence = itemSequence;
	}

	// 출고 지시
	private List<ShippingReadyParam> shippingReadys;
	public List<ShippingReadyParam> getShippingReadys() {
		return shippingReadys;
	}
	public void setShippingReadys(List<ShippingReadyParam> shippingReadys) {
		this.shippingReadys = shippingReadys;
	}

	// 출고 처리
	private List<ShippingParam> shippings;
	public List<ShippingParam> getShippings() {
		return shippings;
	}
	public void setShippings(List<ShippingParam> shippings) {
		this.shippings = shippings;
	}

	private String pgKey;
	private String escrowStatus;

	private String guestUserName;
	private String guestPhoneNumber;

	private String shippingPaymentMethod = "1";


	private String searchDateType;
	private String searchStartDate;
	private String searchStartDateTime = "00";
	private String searchEndDate;
	private String searchEndDateTime = "23";

	private String orderStatus;
	private String orderItemStatus;
	private String orgOrderStatus;
	private String orgOrderItemStatus;
	private String changeOrderStatus;

	// 요청일
	private String requestDate;

	// 구매 확정일
	private String confirmDate;

	// 관리자 검색용
	private List<String> searchOrderStatusList;
	private List<String> orderStatusList;

	// 입금 지연일
	private String searchDelayDay;

	private String[] id;

	private int payAmount;



	// 관리자 송장번호 등록용 - Mapper
	private int deliveryCompanyId;
	private String deliveryCompanyName;
	private String deliveryNumber;
	private String deliveryCompanyUrl;

	// 관리자 환불 처리상태
	private String refundStatusCode;

	// 반송사유
	private String returnItemReason;
	private String returnItemReasonText;

	// 반송비용
	private int returnRealShipping;

	private int orderCancelApplyId;

	//환불정보
	private String returnBankName;
	private String returnBankInName;
	private String returnVirtualNo;
	public String getReturnBankName() {
		return returnBankName;
	}
	public void setReturnBankName(String returnBankName) {
		this.returnBankName = returnBankName;
	}
	public String getReturnBankInName() {
		return returnBankInName;
	}
	public void setReturnBankInName(String returnBankInName) {
		this.returnBankInName = returnBankInName;
	}
	public String getReturnVirtualNo() {
		return returnVirtualNo;
	}
	public void setReturnVirtualNo(String returnVirtualNo) {
		this.returnVirtualNo = returnVirtualNo;
	}

	// 구매확정 정산예정일
	private String remittanceDate;

	private List<String> orderCodes;

	private List<OrderItem> orderItems;

	private String[] status;
	private String payChangeType = "N";
	public String[] getStatus() {

		if (status == null) {
//			return new String[] {"0", "10", "20", "30", "35", "40", "50", "55", "58", "60", "65", "70", "75"};		// 주문 관련 페이지 진입시 검색결과와 진입 후 검색/페이지 이동시 검색결과가 달라서 수정(이관데이터...)
//			return new String[] {"10", "20", "30", "35", "40", "50", "55", "58", "59", "60", "65", "69", "70", "75"};			// 교환거절, 반품거절
			// 오프라인 주문관리 화면 - 입금 대기를 위해 0번 Status 추가
			return new String[] {"0", "10", "20", "30", "35", "40", "50", "55", "58", "59", "60", "65", "69", "70", "75"};			// 교환거절, 반품거절
		}

		return CommonUtils.copy(status);
	}

	private String cancelFlag;
	private String statusType;
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {

		if ("waiting-deposit".equals(statusType)) {
			cancelFlag = "N";
			status = new String[] {"0"};
		} else if ("new-order".equals(statusType)) {
			cancelFlag = "N";
			status = new String[] {"10"};
		} else if ("shipping-ready".equals(statusType)) {
			cancelFlag = "N";
			status = new String[] {"20"};
		} else if ("shipping".equals(statusType)) {
			cancelFlag = "N";
			status = new String[] {"30", "35"};
		} else if ("confirm".equals(statusType)) {
			cancelFlag = "N";
			status = new String[] {"40"};
		} else if ("cancel-request".equals(statusType)) {
			cancelFlag = "N";
			status = new String[] {"70"};
		} else if ("return-request".equals(statusType)) {
			cancelFlag = "N";
			status = new String[] {"60"};
		} else if ("exchange-request".equals(statusType)) {
			cancelFlag = "N";
			status = new String[] {"50"};
		} else if ("cancel-process".equals(statusType)) {
			status = new String[] {"70", "75", "79"};
		} else if ("return-process".equals(statusType)) {
			status = new String[] {"60", "65", "69"};
		} else if ("exchange-process".equals(statusType)) {
			status = new String[] {"50", "55", "58", "59"};
		} else if ("claim-process".equals(statusType)) {
			status = new String[] {"50", "55", "58", "60", "65", "70", "75"};
		}

		if (status != null) {
			List<String> s = new ArrayList<>();
			for(String ss : status) {
				s.add(ss);
			}

			orderStatusList = s;
		}

		this.statusType = statusType;
	}

	public String getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}

	private String[] shippingStatus;
	public String[] getShippingStatus() {

		if("finish".equals(statusType)) {
			if (shippingStatus == null) {
				return new String[] {"35", "58"};
			}
		} else {
			if (shippingStatus == null) {
				return new String[] {"30", "55"};
			}
		}

		return CommonUtils.copy(shippingStatus);
	}
	public void setShippingStatus(String[] shippingStatus) {
		this.shippingStatus = CommonUtils.copy(shippingStatus);
	}
	public void setStatus(String[] status) {
		this.status = CommonUtils.copy(status);
	}
	public List<String> getOrderCodes() {
		return orderCodes;
	}
	public void setOrderCodes(List<String> orderCodes) {
		this.orderCodes = orderCodes;
	}

	public int getOrderCancelApplyId() {
		return orderCancelApplyId;
	}

	public void setOrderCancelApplyId(int orderCancelApplyId) {
		this.orderCancelApplyId = orderCancelApplyId;
	}

	public int getDeliveryCompanyId() {
		return deliveryCompanyId;
	}

	public void setDeliveryCompanyId(int deliveryCompanyId) {
		this.deliveryCompanyId = deliveryCompanyId;
	}

	public String getDeliveryCompanyName() {
		return deliveryCompanyName;
	}

	public void setDeliveryCompanyName(String deliveryCompanyName) {
		this.deliveryCompanyName = deliveryCompanyName;
	}

	public String getDeliveryCompanyUrl() {
		return deliveryCompanyUrl;
	}

	public void setDeliveryCompanyUrl(String deliveryCompanyUrl) {
		this.deliveryCompanyUrl = deliveryCompanyUrl;
	}

	public String getDeliveryNumber() {
		return deliveryNumber;
	}

	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}

	public String getOrderItemStatus() {
		return orderItemStatus;
	}

	public void setOrderItemStatus(String orderItemStatus) {
		this.orderItemStatus = orderItemStatus;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}

	public String getOrgOrderItemStatus() {
		return orgOrderItemStatus;
	}

	public void setOrgOrderItemStatus(String orgOrderItemStatus) {
		this.orgOrderItemStatus = orgOrderItemStatus;
	}

	public int getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(int payAmount) {
		this.payAmount = payAmount;
	}

	public String getPgKey() {
		return pgKey;
	}

	public void setPgKey(String pgKey) {
		this.pgKey = pgKey;
	}

	public String[] getId() {
		return CommonUtils.copy(id);
	}

	public void setId(String[] id) {
		this.id = CommonUtils.copy(id);
	}

	public String getSearchDelayDay() {
		return searchDelayDay;
	}
	public void setSearchDelayDay(String searchDelayDay) {
		this.searchDelayDay = searchDelayDay;
	}
	public List<String> getSearchOrderStatusList() {
		return searchOrderStatusList;
	}
	public void setSearchOrderStatusList(List<String> searchOrderStatusList) {
		this.searchOrderStatusList = searchOrderStatusList;
	}
	public List<String> getOrderStatusList() {
		return orderStatusList;
	}
	public void setOrderStatusList(List<String> orderStatusList) {
		this.orderStatusList = orderStatusList;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public String getConfirmDate() {
		return confirmDate;
	}
	public void setConfirmDate(String confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrgOrderStatus() {
		return orgOrderStatus;
	}
	public void setOrgOrderStatus(String orgOrderStatus) {
		this.orgOrderStatus = orgOrderStatus;
	}

	public String getSearchStartDateTime() {
		return searchStartDateTime;
	}
	public void setSearchStartDateTime(String searchStartDateTime) {
		this.searchStartDateTime = searchStartDateTime;
	}
	public String getSearchEndDateTime() {
		return searchEndDateTime;
	}
	public void setSearchEndDateTime(String searchEndDateTime) {
		this.searchEndDateTime = searchEndDateTime;
	}
	public String getSearchDateType() {
		return searchDateType;
	}
	public void setSearchDateType(String searchDateType) {
		this.searchDateType = searchDateType;
	}
	public String getSearchStartDate() {
		return searchStartDate;
	}
	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}
	public String getSearchEndDate() {
		return searchEndDate;
	}
	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}
	public String getGuestUserName() {
		return guestUserName;
	}
	public void setGuestUserName(String guestUserName) {
		this.guestUserName = guestUserName;
	}
	public String getGuestPhoneNumber() {
		return guestPhoneNumber;
	}
	public void setGuestPhoneNumber(String guestPhoneNumber) {
		this.guestPhoneNumber = guestPhoneNumber;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getShippingPaymentMethod() {
		return shippingPaymentMethod;
	}
	public void setShippingPaymentMethod(String shippingPaymentMethod) {
		this.shippingPaymentMethod = shippingPaymentMethod;
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

	public String getRefundStatusCode() {
		return refundStatusCode;
	}

	public void setRefundStatusCode(String refundStatusCode) {
		this.refundStatusCode = refundStatusCode;
	}

	public String getReturnItemReason() {
		return returnItemReason;
	}

	public void setReturnItemReason(String returnItemReason) {
		this.returnItemReason = returnItemReason;
	}

	public String getReturnItemReasonText() {
		return returnItemReasonText;
	}

	public void setReturnItemReasonText(String returnItemReasonText) {
		this.returnItemReasonText = returnItemReasonText;
	}

	public int getReturnRealShipping() {
		return returnRealShipping;
	}

	public void setReturnRealShipping(int returnRealShipping) {
		this.returnRealShipping = returnRealShipping;
	}

	public String getAdminUserName() {
		return adminUserName;
	}

	public void setAdminUserName(String adminUserName) {
		this.adminUserName = adminUserName;
	}

	public String getRemittanceDate() {
		return remittanceDate;
	}

	public void setRemittanceDate(String remittanceDate) {
		this.remittanceDate = remittanceDate;
	}


	public String getPayChangeType() {
		return payChangeType;
	}
	public void setPayChangeType(String payChangeType) {
		this.payChangeType = payChangeType;
	}

	private String userName;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEscrowStatus() {
		return escrowStatus;
	}
	public void setEscrowStatus(String escrowStatus) {
		this.escrowStatus = escrowStatus;
	}



	// [SKC] 배치 처리용 Pagiantion
	private Paging paging;

	public Paging getPaging() {
		return paging;
	}

	public void setPaging(Paging paging) {
		this.paging = paging;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	private boolean isMobile;

	public boolean isMobile() {
		return isMobile;
	}

	public boolean getIsMobile() {
		return isMobile;
	}

	public void setMobile(boolean mobile) {
		isMobile = mobile;
	}

	private String campaignCode;
	public String getCampaignCode() {
		return campaignCode;
	}
	public void setCampaignCode(String campaignCode) {
		this.campaignCode = campaignCode;
	}

	public int getParentItemId() {
		return parentItemId;
	}
	public void setParentItemId(int parentItemId) {
		this.parentItemId = parentItemId;
	}

	public Integer getParentItemSequence() {
		return parentItemSequence;
	}
	public void setParentItemSequence(Integer parentItemSequence) {
		this.parentItemSequence = parentItemSequence;
	}

	public Integer getSetItemSequence() {
		return setItemSequence;
	}

	public void setSetItemSequence(Integer setItemSequence) {
		this.setItemSequence = setItemSequence;
	}

	private boolean downloadExcel;
	public boolean isDownloadExcel() { return downloadExcel; }
	public void setDownloadExcel(boolean downloadExcel) { this.downloadExcel = downloadExcel; }

	// 지자체 코드
	private String shWdr;
	private String shLocgovCode;

	public String getShWdr() {
		return shWdr;
	}

	public void setShWdr(String shWdr) {
		this.shWdr = shWdr;
	}

	public String getShLocgovCode() {
		return shLocgovCode;
	}

	public void setShLocgovCode(String shLocgovCode) {
		this.shLocgovCode = shLocgovCode;
	}

	// 상품명
	private String itemName;

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	// 지자체
	private String upperLocgovCode;	// 시·도
	private String locgovCode;	// 시·군·구

	public String getUpperLocgovCode() {
		return upperLocgovCode;
	}

	public void setUpperLocgovCode(String upperLocgovCode) {
		this.upperLocgovCode = upperLocgovCode;
	}

	public String getLocgovCode() {
		return locgovCode;
	}

	public void setLocgovCode(String locgovCode) {
		this.locgovCode = locgovCode;
	}

	public String getChangeOrderStatus() {
		return changeOrderStatus;
	}

	public void setChangeOrderStatus(String changeOrderStatus) {
		this.changeOrderStatus = changeOrderStatus;
	}

	private String opOrderDefault = "Y";
	private String opOrderClaim = "Y";

	public String getOpOrderDefault() {
		return opOrderDefault;
	}

	public void setOpOrderDefault(String opOrderDefault) {
		this.opOrderDefault = opOrderDefault;
	}

	public String getOpOrderClaim() {
		return opOrderClaim;
	}

	public void setOpOrderClaim(String opOrderClaim) {
		this.opOrderClaim = opOrderClaim;
	}

	public boolean getIsSellerLogin() {
		return SellerUtils.isSellerLogin();
	}

	// 2023. 09. 13 부터는 구매확정, 취소완료, 환불완료 일자 기준 3개월 이내 건만 조회하기 위한 파라미터
	// 2024. 04. 15. 1달 기간 해제 요청으로 변경
	public boolean getIsSelectLimited() {
//		LocalDate date = LocalDate.now();
//		LocalDate limitDate = LocalDate.parse("20230912", DateTimeFormatter.ofPattern("yyyyMMdd"));
//		return date.isAfter(limitDate);
//		return true;
		return false;
	}

	// 상품등록일
	private String createdDate;	// 시·도

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}
}
