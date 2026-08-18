package saleson.shop.statistics;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.security.userdetails.User;

import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.statistics.domain.AreaStatsSummary;
import saleson.shop.statistics.domain.CategoryStatsSummary;
import saleson.shop.statistics.domain.CategoriesStatsSummary;
import saleson.shop.statistics.domain.DateStatsSummary;
import saleson.shop.statistics.domain.DoNotSellItem;
import saleson.shop.statistics.domain.ItemStatsSummary;
import saleson.shop.statistics.domain.MonthLocgovItemStat;
import saleson.shop.statistics.domain.NhItemSalesStatistics;
import saleson.shop.statistics.domain.PaymentStatistics;
import saleson.shop.statistics.domain.RevenueBaseForDate;
import saleson.shop.statistics.domain.SellerStatsSummary;
import saleson.shop.statistics.domain.ShopBrandStatistics;
import saleson.shop.statistics.domain.ShopItemDateStatistics;
import saleson.shop.statistics.domain.ShopItemDetailStatistics;
import saleson.shop.statistics.domain.ShopItemStatistics;
import saleson.shop.statistics.domain.ShopOrderStatistics;
import saleson.shop.statistics.domain.StatsSummary;
import saleson.shop.statistics.domain.TotalRevenueStatistics;
import saleson.shop.statistics.domain.Wishlist;
import saleson.shop.statistics.domain.order.OrderCountAmtStat;
import saleson.shop.statistics.domain.order.OrderCountAmtStatMonth;
import saleson.shop.statistics.domain.StatisticsReport;
import saleson.shop.statistics.support.StatisticsParam;
import saleson.shop.statistics.support.WishlistParam;


public interface ShopStatisticsService {

	/**
	 * 결제 타입별 통계
	 * @param statisticsParam
	 * @return
	 */
	public List<PaymentStatistics> getPaymentStatisticsListByParam(StatisticsParam statisticsParam);

	/**
	 * 회원별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	public List<ShopItemDetailStatistics> getUserStatisticsListByParam(StatisticsParam statisticsParam);

	/**
	 * 회원별 통계 페이징용 카운트
	 * @param statisticsParam
	 * @return
	 */
	public int getUserStatisticsCountByParam(StatisticsParam statisticsParam);

	/**
	 * 회원별 통계 합계 - 페이징 때문에 쿼리로 처리함.
	 * @param statisticsParam
	 * @return
	 */
	public TotalRevenueStatistics getUserTotalRevenueStatisticsByParam(StatisticsParam statisticsParam);

	/**
	 * 브랜드별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	public List<ShopBrandStatistics> getBrandStatisticsListByParam(StatisticsParam statisticsParam);

	/**
	 * 안팔린 상품 내역 카운트 - 페이징용
	 * @param statisticsParam
	 * @return
	 */
	public int getDoNotSellItemCountByParam(StatisticsParam statisticsParam);

	/**
	 * 안팔린 상품 내역
	 * @param statisticsParam
	 * @return
	 */
	public List<DoNotSellItem> getDoNotSellItemListByParam(StatisticsParam statisticsParam);

	/**
	 * 매출 통계 상세 화면
	 * @param statisticsParam
	 * @return
	 */
	public List<RevenueBaseForDate> getRevenueDetailListForDateByParam(StatisticsParam statisticsParam);

	public List<ShopItemDetailStatistics> getShopItemDetailList(StatisticsParam statisticsParam);

	public List<ShopOrderStatistics> getOrderListByParam(StatisticsParam statisticsParam);

	public List<ShopItemDetailStatistics> getAreaDetailList(StatisticsParam statisticsParam);

	public List<ShopItemStatistics> getUserOrderItemListByParam(StatisticsParam statisticsParam);

	public OrderShippingInfo getUsetOrderTotalDetailById(StatisticsParam statisticsParam);

	public List<ShopOrderStatistics> getUserOrderListByParam(StatisticsParam statisticsParam);

	public List<User> getNotUserList(StatisticsParam statisticsParam);

	public int getNotUserCount(StatisticsParam statisticsParam);

	public ShopItemStatistics getAreaCountParam(StatisticsParam statisticsParam);

	public List<ShopItemDateStatistics> getItemDateListByParam(StatisticsParam statisticsParam);

	public List<RevenueBaseForDate> getBrandStatisticsDetailByParam(StatisticsParam statisticsParam);

	/**
	 * 통계 > 판매자별 통계 요약 정보
	 * @param statisticsParam
	 * @return
	 */
	StatsSummary getSellerStatsSummary(StatisticsParam statisticsParam);

	/**
	 * 통계 > 상품별 통계 요약 정보
	 * @param statisticsParam
	 * @return
	 */
	StatsSummary getItemStatsSummary(StatisticsParam statisticsParam);

