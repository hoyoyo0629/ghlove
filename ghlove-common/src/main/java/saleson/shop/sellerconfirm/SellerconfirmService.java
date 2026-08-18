package saleson.shop.sellerconfirm;

import java.util.List;
import saleson.shop.sellerconfirm.support.SellerconfirmParam;
import saleson.shop.order.domain.Order;
import saleson.shop.order.domain.OrderItem;
import saleson.shop.order.domain.OrderList;
import saleson.shop.order.support.OrderParam;
import saleson.shop.remittance.domain.RemittanceConfirm;

public interface SellerconfirmService {
	
	/**
	 * 정산 확정 내역
	 * 
	 * @param param
	 * @return
	 */
	public List<RemittanceConfirm> getSellerconfirmConfirmListByParam(SellerconfirmParam param);
	
	/**
	 * 지자체 지급 처리
	 * @param param
	 */
	public void updateSellerconfirmPayProcess(SellerconfirmParam param);
	
	/**
	 * 관리자 주문 전체 리스트 조회
	 * @param orderParam
	 * @return
	 */
	
	public List<OrderList> getAllSellerConfirmOrderListByParamForManager(OrderParam orderParam);
	
	/**
	 * 기타 상품 정보를 해당 주문상품에 추가
	 * @param orderList
	 */
	void setOrderItemOther(List<OrderList> orderList);
	
	/**
	 * 기타 상품 정보를 해당 주문상품에 추가
	 * @param orderItem
	 */
	void setOrderItemOther(OrderItem orderItem);
	
	/**
	 * 주문 조회
	 * @param orderParam
	 * @return
	 */
	public Order getOrderByParam(OrderParam orderParam);
	
	
}
