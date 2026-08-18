package saleson.shop.user;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.user.domain.SleepUser;
import saleson.shop.user.support.SleepUserSearchParam;

@Mapper("sleepUserMapper")
public interface SleepUserMapper {

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