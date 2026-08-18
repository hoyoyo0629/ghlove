package saleson.shop.order;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.common.notification.domain.CampaignStatistics;
import saleson.common.notification.support.StatisticsParam;
import saleson.common.opmanager.count.OpmanagerCount;
import saleson.seller.main.domain.Seller;
import saleson.shop.item.domain.Item;
import saleson.shop.item.domain.ItemOption;
import saleson.shop.order.domain.Buy;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.domain.BuyPayment;
import saleson.shop.order.domain.Buyer;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderAgencyOrderLog;
import saleson.shop.order.domain.OrderCancelApply;
import saleson.shop.order.domain.OrderCount;
import saleson.shop.order.domain.OrderExchangeApply;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderList;
import saleson.shop.order.domain.OrderLog;
import saleson.shop.order.domain.OrderPayment;
import saleson.shop.order.domain.OrderPgData;
import saleson.shop.order.domain.OrderReturnApply;
import saleson.shop.order.domain.OrderSendMessageLog;
import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.order.domain.ReCalculationForItem;
import saleson.shop.order.domain.ReCalculationForShipping;
import saleson.shop.order.domain.Receiver;
import saleson.shop.order.domain.Shipping;
import saleson.shop.order.domain.ShippingCoupon;
import saleson.shop.order.givepoint.support.OrderGivePoint;
import saleson.shop.order.support.OrderParam;
import saleson.shop.order.support.StockDeduction;
import saleson.shop.order.support.StockRestoration;
import saleson.shop.point.domain.OrderPoint;

@Mapper("orderMapper")
public interface OrderMapper {

	int getOrderCountByParamForManager(OrderParam orderParam);

	List<OrderList> getOrderListByParamForManager(OrderParam orderParam);

	int getAllOrderCountByParamForManager(OrderParam orderParam);

	List<OrderList> getAllOrderListByParamForManager(OrderParam orderParam);


	/**
	 * 도서산간, 제주 지역 검색
	 * @param zipcode
	 * @return
	 */
	String getIslandTypeByZipcode(String zipcode);

	/**
	 * 결제 정보 조회
	 * @param orderParam
	 * @return
	 */
	OrderPayment getOrderPaymentByParam(OrderParam orderParam);

	/**
	 * 주문 카운트
	 * @param orderParam
	 * @return
	 */
	int getOrderCountByParam(OrderParam orderParam);

	/**
	 * 주문 조회
	 * @param orderParam
	 * @return
	 */
	Order getOrderByParam(OrderParam orderParam);

	/**
	 * 주문 내역
	 * @param orderParam
	 * @return
	 */
	List<Order> getOrderListByParam(OrderParam orderParam);


	/**
	 * 주문(세트상품) 내역
	 * @param orderParam
	 * @return
	 */
	List<OrderItem> getOrderItemSetList(OrderParam orderParam);


	/**
	 * 주문 결제 정보 조회
	 * @param orderParam
	 * @return
	 */
	List<OrderPayment> getOrderPaymentListByParam(OrderParam orderParam);

	/**
	 * 배송지 정보 조회
	 * @param orderParam
	 * @return
	 */
	OrderShippingInfo getOrderShippingInfoByParam(OrderParam orderParam);

	/**
	 * 주문상품 단일 Row 조회
	 * @param orderParam
	 * @return
	 */
	OrderItem getOrderItemByParam(OrderParam orderParam);

	/**
	 * 배송 정보를 조회
	 * @param orderParam
	 * @return
	 */
	Buy getOrderTemp(OrderParam orderParam);

	/**
	 * 실제 주문 결제 방식을 조회 - 임시저장 데이터
	 * @param orderParam
	 * @return
	 */
	List<BuyPayment> getOrderPaymentBuyTempList(OrderParam orderParam);

	/**
	 * 실제 주문 상품을 조회
	 * @param orderParam
	 * @return
	 */
	List<Receiver> getOrderShippingBuyTempList(OrderParam orderParam);

	/**
	 * 실제 주문 상품을 조회
	 * @param orderParam
	 * @return
	 */
	List<BuyItem> getOrderBuyItemTempList(OrderParam orderParam);

	/**
	 * 실제 주문 세트상품을 조회
	 * @param orderParam
	 * @return
	 */
	List<BuyItem> getOrderBuyItemSetTempList(OrderParam orderParam);

