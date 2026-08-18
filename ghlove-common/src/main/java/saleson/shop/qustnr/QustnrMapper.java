package saleson.shop.qustnr;

import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.qustnr.domain.Qestnar;
import saleson.shop.qustnr.domain.QustnrIem;
import saleson.shop.qustnr.domain.QustnrQesitm;
import saleson.shop.qustnr.domain.QustnrRspnsResult;
import saleson.shop.qustnr.support.QustnarData;
import saleson.shop.qustnr.support.QustnrDetailParams;
import saleson.shop.qustnr.support.QustnrSearchParam;

@Mapper("qustnrMapper")
public interface QustnrMapper {

	int getQustnrListCnt(QustnrSearchParam searchParam);

	List<Qestnar> getQustnrList(QustnrSearchParam searchParam);

	int insertQustnr(Qestnar qestnar);

	int insertQustnrQesitm(QustnrQesitm question);

	Qestnar getQustnr(long qustnrSn);

	List<QustnarData> getQustnrQesitmDetail(QustnrDetailParams params);

	int deleteQustnr(long qustnrSn);

	int deleteQustnrQesitm(long qustnrSn);

	int deleteQustnrIem(long qustnrSn);

	int getQustnrRspnsResultCnt(long qustnrSn);

	int deleteQustnrRspnsResult(long qustnrSn);

	int updateQustnr(Qestnar qestnar);

	int getQustnrQesitmCnt(QustnrQesitm question);

	int updateQustnrQesitm(QustnrQesitm question);

	int deleteQustnrQesitmAll(Qestnar qestnar);

	int insertQustnrIem(QustnrIem qustnrIem);

	int updateQustnrIem(QustnrIem qustnrIem);

	int deleteQustnrIemAll(QustnrQesitm question);

	int insertQustnrRspnsResult(List<QustnrRspnsResult> answerList);

	Map<String, Integer> checkQustnr(QustnrDetailParams params);

	Qestnar getQustnrAndValidate(QustnrDetailParams params);

	int checkQusitm(QustnrQesitm question);
	int checkIEM(QustnrIem qustnrIem);

}
