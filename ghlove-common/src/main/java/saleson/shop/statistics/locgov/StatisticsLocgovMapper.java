
package saleson.shop.statistics.locgov;

import java.util.List;

import saleson.shop.order.domain.OrderShippingInfo;
import saleson.shop.statistics.domain.*;
import saleson.shop.statistics.locgov.domain.StatisticsLocgov;
import saleson.shop.statistics.locgov.domain.StatisticsLocgovSearch;
import saleson.shop.statistics.support.StatisticsParam;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;
import com.onlinepowers.framework.security.userdetails.User;

@Mapper("StatisticsLocgovMapper")
public interface StatisticsLocgovMapper {
	
	int getLocgovLikeListTotal(StatisticsLocgovSearch search);
	
	List<StatisticsLocgov> getLocgovLikeList(StatisticsLocgovSearch search);
	
	StatisticsLocgov getLikeCntByLocgov(StatisticsLocgovSearch search);
	
	List<StatisticsLocgov> statisticsLocgovLikeByMonth(StatisticsLocgovSearch search);
	
}