	/**
	 * 사용자 주문 상품 임시 데이터 조회
	 * @param orderParam
	 * @return
	 */
	List<BuyItem> getOrderItemTempList(OrderParam orderParam);

	/**
	 * 사용자 주문 세트상품 임시 데이터 조회
	 * @param orderParam
	 * @return
	 */
	List<BuyItem> getOrderItemSetTempList(OrderParam orderParam);


	/**
	 * 주문 전체 카운트 조회
	 * @return
	 */
	List<OrderCount> getOrderCountListByParam(OrderParam orderParam);

	/**
	 * 주문취소 대기목록 가져오기 -Batch
	 * @param batchKey
	 * @return
	 */
	OrderCancelApply getOrderCancelBatchTargetByBatchKey(String batchKey);

	/**
	 * 환불 승인대기 상품 가져오기 - Batch
	 * @param batchKey
	 * @return
	 */
	OrderReturnApply getOrderReturnBatchTargetByBatchKey(String batchKey);

	/**
	 * 주문 임시 저장 시점에 주문 결제 타입을 저장
	 * @param buyPayment
	 */
	void insertOrderPaymentBuyTemp(BuyPayment buyPayment);

	/**
	 * 주문 임시 저장시점에 주문하려고 하는 배송정보를 저장
	 * @param receiver
	 */
	void insertOrderShippingBuyTemp(Receiver receiver);

	/**
	 * 주문 임시 저장시점에 주문하려고 하는 상품을 다른 테이블로 옮김
	 * @param buyItem
	 */
	void insertOrderItemBuyTemp(BuyItem buyItem);

	/**
	 * 주문 임시 저장시점에 주문하려고 하는 상품을 다른 테이블로 옮김(에스크로 사용시)
	 * @param buyItem
	 */
	void insertOrderItemBuyTempForEscrow(BuyItem buyItem);

	/**
	 * 주문 임시 저장시점에 주문하려고 하는 세트상품을 다른 테이블로 옮김
	 * @param buyItem
	 */
	void insertOrderItemSetBuyTemp(BuyItem buyItem);

	/**
	 * 배송비 정보 등록
	 * @param shipping
	 */
	void insertOrderShipping(Shipping shipping);

	/**
	 * 배송지 정보
	 * @param orderShippingInfo
	 */
	void insertOrderShippingInfo(OrderShippingInfo orderShippingInfo);

	/**
	 * 오프라인 배송지 정보
	 * @param orderShippingInfo
	 */
	void insertOffOrderShippingInfo(OrderShippingInfo orderShippingInfo);

	List<OrderGivePoint> getGiveBlcePointListByUserId(long userId);

	/**
	 * 주문정보 저장
	 * @param order
	 */
	void insertOrder(Buyer buyer);

	/**
	 * 오프라인 주문정보 저장
	 * @param order
	 */
	void insertOffOrder(Buyer buyer);

	/**
	 * 주문상품 정보 저장
	 * @param buyItem
	 */
	void insertOrderItem(BuyItem buyItem);

	/**
	 * 주문 세트상품 정보 저장
	 * @param buyItem
	 */
	void insertOrderItemSet(BuyItem buyItem);


	/**
	 * 주문 수량 수정
	 * @param orderItem
	 * @return
	 */
	int updateOrderItemQuantity(OrderItem orderItem);

	/**
	 * 결제 정보
	 * @param orderItemPayment
	 */
	void insertOrderPayment(OrderPayment orderPayment);

	/**
	 * PG 결제 정보
	 * @param orderPgData
	 */
	void insertOrderPgData(OrderPgData orderPgData);

	/**
	 * 주문 상품 임시 테이블 등록
	 * @param buyItem
	 */
	void insertOrderItemTemp(BuyItem buyItem);

	/**
	 * 주문 세트상품 임시 테이블 등록
	 * @param buyItem
	 */
	void insertOrderItemSetTemp(BuyItem buyItem);

	/**
	 * 배송비 할인 쿠폰 임시 데이터 조회
	 * @param orderParam
	 * @return
	 */
	List<ShippingCoupon> getOrderShippingCouponBuyTemp(OrderParam orderParam);

