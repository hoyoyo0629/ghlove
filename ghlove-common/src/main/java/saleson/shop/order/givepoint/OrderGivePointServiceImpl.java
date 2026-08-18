package saleson.shop.order.givepoint;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.common.utils.UserUtils;
import saleson.model.GCntrUsePoint;
import saleson.shop.give.givepoint.domain.GivePoint;
import saleson.shop.order.givepoint.support.OrderGivePoint;

@Service("orderGivePointService")
public class OrderGivePointServiceImpl extends EgovAbstractServiceImpl implements OrderGivePointService {

//    private static final Logger log = LoggerFactory.getLogger(OrderGivePointServiceImpl.class);

    @Autowired
    private OrderGivePointMapper orderGivePointMapper;
    
	@Override
	public List<OrderGivePoint> getGiveBlcePointListByUserId(long userId) {
		return orderGivePointMapper.getGiveBlcePointListByUserId(userId);
	}

	@Override
	public List<OrderGivePoint> getGiveBlcePointListByUserIdAndLocgov(OrderGivePoint orderGivePoint) {
		return orderGivePointMapper.getGiveBlcePointListByUserIdAndLocgov(orderGivePoint);
	}

	@Override
	public int updateGiveBlcePoint(OrderGivePoint orderGivePoint) {
		if (orderGivePoint.getUserId() <= 0) {
			orderGivePoint.setUserId(UserUtils.getUser().getUserId());
		}
		return orderGivePointMapper.updateGiveBlcePoint(orderGivePoint);
	}

	@Override
	public int insertGiveUsePoint(OrderGivePoint orderGivePoint) {
		if (orderGivePoint.getUserId() <= 0) {
			orderGivePoint.setUserId(UserUtils.getUser().getUserId());
		}
		return orderGivePointMapper.insertGiveUsePoint(orderGivePoint);
	}

	@Override
	public int updateGiveUsePoint(OrderGivePoint orderGivePoint) {
		if (orderGivePoint.getUserId() <= 0) {
			orderGivePoint.setUserId(UserUtils.getUser().getUserId());
		}
		return orderGivePointMapper.updateGiveUsePoint(orderGivePoint);
	}

	@Override
	public List<OrderGivePoint> getGiveUsePoint(OrderGivePoint orderGivePoint) {
		return orderGivePointMapper.getGiveUsePoint(orderGivePoint);
	}

	@Override
	public OrderGivePoint getGiveBlcePointListByCntrSn(OrderGivePoint orderGivePoint) {
		return orderGivePointMapper.getGiveBlcePointListByCntrSn(orderGivePoint);
	}

	@Override
	public int deleteGiveUsePoint(OrderGivePoint gCntrUsePoint) {
		return orderGivePointMapper.deleteGiveUsePoint(gCntrUsePoint);
	}

	@Override
	public List<OrderGivePoint> getCntrPointList(List<OrderGivePoint> usePointList) {
		return orderGivePointMapper.getCntrPointList(usePointList);
	}


}
