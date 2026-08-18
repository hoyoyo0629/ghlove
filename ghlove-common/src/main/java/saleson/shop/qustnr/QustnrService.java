package saleson.shop.qustnr;

import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.web.domain.ListParam;

import saleson.shop.qustnr.domain.Qestnar;
import saleson.shop.qustnr.domain.QestnarResponseDto;
import saleson.shop.qustnr.domain.QustnrRspnsResult;
import saleson.shop.qustnr.support.QustnrSearchParam;


public interface QustnrService {

	public int getQustnrListCnt(QustnrSearchParam searchParam);

	public List<Qestnar> getQustnrList(QustnrSearchParam searchParam);

	public int insertQustnr(Qestnar qestnar);

	public Qestnar getQustnr(long qustnrSn);

	public QestnarResponseDto getQustnrByApi(long qustnrSn);

	public int deleteQustrn(ListParam listParam);

	public int editQustnr(Qestnar qestnar);

	public int getQustnrRspnsResultCnt(long qustnrSn);

	public int insertQustnrRspnsResult(List<QustnrRspnsResult> answerList);

	public QestnarResponseDto checkQustnr(long qustnrSn,long userId);

}