	/**
	 * 배송비 할인쿠폰 임시 테이블 등록
	 * @param shippingCoupon
	 */
	void insertOrderShippingCouponBuyTemp(ShippingCoupon shippingCoupon);

	/**
	 * 주문 임시 저장
	 * @param orderInfo
	 */
	void insertOrderTemp(Buy buy);

	/**
	 * 환불 배치 등록 - Batch
	 * @param batchKey
	 * @return
	 */
	int insertOrderReturnTarget(String batchKey);


	/**
	 * 주문 상품별 적립금 부여내역 저장
	 * @param orderItemId
	 */
	int updateEarnPoint(OrderPoint orderPoint);




	/**
	 * 무통장 입금(가상계좌) 입금확인 취소
	 * @param orderParam
	 */
	int updateConfirmationOfPaymentCancelStep1(OrderParam orderParam);
	int updateConfirmationOfPaymentCancelStep2(OrderParam orderParam);
	int updateConfirmationOfPaymentCancelStep3(OrderParam orderParam);



	/**
	 * 환불정보 기록 OP_ORDER_PAYMENT - Batch
	 * @param orderPayment
	 * @return
	 */
	int updateOrderPayment(OrderPayment orderPayment);

	/**
	 * 주문취소 처리 실패 메시지 등록 - Batch
	 * @param map
	 */
	void updateBatchTargetErrorMessage(HashMap<String, Object> map);

	/**
	 * 주문취소 완료 정보 기록 - Batch
	 * @param orderShippingId
	 */
	void updateCancelStatus(Order orderShipping);

	/**
	 * 환불 완료 정보 기록 - Batch
	 * @param orderItem
	 */
	void updateReturnStatus(OrderItem orderItem);


	void deleteOrderPaymentBuyTemp(Buy buy);

	/**
	 * 주문 상품 임시 테이블 삭제
	 * @param orderParam
	 */
	void deleteOrderItemTemp(OrderParam orderParam);

	/**
	 * 주문 세트상품 임시 테이블 삭제
	 * @param orderParam
	 */
	void deleteOrderItemSetTemp(OrderParam orderParam);

	/**
	 * 기존 주문 임시 데이터 삭제
	 * @param buy
	 */
	void deleteOrderTemp(Buy buy);

	/**
	 * 배송지 임시정보 삭제
	 * @param buy
	 */
	void deleteOrderShippingBuyTemp(Buy buy);

	/**
	 * 주문 상품 삭제
	 * @param buy
	 */
	void deleteOrderItemBuyTemp(Buy buy);

	/**
	 * 주문 세트상품 삭제
	 * @param buy
	 */
	void deleteOrderItemSetBuyTemp(Buy buy);

	/**
	 * 배치정보 삭제 - Batch
	 * @param batchKey
	 */
	void deleteBatchTarget(String batchKey);

	/**
	 * 상품재고 차감 (item)
	 * @param stockDeduction
	 */
	void updateStockDeductionForItem(StockDeduction stockDeduction);

	/**
	 * 상품재고 차감 (ItemOption)
	 * @param stockDeduction
	 */
	void updateStockDeductionForOption(StockDeduction stockDeduction);

	/**
	 * 상품재고 복구 (item)
	 * @param stockRestoration
	 */
	void updateStockRestorationForItem(StockRestoration stockRestoration);

	/**
	 * 상품재고 복구 (ItemOption)
	 * @param stockRestoration
	 */
	void updateStockRestorationForOption(StockRestoration stockRestoration);

	/**
	 * 주문로그 입력
	 * @param orderLog
	 */
	void insertOrderLog(OrderLog orderLog);

	/**
	 * 주문처리시 관리자 업데이트
	 * @param param
	 */
	void updateOrderItemByAdminUserName (HashMap<String, Object> param);

	/**
	 * 주문정보 로그 조회
	 * @param orderParam
	 * @return
	 */
	List<OrderLog> getOrderLogListByParam (OrderParam orderParam) ;

	/**
	 * 환불 되지 않은 상품들 조회 - 배송 정책별
	 * @param orderParam
	 * @return
	 */
	Order getActiveItemsByParam(OrderParam orderParam);

	/**
	 * 배송 정책 삽입 - CancelBatch에서만 사용
	 * @param reCalculationForShipping
	 */
	int insertOrderShippingForCancelBatch(ReCalculationForShipping reCalculationForShipping);

