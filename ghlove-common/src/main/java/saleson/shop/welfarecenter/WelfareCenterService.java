package saleson.shop.welfarecenter;

import java.util.HashMap;
import java.util.List;

import saleson.shop.welfarecenter.domain.WlfrCntrMng;
import saleson.shop.welfarecenter.support.WlfrCntrMngParam;
import saleson.shop.welfarecenter.domain.WlfrCntrMngManagerRequest;
import saleson.shop.welfarecenter.domain.WlfrCntrMngManagerRequestResult;
import saleson.shop.welfarecenter.support.WlfrCntrMngManagerRequestSearchParam;

public interface WelfareCenterService {

	/**
	 * 로그인 사용자 지자체코드정보 조회
	 * @param userId
	 * @return
	 */
	public String getLocgovCodeByUserId(Long userId);

	/**
	 * 행정복지센터 권한 승인관리 - '거절'
	 * @param managerRequest
	 * @return
	 */
	WlfrCntrMngManagerRequestResult updateManagerRequestReject(WlfrCntrMngManagerRequest wlfrCntrMngManagerRequest);

	/**
	 * 행정복지센터 권한 승인관리 - '승인'
	 * @param managerRequest
	 * @return
	 */
	WlfrCntrMngManagerRequestResult updateManagerRequestApproval(WlfrCntrMngManagerRequest wlfrCntrMngManagerRequest);

	/**
	 * 행정복지센터 권한 수정
	 * @param managerRequest
	 * @return
	 */
	int updateManagerRequest(WlfrCntrMngManagerRequest wlfrCntrMngManagerRequest);

	/**
	 * 행정복지센터 권한 승인관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<WlfrCntrMngManagerRequest> getManagerRequestListByParam(WlfrCntrMngManagerRequestSearchParam searchParam);

	/**
	 * 행정복지센터 권한 승인관리 상세 조회
	 * @param searchParam
	 * @return
	 */
	WlfrCntrMngManagerRequest getManagerRequestDetails(WlfrCntrMngManagerRequestSearchParam searchParam);

	/**
	 * 관리자 권한 승인관리 이력 조회
	 * @param searchParam
	 * @return
	 */
	List<WlfrCntrMngManagerRequest> getWlfrCntrMngHistory(WlfrCntrMngManagerRequestSearchParam searchParam);

	// 행정복지센터 목록 조회
	List<WlfrCntrMng> selectWlfrCntrMngList(WlfrCntrMngParam wlfrCntrMngParam);

	//행정복지센터 상세 조회
	WlfrCntrMng selectwlfrCntrMngDetail(Long pbadmsWlfrCntrId);

	// 행정복지센터 목록 카운트 조회
	int selectWlfrCntrMngListCnt(WlfrCntrMngParam wlfrCntrMngParam);

	// 행정복지센터 등록 페이지 조회
	List<WlfrCntrMng> wlfrCntrMngInfo(WlfrCntrMng wlfrCntrMng);

	// 행정복지센터 등록
	int insertWlfrCntrMng(WlfrCntrMng wlfrCntrMng);

	// 행정복지센터 수정
	int updatetWlfrCntrMng(WlfrCntrMngParam param);

	// 행정복지센터 중복체크
	boolean isDuplicateWlfrCntrMng(WlfrCntrMng wlfrCntrMng);

}
