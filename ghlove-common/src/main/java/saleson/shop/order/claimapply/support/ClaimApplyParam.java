package saleson.shop.order.claimapply.support;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.util.ObjectUtils;

import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.utils.CommonUtils;
import saleson.common.utils.SellerUtils;

@SuppressWarnings("serial")
public class ClaimApplyParam extends SearchParam {

	private String orderCode;
	private int orderSequence;
	private int itemSequence;
	private int setItemSequence;
	private long sellerId;

	private String searchStartDate;
	private String searchEndDate;
	private String deliveryType;
	private String shipmentReturnType;

	// 지자체 코드
	private String shWdr;
	private String shLocgovCode;

	public String getShipmentReturnType() {

		if (ObjectUtils.isEmpty(shipmentReturnType)) {
			return "";
		}

		return shipmentReturnType;
	}
	public void setShipmentReturnType(String shipmentReturnType) {
		this.shipmentReturnType = shipmentReturnType;
	}
	private String[] claimStatus;
	public String[] getClaimStatus() {

		if (claimStatus == null) {
			return new String[] {"01", "10"};
		}

		return CommonUtils.copy(claimStatus);
	}
	public void setClaimStatus(String[] claimStatus) {
		this.claimStatus = CommonUtils.copy(claimStatus);
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
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
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

	public int getSetItemSequence() {
		return setItemSequence;
	}

	public void setSetItemSequence(int setItemSequence) {
		this.setItemSequence = setItemSequence;
	}

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
}
