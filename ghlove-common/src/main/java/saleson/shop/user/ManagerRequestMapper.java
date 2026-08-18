package saleson.shop.user;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.security.userdetails.UserRole;

import saleson.shop.notice.domain.ManagerNotice;
import saleson.shop.user.domain.ManagerRequest;
import saleson.shop.user.support.ManagerRequestSearchParam;

@Mapper("managerRequestMapper")
public interface ManagerRequestMapper {

	/**
	 * 관리자 정보 조회 
	 * @param userId
	 * @return
	 */
	User getManagerByLoginId(String loginId);
	
	/**
	 * 관리자 목록 정보 조회
	 * @param managerRequestSearchParam
	 * @return
	 */
	List<ManagerRequest> getManagerRequestList(ManagerRequestSearchParam managerRequestSearchParam);
	
	/**
	 * 관리자 신청 등록
	 * @param managerRequset
	 * @return
	 */
	int insertManagerRequest(ManagerRequest managerRequset);

	/**
	 * 관리자 권한 승인관리 목록 갯수 조회
	 * @param managerRequestSearchParam
	 * @return
	 */
	int getManagerRequestCountByParam(ManagerRequestSearchParam searchParam);

	/**
	 * 관리자 권한 승인관리 목록 조회 (검색조건 포함)
	 * @param searchParam
	 * @return
	 */
	List<ManagerRequest> getManagerRequestListByParam(ManagerRequestSearchParam searchParam);

	/**
	 * 관리자 권한 승인관리 상세 조회
	 * @param searchParam
	 * @return
	 */
	ManagerRequest getManagerRequestDetails(ManagerRequestSearchParam searchParam);
	
	/**
	 * 관리자 신청 수정
	 * @param managerRequset
	 * @return
	 */
	int updateManagerRequest(ManagerRequest managerRequset);

	/**
	 * 관리자 등록 (사용자 정보 동기화)
	 * @param managerRequest
	 * @return
	 */
	int insertManagerByUserSync(ManagerRequest managerRequest);

	/**
	 * 관리자 권한 승인관리 상세 조회 접근 권한 체크 
	 * @param searchParam
	 * @param requestContext 
	 * @return
	 */
	int getManagerRequestDetailsAuthCount(ManagerRequestSearchParam searchParam);

	/**
	 * 비밀번호 변경 타입 조회 
	 * @param userId
	 * @return
	 */
	String getPasswordChangeTypeByUserId(long userId);

	/**
	 * 비밀번호 실패 카운트 증가
	 * @param userId
	 * @return
	 */
	int updateManagerLoginFailCount(long userId);

	/**
	 * 회원 정보 조회
	 * @param loginId
	 * @return
	 */
	ManagerRequest getUserInfoByLoginId(String loginId);

	/**
	 * 사용자 상태값 확인
	 * @param userId
	 * @return
	 */
	String getUserStatusCodeByUserId(Long userId);
	
	/**
	 * 유저 권한 가져오기
	 * @param userId
	 * @return
	 */
	List<UserRole> getUserRole(Long userId);
	
	/**
	 * 관리자 권한 승인 이력 조회
	 * @param userId
	 * @return
	 */
	List<ManagerRequest> getManagerRequestHistory(ManagerRequestSearchParam searchParam);
	
	List<ManagerNotice> getNoticeList(String type);
}
