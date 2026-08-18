package saleson.shop.give.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.statistics.domain.GiveOperate;
import saleson.shop.give.statistics.domain.GiveOperateSearch;
import saleson.shop.give.statistics.domain.GiveStatistics;
import saleson.shop.give.statistics.domain.GiveStatisticsDetailSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsPersonal;
import saleson.shop.give.statistics.domain.GiveStatisticsSearch;
import saleson.shop.give.statistics.domain.GiveStatisticsSeparate;
import saleson.shop.give.statistics.domain.GiveStatisticsTotal;
import saleson.shop.give.statistics.domain.GiveStatisticsTotalSearch;

@Service("giveStatisticsService")
public class GiveStatisticsServiceImpl implements GiveStatisticsService {

	@Autowired
	private GiveStatisticsMapper giveStatisticsMapper; 
	
	
	@Override
	public GiveStatisticsTotal getGiveStatisticsAllCount(GiveStatisticsTotalSearch search) {
		return giveStatisticsMapper.getGiveStatisticsAllCount(search);
	}
	
	@Override
	public int getGivePersonStatisticsCount(GiveStatisticsSearch search) {
		return giveStatisticsMapper.getGivePersonStatisticsCount(search);
	}
	
	@Override
	public List<GiveStatisticsTotal> statisticsGiveAllTotalByMonth(GiveStatisticsTotalSearch search) {
		
		List<GiveStatisticsTotal> list = giveStatisticsMapper.statisticsGiveAllTotalByMonth(search);
		List<GiveStatisticsTotal> result = new ArrayList<GiveStatisticsTotal>();
		
		for (int i = 1; i <= 12; i++) {
			GiveStatisticsTotal data = null;
			
			for (GiveStatisticsTotal gs : list) {
				if (gs.getCntrMonth().equals(i + "")) {
					data = gs;
					break;
				}
			}
			
			if (data == null) {
				data = new GiveStatisticsTotal();
				data.setCntrMonth(i + "");
				data.setCntrPerson("0");
				data.setCntrAmt("0");
				data.setCntrCnt("0");
			}
			
			result.add(data);
		}
		
		
		return result;
	}
	
	@Override
	public List<GiveStatisticsTotal> statisticsGiveAllTotalByHour(GiveStatisticsTotalSearch search) {
		
		List<GiveStatisticsTotal> list = giveStatisticsMapper.statisticsGiveAllTotalByHour(search);
		List<GiveStatisticsTotal> result = new ArrayList<GiveStatisticsTotal>();
		
		
		for (int i = 0; i <= 23; i++) {
			GiveStatisticsTotal data = null;
			
			for (GiveStatisticsTotal gs : list) {
				if (gs.getCntrHour().equals(i + "")) {
					data = gs;
					break;
				}
			}
			
			if (data == null) {
				data = new GiveStatisticsTotal();
				data.setCntrHour(i + "");
				data.setCntrAmt("0");
			}
			
			result.add(data);
		}
		
		return result;
	}
	
	@Override
	public List<GiveStatisticsTotal> statisticsGiveAllTotalByAge(GiveStatisticsTotalSearch search) {
		
		List<GiveStatisticsTotal> list = giveStatisticsMapper.statisticsGiveAllTotalByAge(search);
		List<GiveStatisticsTotal> result = new ArrayList<GiveStatisticsTotal>();
		
		String age = "0";
		for (int i = 1; i <= 7; i++) {
			GiveStatisticsTotal data = null;
			age = (i * 10) + "";
			
			for (GiveStatisticsTotal gs : list) {
				if (gs.getCntrAge().equals(age)) {
					data = gs;
					break;
				}
			}
			
			if (data == null) {
				data = new GiveStatisticsTotal();
				data.setCntrAge(age +"");
				data.setCntrCnt("0");
				data.setCntrAmt("0");
			}
			
			if ("10".equals(age)  || "70".equals(age)) data.setCntrAge("10".equals(age) ? age + "대 이하" : age + "대 이상");
			
			
			result.add(data);
		}
		
		
		return result;
	}

	@Override
	public List<GiveStatistics> getGivePersonStatisticsList(GiveStatisticsSearch search) {
		return giveStatisticsMapper.getGivePersonStatisticsList(search);
	}

	@Override
	public List<GiveStatistics> getGivePersonStatisticsDetail(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGivePersonStatisticsDetail(search);
	}

	@Override
	public List<GiveStatistics> getGivePersonStatisticsDetailByDate(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGivePersonStatisticsDetailByDate(search);
	}

	@Override
	public List<GiveStatistics> getGivePersonStatisticsDetailExcel(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGivePersonStatisticsDetailExcel(search);
	}

	@Override
	public List<GiveStatistics> getGiveAmountStatisticsDetail(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGiveAmountStatisticsDetail(search);
	}

	@Override
	public List<GiveStatistics> getGiveAmountStatisticsDetailByDate(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGiveAmountStatisticsDetailByDate(search);
	}

	@Override
	public List<GiveStatistics> getGiveNumberStatisticsDetail(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGiveNumberStatisticsDetail(search);
	}

	@Override
	public List<GiveStatistics> getGiveNumberStatisticsDetailByDate(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGiveNumberStatisticsDetailByDate(search);
	}

	@Override
	public List<GiveStatistics> getGiveDateStatisticsDetail(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGiveDateStatisticsDetail(search);
	}

	@Override
	public GiveStatistics getGiveDateStatisticsDetailTotal(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGiveDateStatisticsDetailTotal(search);
	}

	@Override
	public List<GiveStatisticsSeparate> getGiveDateStatisticsDetailByDate(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGiveDateStatisticsDetailByDate(search);
	}

	@Override
	public List<GiveStatisticsPersonal> getGivePersonalStatisticsDetail(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGivePersonalStatisticsDetail(search);
	}

	@Override
	public GiveStatisticsPersonal getGivePersonalStatisticsDetailTotal(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGivePersonalStatisticsDetailTotal(search);
	}

	@Override
	public List<GiveStatisticsPersonal> getGivePersonalStatisticsDetailByUser(GiveStatisticsDetailSearch search) {
		return giveStatisticsMapper.getGivePersonalStatisticsDetailByUser(search);
	}
	
	@Override
	public int getGiveOperateListCount(GiveOperateSearch search) {
		return giveStatisticsMapper.getGiveOperateListCount(search);
	}

	@Override
	public List<GiveOperate> getGiveOperateList(GiveOperateSearch search) {
		return giveStatisticsMapper.getGiveOperateList(search);
	}

	@Override
	public List<GiveOperate> getGiveOperateListByLocgov(GiveOperateSearch search) {
		return giveStatisticsMapper.getGiveOperateListByLocgov(search);
	}

}
