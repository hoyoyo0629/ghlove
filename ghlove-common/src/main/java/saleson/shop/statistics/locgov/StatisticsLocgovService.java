package saleson.shop.statistics.locgov;

import java.util.List;

import saleson.shop.statistics.locgov.domain.StatisticsLocgov;
import saleson.shop.statistics.locgov.domain.StatisticsLocgovSearch;

public interface StatisticsLocgovService {
	
	public int getLocgovLikeListTotal(StatisticsLocgovSearch search);
	
	public List<StatisticsLocgov> getLocgovLikeList(StatisticsLocgovSearch search);
	
	public StatisticsLocgov getLikeCntByLocgov(StatisticsLocgovSearch search); 
	
	public List<StatisticsLocgov> statisticsLocgovLikeByMonth(StatisticsLocgovSearch search);
	
}
