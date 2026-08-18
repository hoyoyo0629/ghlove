package saleson.shop.statistics;

public interface ShopStatisticsYearBatchService {

	/**
	 * 연간통계 현황보고 - 총계, 기부방볍(경로)별, 금액별
	 * @param statisticsParam
	 * @return
	 */
	void setStatisticsYearReport();
	//연령별, 월별
	void statisticsYearReportAgeMonthBatch();


	// 거주지별 기부건수 기부금액
	void statisticsYearReportPsintBatch();

	// 답례품 인기순위
	void statisticsYearReportGoodsBatch();

}