	/**
	 * 통계 > 일자/월/년도별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<DateStatsSummary> getDateStatsList(StatisticsParam statisticsParam);

	/**
	 * 통계 > 고향사랑e음 통계 리스트 COUNT
	 * @param statisticsParam
	 * @return
	 */
	int getDateStatsListCountGhlove(StatisticsParam statisticsParam);

	/**
	 * 통계 > 고향사랑e음 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<DateStatsSummary> getDateStatsListGhlove(StatisticsParam statisticsParam);

	/**
	 * 통계 > 고향사랑e음 통계 답례품선호도 리스트 COUNT
	 * @param statisticsParam
	 * @return
	 */
	int getOpWishlistListCount(WishlistParam wishlistParam);

	/**
	 * 통계 > 고향사랑e음 통계 답례품선호도 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<Wishlist> getOpWishlistList(WishlistParam wishlistParam);

	/**
	 * 통계 > 판매자별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<SellerStatsSummary> getSellerStatsList(StatisticsParam statisticsParam);

	/**
	 * 통계 > 상품별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<ItemStatsSummary> getItemStatsList(StatisticsParam statisticsParam);

	/**
	 * 통계 > 카테고리별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<CategoryStatsSummary> getCategoryStatsList(StatisticsParam statisticsParam);

	/**
	 * 통계 > 지역별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<AreaStatsSummary> getAreaStatsList(StatisticsParam statisticsParam);

	/**
	 * 통계 > 답례품 구매현황 > 월별 지자체별 답례품 현황
	 * @param statisticsParam
	 * @return
	 */
	List<MonthLocgovItemStat> getMonthLocgovItemStat(StatisticsParam statisticsParam);

	/**
	 * 통계 > 답례품 구매현황 > 카테고리별
	 * @param statisticsParam
	 * @return
	 */
	List<CategoriesStatsSummary> getCategoriesStatsList(StatisticsParam statisticsParam);

	/**
	 * 통계 > 답례품 구매현황 > 카테고리별
	 * @param statisticsParam
	 * @return Integer
	 */
	Integer getCategoriesStatsListCount(StatisticsParam statisticsParam);

	/**
	 * 답례품 신청건수/신청금액 현황(두간)
	 * @param statisticsParam : searchDate 조회일 yyyyMMdd 형태 ex) 20240523
	 * @return
	 */
	List<OrderCountAmtStat> getOrderCountAmtForWeek(StatisticsParam statisticsParam);

	/**
	 * 답례품 신청건수/신청금액 현황(월간)
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<OrderCountAmtStat> getOrderCountAmtForMonth(StatisticsParam statisticsParam);

	/**
	 * 금일 기준 전일, 전년 대비 주문 누적 금액, 주문 누적 건수 및 비율 조회
	 * @param statisticsParam
	 * @return
	 */
	List<OrderCountAmtStat> getOrderCountAmtRateForDay(StatisticsParam statisticsParam);

	/**
	 * 조회 월 기준 조회년도 1월 ~ 조회월 주문 금액, 주문 건수 데이터를 전년 1월 ~ 전년 조회월 주문 금액, 주문 건수와 비교 통계
	 * @param statisticsParam(startDate(필수) : 조회년도 1월 1일(20240101), endDate(필수) : 조회년도 조회월 마지막날(20240229), upperLocgovCode : 상위 지자체 코드, locgovCode : 지자체 코드)
	 * @return
	 */
	OrderCountAmtStatMonth getOrderCountAmtRateForMonth(StatisticsParam statisticsParam);

	/**
	 * 답례품 신청건수/신청금액 현황(월간) - 조회조건 추가
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	OrderCountAmtStat getStatisticsMonthInfo(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 현황 - 기준년도 리스트(회원)
	 * @return
	 */
	List<StatisticsReport> getCrtrYearForMbrList();

	/**
	 * 보고서 - 총괄 현황 - 기준년도 리스트(기부)
	 * @return
	 */
	List<StatisticsReport> getCrtrYearForDntnList();

	/**
	 * 보고서 - 총괄 현황 - 기준년도 리스트(답례품)
	 * @return
	 */
	List<StatisticsReport> getCrtrYearForGdsList();

	/**
	 * 보고서 - 총괄 현황 - 기준월 리스트(회원)
	 * @return
	 */
	List<StatisticsReport> getCrtrMonthForMbrList(String year);

	/**
	 * 보고서 - 총괄 현황 - 기준월 리스트(기부)
	 * @return
	 */
	List<StatisticsReport> getCrtrMonthForDntnList(String year);

	/**
	 * 보고서 - 총괄 현황 - 기준월 리스트(답례품)
	 * @return
	 */
	List<StatisticsReport> getCrtrMonthForGdsList(String year);

