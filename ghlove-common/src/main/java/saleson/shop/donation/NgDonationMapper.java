package saleson.shop.donation;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.donation.domain.CntrLmtt;
import saleson.shop.donation.domain.DoLocGovInfo;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.donation.domain.HonorCntr;
import saleson.shop.donation.support.ContryParam;
import saleson.shop.donation.support.DonationParam;
import saleson.shop.donation.support.ForeignStatusParam;
import saleson.shop.donation.support.GiroParam;
import saleson.shop.donation.support.SeoulParam;
import saleson.shop.donation.support.SidoListParam;
import saleson.shop.give.givestate.domain.GiveStateTest;
import saleson.shop.item.domain.ItemBase;
import saleson.shop.log.support.GifSeoulParam;
import saleson.shop.log.support.GifStndParam;
import saleson.shop.log.support.RelayLogParam;
import saleson.shop.mypage.domain.Cntr;
import saleson.shop.mypage.support.CntrParam;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.user.domain.Locgov;
import saleson.shop.user.domain.UserDetail;

@Mapper("ngDonationMapper")
public interface NgDonationMapper {

	/* 사용자 기부정보 */
	int getUserCntrLimit(long userId);

	/* 시도 리스트 */
	List<SidoListParam> getSidoList(DonationParam donationParam);

	/* 시군구 리스트 */
	List<SidoListParam> getSigunguList(DonationParam donationParam);

	/* 지자체 정보 */
	DoLocGovInfo getLocGovInfo(LocGovInfo sidoListParam);

	/* 관심 지자체 추가 */
	int insertIntrstLocgovInfo(LocGovInfo locGovInfo);

	/* 관심 지자체 등록 여부 */
	int getIntrstLocgovInfo(LocGovInfo locGovInfo);

	/* 기부데이터 등록(서울시) */
	int insertSntrBuga(SeoulParam seoulParam);

	/* 기부데이터 등록(서울시 제외 나머지 시군구) */
	int insertContryBuga(ContryParam contryParam);

	/* 사용자 상세정보 */
	UserDetail getUserDetail(long userId);

	/* 답례품 타입 리스트 */
	List<SidoListParam> getPresentTypeList(DonationParam donationParam);

	/* 서울시 세외 수입 대장번호 채번 */
	String getBookNoSeoul();

	/* 이택스 대장번호 채번 */
	String getMngNoEtax();

	/* 지방 세외 수입(현세대) 대장번호 채번 */
	String getBookNoContry();

	/* 지자체 행정동코드 매핑 */
	String getMappingLocgovCode(String locgovCode);

	/* 지자체 부서코드 매핑 */
	String getJijachDeptCd(String locgovCode);

	/* 지자체 기관코드 매핑 */
	String getSiguCdSeoul(String locgovCode);

	/* 사용자 CI */
	String getUserCI(long userId);

	/* 수납정보 update(서울시) */
	int sunapSuccess(SeoulParam seoulParam);

	/**
	 * <pre>
	 * comment       : 수납확인 배치 처리 완료
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 12. 13.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * int
	 */
	int updateSunapBatchCompleted(SeoulParam seoulParam);

	/* 기부 데이터 미결재분 삭제(익월 자정까지 미결재시 삭제 처리) */
	int deleteCntrData();

	/* 국세청 전자기부영수증 등록결과 메세지 */
	int updateNtsStatus(SeoulParam seoulParam);

	/* 지방세외 수납결과 처리(batch) */
	int updateStandardSunapInfoBatch();

	/* 지방세외 미수납 리스트 */
	List<SeoulParam> getStandardNotSunapList();

	/* 서울시 미수납 리스트 */
	List<SeoulParam> getSeoulNotSunapList();

	/* 지자체 사업자등록번호 매핑 */
	String getBizNo(String jijacheCd);

	/**
	 * <pre>
	 * comment       : 국세청 영수증 처리 대상 목록 조회
	 * preMethodName : getNtsEreceiptNotList
	 * author        : hybrid
	 * date          : 2023. 3. 8.
	 *
	 * </pre>
	 * @param cntrPathCode
	 * @return
	 * List<SeoulParam>
	 */
	List<SeoulParam> getNtsEreceiptNotList(String cntrPathCode);

