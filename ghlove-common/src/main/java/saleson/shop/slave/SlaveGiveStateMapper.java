package saleson.shop.slave;

import java.util.HashMap;
import java.util.List;

import com.onlinepowers.framework.orm.mybatis.annotation.Mapper;

import saleson.common.configuration.MapperSlave;
import saleson.shop.donation.domain.GiveUserSmsInfo;
import saleson.shop.give.givestate.domain.GiveState;
import saleson.shop.give.givestate.domain.GiveStateCntrUsePoint;
import saleson.shop.give.givestate.domain.GiveStateNts;
import saleson.shop.give.givestate.domain.GiveStateTest;

@MapperSlave("slaveGiveStateMapper")
public interface SlaveGiveStateMapper {

	// 기부금 모금현황 합계
	GiveState getGiveStateSum(GiveState giveState);

	// 기부금 모금현황 목록 count
	int getGiveStateListCount(GiveState giveState);

	// 기부금 모금현황 목록
	List<GiveState> getGiveStateList(GiveState giveState);

	// 기부금 모금현황 상세 누적합계
	GiveState getGiveStateDetail(GiveState giveState);

	// 기부금 모금현황 상세 검색합계
	GiveState getGiveStateDetailSum(GiveState giveState);

	// 기부금 모금현황 상세 목록 count
	int getGiveStateDetailListCount(GiveState giveState);

	// 기부금 모금현황 상세 목록
	List<GiveState> getGiveStateDetailList(GiveState giveState);

	// 기부금 모금현황 상세 목록
	List<HashMap<String, Object>> getLocgovCodeList(String code);

	// 지자체 코드 조회
	HashMap<String, Object> getLocgovCode(long userId);



	// 확인용 : 삭제예정
	int getGiveStateDetailListCountTest(GiveState giveState);
	List<GiveStateTest> getGiveStateDetailListTest(GiveState giveState);
	int getGiveStateDetailListCountCntr(GiveState giveState);
	List<GiveStateTest> getGiveStateDetailListCntr(GiveState giveState);
	List<GiveStateCntrUsePoint> getGiveStateDetailListCntrUsePoint(GiveState giveState);

	/**
	 * <pre>
	 * comment : 기부 전체 현황 > 기부내역변경정보 조회
	 * preMethodName : getGiveStateModifyInfo
	 * author :  primyerim
	 * date : 2023. 2. 28.
	 *
	 *</pre>
	 * @param elctrnPayNo
	 * @return
	 * GiveState
	 */
	GiveStateTest getGiveStateModifyInfo(String elctrnPayNo);

	/**
	 * <pre>
	 * comment : 기부 과오납처리
	 * preMethodName :
	 * author : 이광교
	 * date : 2023. 3. 2.
	 *
	 *</pre>
	 * @param
	 * @return

	 */
	 Integer giveCancelProcess(GiveStateTest giveStateTest);
	 /**
	  * comment : 취소여부
	  * date: 2032.03.06
	  * */
	 Integer giveDeleteAt(GiveStateTest giveStateTest);

	 /**
	  * comment: 포인트 복구
	  * date: 2023.03.06
	  * author :  이광교
	  * date : 2023. 3. 6.
	  *
	  * @param giveStateTest
	  * @return
	  * Integer
	  */
	 Integer givePointRenew(GiveStateTest giveStateTest);

	/**
	 * comment : 기부금변경신청관리 목록 count 조회
	 * author :  배예림
	 * date : 2023. 3. 28.
	 *
	 * @param giveState
	 * @return
	 * int
	 */
	int getGiveReqmngCount(GiveState giveState);

	/**
	 * comment : 기부금변경신청관리 목록 조회
	 * author :  배예림
	 * date : 2023. 3. 28.
	 *
	 * @param giveState
	 * @return
	 * List<GiveState>
	 */
	List<GiveState> getGiveReqmngList(GiveState giveState);

	/**
	 * <pre>
	 * comment : 과오납 취소/승인 SMS전송 정보
	 * author :  USER
	 * date : 2023. 4. 14.
	 *
	 *</pre>
	 * @param searchParam
	 * @return
	 * GiveUserSmsInfo
	 */
	GiveUserSmsInfo giveReqmngSmsUserInfo(GiveStateTest searchParam);

	GiveUserSmsInfo giveReqmngSmsUserInfoByReqId(GiveStateTest searchParam);

	/**
	 * <pre>
	 * comment : 기부금 변경신청 관련 포인트 사용 체크
	 * author :  csh
	 * date : 2023. 11. 07.
	 *
	 *</pre>
	 * @param cntrSn
	 * @return
	 * count
	 */
	int getCntrUsePointCheck(String cntrSn);
	
	/**
	 * <pre>
	 * comment : 기부금 영수증 이력
	 * author :  csh
	 * date : 2024. 6. 18.
	 *
	 *</pre>
	 * @param elctrnPayNo
	 * @return
	 * GiveStateNts
	 */
	List<GiveStateNts> getNtsList(String elctrnPayNo);
}
