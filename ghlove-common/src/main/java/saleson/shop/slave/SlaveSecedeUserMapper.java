package saleson.shop.slave;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.common.configuration.MapperSlave;
import saleson.shop.user.domain.SecedeUser;
import saleson.shop.user.support.SecedeUserSearchParam;

@MapperSlave("slaveSecedeUserMapper")
public interface SlaveSecedeUserMapper {

	/**
	 * 회원탈퇴관리 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	int getSecedeUserCountByParam(SecedeUserSearchParam searchParam);

	/**
	 * 회원탈퇴관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<SecedeUser> getSecedeUserListByParam(SecedeUserSearchParam searchParam);

	/**
	 * 회원탈퇴 상세 조회
	 * @param userId
	 * @return
	 */
	SecedeUser getSecedeUserDetails(Long userId);
}
