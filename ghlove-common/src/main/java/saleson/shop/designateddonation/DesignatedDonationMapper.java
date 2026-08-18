package saleson.shop.designateddonation;

import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.domain.DesignatedDonationNotice;
import saleson.shop.designateddonation.domain.DesignatedPart;
import saleson.shop.designateddonation.domain.DsgnCntrCnttImgDesc;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequest;
import saleson.shop.designateddonation.domain.DsgncntrConfirmLog;
import saleson.shop.designateddonation.domain.DsgncntrConfirmLogParam;
import saleson.shop.designateddonation.domain.DesignatedStat;
import saleson.shop.designateddonation.domain.PrjImage;
import saleson.shop.designateddonation.domain.PrjImageExplain;
import saleson.shop.designateddonation.domain.PrjNoticeFile;
import saleson.shop.designateddonation.domain.PrjNoticeImageExplain;
import saleson.shop.designateddonation.support.DesignatedCntr;
import saleson.shop.designateddonation.support.DsgnCntrManagerRequestSearchParam;
import saleson.shop.designateddonation.support.DesignatedDonationNoticeParam;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.designateddonation.support.DesignatedPartParam;
import saleson.shop.designateddonation.support.LocgovSearchParam;

@Mapper("designatedDonationMapper")
public interface DesignatedDonationMapper {

	/**
	 *
	 * @param prjImage
	 */
	int insertDesignatedDonationInfo(DesignatedDonation designatedDonation);

	/**
	 *
	 * @param prjImage
	 */
	int updateDesignatedDonationInfo(DesignatedDonation designatedDonation);

	/**
	 *
	 * @param prjImage
	 */
	List<DesignatedDonation> selectDesignatedDonationList(DesignatedDonationSearchParam designatedDonationSearchParam);

	/**
	 *
	 * @param prjImage
	 */
	int selectDesignatedDonationListCount(DesignatedDonationSearchParam designatedDonationSearchParam);


	/**
	 *
	 * @param prjImage
	 */
	DesignatedDonation selectDesignatedDonationInfo(long prjId);

	/**
	 *
	 * @param prjImage
	 */
	int insertImgDescList(List<DsgnCntrCnttImgDesc> imgDescList);

	/**
	 *
	 * @param prjImage
	 */
	DesignatedDonation selectDesignatedDonationDetail(DesignatedDonationSearchParam designatedDonationSearchParam);

	/**
	 *
	 * @param prjImage
	 */
	int getMaxOrderingOfPrjImageByPrjId(long prjId);

	/**
	 *
	 * @param prjImage
	 */
	List<DesignatedCntr> selectDesignatedCntrList(DesignatedDonationSearchParam designatedDonationSearchParam);

	/**
	 *
	 * @param prjImage
	 */
	int selectDesignatedCntrCnt(DesignatedDonationSearchParam designatedDonationSearchParam);

	/**
	 * 지정기부 접근성 이미지 설명을 조회한다.
	 * @param itemId
	 * @return
	 */
	List<PrjImageExplain> selectImgDescListByPrjId(long prjId);

	/**
	 * 지정기부 접근성 이미지 설명을 등록한다.
	 * @param designatedDonation
	 */
	int insertImgDescList(DesignatedDonation designatedDonation);

	/**
	 * 지정기부 이미지 조회
	 * @param prjId
	 */
	List<PrjImage> selectImgListByPrjId(long prjId);

	/**
	 * 지정기부 이미지 업데이트
	 * @param designatedDonation
	 */
	int updatePrjImageName(DesignatedDonation designatedDonation);

	/**
	 * 단일 지정기부 이미지 조회 by dsgncntrPrjImageId
	 * @param dsgncntrPrjImageId
	 */
	PrjImage selectPrjImageByImageId(long dsgncntrPrjImageId);

	/**
	 * 단일 지정기부 이미지 삭제 by dsgncntrPrjImageId
	 * @param dsgncntrPrjImageId
	 */
	int deleteImgByImageId(long dsgncntrPrjImageId);

