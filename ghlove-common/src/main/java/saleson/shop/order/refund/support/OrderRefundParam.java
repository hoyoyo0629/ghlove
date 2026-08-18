package saleson.shop.order.refund.support;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.onlinepowers.framework.web.domain.SearchParam;

import saleson.common.utils.SellerUtils;

@SuppressWarnings("serial")
public class OrderRefundParam extends SearchParam {

	private String orderCode;
	private int orderSequence;
	private String refundStatusCode;
	private String searchStartDate;
	private String searchEndDate;
	private String searchDateType;
	private String bankName;
	private String bankInName;
	private String virtualNo;
	// 지자체 코드
	private String shWdr;
	private String shLocgovCode;
	private long sellerId;
	private String refundCode;
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankInName() {
		return bankInName;
	}
	public void setBankInName(String bankInName) {
		this.bankInName = bankInName;
	}
	public String getVirtualNo() {
		return virtualNo;
	}
	public void setVirtualNo(String virtualNo) {
		this.virtualNo = virtualNo;
	}
	public String getSearchDateType() {
		return searchDateType;
	}
	public void setSearchDateType(String searchDateType) {
		this.searchDateType = searchDateType;
	}
	public String getSearchEndDate() {
		return searchEndDate;
	}
	public void setSearchEndDate(String searchEndDate) {
		this.searchEndDate = searchEndDate;
	}
	public String getSearchStartDate() {
		return searchStartDate;
	}
	public void setSearchStartDate(String searchStartDate) {
		this.searchStartDate = searchStartDate;
	}
	public String getRefundStatusCode() {

		//if (ObjectUtils.isEmpty(refundStatusCode)) {
		//	return "1";
		//}

		return refundStatusCode;
	}
	public void setRefundStatusCode(String refundStatusCode) {
		this.refundStatusCode = refundStatusCode;
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
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public String getRefundCode() {
		return refundCode;
	}
	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	@Override
	public boolean equals(Object obj) {
		OrderRefundParam orderRefundParam = (OrderRefundParam)obj;
		return getSellerId() == orderRefundParam.getSellerId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(sellerId);
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
