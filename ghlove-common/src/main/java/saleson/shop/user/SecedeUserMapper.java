package saleson.shop.user;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.user.domain.SecedeUser;
import saleson.shop.user.support.SecedeUserSearchParam;

@Mapper("secedeUserMapper")
public interface SecedeUserMapper {

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
