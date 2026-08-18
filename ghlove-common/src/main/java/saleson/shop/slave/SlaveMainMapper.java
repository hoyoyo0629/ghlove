package saleson.shop.slave;

import java.util.List;
import java.util.Map;

import saleson.common.configuration.MapperSlave;
import saleson.common.opmanager.count.OpmanagerCount;
import saleson.common.opmanager.count.OpmanagerMainCount;
import saleson.shop.give.givestate.domain.CallState;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.main.domain.MainChart;

@MapperSlave("slaveMainMapper")
public interface SlaveMainMapper {

	/**
	 * 관리자 지자체정보
	 */
	String getLocgovCdByUerId(long userId);

	/**
	 * 관리자 메인 전체회원/금일가입회원/관리자권한요청(대기)/오프라인접수현황/총기부현황/총답례품현황 카운트
	 */
	List<OpmanagerMainCount> getOpmanagerMainInfo(Map<String, String> param);

	/**
	 * 관리자 메인 게시판 정보
	 */
	List<OpmanagerCount> getOpmanagerMainBoardInfo(Map<String, String> param);

	/**
	 * 관리자 메인 기부금액 chart
	 */
	List<MainChart> getOpmanagerMainAmountChart(Map<String, String> param);

	/**
	 * 관리자 메인 기부건수 chart
	 */
	List<MainChart> getOpmanagerMainCountChart(Map<String, String> param);

	/**
	 * 관리자 메인 chart 정보
	 */
	List<OpmanagerCount> getOpmanagerMainChartInfo(Map<String, String> param);

	/**
	 * <pre>
	 * comment : 관리자 메인 테이블 정보
	 * preMethodName :getOpmanagerMainTableInfo
	 * author :  USER
	 * date : 2023. 2. 17.
	 *
	 *</pre>
	 * @return
	 * List<OpmanagerMainCount>
	 */
	List<OpmanagerMainCount> getOpmanagerMainTableInfo(GiveState giveState);

	/**
	 * <pre>
	 * comment : 메인 콜 수 정보
	 * preMethodName :getOpmanagerMainCallTableInfo
	 * author :  이광교
	 * date : 2023. 4. 17.
	 *
	 */
	CallState getOpmanagerMainCallTableInfo(GiveState giveState);

	/**
	 * <pre>
	 * comment : 메인 콜 수 등록
	 * preMethodName :opmanagerMainCall
	 * author :  이광교
	 * date : 2023. 4. 17.
	 *
	 */
	int opmanagerMainCall(GiveState giveState);
	/**
	 * <pre>
	 * comment : 메인 콜 수 삭제
	 * preMethodName :opmanagerdeleteCall
	 * author :  이광교
	 * date : 2023. 4. 17.
	 *
	 */
	int opmanagerdeleteCall(GiveState giveState);
	/**
	 * <pre>
	 * comment : 메인 대시보드 삭제
	 * preMethodName :confirmdonationForReportBatch
	 * author :  이광교
	 * date : 2023. 10. 1.
	 *
	 */

	void deleteDashBoardReport(Map<String, Object> resultMap);


	/**
	 * <pre>
	 * comment : 메인 콜 수 배치
	 * preMethodName :confirmdonationForReportBatch
	 * author :  이광교
	 * date : 2023. 4. 17.
	 *
	 */

	void confirmDonationForReportBatch(Map<String, Object> resultMap);

	/**
	 * <pre>
	 * comment : 메인 콜 수
	 * preMethodName :getOpmanagerMainCall
	 * author :  이광교
	 * date : 2023. 4. 17.
	 *
	 */


	CallState getOpmanagerMainCall(GiveState giveState);

	/**
	 * <pre>
	 * comment : 답례품 미등록 지자체
	 * preMethodName :opamanagerMainGift
	 * author :  이광교
	 * date : 2023. 4. 17.
	 *
	 */

	List<String> opamanagerMainGift();

	/**
	 * <pre>
	 * comment : 답례품( 판매중 ,품절) 수
	 * preMethodName : opmanageMainAmountGiftA(판매중), opmanageMainAmountGiftB(품절)
	 * author :  이광교
	 * date : 2023. 4. 17.
	 *
	 */

	long opmanageMainAmountGiftA(GiveState giveState);

	long opmanageMainAmountGiftB(GiveState giveState);

	long opmanageMainAmountGiftADay(GiveState giveState);
	long opmanageMainAmountGiftBDay(GiveState giveState);
	/**
	 *
	 * <pre>
	 * comment       : 유저 집계
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


	CallState dashBoardReport(GiveState giveState);

	CallState dashBoardReportForCal(GiveState giveState);

	/**
	 *
	 * 2026. 1. 21
	 * 관리자 메인 화면
	 * 알림설정 동의 여부 멤버 카운트
	 *
	 */
	Map<String, Object> notificationAgreeMemberCount();
}
