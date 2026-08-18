package saleson.shop.statistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.onlinepowers.framework.security.userdetails.User;
import com.onlinepowers.framework.util.JsonViewUtils;

import saleson.shop.analysis.AnalysisMapper;
import saleson.shop.give.givestate.GiveStateMapper;
import saleson.shop.item.domain.ItemOption;
import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.slave.SlaveShopStatisticsMapper;
import saleson.shop.statistics.domain.AreaStatsSummary;
import saleson.shop.statistics.domain.BaseStats;
import saleson.shop.statistics.domain.CategoryStatsSummary;
import saleson.shop.statistics.domain.CategoriesStatsSummary;
import saleson.shop.statistics.domain.DateStatsSummary;
import saleson.shop.statistics.domain.DoNotSellItem;
import saleson.shop.statistics.domain.ItemStatsSummary;
import saleson.shop.statistics.domain.MonthLocgovItemStat;
import saleson.shop.statistics.domain.NhItemSalesStatistics;
import saleson.shop.statistics.domain.PaymentStatistics;
import saleson.shop.statistics.domain.RevenueBaseForDate;
import saleson.shop.statistics.domain.RevenueDetail;
import saleson.shop.statistics.domain.RevenueDetailItem;
import saleson.shop.statistics.domain.SellerStatsSummary;
import saleson.shop.statistics.domain.ShopBrandStatistics;
import saleson.shop.statistics.domain.ShopItemDateStatistics;
import saleson.shop.statistics.domain.ShopItemDetailStatistics;
import saleson.shop.statistics.domain.ShopItemStatistics;
import saleson.shop.statistics.domain.ShopOrderStatistics;
import saleson.shop.statistics.domain.StatisticsReport;
import saleson.shop.statistics.domain.StatsSummary;
import saleson.shop.statistics.domain.TotalRevenueStatistics;
import saleson.shop.statistics.domain.Wishlist;
import saleson.shop.statistics.domain.order.OrderCountAmtStat;
import saleson.shop.statistics.domain.order.OrderCountAmtStatMonth;
import saleson.shop.statistics.domain.order.OrderCountAmtStatMonthLclgv;
import saleson.shop.statistics.support.StatisticsParam;
import saleson.shop.statistics.support.WishlistParam;

@Service("ShopStatisticsService")
public class ShopStatisticsServiceImpl extends EgovAbstractServiceImpl implements ShopStatisticsService {

	@Autowired
//	ShopStatisticsMapper shopStatisticsMapper;
	private SlaveShopStatisticsMapper shopStatisticsMapper;

	@Autowired
	private AnalysisMapper analysisMapper;

	@Autowired
	GiveStateMapper giveStateMapper;

