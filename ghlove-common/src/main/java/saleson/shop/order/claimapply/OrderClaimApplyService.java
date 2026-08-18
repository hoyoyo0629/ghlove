package saleson.shop.order.claimapply;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import saleson.shop.order.addpayment.domain.OrderAddPayment;
import saleson.shop.order.claimapply.domain.ClaimApply;
import saleson.shop.order.claimapply.domain.OrderCancelApply;
import saleson.shop.order.claimapply.domain.OrderCancelShipping;
import saleson.shop.order.claimapply.domain.OrderExchangeApply;
import saleson.shop.order.claimapply.domain.OrderLog;
import saleson.shop.order.claimapply.domain.OrderReturnApply;
import saleson.shop.order.claimapply.support.ClaimApplyParam;
import saleson.shop.order.claimapply.support.ExchangeApply;
import saleson.shop.order.claimapply.support.ReturnApply;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderShipping;
import saleson.shop.order.support.OrderParam;

public interface OrderClaimApplyService {

	/**
	 * 배송비 재계산 - 사용자
	 * @param claimApply
	 * @return
	 */
	public List<OrderShipping> getReShippingAmountForUser(ClaimApply claimApply);

	/**
	 * 배송비 재계산 - 관리자
	 * @param claimApply
	 */
	public List<OrderShipping> getReShippingAmountForManager(ClaimApply claimApply);

	/**
	 * 처리해야하는 취소 목록
	 * @param claimApplyParam
	 * @return
	 */
	public List<OrderCancelShipping> getActiveCancelListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 주문 상세용 히스토리 (승인대기, 완료, 거절)
	 * @param claimApplyParam
	 * @return
	 */
	public List<OrderCancelApply> getCancelHistoryListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 주문취소 목록
	 * @param claimApplyParam
	 */
	public List<OrderCancelApply> getCancelListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 반품 처리완료 로그 보기
	 * @param claimCode
	 * @return
	 */
	public OrderReturnApply getReturnApplyByClaimCode(String claimCode);

	/**
	 * 교환 처리완료 로그 보기
	 * @param claimCode
	 * @return
	 */
	public OrderExchangeApply getExchangeApplyByClaimCode(String claimCode);


	/**
	 * 처리해야하는 반품 목록
	 * @param claimApplyParam
	 * @return
	 */
	public List<OrderReturnApply> getActiveReturnListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 주문 상세용 히스토리 (승인대기, 완료, 거절)
	 * @param claimApplyParam
	 * @return
	 */
	public List<OrderReturnApply> getReturnHistoryListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 반품 목록
	 * @param claimApplyParam
	 */
	public List<OrderReturnApply> getReturnListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 교환 목록
	 * @param claimApplyParam
	 */
	public List<OrderExchangeApply> getExchangeListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 주문취소 신청
	 * @param claimApply
	 */
	public void insertOrderCancelApply(ClaimApply claimApply);

	/**
	 * 반품 신청
	 * @param returnApply
	 */
	public void insertOrderReturnApply(ReturnApply returnApply);

	/**
	 * 주문취소 처리
	 */
//	public void orderCancelProcess(ClaimApply claimApply);

	/**
	 * 주분 반품 처리
	 * @param claimApply
	 */
	public void orderReturnProcess(ClaimApply claimApply);

	/**
	 * 주문 반품 저장
	 * @param claimApply
	 */
	public void orderReturnSaveProcess(ClaimApply claimApply);

	/**
	 * 주문 반품 미리보기
	 * @param claimApply
	 * @param order
	 * @return
	 */
	public List<OrderAddPayment> orderReturnViewData(ClaimApply claimApply, Order order);

	/**
	 * 주문 교환 처리
	 * @param claimApply
	 */
	public void orderExchangeProcess(ClaimApply claimApply);

	/**
	 * 교환 신청
	 * @param exchangeApply
	 */
	public void insertOrderExchangeApply(ExchangeApply exchangeApply);

	/**
	 * 교환 처리중 목록
	 * @param claimApplyParam
	 */
	public List<OrderExchangeApply> getActiveExchangeListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 주문 상세용 히스토리 (승인대기, 완료, 거절)
	 * @param claimApplyParam
	 * @return
	 */
	public List<OrderExchangeApply> getExchangeHistoryListByParam(ClaimApplyParam claimApplyParam);

	/**
	 * 주문 전체 취소
	 * @param orderParam
	 */
	public void orderCancelAllProcess(OrderParam orderParam, HttpServletRequest request);


	/**
	 * [배치처리용] 주문 전체 취소 - REQUIRES_NEW
	 * @param orderParam
	 */
	void orderCancelAllProcessNewTx(OrderParam orderParam);

	/**
	 * 관리자 주문 상세에서 취소 신청시 환불내역으로 바로보내기 할때 등록된 취소 신청 목록 조회
	 * @param ids
	 * @return
	 */
	public List<OrderCancelApply> getAdminApplyCancelListByIds(String[] ids);

	/**
	 * 교환 목록에 주문 상품 정보 세팅 (사은품, 세트상품...)
	 * @param list
	 */
	void setOrderItemOtherForExchangeApply(List<OrderExchangeApply> list);

	/**
	 * 환불 목록에 주문 상품 정보 세팅 (사은품, 세트상품...)
	 * @param list
	 */
	void setOrderItemOtherForReturnApply(List<OrderReturnApply> list);

	/**
	 * 취소 목록에 주문 상품 정보 세팅 (사은품, 세트상품...)
	 * @param list
	 */
	void setOrderItemOtherForCancelApply(List<OrderCancelApply> list);

	/**
	 * 주문취소 신청(기부포인트)
	 * @param claimApply
	 */
	public void giveGoodsInsertOrderCancelApply(ClaimApply claimApply, boolean isCancel);

	/**
	 * 주문취소 승인 처리
	 */
	public void giveGoodsOrderCancelProcess(ClaimApply claimApply);

	/**
	 * 주문 로그 조회
	 */
	public List<OrderLog> getOrderLogList(ClaimApplyParam claimApplyParam);

}
