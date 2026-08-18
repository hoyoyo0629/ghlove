package saleson.shop.order.givepoint;

import java.util.List;

import saleson.model.GCntrUsePoint;
import saleson.shop.give.givepoint.domain.GivePoint;
import saleson.shop.order.givepoint.support.OrderGivePoint;

public interface OrderGivePointService {

	List<OrderGivePoint> getGiveBlcePointListByUserId(long userId);
	
	List<OrderGivePoint> getGiveBlcePointListByUserIdAndLocgov(OrderGivePoint givePoint);
	
	int updateGiveBlcePoint(OrderGivePoint givePoint);
	
	int insertGiveUsePoint(OrderGivePoint gCntrUsePoint);
	
	int updateGiveUsePoint(OrderGivePoint gCntrUsePoint);
	
	List<OrderGivePoint> getGiveUsePoint(OrderGivePoint gCntrUsePoint);
	
	OrderGivePoint getGiveBlcePointListByCntrSn(OrderGivePoint orderGivePoint);
	
	int deleteGiveUsePoint(OrderGivePoint gCntrUsePoint);
	
	List<OrderGivePoint> getCntrPointList(List<OrderGivePoint> usePointList);
	
}
