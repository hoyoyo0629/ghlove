package saleson.shop.log;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.log.domain.ManagerHist;

@Mapper("managerHistMapper")
public interface ManagerHistMapper {

	/**
	 * 관리자 이력 등록
	 * @param managerHist
	 * @return
	 */
	int insertManagerHist(ManagerHist managerHist);


	/**
	 * 관리자 유저아이디 조회
	 * @param loginId
	 * @return ManagerHist
	 */
	ManagerHist getMangerUserIdByLoginId(String loginId);

}
