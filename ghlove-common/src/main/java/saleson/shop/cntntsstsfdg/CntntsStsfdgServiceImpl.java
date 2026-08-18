package saleson.shop.cntntsstsfdg;

import java.util.ArrayList;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import saleson.shop.cntntsstsfdg.domain.CntntsStsfdg;
import saleson.shop.cntntsstsfdg.support.CntntsStsfdgParam;


@Service("cntntsStsfdgService")
public class CntntsStsfdgServiceImpl extends EgovAbstractServiceImpl implements CntntsStsfdgService{

	@Autowired
	CntntsStsfdgMapper cntntsStsfdgMapper;

	@Override
	public int getCntntsStsfdgCount(CntntsStsfdgParam param) {
		return cntntsStsfdgMapper.getCntntsStsfdgCount(param);
	}

	@Override
	public List<CntntsStsfdg> getCntntsStsfdgList(CntntsStsfdgParam param) {
		return cntntsStsfdgMapper.getCntntsStsfdgList(param);
	}

	@Override
	public CntntsStsfdg getCntntsStsfdgSum(CntntsStsfdgParam param) {

		CntntsStsfdg cntntsStsfdg = cntntsStsfdgMapper.getCntntsStsfdgSum(param);
		if (cntntsStsfdg == null) {
			cntntsStsfdg = new CntntsStsfdg();
			cntntsStsfdg.setMenuUrl(param.getMenuUrl());
			cntntsStsfdg.setStsfdg1("0");
			cntntsStsfdg.setStsfdg2("0");
			cntntsStsfdg.setStsfdg3("0");
			cntntsStsfdg.setStsfdg4("0");
			cntntsStsfdg.setSumCount("0");
		}

		return cntntsStsfdg;
	}

	@Override
	public int getCntntsStsfdgByDateCount(CntntsStsfdgParam param) {
		return cntntsStsfdgMapper.getCntntsStsfdgByDateCount(param);
	}

	@Override
	public List<CntntsStsfdg> getCntntsStsfdgByDateList(CntntsStsfdgParam param) {
		return cntntsStsfdgMapper.getCntntsStsfdgByDateList(param);
	}

	@Override
	public List<CntntsStsfdg> getStatisticsByMonth(CntntsStsfdgParam param) {
		List<CntntsStsfdg> list = cntntsStsfdgMapper.getStatisticsByMonth(param);
		List<CntntsStsfdg> result = new ArrayList<CntntsStsfdg>();
		
		for (int i = 1; i <= 12; i++) {
			CntntsStsfdg data = null;
			
			for (CntntsStsfdg d : list) {
				if (d.getFrstRegistPnttm().equals(i + "")) {
					data = d;
					break;
				}
			}
			
			
			if (data == null) {
				data = new CntntsStsfdg();
				data.setFrstRegistPnttm(i + "");
				data.setStsfdg4("0");
				data.setStsfdg3("0");
				data.setStsfdg2("0");
				data.setStsfdg1("0");
			}
			
			result.add(data);
			
		}
		
		
		return result;
	}

}
