package saleson.shop.user;

import java.util.List;

import saleson.shop.user.domain.SleepUser;
import saleson.shop.user.support.SleepUserSearchParam;

public interface SleepUserService {

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

	/**
	 * 휴면회원 해제
	 * @param sleepUser
	 * @return
	 */
	String updateWakeupSleepUser(SleepUser sleepUser);
}
