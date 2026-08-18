package saleson.shop.slave;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.common.configuration.MapperSlave;
import saleson.shop.user.domain.SleepUser;
import saleson.shop.user.support.SleepUserSearchParam;

@MapperSlave("slaveSleepUserMapper")
public interface SlaveSleepUserMapper {

	/**
	 * 휴면회원관리 목록 갯수 조회  
	 * @param searchParam
	 * @return
	 */
	int getSleepUserCountByParam(SleepUserSearchParam searchParam);

	/**
	 * 휴면회원관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<SleepUser> getSleepUserListByParam(SleepUserSearchParam searchParam);
}