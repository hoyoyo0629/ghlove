package saleson.shop.give.givestate;

import java.util.HashMap;
import java.util.List;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.givestate.domain.GiveStateCntrUsePoint;
import saleson.shop.give.givestate.domain.GiveStateNts;
import saleson.shop.give.givestate.domain.GiveStateTest;

public interface GiveStateService {

	// 기부금 모금현황 합계
	public GiveState getGiveStateSum(GiveState giveState);

	// 기부금 모금현황 목록 count
	public int getGiveStateListCount(GiveState giveState);

	// 기부금 모금현황 목록
	public List<GiveState> getGiveStateList(GiveState giveState);

	// 기부금 모금현황 상세 누적합계
	public GiveState getGiveStateDetail(GiveState giveState);

	// 기부금 모금현황 상세 검색합계
	public GiveState getGiveStateDetailSum(GiveState giveState);

	// 기부금 모금현황 상세 목록 count
	public int getGiveStateDetailListCount(GiveState giveState);

	// 기부금 모금현황 상세 목록
	public List<GiveState> getGiveStateDetailList(GiveState giveState);

	// 기부금 모금현황 상세 목록 엑셀 다운로드
	public SXSSFWorkbook streamGiveStateDetailData(GiveState giveState, int totalCount);

	// 지자체 코드 목록
	public List<HashMap<String, Object>> getLocgovCodeList(String code);

	// 지자체 코드 조회
	public HashMap<String, Object> getLocgovCode(long userId);

	//2023-03-02 추가
	//기부내역변경 정보 조회
	GiveStateTest getGiveStateModifyInfo(String electroPayNo);

	// 기부금 전체현황 목록 count
	public int getGiveStateDetailListCountTest(GiveState giveState);
	// 기부금 전체현황 목록
	public List<GiveStateTest> getGiveStateDetailListTest(GiveState giveState);
	// 기부금 전체현황 목록 엑셀 다운로드
	public SXSSFWorkbook streamGiveStateDetailListTest(GiveState giveState, int totalCount);

	public int getGiveStateDetailListCountCntr(GiveState giveState);
	public List<GiveStateTest> getGiveStateDetailListCntr(GiveState giveState);
	public List<GiveStateCntrUsePoint> getGiveStateDetailListCntrUsePoint(GiveState giveState);

	// 2023-03-28 추가
	// 기부금변경관리신청 count 조회
	public int getGiveReqmngCount(GiveState searchParam);
	// 2023-03-28 추가
	// 기부금변경신청관리 목록조회
	public List<GiveState> getGiveReqmngList(GiveState searchParam);
	// 2023-03-29 추가
	// 기부금변경신청관리 등록
	HashMap<String, Object> giveReqmngInsert(GiveStateTest giveStateTest);
    // 2023-03-30 추가
    // 기부금변경신청관리 승인
    HashMap<String, Object> giveReqmngApprove(GiveStateTest giveStateTest);
    // 2023-03-30 추가
	// 기부금변경신청관리 삭제
    HashMap<String, Object> giveReqmngCancel(GiveStateTest giveStateTest);

    //기부금 변경신청 관련 포인트사용 체크
    int getCntrUsePointCheck(String cntrSn);

    // 기부금 영수증 이력
    public List<GiveStateNts> getNtsList(String electroPayNo);

    // 기부금 모금현황 목록 엑셀 다운로드
	public SXSSFWorkbook streamGiveStateList(GiveState giveState, boolean isLocgov);
}
