package saleson.shop.offgive;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.offgive.domain.Manager;
import saleson.shop.offgive.domain.Offgive;
import saleson.shop.offgive.domain.WlfrCntrMng;
import saleson.shop.offgive.support.WlfrCntrMngParam;

@Mapper("offgiveMapper")
public interface OffgiveMapper {

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

	/**
	 * 오프라인 기탁서 등록
	 * 대표 답례품 조회
	 */
	public List<Map<String,Object>> selectOffRprs(String lclgvCd);

	/**
	 * 오프라인 기탁서 대표답례품 조회시
	 * option 정보 조회
	 */
	public HashMap<String, Object> selectOffRprsOption(Map<String, Object> data);

	/**
	 * 오프라인 기탁서
	 * 기탁자 휴대전화번호 수정
	 */
	public int updatePhoneNumber(@Param("userId")Integer userId, @Param("phoneNumber")String phoneNumber);

	/*
	 * 오프라인 답례품 주문 목록(filler9 이 있을경우만)
	 */
	public List<Map<String, Object>> selectOffItem();

	/*
	 *  오프라인 답례품구매 이력 조회
	 */
	public HashMap<String, Object> selectGiveSunapCheck(String cntrSn);

	/*
	 * 기부 미수납시 다음날 상태 주문취소완료 상태로 변경
	 */
	public int updateItemStatus(String cntrSn);
}
