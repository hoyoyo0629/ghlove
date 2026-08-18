package saleson.shop.user;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import saleson.shop.slave.SlaveSecedeUserMapper;
import saleson.shop.user.domain.SecedeUser;
import saleson.shop.user.support.SecedeUserSearchParam;

@RequiredArgsConstructor
@Service("SecedeUserService")
public class SecedeUserServiceImpl extends EgovAbstractServiceImpl implements SecedeUserService {

//	private final SecedeUserMapper secedeUserMapper;
	private final SlaveSecedeUserMapper slaveSecedeUserMapper;
	
	/**
	 * 회원탈퇴관리 목록 갯수 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public int getSecedeUserCountByParam(SecedeUserSearchParam searchParam) {
		return slaveSecedeUserMapper.getSecedeUserCountByParam(searchParam);
	}
	
	/**
	 * 회원탈퇴관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	@Override
	public List<SecedeUser> getSecedeUserListByParam(SecedeUserSearchParam searchParam) {
		return slaveSecedeUserMapper.getSecedeUserListByParam(searchParam);
	}
	
	/**
	 * 회원탈퇴 상세 조회
	 * @param userId
	 * @return
	 */
	@Override
	public SecedeUser getSecedeUserDetails(Long userId) {
		return slaveSecedeUserMapper.getSecedeUserDetails(userId);
	}
}