	/**
	 * <pre>
	 * comment       : 이전 영수증처리 데이터 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 3. 13.
	 *
	 * </pre>
	 * @return
	 * List<SeoulParam>
	 */
	List<SeoulParam> getNtsEreceiptNotListOld();

	/**
	 * <pre>
	 * comment       : 영수증 단건 처리를 위한 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 3. 28.
	 *
	 * </pre>
	 * @param enapbuNo
	 * @return
	 * SeoulParam
	 */
	SeoulParam getNtsEreceipt(String enapbuNo);

	/* 2023.01.20 지방세외(현세대) 기존 미납건 데이터 삭제 처리(batch) */
	int deleteNotSunapStndOneBatch();

	/* 지방세외(현세대) 미납건 데이터 삭제 처리(batch) */
	int deleteNotSunapStndBatch();

	/* 국세청 전자기부영수증 i/f insert */
	int insertHometaxGif(SeoulParam seoulParam);

	/**
	 * <pre>
	 * comment       : 국세청 영수증 처리 이력 여부 조회
	 * preMethodName : selectHometaxCount
	 * author        : hybrid
	 * date          : 2023. 3. 8.
	 *
	 * </pre>
	 * @param seoulParam
	 * @return
	 * int
	 */
	int selectHometaxCount(SeoulParam seoulParam);

	/**
	 * <pre>
	 * comment       : 금결원 지로 데이타 조회
	 * preMethodName : getGiroData
	 * author        : hybrid
	 * date          : 2023. 2. 14.
	 *
	 * </pre>
	 * @param paramMap
	 * @return
	 * GiroParam
	 */
	GiroParam getGiroData(GiroParam giroParam);

	/**
	 * <pre>
	 * comment       : 처리부서코드 가져오기
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 3. 9.
	 *
	 * </pre>
	 * @param locgovCode
	 * @return
	 * String
	 */
	String getProcessDeptCd(HashMap<String,String> searchMap);

	/* 기부 제한 지자체 조회*/
	CntrLmtt getCntrLmtt(SidoListParam donationParam);

	/* 명예 기부자 조회 */
	HonorCntr getHonorCntrLevel(LocGovInfo locgovInfo);

	/* 명예 기부자 등록 */
	int insertHonorCntrbtr(HonorCntr honorCntr);

	/* 명예 기부자 수정 */
	int updateHonorCntrbtr(HonorCntr honorCntr);

	/* 명예 기부자 삭제 */
	int deleteHonorCntrbtr(HonorCntr honorCntr);

	/**
	 * <pre>
	 * comment       : 수납확인을 위한 취소처리된 기부건들 목록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 4. 6.
	 *
	 * </pre>
	 * @return
	 * List<HashMap<String,Object>>
	 */
	List<HashMap<String, Object>> confirmSunapForDeletedList();

	/**
	 * <pre>
	 * comment       : 지자체 회계코드 가져오기
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 4. 13.
	 *
	 * </pre>
	 * @param locgovCode
	 * @return
	 * String
	 */
	String getLocgovFisSp(String locgovCode);

	/**
	 * <pre>
	 * comment       : 잔여포인트 정상화 처리
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 4. 21.
	 *
	 * </pre>
	 * @param hashMap
	 * @return
	 * int
	 */
	int updateCntrBlcePoint(String cntrSn);

	/**
	 * <pre>
	 * comment       : 국민비서 알리미(기부감사인사) 사용자 정보
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 4. 21.
	 *
	 * </pre>
	 * @param hashMap
	 * @return
	 * int
	 */
	GiveUserSmsInfo getSmsSendGiveUserInfo(SeoulParam seoulParam);

	/**
	 * 연계 로그 개수 조회
	 * @param relayLogParam
	 * @return
	 */
	int selectRelayLogListCount(RelayLogParam relayLogParam);

	/**
	 * 연계 로그 목록 조회
	 * @param relayLogParam
	 * @return
	 */
	List<RelayLogParam> selectRelayLogList(RelayLogParam relayLogParam);

	/**
	 * 연계 로그 저장
	 * @param relayLogParam
	 * @return
	 */
	int insertRelayLog(RelayLogParam relayLogParam);