	/**
	 * 지정기부 이미지 등록
	 * @param prjImage
	 */
	int insertPrjImage(PrjImage prjimage);


	/**
	 * 지정기부 이미지를 삭제한다
	 * @param item
	 */
	int deleteImg(long prjId);


	/**
	 * 지정기부 접근성 이미지 설명을 조회한다.
	 * @param prjImage
	 */
	List<DsgnCntrCnttImgDesc> selectImgDescList(long prjId);


	/**
	 * 지정기부 접근성 이미지 설명을 제거한다.
	 * @param prjId
	 */
	int deleteImgDescListByPrjId(long prjId);

	/**
	 * 지정기부ID에 설정되는 지정기부 접근성 이미지 설명 정보를 삭제한다.
	 * @param prjId
	 */
	//void deleteImgDescList(int prjId);

	/**
	 * 지정기부 리스트에서 상태값 변경한다
	 * @param prjId
	 */
	int updatePrjLabelByListParam(DesignatedDonation designatedDonation);

	// 지지정기부 지자체별 통계 집계
	DesignatedStat selectDesignatedLocgovStat(LocgovSearchParam locgovSearchParam);

	// 지정기부 지자체별 기부 집계
	List<DesignatedStat> selectDesignatedLocgovCntrStat(LocgovSearchParam locgovSearchParam);

	// 지정기부 지자체별 기부 집계 리스트 카운트
	int selectDesignatedLocgovCntrStatCount(LocgovSearchParam locgovSearchParam);

	// 사업별 갬페인 진행건 비율 조회
	List<DesignatedStat> selectDesignatedLocgovMonthCampaign(LocgovSearchParam locgovSearchParam);

	// 모금액 비율 조회
	List<DesignatedStat> selectDesignatedLocgovMonthAmountRaised(LocgovSearchParam locgovSearchParam);

	// 모금액 추이
	List<DesignatedStat> selectDesignatedLocgovMonthAmount(LocgovSearchParam locgovSearchParam);

	// 지정기부 지자체별 상세내역
	DesignatedStat selectDesignatedLocgovCntrDetail(LocgovSearchParam locgovSearchParam);

	// 응원메시지 저장
	int saveCheerMsg(DesignatedCntr designatedCntr);

	// 응원메시지 체크용 기부 정보 조회
	DesignatedCntr selectCntrInfo(DesignatedCntr designatedCntr);

	// 공지내역 목록 조회
	List<DesignatedDonationNotice> selectDesignatedDonationNoticeList(DesignatedDonationSearchParam designatedDonationSearchParam);

	// 공지내역 목록 카운트
	int selectDesignatedDonationNoticeListCnt(DesignatedDonationSearchParam designatedDonationSearchParam);

	// 공지내역 상세 조회
	DesignatedDonationNotice selectDesignatedDonationNoticeDetail(DesignatedDonationNoticeParam designatedDonationNoticeParam);

	// 지정기부 공지내용 이미지 접근성 설명 조회
	List<PrjNoticeImageExplain> selectDesignatedDonationNoticeImgDescList(long prjNoticeId);

	// 지정기부 이미지 접근성 설명 삭제
	int deleteDesignatedDonationNoticeImgDescList(long prjNoticeId);

	// 지정기부 이미지 접근성 설명 등록
	int insertDesignatedDonationNoticeImgDesc(DesignatedDonationNotice designatedDonationNotice);

	// 지정기부 공지사항 파일 등록
	int insertDesignatedDonationNoticeFile(List<PrjNoticeFile> noticeFiles);

	// 지정기부 공지사항 파일 목록 조회
	List<PrjNoticeFile> selectDesignatedDonationNoticeFileList(long prjNoticeId);

	// 지정기부 공지사항 최대 순번값 조회
	PrjNoticeFile getDesignatedDonationNoticeFileMaxSequence(long prjNoticeId);

	// 지정기부 이미지 접근성 설명 등록
	int deleteDesignatedDonationNoticeFile(DesignatedDonationNoticeParam param);

	// 지정기부 공지사항 등록
	int insertDesignatedDonationNotice(DesignatedDonationNotice designatedDonationNotice);

