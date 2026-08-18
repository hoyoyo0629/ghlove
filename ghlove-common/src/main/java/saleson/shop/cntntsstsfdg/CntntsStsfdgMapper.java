package saleson.shop.cntntsstsfdg;

import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.cntntsstsfdg.domain.CntntsStsfdg;
import saleson.shop.cntntsstsfdg.support.CntntsStsfdgParam;

@Mapper("cntntsStsfdgMapper")
public interface CntntsStsfdgMapper {

	int getCntntsStsfdgCount(CntntsStsfdgParam param);
	List<CntntsStsfdg> getCntntsStsfdgList(CntntsStsfdgParam param);

	CntntsStsfdg getCntntsStsfdgSum(CntntsStsfdgParam param);

	int getCntntsStsfdgByDateCount(CntntsStsfdgParam param);

	List<CntntsStsfdg> getCntntsStsfdgByDateList(CntntsStsfdgParam param);

	List<CntntsStsfdg> getStatisticsByMonth(CntntsStsfdgParam param);

	/**
	 * <pre>
	 * comment       : 통계 디비 저장을 위한 목록 조회
	 * preMethodName :
	 * author        : hybrid
	 * date          : 2023. 9. 18.
	 *
	 * </pre>
	 * @param param
	 * @return
	 * List<CntntsStsfdg>
	 */
	List<CntntsStsfdg> getCntntsStsfdgListAnalysis();

}