	@Override
	public List<PaymentStatistics> getPaymentStatisticsListByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getPaymentStatisticsListByParam(statisticsParam);
	}


	private boolean hasOrderCode(List<String> shippingOrderCodes, String orderCode) {
		for (String shippingOrderCode : shippingOrderCodes) {
			if (shippingOrderCode.equals(orderCode)) {
				return true;
			}
		}
		return false;
	}
	@Override
	public List<RevenueBaseForDate> getRevenueDetailListForDateByParam(StatisticsParam statisticsParam) {

 		List<RevenueBaseForDate> list = shopStatisticsMapper.getRevenueDetailListForDateByParam(statisticsParam);
		List<RevenueDetail> shippingList = shopStatisticsMapper.getShippingDetailListForDateByParam(statisticsParam);

		if (list == null) {
			return null;
		}

		List<String> shippingOrderCodes = new ArrayList<>();

		for (RevenueBaseForDate revenueBaseForDate : list) {
			if (revenueBaseForDate.getList() == null) {
				continue;
			}

			for (RevenueDetail detail : revenueBaseForDate.getList()) {
				if (detail.getItems() == null) {
					continue;
				}

				// list에 ITEM_SEQUENCE로 조인해서 shippingList 정보 add[2017-04-06]minae.yun
				// [2017-04-28] 판매자별 보기에서는 배송비 노출하지 않음.
				for (RevenueDetail shippingDetail : shippingList) {
					if (detail.getOrderCode().equals(shippingDetail.getOrderCode()) && detail.getOrderType().equals(shippingDetail.getOrderType())
							&& revenueBaseForDate.getKey().equals(shippingDetail.getDate()) ) {

						detail.setPrice(shippingDetail.getPrice());
						detail.setCartCouponDiscountAmount(shippingDetail.getCartCouponDiscountAmount());

						if (statisticsParam.getSellerId() > 0) {
							detail.getItems().get(0).setOrderShipping(0);
                            detail.getItems().get(0).setSubTotal(shippingDetail.getItemAmount());
						} else {
							String shippingKey = revenueBaseForDate.getKey() + ":" + detail.getOrderType() + ":" + detail.getOrderCode();

							if (!hasOrderCode(shippingOrderCodes, shippingKey) && detail.getItems().size() > 0) {
								detail.getItems().get(0).setOrderShipping(shippingDetail.getSumDeliveryPrice());
                                detail.getItems().get(0).setSubTotal(shippingDetail.getItemAmount() + shippingDetail.getSumDeliveryPrice());
								shippingOrderCodes.add(shippingKey);
							}
						}

					}
				}

				for (RevenueDetailItem orderItem : detail.getItems()) {
					// 필수 옵션
					if (!ObjectUtils.isEmpty(orderItem.getRequiredOptions())) {
						if (!"Y".equals(orderItem.getSetItemFlag()) && orderItem.getRequiredOptions().startsWith("[")) {
							List<ItemOption> requiredOptionsList = (List<ItemOption>) JsonViewUtils.jsonToObject(orderItem.getRequiredOptions(), new TypeReference<List<ItemOption>>(){});

							if (!requiredOptionsList.isEmpty()) {
								orderItem.setRequiredOptionsList(requiredOptionsList);
							}
						} else {
							orderItem.setOpenMarketOption(orderItem.getRequiredOptions());
						}
					}
				}
			}
		}

		return list;
	}

	@Override
	public TotalRevenueStatistics getUserTotalRevenueStatisticsByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getUserTotalRevenueStatisticsByParam(statisticsParam);
	}

	@Override
	public List<ShopItemDetailStatistics> getShopItemDetailList(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getShopItemDetailList(statisticsParam);
	}

	@Override
	public List<ShopOrderStatistics> getOrderListByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getOrderListByParam(statisticsParam);
	}

	@Override
	public List<ShopItemDetailStatistics> getAreaDetailList(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getAreaDetailList(statisticsParam);
	}

	@Override
	public List<ShopItemDetailStatistics> getUserStatisticsListByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getUserStatisticsListByParam(statisticsParam);
	}

	@Override
	public int getUserStatisticsCountByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getUserStatisticsCountByParam(statisticsParam);
	}

	@Override
	public List<ShopItemStatistics> getUserOrderItemListByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getUserOrderItemListByParam(statisticsParam);
	}

	@Override
	public OrderShippingInfo getUsetOrderTotalDetailById(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getUsetOrderTotalDetailById(statisticsParam);
	}

	@Override
	public List<ShopOrderStatistics> getUserOrderListByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getUserOrderListByParam(statisticsParam);
	}

	@Override
	public List<DoNotSellItem> getDoNotSellItemListByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getDoNotSellItemListByParam(statisticsParam);
	}

	@Override
	public List<User> getNotUserList(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getNotUserList(statisticsParam);
	}

	@Override
	public int getNotUserCount(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getNotUserCount(statisticsParam);
	}

	@Override
	public ShopItemStatistics getAreaCountParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getAreaCountParam(statisticsParam);
	}

	@Override
	public int getDoNotSellItemCountByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getDoNotSellItemCountByParam(statisticsParam);
	}

	@Override
	public List<ShopItemDateStatistics> getItemDateListByParam(
			StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getItemDateListByParam(statisticsParam);
	}

	@Override
	public List<ShopBrandStatistics> getBrandStatisticsListByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getBrandStatisticsListByParam(statisticsParam);
	}

	@Override
	public List<RevenueBaseForDate> getBrandStatisticsDetailByParam(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getBrandStatisticsDetailByParam(statisticsParam);
	}

	@Override
	public StatsSummary getSellerStatsSummary(StatisticsParam statisticsParam) {
		List<BaseStats> baseStats =  shopStatisticsMapper.getSellerStats(statisticsParam);

		return new StatsSummary(baseStats);
	}

	@Override
	public StatsSummary getItemStatsSummary(StatisticsParam statisticsParam) {
		List<BaseStats> baseStats =  shopStatisticsMapper.getItemStats(statisticsParam);

		return new StatsSummary(baseStats);
	}

	@Override
	public List<DateStatsSummary> getDateStatsList(StatisticsParam statisticsParam) {

		statisticsParam.setExtra("DATE");

		return shopStatisticsMapper.getDateStatsList(statisticsParam);
	}

	@Override
	public int getDateStatsListCountGhlove(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getDateStatsListCountGhlove(statisticsParam);
	}

	@Override
	public List<DateStatsSummary> getDateStatsListGhlove(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getDateStatsListGhlove(statisticsParam);
	}

	@Override
	public int getOpWishlistListCount(WishlistParam wishlistParam) {
		return shopStatisticsMapper.getOpWishlistListCount(wishlistParam);
	}

	@Override
	public List<Wishlist> getOpWishlistList(WishlistParam wishlistParam) {
		return shopStatisticsMapper.getOpWishlistList(wishlistParam);
	}

	@Override
	public List<SellerStatsSummary> getSellerStatsList(StatisticsParam statisticsParam) {
		statisticsParam.setExtra("SELLER");

		return shopStatisticsMapper.getSellerStatsList(statisticsParam);
	}

	@Override
	public List<ItemStatsSummary> getItemStatsList(StatisticsParam statisticsParam) {
		statisticsParam.setExtra("ITEM");

		List<ItemStatsSummary> itemStatsList = shopStatisticsMapper.getItemStatsList(statisticsParam);

		if (!ObjectUtils.isEmpty(statisticsParam.getOrderBy())) {
			if ("QUANTITY".equals(statisticsParam.getOrderBy())) {
				if ("ASC".equals(statisticsParam.getSort())) {
					itemStatsList = itemStatsList.stream().sorted(Comparator.comparing((ItemStatsSummary::getSubTotalCount))).collect(Collectors.toList());
				} else {
					itemStatsList = itemStatsList.stream().sorted(Comparator.comparing((ItemStatsSummary::getSubTotalCount), Comparator.reverseOrder())).collect(Collectors.toList());
				}
			} else {
				if ("ASC".equals(statisticsParam.getSort())) {
					itemStatsList = itemStatsList.stream().sorted(Comparator.comparing((ItemStatsSummary::getSubTotalAmount))).collect(Collectors.toList());
				} else {
					itemStatsList = itemStatsList.stream().sorted(Comparator.comparing((ItemStatsSummary::getSubTotalAmount), Comparator.reverseOrder())).collect(Collectors.toList());
				}
			}
		}

		return itemStatsList;
	}

	@Override
	public List<CategoryStatsSummary> getCategoryStatsList(StatisticsParam statisticsParam) {
		statisticsParam.setExtra("CATEGORY");

		return shopStatisticsMapper.getCategoryStatsList(statisticsParam)
				.stream().sorted(Comparator.comparing((CategoryStatsSummary::getSubTotalAmount), Comparator.reverseOrder())).collect(Collectors.toList());
	}

	@Override
	public List<CategoriesStatsSummary> getCategoriesStatsList(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getCategoriesStatsList(statisticsParam);
	}

	@Override
	public Integer getCategoriesStatsListCount(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getCategoriesStatsListCount(statisticsParam);
	}

	@Override
	public List<AreaStatsSummary> getAreaStatsList(StatisticsParam statisticsParam) {
		statisticsParam.setExtra("AREA");

		List<AreaStatsSummary> areaStatsList = shopStatisticsMapper.getAreaStatsList(statisticsParam);

		if (!ObjectUtils.isEmpty(statisticsParam.getOrderBy())) {
			if ("QUANTITY".equals(statisticsParam.getOrderBy())) {
				if ("ASC".equals(statisticsParam.getSort())) {
					areaStatsList = areaStatsList.stream().sorted(Comparator.comparing((AreaStatsSummary::getSubTotalCount))).collect(Collectors.toList());
				} else {
					areaStatsList = areaStatsList.stream().sorted(Comparator.comparing((AreaStatsSummary::getSubTotalCount), Comparator.reverseOrder())).collect(Collectors.toList());
				}
			} else {
				if ("ASC".equals(statisticsParam.getSort())) {
					areaStatsList = areaStatsList.stream().sorted(Comparator.comparing((AreaStatsSummary::getSubTotalAmount))).collect(Collectors.toList());
				} else {
					areaStatsList = areaStatsList.stream().sorted(Comparator.comparing((AreaStatsSummary::getSubTotalAmount), Comparator.reverseOrder())).collect(Collectors.toList());
				}
			}
		}

		return areaStatsList;
	}


	@Override
	public List<MonthLocgovItemStat> getMonthLocgovItemStat(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getMonthLocgovItemStat(statisticsParam);
	}


	@Override
	public List<OrderCountAmtStat> getOrderCountAmtForWeek(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getOrderCountAmtForWeek(statisticsParam);
	}


	@Override
	public List<OrderCountAmtStat> getOrderCountAmtForMonth(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getOrderCountAmtForMonth(statisticsParam);
	}


	@Override
	public List<OrderCountAmtStat> getOrderCountAmtRateForDay(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getOrderCountAmtRateForDay(statisticsParam);
	}


	@Override
	public OrderCountAmtStatMonth getOrderCountAmtRateForMonth(StatisticsParam statisticsParam) {
		OrderCountAmtStatMonth result = new OrderCountAmtStatMonth();
		List<OrderCountAmtStatMonthLclgv> searchList = shopStatisticsMapper.getOrderCountAmtRateForMonth(statisticsParam);
		result.setOrderCountAmtStatMonthLclgvList(searchList);
		return result;
	}

	@Override
	public List<StatisticsReport> getCrtrYearForMbrList() {
		return analysisMapper.getCrtrYearForMbrList();
	}

	@Override
	public List<StatisticsReport> getCrtrYearForDntnList() {
		return analysisMapper.getCrtrYearForDntnList();
	}

	@Override
	public List<StatisticsReport> getCrtrYearForGdsList() {
		return analysisMapper.getCrtrYearForGdsList();
	}

	@Override
	public List<StatisticsReport> getCrtrMonthForMbrList(String year) {
		return analysisMapper.getCrtrMonthForMbrList(year);
	}

	@Override
	public List<StatisticsReport> getCrtrMonthForDntnList(String year) {
		return analysisMapper.getCrtrMonthForDntnList(year);
	}

	@Override
	public List<StatisticsReport> getCrtrMonthForGdsList(String year) {
		return analysisMapper.getCrtrMonthForGdsList(year);
	}

	@Override
	public OrderCountAmtStat getStatisticsMonthInfo(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getStatisticsMonthInfo(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListMbrTnocsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListMbrTnocsStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListDntnTnocsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnTnocsStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListDntnTnocs500Stats(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnTnocs500Stats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListDntnTnocsMaxAmtStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnTnocsMaxAmtStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListGdsTnocsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListGdsTnocsStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListDntnPathTnocsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnPathTnocsStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListDntnAmtTnocsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnAmtTnocsStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListDntnAgeTnocsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnAgeTnocsStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListHabDntnMctpvTnocsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListHabDntnMctpvTnocsStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListMctpvDntnStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListMctpvDntnStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListMctpvDntn500Stats(StatisticsParam statisticsParam) {
		return analysisMapper.getListMctpvDntn500Stats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListMctpvDntnMaxAmtStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListMctpvDntnMaxAmtStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListMctpvGdsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListMctpvGdsStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getMctpvForDntnList() {
		return analysisMapper.getMctpvForDntnList();
	}

	@Override
	public List<StatisticsReport> getMctpvForGdsList() {
		return analysisMapper.getMctpvForGdsList();
	}

	@Override
	public List<StatisticsReport> getLclgvForDntnList(String mctpv) {
		return analysisMapper.getLclgvForDntnList(mctpv);
	}

	@Override
	public List<StatisticsReport> getLclgvForGdsList(String mctpv) {
		return analysisMapper.getLclgvForGdsList(mctpv);
	}

	@Override
	public List<StatisticsReport> getListLclgvAodStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListLclgvAodStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListLclgvAod500Stats(StatisticsParam statisticsParam) {
		return analysisMapper.getListLclgvAod500Stats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListLclgvAodMaxAmtStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListLclgvAodMaxAmtStats(statisticsParam);
	}

	@Override
	public List<StatisticsReport> getListLclgvGdsStats(StatisticsParam statisticsParam) {
		return analysisMapper.getListLclgvGdsStats(statisticsParam);
	}


	@Override
	public List<NhItemSalesStatistics> getNhItemSalesStats(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getNhItemSalesStats(statisticsParam);
	}


	@Override
	public int getNhItemSalesStatsCount(StatisticsParam statisticsParam) {
		return shopStatisticsMapper.getNhItemSalesStatsCount(statisticsParam);
	}

	/**
	 * 연통계 보고서 - 1-1. 기부현황 총괄 : 건수
	 *
	 * 현재펑션 삭제예정
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnTnocsPastYearStats(StatisticsParam statisticsParam) {
		// SYSDATE YEAR 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar c1 = Calendar.getInstance();
		String sysYear = sdf.format(c1.getTime());

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", statisticsParam.getShYear());
		hashMap.put("sysYear", sysYear);
		hashMap.put("paramPrvYr", "");
		return shopStatisticsMapper.getListForDntnTnocsStats(hashMap);//기존의 총괄 통계배치(ShopStatisticsBatchServiceImpl.java)에서 사용하는 매퍼 그대로 사용
	}

	/**
	 * 연통계 보고서 - 1-2. 기부현황 총괄 : 금액
	 *
	 * 현재펑션 삭제예정
	 */
	@Override
	public List<HashMap<String, Object>> getListMctpvDntnPastYearStats(StatisticsParam statisticsParam) {
		// SYSDATE YEAR 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar c1 = Calendar.getInstance();
		String sysYear = sdf.format(c1.getTime());

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", statisticsParam.getShYear());
		hashMap.put("sysYear", sysYear);
		hashMap.put("paramPrvYr", "");
		return shopStatisticsMapper.getListForMctpvDntnStats(hashMap);//기존의 총괄 통계배치(ShopStatisticsBatchServiceImpl.java)에서 사용하는 매퍼 그대로 사용
	}

	/**
	 * 연통계 보고서 - 2. 기부현황 : 기부방법별 기부건수,기부금액
	 *
	 * 현재펑션 삭제예정
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnPathPastYearStats(StatisticsParam statisticsParam) {
		// SYSDATE YEAR 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar c1 = Calendar.getInstance();
		String sysYear = sdf.format(c1.getTime());

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", statisticsParam.getShYear());
		hashMap.put("sysYear", sysYear);
		hashMap.put("crtrMm", "12");//연통계 1년치
		hashMap.put("paramPrvYr", "");
		return shopStatisticsMapper.getListForDntnPathTnocsStats(hashMap);//기존의 총괄 통계배치(ShopStatisticsBatchServiceImpl.java)에서 사용하는 매퍼 그대로 사용
	}

	/**
	 * 연통계 보고서 - 3. 기부현황 : 기부 금액별 건수
	 *
	 * 현재펑션 삭제예정
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnAmtPastYearStats(StatisticsParam statisticsParam) {
		// SYSDATE YEAR 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar c1 = Calendar.getInstance();
		String sysYear = sdf.format(c1.getTime());

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", statisticsParam.getShYear());
		hashMap.put("sysYear", sysYear);
		hashMap.put("crtrMm", "12");//연통계 1년치
		hashMap.put("paramPrvYr", "");
		return giveStateMapper.getListPastYearForDntnAmtTnocsStats(hashMap);//기존 쿼리와 차이가 있어서 새로 쿼리 추가함
	}

	/**
	 * 연통계 보고서 - 4. 기부현황 : 기부 연령별 건수
	 *
	 * 현재펑션 삭제예정
	 */
	@Override
	public List<HashMap<String, Object>> getListAgeDntnAmtPastYearStats(StatisticsParam statisticsParam) {
		// SYSDATE YEAR 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar c1 = Calendar.getInstance();
		String sysYear = sdf.format(c1.getTime());

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", statisticsParam.getShYear());
		hashMap.put("sysYear", sysYear);
		hashMap.put("crtrMm", "12");//연통계 1년치
		hashMap.put("paramPrvYr", "");
		return giveStateMapper.getListPastYearForAgeDntnAmtTnocsStats(hashMap);//기존 쿼리와 차이가 있어서 새로 쿼리 추가함
	}

	/**
	 * 연통계 보고서 - 6. 기부현황 (월별건수금액) : 금액, 고유아이디
	 *
	 * 현재펑션 삭제예정
	 */
	@Override
	public List<HashMap<String, Object>> getListMctpvUniqIdPastYearStats(StatisticsParam statisticsParam) {
		// SYSDATE YEAR 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar c1 = Calendar.getInstance();
		String sysYear = sdf.format(c1.getTime());

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", statisticsParam.getShYear());
		hashMap.put("sysYear", sysYear);
		hashMap.put("crtrMm", "12");//연통계 1년치
		hashMap.put("paramPrvYr", "");
		return giveStateMapper.getListForMctpvUniqIdPastYearStats(hashMap);//기존 쿼리와 차이가 있어서 새로 쿼리 추가함

	}

	/**
	 * 연통계 보고서 - 7. 답례품현황 : 인기답례품 현황(판매량순 상위 30개)m
	 *
	 * 현재펑션 삭제예정
	 */
	@Override
	public List<HashMap<String, Object>> getListPubGoodsPastYearStats(StatisticsParam statisticsParam) {
		// SYSDATE YEAR 구하기
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		Calendar c1 = Calendar.getInstance();
		String sysYear = sdf.format(c1.getTime());

		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("crtrYr", statisticsParam.getShYear());
		hashMap.put("sysYear", sysYear);
		hashMap.put("crtrMm", "12");//연통계 1년치
		hashMap.put("paramPrvYr", "");
		return giveStateMapper.getListForPubGoodsPastYearStats(hashMap);
	}

	/**
	 * 연통계 보고서 집계된 통계테이블 select - 1. - 연통계 총괄 건수 금액
	 */
	@Override
	public List<HashMap<String, Object>> getListMbrTnocsStatsStored(StatisticsParam statisticsParam) {
		return analysisMapper.getListMbrTnocsStatsStored(statisticsParam);
	}
	/**
	 * 연통계 보고서 집계된 통계테이블 select - 1. - 연통계 기부방법별
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnPathPastYearStatsStored(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnPathPastYearStatsStored(statisticsParam);
	}
	/**
	 * 연통계 보고서 집계된 통계테이블 select - 1. - 연통계 기부금액별
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnPricePastYearStatsStored(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnPricePastYearStatsStored(statisticsParam);
	}
	/**
	 * 연통계 보고서 집계된 통계테이블 select - 1. - 연통계 연령별
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnAgesPastYearStatsStored(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnAgesPastYearStatsStored(statisticsParam);
	}
	/**
	 * 연통계 보고서 집계된 통계테이블 select - 1. - 기부 기부자 주소지_광역시 별
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnPsintPastYearStatsStored(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnPsintPastYearStatsStored(statisticsParam);
	}
	/**
	 * 연통계 보고서 집계된 통계테이블 select - 1. - 기부 기부자 주소지_지역 별
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnLocGovPastYearStatsStored(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnLocGovPastYearStatsStored(statisticsParam);
	}
	/**
	 * 연통계 보고서 집계된 통계테이블 select - 1. - 연통계 월별
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnMonthPastYearStatsStored(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnMonthPastYearStatsStored(statisticsParam);
	}
	/**
	 * 연통계 보고서 집계된 통계테이블 select - 1. - 연통계 답례품 인기순
	 */
	@Override
	public List<HashMap<String, Object>> getListDntnGoodsPastYearStatsStored(StatisticsParam statisticsParam) {
		return analysisMapper.getListDntnGoodsPastYearStatsStored(statisticsParam);
	}

}
