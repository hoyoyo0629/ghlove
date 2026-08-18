package saleson.shop.orderagency;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.order.support.OrderParam;
import saleson.shop.orderagency.domain.OrderAgencyLoginConfirmInfo;
import saleson.shop.orderagency.domain.OrderAgencyManagerInfo;
import saleson.shop.orderagency.domain.OrderAgencyOrderListInfo;

@Mapper("orderAgencyMapper")
public interface OrderAgencyMapper {
	
	public void insertAgencyLoginConfirmInfo(OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo);
	
	public void deleteAgencyLoginConfirmInfo(OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo);
	
	public OrderAgencyLoginConfirmInfo selectAgencyLoginConfirmInfo(OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo);
	
	public int updateCntrbtrMobileInfo(OrderAgencyLoginConfirmInfo orderAgencyLoginConfirmInfo);
	
	public OrderAgencyManagerInfo selectOrderAgencyManagerInfo(long managerId);
	
	public List<OrderAgencyOrderListInfo> selectOrderAgencyOrderList(OrderParam orderParam);
	
	public int selectOrderAgencyOrderCnt(OrderParam orderParam);
	
}
