package saleson.shop.analysis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import saleson.common.configuration.MapperAnalisys;
import saleson.shop.analysis.domain.AnalysisVo;
import saleson.shop.give.givestate.domain.CallState;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.statistics.domain.StatisticsReport;
import saleson.shop.statistics.support.StatisticsParam;

@MapperAnalisys("analysisMapper")
public interface AnalysisMapper {

	/**
	 * <pre>
	 * comment       : 기부금전체 총 월별 현황 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertCntrAllTotalMonthStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 기부금전체나이별 현황 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertCntrAllAgeStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 기부금 전체 시간대별 현황 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertCntrAllHourStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 기부금 지자체별 월별 현황 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertCntrLocgovMonthStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 기부금 지자체별 나이별 현황 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertCntrLocgovAgeStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 기부금 지자체별 시간대별 현황 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertCntrLocgovHourStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 매출통계 일자별 통계
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertSalesOrderItemDayStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 매출통계 월별 통계
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertSalesOrderItemMonthStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 매출통계 년도별 통계
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertSalesOrderItemYearStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 매출통계 결제타입별
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertSalesApprovalType(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 기부금 운영현황
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertCtbnyOpratnStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 관심지자체현황
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertLocgovLikeStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 만족도 현황
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertStsfdgStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 답례품구매현황 전체
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertOrderItemAllStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 답례품지자체별구매현황
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertOrderItemLocgovStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 답례품월별지자체별현황
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertOrderItemMonthLocgovStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 접속통계_월별
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertVisitMonthStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 접속통계_일별
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertVisitDayStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 방문자접속현황_도메인
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertVisitDomainStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 방문자접속현황_브라우저
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertVisitBrowserStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 방문자접속현황_OS
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 6.
	 *
	 * </pre>
	 * @param vo
	 * @return
	 * int
	 */
	public int insertVisitOsStats(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 유저 기부 정보 저장(CI 포함)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertUserCntrForNh(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 유저기부 정보 및 기부지자체 정보 저장(CI 포함)
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertUserCntrLocgovForNh(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 농협연계를 위한 지자체 코드 정보 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 8. 29.
	 *
	 * </pre>
	 * @param vo
	 * int
	 */
	public int insertLocgovForNh(AnalysisVo vo);

	/**
	 * <pre>
	 * comment       : 농협 지자체별 기부 데이터 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 27.
	 *
	 * </pre>
	 * void
	 */
	public void deleteUserCntrLocgovForNh();

	/**
	 * <pre>
	 * comment       : 농협 기부 데이터 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 27.
	 *
	 * </pre>
	 * void
	 */
	public void deleteUserCntrForNh();

	/**
	 * <pre>
	 * comment       : 농협 지자체 데이터 삭제
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 27.
	 *
	 * </pre>
	 * void
	 */
	public void deleteLocgovForNh();

	/**
	 * <pre>
	 * comment : 대시보드 배치
	 * preMethodName :confirmdonationForReportBatch
	 * author :  이광교
	 * date : 2023. 10. 11.
	 *
	 */

	void confirmDonationForReportBatch(Map<String, Object> resultMap);

	/**
	 * <pre>
	 * comment : 대시보드 보고용 정보
	 * preMethodName :dashBoardReport
	 * author :  이광교
	 * date : 2023. 10. 12.
	 *
	 */

	CallState dashBoardReport(GiveState giveState);
	/**
	 * <pre>
	 * comment : 대시보드 보고용 기부 건 수 증감 정보
	 * preMethodName :dashBoardReportForCal
	 * author :  이광교
	 * date : 2023. 10. 12.
	 *
	 */

	CallState dashBoardReportForCal(GiveState giveState);


	/**
	 * 통계현황 보고
	 * @param hashMap
	 */
	void insertOpuserBirthdayDecListBatch(List<HashMap<String, Object>> hashMap);

	String selectMaxUserIdOpUserBirthday();

	String selectMaxYrMmForMbrTnocsStats();

	void deleteListForMbrTnocsStats(Map<String, Object> hashMap);

	void insertListForMbrTnocsStats(Map<String, Object> hashMap);

	String selectMaxYrMmForDntnTnocsStats();

	void deleteListForDntnTnocsStats(Map<String, Object> hashMap);

	void insertListForDntnTnocsStats(Map<String, Object> hashMap);

	String selectMaxYrMmForDntnTnocs500Stats();

	String selectMaxYrMmForDntnTnocsMaxAmtStats();//2000

	void deleteListForDntnTnocs500Stats(Map<String, Object> hashMap);

	void deleteListForDntnTnocsMaxAmtStats(Map<String, Object> hashMap);//2000

	void insertListForDntnTnocs500Stats(Map<String, Object> hashMap);

	void insertListForDntnTnocsMaxAmtStats(Map<String, Object> hashMap);//2000

	String selectMaxYrMmForGdsTnocsStats();

	void deleteListForGdsTnocsStats(Map<String, Object> hashMap);

	void insertListForGdsTnocsStats(Map<String, Object> hashMap);

	String selectMaxYrMmForMctpvDntnStats();

	void deleteListForMctpvDntnStats(Map<String, Object> hashMap);

	void insertListForMctpvDntnStats(Map<String, Object> hashMap);

	String selectMaxYrMmForMctpvDntn500Stats();

	String selectMaxYrMmForMctpvDntnMaxAmtStats();//2000

	void deleteListForMctpvDntn500Stats(Map<String, Object> hashMap);

	void deleteListForMctpvDntnMaxAmtStats(Map<String, Object> hashMap);//2000

	void insertListForMctpvDntn500Stats(Map<String, Object> hashMap);

	void insertListForMctpvDntnMaxAmtStats(Map<String, Object> hashMap);//2000 만원

	String selectMaxYrMmForLclgvAodStats();

	void deleteListForLclgvAodStats(Map<String, Object> hashMap);

	void insertListForLclgvAodStats(Map<String, Object> hashMap);

	String selectMaxYrMmForLclgvAod500Stats();

	String selectMaxYrMmForLclgvAodMaxAmtStats();//2000

	void deleteListForLclgvAod500Stats(Map<String, Object> hashMap);

	void deleteListForLclgvAodMaxAmtStats(Map<String, Object> hashMap);//2000

	void insertListForLclgvAod500Stats(Map<String, Object> hashMap);

	void insertListForLclgvAodMaxAmtStats(Map<String, Object> hashMap);//2000

	String selectMaxYrMmForMctpvGdsStats();

	void deleteListForMctpvGdsStats(Map<String, Object> hashMap);

	void insertListForMctpvGdsStats(Map<String, Object> hashMap);

	String selectMaxYrMmForLclgvGdsStats();

	void deleteListForLclgvGdsStats(Map<String, Object> hashMap);

	void insertListForLclgvGdsStats(Map<String, Object> hashMap);

	String selectMaxYrMmForDntnPathTnocsStats();

	void deleteListForDntnPathTnocsStats(Map<String, Object> hashMap);

	void insertListForDntnPathTnocsStats(Map<String, Object> hashMap);

	String selectMaxYrMmForDntnAmtTnocsStats();

	void deleteListForDntnAmtTnocsStats(Map<String, Object> hashMap);

	void insertListForDntnAmtTnocsStats(Map<String, Object> hashMap);

	String selectMaxYrMmForDntnAgeTnocsStats();

	void deleteListForDntnAgeTnocsStats(Map<String, Object> hashMap);

	void insertListForDntnAgeTnocsStats(Map<String, Object> hashMap);

	String selectMaxYrMmForHabDntnMctpvTnocsStats();

	void deleteListForHabDntnMctpvTnocsStats(Map<String, Object> hashMap);

	void insertListForHabDntnMctpvTnocsStats(Map<String, Object> hashMap);

	List<StatisticsReport> getCrtrYearForMbrList();
	List<StatisticsReport> getCrtrYearForDntnList();
	List<StatisticsReport> getCrtrYearForGdsList();
	List<StatisticsReport> getMctpvForDntnList();
	List<StatisticsReport> getMctpvForGdsList();
	List<StatisticsReport> getCrtrMonthForMbrList(String year);
	List<StatisticsReport> getCrtrMonthForDntnList(String year);
	List<StatisticsReport> getCrtrMonthForGdsList(String year);
	List<StatisticsReport> getLclgvForDntnList(String mctpv);
	List<StatisticsReport> getLclgvForGdsList(String mctpv);
	List<StatisticsReport> getListMbrTnocsStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListDntnTnocsStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListDntnTnocs500Stats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListDntnTnocsMaxAmtStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListGdsTnocsStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListMctpvDntnStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListMctpvDntn500Stats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListMctpvDntnMaxAmtStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListLclgvAodStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListLclgvAod500Stats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListLclgvAodMaxAmtStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListMctpvGdsStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListLclgvGdsStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListDntnPathTnocsStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListDntnAmtTnocsStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListDntnAgeTnocsStats(StatisticsParam statisticsParam);
	List<StatisticsReport> getListHabDntnMctpvTnocsStats(StatisticsParam statisticsParam);

	/**
	 * 연간통계 - 집계 후 통계테이블 insert
	 */
	void insertListForYearSumAmtTnocs(Map<String, Object> hashMap);
	void insertListForYearPathAmtTnocs(Map<String, Object> hashMap);
	void insertListForYearPriceAmtTnocs(Map<String, Object> hashMap);
	void insertListForYearAgesAmtTnocs(Map<String, Object> hashMap);
	void insertListForYearPsintAmtTnocs(Map<String, Object> hashMap);
	void insertListForYearMonthAmtTnocs(Map<String, Object> hashMap);
	void insertListForYearGoodsAmtTnocs(Map<String, Object> hashMap);

	/**
	 * 연간통계 - 집계된 통계테이블 select
	 */
	List<HashMap<String, Object>> getListMbrTnocsStatsStored(StatisticsParam statisticsParam);
	List<HashMap<String, Object>> getListDntnPathPastYearStatsStored(StatisticsParam statisticsParam);
	List<HashMap<String, Object>> getListDntnPricePastYearStatsStored(StatisticsParam statisticsParam);
	List<HashMap<String, Object>> getListDntnAgesPastYearStatsStored(StatisticsParam statisticsParam);
	List<HashMap<String, Object>> getListDntnPsintPastYearStatsStored(StatisticsParam statisticsParam);
	List<HashMap<String, Object>> getListDntnLocGovPastYearStatsStored(StatisticsParam statisticsParam);
	List<HashMap<String, Object>> getListDntnMonthPastYearStatsStored(StatisticsParam statisticsParam);
	List<HashMap<String, Object>> getListDntnGoodsPastYearStatsStored(StatisticsParam statisticsParam);


	/**
	 * 통계 누계현황보고 - 연통계 기부금액별 기부건수 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	int getListPastYearForDntnAmtTnocsStatsCnt(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 연통계 연령별별 기부건수 금액 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	int getListPastYearForAgeDntnAmtTnocsStatsCnt(HashMap<String, Object> hashMap);



	/**
	 * 통계 누계현황보고 - 연통계 인기답례품현황(판매량순 상위 30개)
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	int getListForPubGoodsPastYearStatsCnt(HashMap<String, Object> hashMap);

	/**
	 * 연통계 데이터 select
	 * 1. 지자체별 기부건수 기부금액
	 */
	int getListForSumAmtTnocsStatsCnt(HashMap<String, Object> hashMap);

	/**
	 * 연통계 데이터 select
	 * 1. 지자체별 기부경로별 :
	 */
	int getListForPathAmtTnocsStatsCnt(HashMap<String, Object> hashMap);

	/**
	 * 연통계 데이터 select
	 * 1. 지자체별 기부 거주지별 :
	 */
	int getListForPsintAmtTnocsStatsCnt(HashMap<String, Object> hashMap);

	/**
	 * 연통계 데이터 select
	 * 1. 지자체별 기부 월별 :
	 */
	int getListForMonthAmtTnocsStatsCnt(HashMap<String, Object> hashMap);


}


