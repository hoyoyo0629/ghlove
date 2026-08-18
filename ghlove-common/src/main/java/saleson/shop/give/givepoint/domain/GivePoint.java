package saleson.shop.give.givepoint.domain;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GivePoint extends SearchParam {

	private static final long serialVersionUID = 1L;

	private String locgovFullNm;
	private String shWdr;

	// 조회조건
	private String shCntrYear;
	private String shCntrYearStart;
	private String shCntrYearEnd;
	private String shLocgovCode;
	private String shUserName;
	private String shCntrDeStart;
	private String shCntrDeEnd;


//	<!-- 기부 포인트현황 합계 -->
	private String cntrAmt;
	private String cntrPoint;
	private String cntrUsePoint;
	private String cntrBlcePoint;

//	<!-- 기부 포인트현황 목록 -->
	private String cntrYear;
	private String upperLocgovCode;
	private String upperLocgovNm;
	private String locgovCode;
	private String locgovNm;
//	private String cntrAmt;
//	private String cntrPoint;
//	private String cntrUsePoint;
//	private String cntrBlcePoint;

//	<!-- 기부 포인트현황 상세 누적합계 -->
//	private String cntrPoint;
//	private String cntrUsePoint;
//	private String cntrBlcePoint;

//	<!-- 기부 포인트현황 상세 검색합계 -->
//	private String cntrPoint;
//	private String cntrUsePoint;
//	private String cntrBlcePoint;

//	<!-- 기부 포인트현황 상세 목록 -->
	private String cntrDe;
	private String sttemntPayDe;
	private String elctrnPayNo;
	private String userId;
    private String userName;
    private String loginId;
//    private String cntrAmt;
//    private String cntrPoint;
//	private String cntrUsePoint;
//	private String cntrBlcePoint;

}
