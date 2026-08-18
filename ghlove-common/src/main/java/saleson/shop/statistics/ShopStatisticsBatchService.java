package saleson.shop.statistics;

public interface ShopStatisticsBatchService {

	/**
	 * 통계 현황보고
	 * @param statisticsParam
	 * @return
	 */
	void setStatisticsReport();

	/**
	 * 연간통계 현황보고 - 총계, 기부방볍(경로)별, 금액별
	 * @param statisticsParam
	 * @return
	 */
	void setStatisticsYearReport();
	//연령별, 월별
	void statisticsYearReportAgeMonthBatch();

}