	// 지정기부 공지사항 수정
	int updateDesignatedDonationNotice(DesignatedDonationNotice designatedDonationNotice);

	// 공지사항 파일 조회
	PrjNoticeFile selectDesignatedDonationNoticeFile(DesignatedDonationNoticeParam designatedDonationNoticeParam);

	/**
	 * 관리자 권한 승인관리 목록 갯수 조회
	 * @param managerRequestSearchParam
	 * @return
	 */
	int getManagerRequestCountByParam(DsgnCntrManagerRequestSearchParam searchParam);

	/**
	 * 관리자 권한 승인관리 목록 조회 (검색조건 포함)
	 * @param searchParam
	 * @return
	 */
	List<DsgnCntrManagerRequest> getManagerRequestListByParam(DsgnCntrManagerRequestSearchParam searchParam);

	/**
	 * 관리자 권한 승인관리 상세 조회
	 * @param searchParam
	 * @return
	 */
	DsgnCntrManagerRequest getManagerRequestDetails(DsgnCntrManagerRequestSearchParam searchParam);

	/**
	 * 지정기부 공지사항 상태변경
	 * @param designatedDonationNotice
	 * @return
	 */
	int updateNoticeListDataByLabel(DesignatedDonationNotice designatedDonationNotice);

	/**
	 * 지정기부 부서관리 목록 카운트 조회
	 * @param param
	 * @return
	 */
	int selectDsgncntrPartMngCnt(DesignatedPartParam param);

	/**
	 * 지정기부 부서관리 목록 조회
	 * @param param
	 * @return
	 */
	List<DesignatedPart> selectDsgncntrPartMngList(DesignatedPartParam param);

	/**
	 * 지정기부 부서관리 등록
	 * @param param
	 * @return
	 */
	int insertDsgncntrPartMng(DesignatedPart param);

	/**
	 * 지정기부 부서관리 수정
	 * @param param
	 * @return
	 */
	int updateDsgncntrPartMng(DesignatedPart param);

	/**
	 * 지정기부 사업부서 관리 상세 조회
	 * @param searchParam
	 * @return
	 */
	DesignatedPart selectDsgncntrPartMngDetail(DesignatedPartParam searchParam);


	/**
	 * 지정기부 담당자 - 부서정보 매핑 데이터 삭제
	 * @param designatedPart
	 * @return
	 */
	int deleteDsgncntrPartMppng(DesignatedPart designatedPart);


	/**
	 * 지정기부 담당자 - 부서정보 매핑 데이터 등록
	 * @param designatedPart
	 * @return
	 */
	int insertDsgncntrPartMppng(DesignatedPart designatedPart);


	/**
	 * 지정기부 승인 이력
	 * @param designatedDonation
	 * @return
	 */
	int insertDsgncntrConfirmLog(DesignatedDonation designatedDonation);

	/**
	 * 지정기부 승인 이력 조회 카운트
	 * @param designatedDonation
	 * @return
	 */
	int selectDsgncntrConfirmLogCnt(DsgncntrConfirmLogParam param);

	/**
	 * 지정기부 승인 이력 조회
	 * @param designatedDonation
	 * @return
	 */
	List<DsgncntrConfirmLog> selectDsgncntrConfirmLog(DsgncntrConfirmLogParam param);

	/**
	 * 지정기부 권한 소유자 부서 조회
	 * @param designatedDonation
	 * @return
	 */
	long selectDsgncntrPartId(long userId);

	/**
	 * 관리자 권한 신청 내역 부서정보 업데이트
	 * @param dsgnCntrManagerRequest
	 * @return
	 */
	int updateManagerRequestPartName(DsgnCntrManagerRequest dsgnCntrManagerRequest);

	/**
	 * 모금완료 비교를 위한 조회
	 */

	List<Map<String, Long>> selectDesinatedDonationGoalAmt();

	/**
	 * 모금완료된 기정기부 상태 변경
	 */
	int updateDesinatedDonationStatus(Long bizId);

}


