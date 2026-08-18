package saleson.shop.designateddonation;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import saleson.shop.code.domain.Code;
import saleson.shop.designateddonation.domain.DesignatedDonation;
import saleson.shop.designateddonation.domain.DesignatedDonationNotice;
import saleson.shop.designateddonation.domain.DesignatedPart;
import saleson.shop.designateddonation.domain.DesignatedStat;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequest;
import saleson.shop.designateddonation.domain.DsgnCntrManagerRequestResult;
import saleson.shop.designateddonation.domain.DsgncntrConfirmLog;
import saleson.shop.designateddonation.domain.DsgncntrConfirmLogParam;
import saleson.shop.designateddonation.domain.PrjImageExplain;
import saleson.shop.designateddonation.support.DesignatedCntr;
import saleson.shop.designateddonation.support.DsgnCntrManagerRequestSearchParam;
import saleson.shop.designateddonation.support.DesignatedDonationNoticeParam;
import saleson.shop.designateddonation.support.DesignatedDonationSearchParam;
import saleson.shop.designateddonation.support.DesignatedPartParam;
import saleson.shop.designateddonation.support.LocgovSearchParam;

public interface DesignatedDonationService {

	void saveDesignatedDonationInfo(DesignatedDonation designatedDonation, MultipartFile[] detailImageFiles);

	List<DesignatedDonation> selectDesignatedDonationList(DesignatedDonationSearchParam designatedDonationSearchParam);

//	DesignatedDonation selectDesignatedDonation(DesignatedDonation designatedDonation);

	List<Code> getDesignatedDonationBsnsTypes();

	List<Code> getDesignatedDonationPrjStatus();

	DesignatedDonation selectDesignatedDonationDetail(DesignatedDonationSearchParam designatedDonationSearchParam);

	DesignatedDonation selectDesignatedCntrList(DesignatedDonation designatedDonation, DesignatedDonationSearchParam designatedDonationSearchParam);

	DesignatedStat selectDesignatedLocgovStat(LocgovSearchParam locgovSearchParam);

	List<DesignatedStat> selectDesignatedLocgovCntrStat(LocgovSearchParam locgovSearchParam);

	int selectDesignatedLocgovCntrStatCount(LocgovSearchParam locgovSearchParam);

	public List<PrjImageExplain> selectImgDescListByPrjId(long prjId);

	public void deletePrjImageById(int prjImageId);

	List<DesignatedStat> selectDesignatedLocgovMonthCampaign(LocgovSearchParam locgovSearchParam);

	List<DesignatedStat> selectDesignatedLocgovMonthAmountRaised(LocgovSearchParam locgovSearchParam);

	List<DesignatedStat> selectDesignatedLocgovMonthAmount(LocgovSearchParam locgovSearchParam);

	DesignatedStat selectDesignatedLocgovCntrDetail(LocgovSearchParam locgovSearchParam);

	public void updateListDataByLabel(DesignatedDonation designateddonation);

	List<Code> getDesignatedDonationBsnsSubTypes(String upId);

	int saveCheerMsg(DesignatedCntr designatedCntr);

	List<DesignatedDonationNotice> selectPrjNoticeList(DesignatedDonationSearchParam designatedDonationSearchParam);

	DesignatedDonationNotice selectPrjNoticeDetail(DesignatedDonationNoticeParam designatedDonationNoticeParam);

	int savePrjNoticeInfo(DesignatedDonationNotice designatedDonationNotice);

	int deletePrjNoticeFile(DesignatedDonationNoticeParam param);

	boolean hasDesignatedDonationAuth(long prjId);

	ResponseEntity<byte[]> downloadPrjNoticeFile(DesignatedDonationNoticeParam param);

	List<DesignatedDonationNotice> selectPrjNoticeListFront(DesignatedDonationSearchParam designatedDonationSearchParam);

	/**
	 * 관리자 권한 승인관리 이력 조회
	 * @param searchParam
	 * @return
	 */
	List<DsgnCntrManagerRequest> getManagerRequestHistory(DsgnCntrManagerRequestSearchParam searchParam);

	/**
	 * 관리자 권한 승인관리 상세 조회 접근 권한 체크
	 * @param searchParam
	 * @return
	 */
	int getManagerRequestDetailsAuthCount(DsgnCntrManagerRequestSearchParam searchParam);

	/**
	 * 관리자 권한 승인관리 - '거절'
	 * @param managerRequest
	 * @return
	 */
	DsgnCntrManagerRequestResult updateManagerRequestReject(DsgnCntrManagerRequest dsgnCntrManagerRequest);

	/**
	 * 관리자 권한 승인관리 - '승인'
	 * @param managerRequest
	 * @return
	 */
	DsgnCntrManagerRequestResult updateManagerRequestApproval(DsgnCntrManagerRequest dsgnCntrManagerRequest);

	/**
	 * 관리자 권한 승인관리 목록 조회
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
	void updateNoticeListDataByLabel(DesignatedDonationNotice designatedDonationNotice);

	/**
	 * 지정기부 사업부서 관리 목록 조회
	 * @param searchParam
	 * @return
	 */
	List<DesignatedPart> selectDsgncntrPartMngList(DesignatedPartParam searchParam);

	/**
	 * 지정기부 사업부서 관리 등록
	 * @param designatedPart
	 * @return
	 */
	int insertDsgncntrPartMng(DesignatedPart designatedPart);

	/**
	 * 지정기부 사업부서 관리 수정
	 * @param designatedPart
	 * @return
	 */
	int updateDsgncntrPartMng(DesignatedPart designatedPart);

	/**
	 * 지정기부 사업부서 관리 상세 조회
	 * @param searchParam
	 * @return
	 */
	DesignatedPart selectDsgncntrPartMngDetail(DesignatedPartParam searchParam);

	/**
	 * 지정기부 담당자 사업부서 정보 업데이트
	 * @param searchParam
	 * @return
	 */
	void updatePartInfo(DesignatedPart designatedPart, DsgnCntrManagerRequestResult result);


	/**
	 * 지정기부 승인 이력 등록
	 * @param designatedDonation
	 * @return
	 */
	int insertDsgncntrConfirmLog(DesignatedDonation designatedDonation);

	/**
	 * 지정기부 승인 이력 조회
	 * @param designatedDonation
	 * @return
	 */
	List<DsgncntrConfirmLog> selectDsgncntrConfirmLog(DsgncntrConfirmLogParam param);

	/**
	 * 지정기부 권한 보유자 부서 정보 체크
	 * @param designatedDonation
	 * @return
	 */
	long checkDsgncntrAuthPartInfo();

	/**
	 * 지정기부 상세조회_미리보기
	 * @param DesignatedDonationSearchParam
	 * @return
	 */
	DesignatedDonation selectDesignatedDonationDetailPreview(DesignatedDonationSearchParam designatedDonationSearchParam);

	/**
	 * 지정기부 목록 조회 - 검색엔진
	 */
	List<DesignatedDonation> selectDesignatedDonationListBySearchEngine(
			DesignatedDonationSearchParam designatedDonationSearchParam);

	/**
	 * 모금완료된 기정기부 상태 변경
	 */
	void updateDesinatedDonationStatus();
}
