package saleson.shop.statistics.locgov;

import java.util.ArrayList;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dreamsecurity.jcaos.asn1.b.s;

import saleson.shop.give.statistics.domain.GiveStatisticsTotal;
import saleson.shop.statistics.locgov.domain.StatisticsLocgov;
import saleson.shop.statistics.locgov.domain.StatisticsLocgovSearch;

@Service("StatisticsLocgovService")
public class StatisticsLocgovServiceImpl extends EgovAbstractServiceImpl implements StatisticsLocgovService {
	
	@Autowired
	private StatisticsLocgovMapper statisticsLocgovMapper;
	
	@Override
	public int getLocgovLikeListTotal(StatisticsLocgovSearch search) {
		// TODO Auto-generated method stub
		return statisticsLocgovMapper.getLocgovLikeListTotal(search);
	}
	
	@Override
	public List<StatisticsLocgov> getLocgovLikeList(StatisticsLocgovSearch search) {
		// TODO Auto-generated method stub
		return statisticsLocgovMapper.getLocgovLikeList(search);
	}

	@Override
	public StatisticsLocgov getLikeCntByLocgov(StatisticsLocgovSearch search) {
		// TODO Auto-generated method stub
		return statisticsLocgovMapper.getLikeCntByLocgov(search);
	}

	@Override
	public List<StatisticsLocgov> statisticsLocgovLikeByMonth(StatisticsLocgovSearch search) {
		// TODO Auto-generated method stub
		
		List<StatisticsLocgov> list = statisticsLocgovMapper.statisticsLocgovLikeByMonth(search);
		List<StatisticsLocgov> result = new ArrayList<StatisticsLocgov>();
		
		for (int i = 1; i <= 12; i++) {
			StatisticsLocgov data = null;
			
			for (StatisticsLocgov gs : list) {
				if (gs.getCntrMonth().equals(i + "")) {
					data = gs;
					break;
				}
			}
			
			if (data == null) {
				data = new StatisticsLocgov();
				data.setCntrMonth(i + "");
				data.setLikeCnt("0");
			}
			
			result.add(data);
		}
		
		return result;
	}

}
