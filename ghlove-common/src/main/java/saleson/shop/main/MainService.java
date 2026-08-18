package saleson.shop.main;

import java.util.List;
import java.util.Map;

import com.onlinepowers.framework.context.RequestContext;

import saleson.common.opmanager.count.OpmanagerCount;
import saleson.common.opmanager.count.OpmanagerMainCount;
import saleson.shop.give.givestate.domain.CallState;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.main.domain.MainChart;

public interface MainService {

	/**
	 * 관리자 지자체코드
	 */
	String getLocgovCdByUerId(long userId);

	/**
	 * 관리자 메인 전체회원/금일가입회원/관리자권한요청(대기)/오프라인접수현황/총기부현황/총답례품현황 카운트
	 */
	List<OpmanagerMainCount> getOpmanagerMainInfo(String locgovCd);

	/**
	 * 관리자 메인 게시판정보
	 */
	List<OpmanagerCount> getOpmanagerMainBoardInfo();

	/**
	 * 관리자 메인 기부금액 chart
	 */
	List<MainChart> getOpmanagerMainAmountChart(String locgovCd);

	/**
	 * 관리자 메인 기부건수 chart
	 */
	List<MainChart> getOpmanagerMainCountChart(String locgovCd);

	/**
	 * 관리자 메인 chart 정보
	 */
	List<OpmanagerCount> getOpmanagerMainChartInfo(String locgovCd);

	/**
	 * <pre>
	 * comment : 관리자 메인 테이블 정보
	 * MethodName : getOpmanagerMainTableInfo
	 * author : primyerim
	 * date : 2023. 2. 17.
	 *
	 *</pre>
	 * @return
	 * Object
	 */
	List<OpmanagerMainCount> getOpmanagerMainTableInfo(GiveState giveState);

	/**
	 * comment: 관리자 메인 콜수 정보
	 * MethodName: getOpmanagerMainTableInfo
	 * author: 이광교
	 * @param giveState
	 * @return
	 */
	CallState getOpmanagerMainCallTableInfo(GiveState giveState);

	/**
	 * comment: 관리자 메인 콜수 등록
	 * MethodName: opmanagerInsertCall
	 * author: 이광교
	 * @param giveState
	 * @return
	 */
	int opmanagerInsertCall(GiveState giveState);

	/**
	 * comment: 답례품 미등록 지자체
	 * MethodName: opamanagerMainGift
	 * author: 이광교
	 * @param giveState
	 * @return
	 */

	List<String> opamanagerMainGift();
	/**
	 * comment: 답례품(판매중, 품절 )
	 * MethodName: opmanageMainAmountGiftA
	 * author: 이광교
	 * @param giveState
	 * @return
	 */

	long opmanageMainAmountGift(GiveState giveState);

	/**
	 *
	 * <pre>
	 * comment       : 유저 집계(날짜)
	 * preMethodName :
	 * author        : csh
	 * date          : 2023. 9. 18.
	 *
	 * </pre>
	 * @param giveState
	 * @return
	 * int
	 */
	int getUserDashboard(GiveState giveState);

	/**
	 *
	 * <pre>
	 * comment       : 대시보드 2차
	 * preMethodName :
	 * author        : csh
	 * date          : 2023. 10. 12.
	 *
	 * </pre>
	 * @param giveState
	 * @return
	 * int
	 */
	CallState dashBoardReport(GiveState giveState);

	/**
	 * @return Map
	 */
	Long getGiveTotalAmt(String std);

	/**
	 * 매일 총 기부금 조회
	 * @return Map
	 * */
	Map<String, Object> nowDayGramt();

	Map<String, Object> getGiveStateByCache();

	void updateWaitUser();
	Map<String, Object> getRealTimeWaitUser();

	/**
	 *
	 * 2026. 1. 21
	 * 관리자 메인 화면
	 * 알림설정 동의 여부 멤버 카운트
	 *
	 */
	Map<String, Object> notificationAgreeMemberCount();
}
