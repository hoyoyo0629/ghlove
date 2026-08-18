package saleson.shop.welfarecenter;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.welfarecenter.domain.WlfrCntrMng;
import saleson.shop.welfarecenter.support.WlfrCntrMngParam;
import saleson.shop.user.domain.ManagerRequest;
import saleson.shop.welfarecenter.domain.WlfrCntrMngManagerRequest;
import saleson.shop.welfarecenter.support.WlfrCntrMngManagerRequestSearchParam;

@Mapper("welfareCenterMapper")
public interface WelfareCenterMapper {

	/**
	 * 행정복지센터 권한 수정
	 * @param ManagerRequest
	 * @return
	 */
	int updateManagerRequest(ManagerRequest param);

	/**
	 * 행정복지센터 권한 승인관리 목록 갯수 조회
	 * @param managerRequestSearchParam
	 * @return
	 */
	int getManagerRequestCountByParam(WlfrCntrMngManagerRequestSearchParam searchParam);

	/**
	 * 행정복지센터 권한 승인관리 목록 조회 (검색조건 포함)
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
	 * 관리자 권한 승인 이력 조회
	 * @param userId
	 * @return
	 */
	List<WlfrCntrMngManagerRequest> getWlfrCntrMngHistory(WlfrCntrMngManagerRequestSearchParam searchParam);

	//행정복지센터 권한 승인 리스트 조회
	List<WlfrCntrMng> selectWlfrCntrMngList(WlfrCntrMngParam wlfrCntrMngParam);

	//행정복지센터 조회
	List<WlfrCntrMng> wlfrCntrMngInfo(WlfrCntrMng wlfrCntrMng);

	//행정복지센터 상세 조회
	WlfrCntrMng selectwlfrCntrMngDetail(Long pbadmsWlfrCntrId);

	//행정복지센터 권한승인 총계 조회
	int selectWlfrCntrMngListCnt(WlfrCntrMngParam wlfrCntrMngParam);

	//행정복지센터 등록
	int insertWlfrCntrMng(WlfrCntrMng wlfrCntrMng);

	//행정복지센터 등록
	int updatetWlfrCntrMng(WlfrCntrMngParam wlfrCntrMngParam);

	//행정복지센터 중복조회
	int isDuplicateWlfrCntrMng(WlfrCntrMng wlfrCntrMng);
}
