package saleson.shop.mypage;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.shop.mypage.domain.Cntr;
import saleson.shop.mypage.domain.CntrPoint;
import saleson.shop.mypage.domain.CntrPointDetail;
import saleson.shop.mypage.domain.IntrstLocGov;
import saleson.shop.mypage.domain.MypageMain;
import saleson.shop.mypage.support.CntrParam;
import saleson.shop.mypage.support.CntrPointParam;
import saleson.shop.mypage.support.CntrReceipt;
import saleson.shop.mypage.support.HonorInfo;
import saleson.shop.mypage.support.IntrsLocgovParam;
import saleson.shop.mypage.support.IntrstLocGovParam;
import saleson.shop.mypage.support.ReceiptParam;
import saleson.shop.user.domain.HonorCntrbtr;
import saleson.shop.user.domain.LocGovInfo;
import saleson.shop.mypage.domain.Order;

import java.util.List;
import java.util.Map;

/**
 * @author hybrid
 *
 */
@Mapper("mypageMapper")
public interface MypageMapper {

	/**
	 * 마이페이지 관심지차체
	 * @param model
	 */
	List<IntrstLocGov> intrstLocGovInfo(IntrstLocGovParam intrstLocGovInfo);

	/**
	 * 마이페이지 관심지차체 총 건수
	 * @param model
	 */
	int intrstLocGovCnt(IntrstLocGovParam intrstLocGovInfo);

	/**
	 * 관심지차체 정보 확인
	 * @param model
	 */
	IntrstLocGov getIntrstLocGovById(IntrstLocGovParam intrstLocGovParam);

	/**
	 * 관심지차체 삭제
	 * @param model
	 */
	int deleteIntrstLocGov(IntrstLocGovParam intrstLocGovParam);

	/**
	 * 기부포인트 현황 목록 총 개수
	 * @param CntrPointParam
	 */
	int getCntrPointCnt(CntrPointParam gntrPointParam);

	/**
	 * 기부포인트 현황 목록
	 * @param CntrPointParam
	 */
	List<CntrPoint> getCntrPointInfo(CntrPointParam gntrPointParam);

	/**
	 * 기부포인트 현황 총 적립 포인트
	 * @param CntrPointParam
	 */
	int cntrPointTotal(CntrPointParam gntrPointParam);

	/**
	 * 기부포인트 현황 총 사용 포인트
	 * @param CntrPointParam
	 */
	int usePointTotal(CntrPointParam gntrPointParam);

	/**
	 * 기부내역현황 총 기부액
	 * @param getTotalCntrAmt
	 */
	int getTotalCntrAmt(CntrParam cntrParam);

	/**
	 * 기부내역현황 총 기부액
	 * @param getTotalCntrAmt
	 */
	int getThisYearTotalCntrAmt(Long userId);

	/**
	 * 기부내역현황 목록 총 개수
	 * @param getTotalCntrAmt
	 */
	int getGntrListTotalCnt(CntrParam cntrParam);

	/**
	 * 기부내역현황 목록
	 * @param getTotalCntrAmt
	 */
	List<Cntr> getCntrListInfo(CntrParam cntrParam);

	/**
	 * 지자체 정보 가져오기
	 * @param LocGovInfo
	 * */
	List<LocGovInfo> getLocGovList(LocGovInfo locGovInfo);

	/**
	 * 기부포인트 현황 상세 목록 총 개수
	 * @param CntrPointParam
	 */
	int getCntrPointDetailCnt(CntrPointParam gntrPointParam);

	/**
	 * 기부포인트 현황 상세 목록
	 * @param CntrPointParam
	 */
	List<CntrPoint> getCntrPointDetail(CntrPointParam gntrPointParam);

	/**
	 * 기부포인트 현황 총 기부 잔액 포인트
	 * @param CntrPointParam
	 */
	int blcePointTotal(CntrPointParam gntrPointParam);

	/**
	 * 기부포인트 현황 상세 지자체 명 정보
	 * @param CntrPointParam
	 */
	CntrPointDetail getLocgovNm(CntrPointParam gntrPointParam);

	/**
	 * 세액공제 예상금액
	 * @param CntrPointParam
	 */
	int getTaxRedutionEstimate(CntrParam cntrParam);

	/**
	 * 주문 단계 목록 가져오기
	 * @param CntrParam
	 */
	List<Order> getOrderLevelList(CntrParam cntrParam);

	/**
	 * 주문 클레임 단계 목록 가져오기
	 * @param CntrParam
	 */
	List<Order> getOrderClaimLevelList(CntrParam cntrParam);

	/**
	 * 접수번호에 대한 영수증 확인
	 * @param receiptParam
	 * @return
	 */
	Cntr getCntrReceipt(ReceiptParam receiptParam);

	/**
	 * 영수증 출력 로그 등록
	 * @param receiptParam
	 * @return
	 */
	int insertCntrReceipt(ReceiptParam receiptParam);

	List<HonorCntrbtr> getUserHonorListTotal(long userId);

	List<HonorCntrbtr> getUserHonorList(long userId);

	int deleteIntrstLocGovAll(IntrsLocgovParam intrstLocGovParam);

	/**
	 * 지자체별 명예등급 조회
	 * @param userId
	 */
	List<HonorInfo> getUserCntrHonorList(long userId);

	/**
	 * 조건부 총 기부금액, 기부건수(기부확인증)
	 * @param CntrReceipt
	 */
	Map<String, Object> getReceiptCntrAmtAndTotalCnt(CntrReceipt receiptParam);

	/**
	 * 기부 리스트(기부확인증)
	 * @param CntrReceipt
	 */
	List<Cntr> getReceiptListInfo(CntrReceipt receiptParam);

	/**
	 * 조건부 총 기부금액, 기부건수(기부확인증 팝업)
	 * @param CntrReceipt
	 */
	Map<String, Object> getReceiptCntrPopInfo(CntrReceipt receiptParam);

	/**
	 * 기부 팝업 리스트(기부확인증)
	 * @param CntrReceipt
	 */
	List<Cntr> getReceiptPopListInfo(CntrReceipt receiptParam);

	/**
	 * 최상위 지차제 추출(기부확인증)
	 * @param CntrReceipt
	 */
	Map<String, Object> getTopLocGov(CntrReceipt receiptParam);

	/**
	 * 기부혜택증 열람기록 저장
	 * @param receiptParam
	 */
	public void saveHonorViewHist(ReceiptParam receiptParam);

	/**
	 * 특별재난 기부 리스트(Compare용)
	 */
	List<Map<String, Object>> getSpelDstrZn();

}
