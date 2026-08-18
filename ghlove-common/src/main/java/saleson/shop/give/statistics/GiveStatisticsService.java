package saleson.shop.give.statistics;

import java.util.List;
import java.util.Map;

import saleson.shop.give.statistics.domain.GiveOperate;
import saleson.shop.give.statistics.domain.GiveOperateSearch;
import saleson.shop.give.statistics.domain.GiveStatistics;
import saleson.shop.give.statistics.domain.GiveStatisticsDetailSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsPersonal;
import saleson.shop.give.statistics.domain.GiveStatisticsSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsSeparate;
import saleson.shop.give.statistics.domain.GiveStatisticsTotal;
import saleson.shop.give.statistics.domain.GiveStatisticsTotalSearch;

public interface GiveStatisticsService {
	
	public GiveStatisticsTotal getGiveStatisticsAllCount(GiveStatisticsTotalSearch search);
	
	/**
	 * 
	 * @param search
	 * @return
	 */
	public int getGivePersonStatisticsCount(GiveStatisticsSearch search);
	
	public List<GiveStatisticsTotal> statisticsGiveAllTotalByMonth(GiveStatisticsTotalSearch search);
	
	public List<GiveStatisticsTotal> statisticsGiveAllTotalByAge(GiveStatisticsTotalSearch search);
	
	public List<GiveStatisticsTotal> statisticsGiveAllTotalByHour(GiveStatisticsTotalSearch search);
	
	public List<GiveStatistics> getGivePersonStatisticsList(GiveStatisticsSearch search);
	
	public List<GiveStatistics> getGivePersonStatisticsDetail(GiveStatisticsDetailSearch search);
	
	public List<GiveStatistics> getGivePersonStatisticsDetailByDate(GiveStatisticsDetailSearch search); 
	
	public List<GiveStatistics> getGivePersonStatisticsDetailExcel(GiveStatisticsDetailSearch search);
	
	public List<GiveStatistics> getGiveAmountStatisticsDetail(GiveStatisticsDetailSearch search);
	
	public List<GiveStatistics> getGiveAmountStatisticsDetailByDate(GiveStatisticsDetailSearch search);
	
	public List<GiveStatistics> getGiveNumberStatisticsDetail(GiveStatisticsDetailSearch search);
	
	public List<GiveStatistics> getGiveNumberStatisticsDetailByDate(GiveStatisticsDetailSearch search);
	
	public List<GiveStatisticsPersonal> getGivePersonalStatisticsDetail(GiveStatisticsDetailSearch search);
	
	public GiveStatisticsPersonal getGivePersonalStatisticsDetailTotal(GiveStatisticsDetailSearch search);
	
	public List<GiveStatisticsPersonal> getGivePersonalStatisticsDetailByUser(GiveStatisticsDetailSearch search);
	
	public List<GiveStatistics> getGiveDateStatisticsDetail(GiveStatisticsDetailSearch search);
	
	public GiveStatistics getGiveDateStatisticsDetailTotal(GiveStatisticsDetailSearch search);
	
	public List<GiveStatisticsSeparate> getGiveDateStatisticsDetailByDate(GiveStatisticsDetailSearch search);
	
	public int getGiveOperateListCount(GiveOperateSearch search);
	
	public List<GiveOperate> getGiveOperateList(GiveOperateSearch search);
	
	public List<GiveOperate> getGiveOperateListByLocgov(GiveOperateSearch search);

}
