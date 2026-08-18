package saleson.shop.give.givestate;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.donation.CntrTaxTempDto;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.givestate.domain.GiveStateCntrUsePoint;
import saleson.shop.give.givestate.domain.GiveStateTest;

@Mapper("giveStateMapper")
public interface GiveStateMapper {

	// 기부금 모금현황 합계
	GiveState getGiveStateSum(GiveState giveState);

	// 기부금 모금현황 목록 count
	int getGiveStateListCount(GiveState giveState);

	// 기부금 모금현황 목록
	List<GiveState> getGiveStateList(GiveState giveState);

	// 기부금 모금현황 상세 누적합계
	GiveState getGiveStateDetail(GiveState giveState);

	// 기부금 모금현황 상세 검색합계
	GiveState getGiveStateDetailSum(GiveState giveState);

	// 기부금 모금현황 상세 목록 count
	int getGiveStateDetailListCount(GiveState giveState);

	// 기부금 모금현황 상세 목록
	List<GiveState> getGiveStateDetailList(GiveState giveState);

	// 기부금 모금현황 상세 목록
	List<HashMap<String, Object>> getLocgovCodeList(String code);

	// 지자체 코드 1depth 2depth 목록 조회
	List<HashMap<String, Object>> getUppLocgovCodeList();

	// 지자체 코드 조회
	HashMap<String, Object> getLocgovCode(long userId);



	// 확인용 : 삭제예정
	int getGiveStateDetailListCountTest(GiveState giveState);
	List<GiveStateTest> getGiveStateDetailListTest(GiveState giveState);
	int getGiveStateDetailListCountCntr(GiveState giveState);
	List<GiveStateTest> getGiveStateDetailListCntr(GiveState giveState);
	List<GiveStateCntrUsePoint> getGiveStateDetailListCntrUsePoint(GiveState giveState);

	/**
	 * <pre>
	 * comment : 기부 전체 현황 > 기부내역변경정보 조회
	 * preMethodName : getGiveStateModifyInfo
	 * author :  primyerim
	 * date : 2023. 2. 28.
	 *
	 *</pre>
	 * @param elctrnPayNo
	 * @return
	 * GiveState
	 */
	GiveStateTest getGiveStateModifyInfo(String elctrnPayNo);

	/**
	 * <pre>
	 * comment : 기부 과오납처리
	 * preMethodName :
	 * author : 이광교
	 * date : 2023. 3. 2.
	 *
	 *</pre>
	 * @param
	 * @return

	 */
	 Integer giveCancelProcess(GiveStateTest giveStateTest);
	 /**
	  * comment : 취소여부
	  * date: 2032.03.06
	  * */
	 Integer giveDeleteAt(GiveStateTest giveStateTest);

	 /**
	  * comment: 포인트 복구
	  * date: 2023.03.06
	  * author :  이광교
	  * date : 2023. 3. 6.
	  *
	  * @param giveStateTest
	  * @return
	  * Integer
	  */
	 Integer givePointRenew(GiveStateTest giveStateTest);

	/**
	 * comment : 기부금변경신청관리 목록 count 조회
	 * author :  배예림
	 * date : 2023. 3. 28.
	 *
	 * @param giveState
	 * @return
	 * int
	 */
	int getGiveReqmngCount(GiveState giveState);

	/**
	 * comment : 기부금변경신청관리 목록 조회
	 * author :  배예림
	 * date : 2023. 3. 28.
	 *
	 * @param giveState
	 * @return
	 * List<GiveState>
	 */
	List<GiveState> getGiveReqmngList(GiveState giveState);

	/**
	 * comment : 기부금변경신청관리 변경신청등록
	 * author :  배예림
	 * date : 2023. 3. 29.
	 *
	 * @param giveStateTest
	 * @return
	 * Integer
	 */
	Integer giveReqmngInsert(GiveStateTest giveStateTest);

	/**
	 * comment : 기부금변경신청관리 변경신청승인
	 * author :  배예림
	 * date : 2023. 3. 30.
	 *
	 * @param giveStateTest
	 * @return
	 * Integer
	 */
	Integer giveReqmngApprove(GiveStateTest giveStateTest);

	/**
	 * comment : 기부금변경신청관리 변경신청취소
	 * author :  배예림
	 * date : 2023. 3. 30.
	 *
	 * @param giveStateTest
	 * @return
	 * Integer
	 */
	Integer giveReqmngCancel(GiveStateTest giveStateTest);

	/**
	 * <pre>
	 * comment : 기부금전체현황 수납Y
	 * author :  배예림
	 * date : 2023. 4. 14.
	 *
	 *</pre>
	 * @param searchParam
	 * @return
	 * Object
	 */
	Integer sunapUpdateY(GiveStateTest searchParam);

	/**
	 * <pre>
	 * comment : 포인트 임시포인트에 저장
	 * author :  배예림
	 * date : 2023. 5. 9.
	 *
	 *</pre>
	 * @param searchParam
	 * @return
	 * Integer
	 */
	Integer cancelTemPointSet(GiveStateTest searchParam);

