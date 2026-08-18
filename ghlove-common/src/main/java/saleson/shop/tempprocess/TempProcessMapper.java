package saleson.shop.tempprocess;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderPayment;
import saleson.shop.remittance.domain.Remittance;
import saleson.shop.remittance.domain.RemittanceConfirm;
import saleson.shop.remittance.domain.RemittanceConfirmDetail;
import saleson.shop.remittance.support.RemittanceParam;
import saleson.shop.tempprocess.domain.HoldOrderList;
import saleson.shop.tempprocess.domain.HoldOrderListParam;

@Mapper("TempProcessMapper")
public interface TempProcessMapper {
	
	
	/**
	 * 주문 내역
	 * @param orderParam
	 * @return
	 */
	public int getTempAllOrderCountByParamForManager(HoldOrderListParam orderParam);

	/**
	 * 주문 내역
	 * @param orderParam
	 * @return
	 */
	public List<HoldOrderList> getTempAllOrderListByParamForManager(HoldOrderListParam orderParam);
	
	
	public Order getOrderByParam(HoldOrderListParam orderParam);


	/**
	 * 주문 결제 정보 조회
	 * @param orderParam
	 * @return
	 */
	List<OrderPayment> getOrderPaymentListByParam(HoldOrderListParam orderParam);

	
	
	/**
	 * 정산 확정 내역
	 * @param param
	 * @return
	 */
	public List<RemittanceConfirm> getTempRemittanceConfirmListByParamNew(RemittanceParam param);
	
	int getTempRemittanceConfirmCountByParamNew(RemittanceParam param);
	
	/**
	 * 판매자별 정산 대기 목록 리스트 - 상품
	 * @param param
	 * @return
	 */
	List<RemittanceConfirmDetail> getTempRemittanceConfirmDetailList(RemittanceParam param);
	
	/**
	 * 아이디로 정산 정보 조회
	 * @param param
	 */
	Remittance getTempRemittanceInfoById(RemittanceParam param);
	
	
	int getTempRemittanceConfirmDetailCountByParamNew(RemittanceParam param);
	
	int updateTempRemittanceConfirmCheckByParam(RemittanceParam param);
	
	int updateHoldOrderListByParam(HoldOrderListParam param);
	
	int insertHoldToOrderItem(HoldOrderListParam param);
	
	int insertHoldToOrder(HoldOrderListParam param);
	
	int insertHoldToPayment(HoldOrderListParam param);
	
	int insertHoldOrderListLog(HoldOrderListParam param);
	
	HoldOrderList selectHoldOrderStatus(HoldOrderListParam param);
	
	int insertHoldOrderExchangeInfo(HoldOrderList param);
	
	int insertHoldOrderReturnInfo(HoldOrderList param);
	
	int insertHoldOrderRefundInfo(HoldOrderList param);
	
	int insertHoldOrderCancelInfo(HoldOrderList param);
	
}
