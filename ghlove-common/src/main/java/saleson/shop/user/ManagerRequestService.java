package saleson.shop.user;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.onlinepowers.framework.security.userdetails.User;

import saleson.shop.notice.domain.ManagerNotice;
import saleson.shop.user.domain.ManagerRequest;
import saleson.shop.user.domain.ManagerRequestResult;
import saleson.shop.user.support.ManagerRequestSearchParam;

public interface ManagerRequestService {

	/**
	 * 관리자 정보 조회
	 * @param loginId
	 * @return
	 */
	User getManagerByLoginId(String loginId);

	/**
	 * 로그인 사용자 관리자 확인
	 * @param loginId
	 * @param password
	 * @return
	 */
	ManagerRequestResult getLoginUserValid(HttpServletRequest request, String loginId, String password);

	/**
	 * 로그인 사용자 관리자 확인
	 * @param loginId
	 * @param password
	 * @return
	 */
	ManagerRequestResult getLoginUserValid(HttpServletRequest request, String loginId, String password, String managerUse);

	/**
	 * 관리자 신청 등록
	 * @param managerRequest
	 * @return
	 */
	int insertManagerRequest(ManagerRequest managerRequest);

	/**
	 * 관리자 권한 승인관리 목록 갯수 조회
	 * @param managerRequestSearchParam
	 * @return
	 */
	int getManagerRequestCountByParam(ManagerRequestSearchParam searchParam);

	/**
	 * 관리자 권한 승인관리 목록 조회
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
	 * 관리자 권한 승인관리 - '거절'
	 * @param managerRequest
	 * @return
	 */
	ManagerRequestResult updateManagerRequestReject(ManagerRequest managerRequest);

	/**
	 * 관리자 권한 승인관리 - '승인'
	 * @param managerRequest
	 * @return
	 */
	ManagerRequestResult updateManagerRequestApproval(ManagerRequest managerRequest);

	/**
	 * 관리자 권한 승인관리 상세 조회 접근 권한 체크
	 * @param searchParam
	 * @return
	 */
	int getManagerRequestDetailsAuthCount(ManagerRequestSearchParam searchParam);

	/**
	 * 회원 정보 조회
	 * @param loginId
	 * @return
	 */
	ManagerRequest getUserInfoByLoginId(String loginId);

	/**
	 * 관리자 권한 승인관리 이력 조회
	 * @param searchParam
	 * @return
	 */
	List<ManagerRequest> getManagerRequestHistory(ManagerRequestSearchParam searchParam);

	List<ManagerNotice> getNoticeList(String type);

	/**
	 * 오프라인 관리자 로그인 정보 안내문
	 * @param User
	 * @return String
	 * */
	String getMsgLoginOFFManager();
}