	/**
	 * <pre>
	 * comment : 임시포인트 포인트 복구
	 * author :  배예림
	 * date : 2023. 5. 9.
	 *
	 *</pre>
	 * @param searchParam
	 * @return
	 * Integer
	 */
	Integer cancelPointReset(GiveStateTest searchParam);

	/**
	 * <pre>
	 * comment : 수납 취소
	 * author :  배예림
	 * date : 2023. 5. 15.
	 *
	 *</pre>
	 * @param giveStateTest
	 * void
	 */
	Integer sunapCancel(GiveStateTest searchParam);

	/**
	 * <pre>
	 * comment : 과오납 취소/승인 SMS전송 정보
	 * author :  USER
	 * date : 2023. 4. 14.
	 *
	 *</pre>
	 * @param searchParam
	 * @return
	 * GiveUserSmsInfo
	 */
	GiveUserSmsInfo giveReqmngSmsUserInfo(GiveStateTest searchParam);

	GiveUserSmsInfo giveReqmngSmsUserInfoByReqId(GiveStateTest searchParam);


	/**
	 * <pre>
	 * comment : 기부금 변경신청 관련 포인트 사용 체크
	 * author :  csh
	 * date : 2023. 11. 07.
	 *
	 *</pre>
	 * @param cntrSn
	 * @return
	 * count
	 */
	int getCntrUsePointCheck(String cntrSn);

	/**
	 * <pre>
	 * comment : 차세대 수납이력 체크
	 * author :  USER
	 * date : 2024. 03. 07.
	 *
	 *</pre>
	 * @param cntrSn
	 * @return
	 * count
	 */
	int getSunapInfo(String cntrSn);

	/**
	 * <pre>
	 * comment : 차세대 수납이력 등록
	 * author :  USER
	 * date : 2024. 03. 07.
	 *
	 *</pre>
	 * @param cntrSn
	 * @return
	 * count
	 */
	int insertGiveSunapInfo(GiveStateTest searchParam);

	/**
	 * <pre>
	 * comment : 과오납 등록 후 영수증 취소 정보 조회
	 * author :  csh
	 * date : 2024. 5. 21.
	 *
	 *</pre>
	 * @param
	 * @return
	 *
	 */
	CntrTaxTempDto getCntrTaxTemp(GiveStateTest giveStateTest);

	/**
	 * <pre>
	 * comment : 과오납 등록 후 영수증 취소 정보 등록
	 * author :  csh
	 * date : 2024. 5. 21.
	 *
	 *</pre>
	 * @param
	 * @return
	 *
	 */
	int insertCntrTaxTemp(CntrTaxTempDto cntrTaxTempDto);

	/**
	 * <pre>
	 * comment : 영수증 등록 실패
	 * author :  csh
	 * date : 2024. 5. 21.
	 *
	 *</pre>
	 * @param
	 * @return
	 *
	 */
	int insertCntrTaxTempLog(CntrTaxTempDto cntrTaxTempDto);

	/**
	 * <pre>
	 * comment : G_CNTR_TAX_TEMP 업데이트
	 * author :  csh
	 * date : 2024. 5. 22.
	 *
	 *</pre>
	 * @param
	 * @return
	 *
	 */
	int updateCntrTaxTemp(CntrTaxTempDto cntrTaxTempDto);

	/**
	 * <pre>
	 * comment : G_CNTR_TAX_LOG 삭제
	 * author :  csh
	 * date : 2024. 5. 22.
	 *
	 *</pre>
	 * @param
	 * @return
	 *
	 */
	int deleteCntrTaxTempLog(CntrTaxTempDto cntrTaxTempDto);

	/**
	 * <pre>
	 * comment : G_CNTR_TAX_LOG 조회
	 * author :  csh
	 * date : 2024. 5. 22.
	 *
	 *</pre>
	 * @param
	 * @return
	 *
	 */
	int getCntrTaxTempLog(CntrTaxTempDto cntrTaxTempDto);

	/**
	 * 연통계 데이터 select
	 * 1. 지자체별 기부건수 기부금액
	 */
	List<HashMap<String, Object>> getListForSumAmtTnocsStats(HashMap<String, Object> hashMap);
	/**
	 * 연통계 데이터 select
	 * 1. 지자체별 기부경로별 :
	 */
	List<HashMap<String, Object>> getListForPathAmtTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 연통계 데이터 select
	 * 1. 지자체별 기부 거주지별 :
	 */
	List<HashMap<String, Object>> getListForPsintAmtTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 연통계 데이터 select
	 * 1. 지자체별 기부 월별 :
	 */
	List<HashMap<String, Object>> getListForMonthAmtTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 연통계 기부금액별 기부건수 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListPastYearForDntnAmtTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 연통계 연령별별 기부건수 금액 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListPastYearForAgeDntnAmtTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 연통계 인기답례품현황(판매량순 상위 30개)
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForPubGoodsPastYearStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 연통계 월별건수 : 금액 고유아이디
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForMctpvUniqIdPastYearStats(HashMap<String, Object> hashMap);

}
