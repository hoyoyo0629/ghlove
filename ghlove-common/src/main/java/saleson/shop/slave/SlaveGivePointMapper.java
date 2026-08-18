package saleson.shop.slave;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.common.configuration.MapperSlave;
import saleson.shop.give.givepoint.domain.GivePoint;

@MapperSlave("slaveGivePointMapper")
public interface SlaveGivePointMapper {

	// 기부 포인트현황 합계
	GivePoint getGivePointSum(GivePoint givePoint);

	// 기부 포인트현황 목록 count
	int getGivePointListCount(GivePoint givePoint);

	// 기부 포인트현황 목록
	List<GivePoint> getGivePointList(GivePoint givePoint);
	
	// 기부 포인트현황 상세 누적합계
	GivePoint getGivePointDetail(GivePoint givePoint);

	// 기부 포인트현황 상세 검색합계
	GivePoint getGivePointDetailSum(GivePoint givePoint);

	// 기부 포인트현황 상세 목록 count
	int getGivePointDetailListCount(GivePoint givePoint);

	// 기부 포인트현황 상세 목록
	List<GivePoint> getGivePointDetailList(GivePoint givePoint);

	// 회원 지자체 조회
	HashMap<String, Object> getLocgovCode(long userId);
	
	// 지자체 기부 포인트현황 목록 count(년도포함)
	int getGivePointLocgovListCount(GivePoint givePoint);

	// 지자체 기부 포인트현황 목록(년도포함)
	List<GivePoint> getGivePointLocgovList(GivePoint givePoint);

}
