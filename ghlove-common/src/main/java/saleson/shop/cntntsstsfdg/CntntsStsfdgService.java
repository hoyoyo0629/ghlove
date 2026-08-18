package saleson.shop.cntntsstsfdg;

import java.util.List;

import saleson.shop.cntntsstsfdg.domain.CntntsStsfdg;
import saleson.shop.cntntsstsfdg.support.CntntsStsfdgParam;

public interface CntntsStsfdgService {

	public int getCntntsStsfdgCount(CntntsStsfdgParam param);
	public List<CntntsStsfdg> getCntntsStsfdgList(CntntsStsfdgParam param);

	public CntntsStsfdg getCntntsStsfdgSum(CntntsStsfdgParam param);

	public int getCntntsStsfdgByDateCount(CntntsStsfdgParam param);
	public List<CntntsStsfdg> getCntntsStsfdgByDateList(CntntsStsfdgParam param);
	
	
	public List<CntntsStsfdg> getStatisticsByMonth(CntntsStsfdgParam param);

}
