package saleson.shop.order.givepoint;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.offgive.support.OffStockInfo;
import saleson.shop.order.domain.BuyItem;
import saleson.shop.order.givepoint.support.OrderGivePoint;

@Mapper("orderGivePointMapper")
public interface OrderGivePointMapper {

	List<OrderGivePoint> getGiveBlcePointListByUserId(long userId);

	List<OrderGivePoint> getGiveBlcePointListByUserIdAndLocgov(OrderGivePoint orderGivePoint);
//	List<OrderGivePoint> getGiveBlcePointListByUserIdAndLocgovByOff(OrderGivePoint orderGivePoint);

	int updateGiveBlcePoint(OrderGivePoint orderGivePoint);

	int insertGiveUsePoint(OrderGivePoint orderGivePoint);

	int updateGiveUsePoint(OrderGivePoint orderGivePoint);

	List<OrderGivePoint> getGiveUsePoint(OrderGivePoint orderGivePoint);

	OrderGivePoint getGiveBlcePointListByCntrSn(OrderGivePoint orderGivePoint);

	int deleteGiveUsePoint(OrderGivePoint orderGivePoint);

	List<OrderGivePoint> getCntrPointList(List<OrderGivePoint> usePointList);

	OrderGivePoint getGivePayOrder(String orderCode);

	List<OffStockInfo> selectItemStockInfo(String orderCode);

	int updateItemStock(OffStockInfo offStockInfo);

	int updateItemOptionStock(OffStockInfo offStockInfo);

	List<BuyItem> selectBuyItemList(String orderCode);

	List<OffStockInfo> selectOffOrderList();

	int updateOrderOffProcYn(String orderCode);

}