	/**
	 * 배송 정책 수정 - CancelBatch에서만 사용
	 * @param reCalculationForShipping
	 */
	int updateOrderShippingForCancelBatch(ReCalculationForShipping reCalculationForShipping);

	/**
	 * 주문서 삽입 - CancelBatch에서만 사용
	 * @param reCalculation
	 * @return
	 */
	int insertOrderItemForCancelBatch(ReCalculationForItem reCalculationForItem);

	/**
	 * 주문서 수정 - CancelBatch에서만 사용
	 * @param orderItem
	 */
	int updateOrderItemForCancelBatch(ReCalculationForItem reCalculationForItem);

	/**
	 * 주문서 삽입 - ReturnBatch에서만 사용
	 * @param reCalculation
	 * @return
	 */
	int insertOrderItemForReturnBatch(ReCalculationForItem reCalculationForItem);

	/**
	 * 주문서 수정 - ReturnBatch에서만 사용
	 * @param orderItem
	 */
	int updateOrderItemForReturnBatch(ReCalculationForItem reCalculationForItem);

	/**
	 * 주문 교환정보 가지고 오기
	 * @param orderItemId
	 * @return
	 */
	OrderExchangeApply getOrderExchangeApplyByOrderItemId(HashMap<String, Integer> param);


	/**
	 * 배송정보 수정
	 * @param orderShippingInfo
	 */
	void updateOrderShippingInfo(OrderShippingInfo orderShippingInfo);

	/**
	 * 관리자 메모 수정
	 * @param order
	 */
	void updateAdminMemo(Order order);

	/**
	 * 주문관련 메시지(메일/SMS) 발송된적 있는지 확인용
	 * @param orderSendMessageLog
	 */
	void insertOrderSendMessageLog(OrderSendMessageLog orderSendMessageLog);

	/**
	 * 주문관련 메시지(메일/SMS) 발송된적 있는지 확인용
	 * @param orderSendMessageLog
	 */
	int getOrderSendMessageLogCount(OrderSendMessageLog orderSendMessageLog);

	/** 구매확정 확인 */
	int getOrderItemCntForReview(OrderItem orderItem);

	/**
	 * 주문전체 취소
	 * @param orderParam
	 */
	void updateOrderCancelAll(OrderParam orderParam);

	/**
	 * 환불정보조회
	 * @param orderParam
	 */
	Order getOrderReturnInfo(OrderParam orderParam);

	/**
	 * 환불정보 업데이트
	 * @param orderParam
	 */
	void updateOrderReturnInfo(OrderParam orderParam);

	/**
	 * 포인트 회수처리 기록
	 * @param orderItem
	 */
	void updateReturnPointFlag(OrderItem orderItem);

	/**
	 * 주문상태별 개수
	 * @param userId
	 * @return
	 */
	OrderCount getOrderCountsByUserId(long userId);

	/**
	 * 이상우 [2017-05-11 추가]
	 * 관리자메인 배송출고, 교환회수, 반품회수 지연 카운트
	 * @return
	 */
	List<OpmanagerCount> getOpmanagerShippingDelayCountAll(HashMap<String, Object> map);

	/**
	 * Kspay 부분취소시 일련번호 갱신
	 * @author 손준의 [2017-05-25]
	 * @param orderPgData
	 */
	void updateOrderPgData(OrderPgData orderPgData);

	/**
	 * 임시저장한 주문상품 정보중에서 에스크로 사용여부 가져오기.
	 * @param orderCode
	 * @return
	 */
	String getOrderItemTempByEscrow(String orderCode);

	/**
	 * 이니시스 배송등록 요청을 위한 정보 가져오기.
	 * @param orderParam
	 * @return
	 */
	HashMap<String, Object> getInicisDeliveryInfoByParam(OrderParam orderParam);

	/**
	 * PG거래번호 가져오기
	 * @param orderCode
	 * @return
	 */
	String getTidByParam(String orderCode);

	/**
	 * 주문상품의 에스크로 사용여부 가져오기.
	 * @param orderCode
	 * @return
	 */
	String getOrderItemByEscrow(String orderCode);

	/**
	 * 에스크로 상태 갱신
	 * @param orderParam
	 */
	void updateEscrowStatus(OrderParam orderParam);