	/**
	 * 지방세외 부과등록 로그 개수
	 * @param nextBugaRequestLogDto
	 * @return
	 */
	int selectNextBugaLogCount(NextBugaRequestLogDto nextBugaRequestLogDto);

	/**
	 * 지방세외 부과등록 로그 목록
	 * @param nextBugaRequestLogDto
	 * @return
	 */
	List<NextBugaResponseLogDto> selectNextBugaLogList(NextBugaRequestLogDto nextBugaRequestLogDto);


	/**
	 * 지방세외 수납 로그 개수
	 * @param nextSunapRequestLogDto
	 * @return
	 */
	int selectNextSunapLogCount(NextSunapRequestLogDto nextSunapRequestLogDto);


	/**
	 * 지방세외 수납 로그 목록
	 * @param nextSunapRequestLogDto
	 * @return
	 */
	List<NextSunapResponseLogDto> selectNextSunapLogList(NextSunapRequestLogDto nextSunapRequestLogDto);

	/**
	 * 서울세외 부과등록 로그 개수
	 * @param gifStndParam
	 * @return
	 */
	int selectGifSeoulBugaCount(GifSeoulParam gifSeoulParam);

	/**
	 * 서울세외 부과등록 로그 목록
	 * @param gifStndParam
	 * @return
	 */
	List<GifSeoulParam> selectGifSeoulBugaList(GifSeoulParam gifSeoulParam);

	/**
	 * 서울세외 수납 로그 개수
	 * @param gifSeoulParam
	 * @return
	 */
	int selectGifSeoulSunapCount(GifSeoulParam gifSeoulParam);

	/**
	 * 서울세외 수납 로그 목록
	 * @param gifSeoulParam
	 * @return
	 */
	List<GifSeoulParam> selectGifSeoulSunapList(GifSeoulParam gifSeoulParam);

	/**
	 * <pre>
	 * comment       : 처리부서코드 가져오기
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 3. 9.
	 *
	 * </pre>
	 * @param locgovCode
	 * @return
	 * String
	 */
	Locgov getLocgovInfo(HashMap<String,String> searchMap);

	/**
	 * 취소된 기부건에 대한 재수납 확인 목록
	 * @param hashMap
	 * @return
	 */
	List<HashMap<String, Object>> selectDeleteCntrReSunapList(HashMap<String, Object> hashMap);

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 유저 지자체 기부정보
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 30.
	 *
	 * </pre>
	 * @param hashMap
	 * @return
	 * List<HashMap<String,Object>>
	 */
	List<HashMap<String, Object>> selectUserCntrLocgovForNhList(HashMap<String, Object> hashMap);

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 유저 기부정보
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 30.
	 *
	 * </pre>
	 * @param hashMap
	 * @return
	 * List<HashMap<String,Object>>
	 */
	List<HashMap<String, Object>> selectUserCntrForNhList(HashMap<String, Object> hashMap);

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 지자체코드정보
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 30.
	 *
	 * </pre>
	 * @param hashMap
	 * @return
	 * List<HashMap<String,Object>>
	 */
	List<HashMap<String, Object>> selectLocgovForNhList();


	/**
	 * 기부자 내/외국인 정보 업데이트
	 * @param ForeignStatusParam
	 * @return
	 */
	int updateForeignStatusCode(ForeignStatusParam foreignStatusParam);


	/**
	 * 기부자 생년월일 업데이트
	 * @param UserDetail
	 * @return
	 */
	int updateKakaoBirthdayInfo(UserDetail userDetail);

	/**
	 * 행정기관코드로 법정동 코드 조회
	 * @param admCd
	 * @return
	 */
	String selectLocgovByAdmCd(String admCd);

	/**
	 * <pre>
	 * comment       : 기부건수가 없는 지자체 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 12. 11.
	 *
	 * </pre>
	 * @return
	 * List<HashMap<String,Object>>
	 */
	List<HashMap<String, Object>> getNoBugaLocgovList();

	/**
	 * <pre>
	 * comment       : 세외 데이터 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 1. 4.
	 *
	 * </pre>
	 * @return
	 * List<SeoulParam>
	 */
	List<SeoulParam> getCntrTaxTempList();