	/**
	 * 보고서 - 총괄 현황 - 회원수 현황
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListMbrTnocsStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 현황 - 총 기부건수
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListDntnTnocsStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 현황 - 총 기부건수(500만원)
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListDntnTnocs500Stats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 현황 - 총 한도금액 기부건수(2000만원)
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListDntnTnocsMaxAmtStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 현황 - 총 답례품건수
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListGdsTnocsStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 누계 현황 - 기부방법별 기부건수
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListDntnPathTnocsStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 누계 현황 - 기부금액별 기부건수
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListDntnAmtTnocsStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 누계 현황 - 기부연령별 기부건수
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListDntnAgeTnocsStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 총괄 누계 현황 - 거주지역->기부지역 별 기부건수
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListHabDntnMctpvTnocsStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 지자체별 현황 - 지자체별 기부금 현황
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListMctpvDntnStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 지자체별 현황 - 지자체별 기부금 현황(500만원)
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListMctpvDntn500Stats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 지자체별 현황 - 지자체별 한도금액 기부금 현황(2000만원)
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListMctpvDntnMaxAmtStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 지자체별 현황 - 지자체별 답례품제공 현황
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListMctpvGdsStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 지자체(243개) 현황 - 시도 리스트(기부)
	 * @return
	 */
	List<StatisticsReport> getMctpvForDntnList();

	/**
	 * 보고서 - 지자체(243개) 현황 - 시도 리스트(답례품)
	 * @return
	 */
	List<StatisticsReport> getMctpvForGdsList();

	/**
	 * 보고서 - 지자체(243개) 현황 - 시군구 리스트(기부)
	 * @return
	 */
	List<StatisticsReport> getLclgvForDntnList(String mctpv);

	/**
	 * 보고서 - 지자체(243개) 현황 - 시군구 리스트(답례품)
	 * @return
	 */
	List<StatisticsReport> getLclgvForGdsList(String mctpv);

	/**
	 * 보고서 - 지자체별 현황 - 지자체별(243개) 기부금 현황
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListLclgvAodStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 지자체별 현황 - 지자체별(243개) 기부금 현황(500만원)
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListLclgvAod500Stats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 지자체별 현황 - 지자체별(243개) 한도금액 기부금 현황(2000만원)
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListLclgvAodMaxAmtStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 지자체별 현황 - 지자체별(243개) 답례품제공 현황
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<StatisticsReport> getListLclgvGdsStats(StatisticsParam statisticsParam);

	/**
	 * 답례품 구매현황 - 농협 사업자 답례품 현황
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	List<NhItemSalesStatistics> getNhItemSalesStats(StatisticsParam statisticsParam);

	/**
	 * 답례품 구매현황 - 농협 사업자 답례품 현황 카운트
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	int getNhItemSalesStatsCount(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 연통계 총괄 건수
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnTnocsPastYearStats(StatisticsParam statisticsParam);//삭제대상isb

	/**
	 * 보고서 - 연통계 총괄 금액
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	List<HashMap<String, Object>> getListMctpvDntnPastYearStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 연통계 - 기부방법별 건수, 금액
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnPathPastYearStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 연통계 - 기부 금액별
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnAmtPastYearStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 연통계 - 기부 연령별
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	List<HashMap<String, Object>> getListAgeDntnAmtPastYearStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 연통계 - 기부 연령별
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	List<HashMap<String, Object>> getListMctpvUniqIdPastYearStats(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 연통계 - 답례품 현황: 인기답례품 현황(판매량순 상위 30개)
	 * @param statisticsParam : searchYear, searchMonth, locgovCode, upperLocgovCode, where, query
	 * @return
	 */
	List<HashMap<String, Object>> getListPubGoodsPastYearStats(StatisticsParam statisticsParam);


	/**
	 * 보고서 - 집계된 통계테이블select - 연통계 총괄 건수 금액
	 * @param statisticsParam :
	 * @return
	 */
	List<HashMap<String, Object>> getListMbrTnocsStatsStored(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 집계된 통계테이블select - 연통계 기부방법별
	 * @param statisticsParam :
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnPathPastYearStatsStored(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 집계된 통계테이블select - 연통계 기부금액별
	 * @param statisticsParam :
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnPricePastYearStatsStored(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 집계된 통계테이블select - 연통계 연령별
	 * @param statisticsParam :
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnAgesPastYearStatsStored(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 집계된 통계테이블select - 연통계 기부자 주소지 광역시 별
	 * @param statisticsParam :
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnPsintPastYearStatsStored(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 집계된 통계테이블select - 연통계 기부자 주소지 지역 별
	 * @param statisticsParam :
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnLocGovPastYearStatsStored(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 집계된 통계테이블select - 연통계 월별
	 * @param statisticsParam :
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnMonthPastYearStatsStored(StatisticsParam statisticsParam);

	/**
	 * 보고서 - 집계된 통계테이블select - 연통계 답례품 인기순
	 * @param statisticsParam :
	 * @return
	 */
	List<HashMap<String, Object>> getListDntnGoodsPastYearStatsStored(StatisticsParam statisticsParam);


}
