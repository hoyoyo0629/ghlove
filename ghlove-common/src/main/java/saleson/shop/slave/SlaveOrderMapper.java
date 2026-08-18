package saleson.shop.slave;

import saleson.common.configuration.MapperSlave;
import saleson.shop.order.domain.*;
import saleson.shop.order.support.OrderParam;

import java.util.List;

@MapperSlave("slaveOrderMapper")
public interface SlaveOrderMapper {

	int getAllOrderCountByParamForManager(OrderParam orderParam);

	List<OrderList> getAllOrderListByParamForManager(OrderParam orderParam);

	/**
	 * 관리자 오프라인 주문 전체 건수 조회
	 * @param orderParam
	 * @return
	 */
	int getOfflineAllOrderCountByParamForManager(OrderParam orderParam);

	/**
	 * 관리자 오프라인 주문 전체 조회
	 * @param orderParam
	 * @return
	 */
	List<OrderList> getOfflineAllOrderListByParamForManager(OrderParam orderParam);

}