	/**
	 * <pre>
	 * comment       : 세금신고 이력 등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 1. 4.
	 *
	 * </pre>
	 * @param CntrTaxLogDto
	 * @return
	 * String
	 */
	int insertCntrTaxLog(CntrTaxLogDto cntrTaxLogDto);

	/**
	 * <pre>
	 * comment       :
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 1. 4.
	 *
	 * </pre>
	 * @param CntrTaxLogDto
	 * void
	 */
	int updateCntrTaxLog(CntrTaxLogDto cntrTaxLogDto);

	/**
	 * <pre>
	 * comment       : 연계대상 기관에서 관리하는 유일키
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 1. 30.
	 *
	 * </pre>
	 * @return
	 * String
	 */
	String getLinkMngKeyNextValue();

	/**
	 * <pre>
	 * comment       : 부과등록
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 2. 5.
	 *
	 * </pre>
	 * @param nextBugaRequestDto
	 * @return
	 * int
	 */
	int insertNextBugaCntr(NextBugaRequestDto nextBugaRequestDto);

	/**
	 * <pre>
	 * comment       : 수납확인
	 * preMethodName :
	 * author        :
	 * date          : 2024. 2. 12.
	 *
	 * </pre>
	 * @param getSunapInfo
	 * @return
	 * int
	 */
	int getSunapInfo(String elctrnPayNo);

	/**
	 * <pre>
	 * comment       : 미수납 기부건 조회
	 * preMethodName :
	 * author        :
	 * date          : 2024. 2. 13.
	 *
	 * </pre>
	 * @return
	 * List<SeoulParam>
	 */
	List<SeoulParam> getCntrNonSunapDataList(String cntrDt);

	/**
	 * <pre>
	 * comment       : 영수증 처리 후 임시테이블(TEMP) 업데이트
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 7. 9.
	 *
	 * </pre>
	 * @param CntrTaxLogDto
	 * void
	 */
	int updateCntrTaxTemp(CntrTaxLogDto cntrTaxLogDto);


	/**
	 * <pre>
	 * comment       : 기부 한도금 체크
	 * preMethodName :
	 * author        :
	 * date          : 2024. 12. 10.
	 *
	 * </pre>
	 * @param
	 * void
	 */
	Integer getGCntrSumCntrAmt(long userId);
	Integer getGMberSecsnSumCntrAmt(String mberCi);

	/* 주문테이블 상태 변경 */
	int updateOrderStatus(String orderCode);
	/**
	 * <pre>
	 * comment       : 오늘 해당 지자체에 기부한건이 있는지 체크
	 * preMethodName :
	 * author        :
	 * date          : 2025. 6. 2.
	 *
	 * </pre>
	 * @param LocGovInfo
	 */
	int selectTodayCntrSigunguCnt(LocGovInfo locGovParam);

	/**
	 * <pre>
	 * comment       : 오늘 해당 지자체에 기부한건이 있는지 체크
	 * preMethodName :
	 * author        :
	 * date          : 2025. 6. 2.
	 *
	 * </pre>
	 * @param CntrParam
	 */
	List<Cntr> getTodayCntrListInfo(CntrParam cntrParam);


	/**
	 * 수납 테이블에서 이번년도 미수납 건 가져오기(5분에 한번씩 배치 돈다)
	 * @param hashMap
	 * @return
	 */
	List<GiveStateTest> getCntrNonSunapFiveMinList();

	int updateSunapBatchProcessById(GiveStateTest sunapInfo);

	/**
	 * 수납 테이블에서 이번년도 미수납 건 가져오기(하루에 한번)
	 * @param hashMap
	 * @return
	 * @param hashMap
	 * @return
	 */
	List<GiveStateTest> getCntrNonSunapAlldayList();

	/* 수납정보 update 처리 배치확인 */
	int sunapResultSuccess(SeoulParam param);

	/**
	 * 마갈일인 답례 정보 조회
	 *
	 * @return
	 */
	List<ItemBase> getItemDisplayCheckList();

	/**
	 * 답례품 공개여부 변경
	 *
	 * @param targetItem
	 */
	void updateItemDisplayFlag(ItemBase targetItem);

}