	/**
	 * 주문상품정보리스트 가져오기.
	 * @param orderParam
	 * @return
	 */
	List<OrderItem> getOrderItemListByParam(OrderParam orderParam);

	/**
	 * 주문번호로 구매자 Email조회
	 * @param orderParam
	 * @return
	 */
	String getEmailByOrderCode(OrderParam orderParam);

	/**
	 * 해당 주문코드가 OP_ORDER_TEMP 에 존재 하는지 조회
	 * @param orderCode
	 * @return
	 */
	int getOrderCodeCountForOrderTemp(String orderCode);

    /**
     * 취소하지 않고 남은 주문상품 카운트
     * @param cancelApply
     * @return
     */
    int getOrderItemCountForCancel(saleson.shop.order.claimapply.domain.OrderCancelApply cancelApply);

	/**
	 * 주문번호로 OP_ORDER_PG_DATA 조회
	 * @param orderCode
	 * @return
	 */
	OrderPgData getOrderPgDataByOrderCode(String orderCode);


	/**
	 * 캠페인별 주문 통계 리스트 조회
	 * @param statisticsParam
	 * @return
	 */
	List<CampaignStatistics> getCampaignOrderStatisticsListByParam(StatisticsParam statisticsParam);

	/**
	 * 캠페인, 사용자별 주문 통계 리스트 조회
	 * @param statisticsParam
	 * @return
	 */
	List<CampaignStatistics> getCampaignOrderStatisticsListByParamForUser(StatisticsParam statisticsParam);

	/**
	 * 세트상품 주문 조회
	 * @param orderCodes
	 * @return
	 */
	List<OrderItem> getOrderItemSetListByOrderCodes(String[] orderCodes);

    /**
     * orderTemp 삭제 배치
     */
    void deleteOrderTempForBatch(int retentionPeriod);

    /**
     * orderItemBuyTemp 삭제 배치
     */
    void deleteOrderItemBuyTempForBatch(int retentionPeriod);

    /**
     * orderPaymentBuyTemp 삭제 배치
     */
    void deleteOrderPaymentBuyTempForBatch(int retentionPeriod);

    /**
     * orderShippingBuyTemp 삭제 배치
     */
    void deleteOrderShippingBuyTempForBatch(int retentionPeriod);

	/**
	 * 주문번호로 구매자 정보 조회
	 * @param orderCode
	 * @return
	 */
	Buyer getBuyerByOrderCode(String orderCode);

	/**
	 * 사용자 아이디로 주문 상품 건수 조회
	 * @param orderCode
	 * @return
	 */
	long getOrderItemCntForMyPage(long userId);

	/**
	 * 사용자 아이디로 주문 클레임 상품 건수 조회
	 * @param orderCode
	 * @return
	 */
	long getOrderItemClaimCntForMyPage(long userId);

	/**
	 * 재고 또는 옵션재고가 모두 0인지 확인
	 * @param itemId
	 * @return
	 */
	Item getSoldoutItemCheckData(long itemId);

	/**
	 * 주문 취소 상태로 변경
	 * @param orderParam
	 */
	void updateOrderCancel(OrderParam orderParam);

	/**
	 * 주문 번호 채번
	 */
	long getOrderCodeNum();

	/**
	 * 대행주문 로그 추가
	 */
	void insertOrderAgencyOrderLog(OrderAgencyOrderLog orderAgencyOrderLog);

	/**
	 * (회원 CI 정보로 회원 정보 조회 - 단건)
	 * @param mbrCi
	 * @return
	 * @throws Exception
	 */
	HashMap<String, Object> getUserByMberCi(String mbrCi) throws Exception;

	/**
	 * 상품ID로 상품을 조회한다.
	 * @param itemId
	 * @return
	 */
	Item getItemById(int itemId);

	List<ItemOption> getItemOptionList(int itemId);

	/**
	 * 판매자 ID로 판매자 정보 조회.
	 * @param sellerId
	 * @return
	 */
	Seller getSellerById(long sellerId);

	/**
	 * 상품코드로 상품을 조회한다.
	 * @param itemUserCode
	 * @return
	 */
	Item getItemByItemUserCode(String itemUserCode);

	/**
	 * 주문번호 기준 상품들의 모바일 여부 조회.
	 * @param orderParam
	 * @return
	 */
	List<String> getIsMobiles(OrderParam orderParam);
}
