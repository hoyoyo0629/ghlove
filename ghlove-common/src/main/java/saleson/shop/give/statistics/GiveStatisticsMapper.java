package saleson.shop.give.statistics;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.give.statistics.domain.GiveOperate;
import saleson.shop.give.statistics.domain.GiveOperateSearch;
import saleson.shop.give.statistics.domain.GiveStatistics;
import saleson.shop.give.statistics.domain.GiveStatisticsDetailSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsPersonal;
import saleson.shop.give.statistics.domain.GiveStatisticsSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsSeparate;
import saleson.shop.give.statistics.domain.GiveStatisticsTotal;
import saleson.shop.give.statistics.domain.GiveStatisticsTotalSearch;

@Mapper("giveStatisticsMapper")
public interface GiveStatisticsMapper {

	GiveStatisticsTotal getGiveStatisticsAllCount(GiveStatisticsTotalSearch search);

	int getGivePersonStatisticsCount(GiveStatisticsSearch search);

	List<GiveStatisticsTotal> statisticsGiveAllTotalByMonth(GiveStatisticsTotalSearch search);

	List<GiveStatisticsTotal> statisticsGiveAllTotalByAge(GiveStatisticsTotalSearch search);

	List<GiveStatisticsTotal> statisticsGiveAllTotalByHour(GiveStatisticsTotalSearch search);

	List<GiveStatisticsTotal> statisticsGiveLocgovByMonth(GiveStatisticsTotalSearch search);

	List<GiveStatisticsTotal> statisticsGiveLocgovByAge(GiveStatisticsTotalSearch search);

	List<GiveStatisticsTotal> statisticsGiveLocgovByHour(GiveStatisticsTotalSearch search);

	List<GiveStatistics> getGivePersonStatisticsList(GiveStatisticsSearch search);

	List<GiveStatistics> getGivePersonStatisticsDetail(GiveStatisticsDetailSearch search);

	List<GiveStatistics> getGivePersonStatisticsDetailByDate(GiveStatisticsDetailSearch search);

	List<GiveStatistics> getGivePersonStatisticsDetailExcel(GiveStatisticsDetailSearch search);

	List<GiveStatistics> getGiveAmountStatisticsDetail(GiveStatisticsDetailSearch search);

	List<GiveStatistics> getGiveAmountStatisticsDetailByDate(GiveStatisticsDetailSearch search);

	List<GiveStatistics> getGiveNumberStatisticsDetail(GiveStatisticsDetailSearch search);

	List<GiveStatistics> getGiveNumberStatisticsDetailByDate(GiveStatisticsDetailSearch search);

	List<GiveStatisticsPersonal> getGivePersonalStatisticsDetail(GiveStatisticsDetailSearch search);

	GiveStatisticsPersonal getGivePersonalStatisticsDetailTotal(GiveStatisticsDetailSearch search);

	List<GiveStatisticsPersonal> getGivePersonalStatisticsDetailByUser(GiveStatisticsDetailSearch search);

	List<GiveStatistics> getGiveDateStatisticsDetail(GiveStatisticsDetailSearch search);

	GiveStatistics getGiveDateStatisticsDetailTotal(GiveStatisticsDetailSearch search);

	List<GiveStatisticsSeparate> getGiveDateStatisticsDetailByDate(GiveStatisticsDetailSearch search);

	int getGiveOperateListCount(GiveOperateSearch search);

	List<GiveOperate> getGiveOperateList(GiveOperateSearch search);

	List<GiveOperate> getGiveOperateListByLocgov(GiveOperateSearch search);

}
