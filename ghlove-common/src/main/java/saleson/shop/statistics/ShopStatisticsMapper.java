
package saleson.shop.statistics;

import java.util.List;

import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.statistics.domain.*;
import saleson.shop.statistics.domain.order.OrderCountAmtStat;
import saleson.shop.statistics.support.StatisticsParam;
import saleson.shop.statistics.support.WishlistParam;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.security.userdetails.User;

@Mapper("ShopStatistics")
public interface ShopStatisticsMapper {



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
	OrderCountAmtStat getOrderCountAmtRateForDay(StatisticsParam statisticsParam);
	
	/**
	 * 답례품 신청건수/신청금액 현황(월간) - 조회조건 추가
	 * @param statisticsParam : searchDate 조회년도 yyyy 형태 ex) 2024
	 * @return
	 */
	OrderCountAmtStat getStatisticsMonthInfo(StatisticsParam statisticsParam);
}
