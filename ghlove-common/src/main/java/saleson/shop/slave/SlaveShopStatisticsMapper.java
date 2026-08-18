
package saleson.shop.slave;

import java.util.HashMap;
import java.util.List;

import saleson.common.configuration.MapperSlave;
import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.statistics.domain.*;
import saleson.shop.statistics.domain.order.OrderCountAmtStat;
import saleson.shop.statistics.domain.order.OrderCountAmtStatMonthLclgv;
import saleson.shop.statistics.support.StatisticsParam;
import saleson.shop.statistics.support.WishlistParam;

import com.onlinepowers.framework.security.userdetails.User;

@MapperSlave("slaveShopStatistics")
public interface SlaveShopStatisticsMapper {



	/**
	 * 결제 타입별 통계
	 * @param statisticsParam
	 * @return
	 */
	List<PaymentStatistics> getPaymentStatisticsListByParam(StatisticsParam statisticsParam);

	/**
	 * 회원별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<ShopItemDetailStatistics> getUserStatisticsListByParam(StatisticsParam statisticsParam);

	/**
	 * 회원별 통계 페이징용 카운트
	 * @param statisticsParam
	 * @return
	 */
	int getUserStatisticsCountByParam(StatisticsParam statisticsParam);

	/**
	 * 회원별 통계 합계 - 페이징 때문에 쿼리로 처리함.
	 * @param statisticsParam
	 * @return
	 */
	TotalRevenueStatistics getUserTotalRevenueStatisticsByParam(StatisticsParam statisticsParam);

	/**
	 * 브랜드별 통계 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<ShopBrandStatistics> getBrandStatisticsListByParam(StatisticsParam statisticsParam);

	/**
	 * 안팔린 상품 카운트 - 페이징용
	 * @param statisticsParam
	 * @return
	 */
	int getDoNotSellItemCountByParam(StatisticsParam statisticsParam);

	/**
	 * 안팔린 상품 리스트
	 * @param statisticsParam
	 * @return
	 */
	List<DoNotSellItem> getDoNotSellItemListByParam(StatisticsParam statisticsParam);

	/**
	 * 매출 통계 상세 화면 - 일자별
	 * @param statisticsParam
	 * @return
	 */
	List<RevenueBaseForDate> getRevenueDetailListForDateByParam(StatisticsParam statisticsParam);

	List<RevenueDetail> getShippingDetailListForDateByParam(StatisticsParam statisticsParam);

	List<ShopItemDetailStatistics> getShopItemDetailList(StatisticsParam statisticsParam);

	List<ShopOrderStatistics> getOrderListByParam(StatisticsParam statisticsParam);



	List<ShopItemDetailStatistics> getAreaDetailList(StatisticsParam statisticsParam);



	List<ShopItemStatistics> getUserOrderItemListByParam(StatisticsParam statisticsParam);

	OrderShippingInfo getUsetOrderTotalDetailById(StatisticsParam statisticsParam);

	List<ShopOrderStatistics> getUserOrderListByParam(StatisticsParam statisticsParam);



	List<User> getNotUserList(StatisticsParam statisticsParam);

	int getNotUserCount(StatisticsParam statisticsParam);

	ShopItemStatistics getAreaCountParam(StatisticsParam statisticsParam);



	List<ShopItemDateStatistics> getItemDateListByParam(StatisticsParam statisticsParam);

	List<RevenueBaseForDate> getBrandStatisticsDetailByParam(StatisticsParam statisticsParam);

	/**
	 * 통계 > 판매자별 통계 요약 정보
	 * @param statisticsParam
	 * @return
	 */
	List<BaseStats> getSellerStats(StatisticsParam statisticsParam);

	/**
	 * 통계 > 상품별 통계 요약 정보
	 * @param statisticsParam
	 * @return
	 */
	List<BaseStats> getItemStats(StatisticsParam statisticsParam);

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
	 * <pre>
	 * comment       : 통계 > 고향사랑e음 통계 리스트 페이징처리 제거
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 12.
	 *
	 * </pre>
	 * @param statisticsParam
	 * @return
	 * List<DateStatsSummary>
	 */
	List<DateStatsSummary> getDateStatsListGhloveAnalysis(StatisticsParam statisticsParam);


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
	 * 금일 기준 전일, 전년 대비 주문 누적 금액, 주문 누적 건수 및 비율 조회
	 * @param statisticsParam
	 * @return
	 */
	List<OrderCountAmtStatMonthLclgv> getOrderCountAmtRateForMonth(StatisticsParam statisticsParam);

	/**
	 * 답례품 신청건수/신청금액 현황(월간) - 조회조건 추가
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	OrderCountAmtStat getStatisticsMonthInfo(StatisticsParam statisticsParam);

	/**
	 * 통계 현황보고 - 조회할 연월 리스트 조회 (총 회원수)
	 * @param paramDate : 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<HashMap<String, Object>> getParamDateListForMbrTnocsStats(String paramDate);

	/**
	 * 통계 현황보고 - 1. 총 회원수(월별) 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForMbrTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 조회할 연월 리스트 조회 (총 기부건수)
	 * @param paramDate : 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<HashMap<String, Object>> getParamDateListForDntnTnocsStats(String paramDate);

	/**
	 * 통계 현황보고 - 조회할 연월 리스트 조회 (총 기부건수)
	 * @param paramDate : 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<HashMap<String, Object>> getParamDateListForTotalDntnTnocsStats(String paramDate);

	/**
	 * 통계 현황보고 - 2. 총 기부건수(월별) 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForDntnTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 3-1. 총 기부건수-500만(월별) 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForDntnTnocs500Stats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 3-2. 총 기부건수-2000만(월별) 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForDntnTnocsMaxAmtStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 조회할 연월 리스트 조회 (총 답례품건수)
	 * @param paramDate : 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	List<HashMap<String, Object>> getParamDateListForGdsTnocsStats(String paramDate);

	/**
	 * 통계 현황보고 - 4. 총 답례품건수 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForGdsTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 5. 지자체별 기부현황 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForMctpvDntnStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 6-1. 지자체별 기부현황-500만원 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForMctpvDntn500Stats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 6-2. 지자체별 한도금액 기부현황-2000만원 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForMctpvDntnMaxAmtStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 7. 243개 지자체별 기부금현황 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForLclgvAodStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 8-1. 243개 지자체별 기부금현황-500만원 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForLclgvAod500Stats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 8-2. 243개 지자체별 한도금액 기부금현황-2000만원 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForLclgvAodMaxAmtStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 9. 지자체별 답례품제공현황 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForMctpvGdsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 현황보고 - 10. 243개 지자체별 답례품제공현황 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForLclgvGdsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 1. 기부방법별 기부건수 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForDntnPathTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 2. 기부금액별 기부건수 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForDntnAmtTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 3. 연령별 기부건수 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForDntnAgeTnocsStats(HashMap<String, Object> hashMap);

	/**
	 * 통계 누계현황보고 - 4. 거주지역 및 기부지역 별 기부건수 조회
	 * @param hashMap : 조회년월 yyyymm 형태 ex) 202407
	 * @return
	 */
	List<HashMap<String, Object>> getListForHabDntnMctpvTnocsStats(HashMap<String, Object> hashMap);

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


}
