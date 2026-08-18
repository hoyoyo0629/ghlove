package saleson.shop.report;

import java.util.Map;

import com.fasterxml.jackson.core.JacksonException;

public interface ReportService {


	/**
	 * <pre>
	 * comment       : 일일 보고용 통계
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 12. 3.
	 *
	 * </pre>
	 * @param sttemntPayDe
	 * @return
	 * List<HashMap<String,Object>>
	 */
	public Map<String, Object> dailyReportTotalStatistics(String searchDay);



	/**
	 * <pre>
	 * comment       : 일일 보고 저장
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2024. 12. 3.
	 *
	 * </pre>
	 * void
	 * @throws JacksonException
	 */
	public void saveDailyReport() throws JacksonException;

	/**
	 * <pre>
	 * comment       : 일일 보고용 통계 날짜범위검색V2
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2026. 2. 3.
	 *
	 * </pre>
	 * @param startDay
	 * @param endDay
	 * @return
	 * List<HashMap<String,Object>>
	 */
	public Map<String, Object> dayReportTotalStatistics(String startDay, String endDay);
}
