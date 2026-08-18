package saleson.shop.analysis;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.cntntsstsfdg.CntntsStsfdgMapper;
import saleson.shop.statistics.ShopStatisticsMapper;

@Service("analysisBatchService")
public class AnalysisBatchServiceImpl extends EgovAbstractServiceImpl implements AnalysisBatchService {
	private static final Logger logger = LoggerFactory.getLogger(AnalysisBatchServiceImpl.class);

	@Autowired
	ShopStatisticsMapper shopStatisticsMapper;

	@Autowired
	CntntsStsfdgMapper cntntsStsfdgMapper;



}
