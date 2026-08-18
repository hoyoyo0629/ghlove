package saleson.shop.slave;

import java.util.HashMap;
import java.util.List;

import saleson.common.configuration.MapperSlave;
import saleson.shop.offgive.domain.Manager;
import saleson.shop.offgive.domain.Offgive;
import saleson.shop.offgive.domain.WlfrCntrMng;
import saleson.shop.offgive.support.WlfrCntrMngParam;

@MapperSlave("slaveOffgiveMapper")
public interface SlaveOffgiveMapper {

	// 오프라인접수 목록 count
	int getOffgiveListCount(Offgive offgive);

	// 오프라인접수 목록
	List<Offgive> getOffgiveList(Offgive offgive);

	// 오프라인접수 상세
	Offgive getOffgive(String cntrSn);

	// 오프라인접수 상세 출력
	Offgive getOffgivePrint(Offgive offgive);

	// 관리자 정보 조회
	Manager getManager(long userId);

	Integer getMaxCheck(long userId);
	Integer getGCntrSumCntrAmt(long userId);
	Integer getGMberSecsnSumCntrAmt(String mberCi);

	HashMap<String, Object> getLocgovMapngCode(String locgovCode);

	/**
	 * 로그인 사용자 지자체코드정보 조회
	 * @param userId
	 * @return
	 */
	String getLocgovCodeByUserId(Long userId);

	//행정복지센터 권한 승인 리스트 조회
	List<WlfrCntrMng> selectWlfrCntrMngList(WlfrCntrMngParam wlfrCntrMngParam);

	//행정복지센터 조회
	List<WlfrCntrMng> wlfrCntrMngInfo(WlfrCntrMng wlfrCntrMng);
	
	//행정복지센터 상세 조회
	WlfrCntrMng selectwlfrCntrMngDetail(Long pbadmsWlfrCntrId);

	//행정복지센터 권한승인 총계 조회
	int selectWlfrCntrMngListCnt(WlfrCntrMngParam wlfrCntrMngParam);

	//행정복지센터 등록
	int insertWlfrCntrMng(WlfrCntrMng wlfrCntrMng);
	
	//행정복지센터 등록
	int updatetWlfrCntrMng(WlfrCntrMngParam wlfrCntrMngParam);

	//행정복지센터 중복조회
	int isDuplicateWlfrCntrMng(WlfrCntrMng wlfrCntrMng);

}
