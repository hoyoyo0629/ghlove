package saleson.shop.give.givestate.domain;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GiveState extends SearchParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// 조회조건
	private String locgovFullNm;
	private String shCntrYear;
	private String shWdr;
	private String shLocgovCode;
	private String shUserName;
	private String shCntrDeStart;
	private String shCntrDeEnd;
	private String shCntrPathCode;
	private String shDsgnDonateAt;

	private String shSttemntPayDeStart; //2023-02-13 조회조건(납부일자) 추가
	private String shSttemntPayDeEnd; //2023-02-13 조회조건(납부일자) 추가

	private String locgovCd;//2023-02-21 대시보드

	private String shCntrBlcePointAt; 	//조회조건(잔여포인트여부) 추가 20241004

//	<!-- 기부금 모금현황 합계 -->
	private String cntrAmt;
	private String givePersons;
	private String giveCnt;
	private String cntrPoint;
	private String cntrUsePoint;

//	<!-- 기부금 모금현황 목록 -->
	private String cntrYear;
	private String upperLocgovCode;
	private String upperLocgovNm;
	private String locgovCode;
	private String locgovNm;
//	private String cntrAmt;
//	private String givePersons;
//	private String cntrPoint;
//	private String cntrUsePoint;

//	<!-- 기부금 모금현황 상세 누적합계 -->
//	private String cntrAmt;
//	private String givePersons;
//	private String cntrPoint;
	private String userName;

//	<!-- 기부금 모금현황 상세 검색합계 -->
//	private String cntrAmt;
//	private String givePersons;
//	private String cntrPoint;
//	private String userName;

//	<!-- 기부금 모금현황 상세 목록 -->
	private String cntrDe;
//    private String userName;
	private String loginId;
//    private String cntrAmt;
//    private String cntrPoint;
    private String cntrPathCode;
    private String cntrPathName;
    private String rtnpsntReqstCode;

    private String sttemntPayDe;	// 납부일자 20230104 추가

    private String psitnLocgovName;	// 거소지자체,생년월일 추가 20240229
    private String birthday;
    private String phoneNumber;		//핸드폰번호
    private String pbancName;		//SMS 수신동의 여부
    private int ntsCnt;		//기부영수증 처리 여부
    private String cntrBlcePoint;	// 잔여포인트 20241004

    // 임시 확인용 파라미터
    private String shCntrSn            ;
//    private String shCntrDeStart       ;
//    private String shCntrDeEnd         ;
//    private String shUserName          ;
    private String shLoginId           ;
//    private String shLocgovCode        ;
    private String shSttemntPayDe      ;
//    private String shCntrPathCode      ;
    private String shCntrSttusCode     ;
    private String shElctrnPayNo       ;
    private String shDeleteAt       ;
    private String shUsePointInclude       ;

    private String shCntrStatusCode;	// 기부상태 추가 20241220

    private String shCntrAmtStart;		// 20260414 금액검색 추가 최소액
    private String shCntrAmtEnd;		// 20260414 금액검색 추가 최대액

    private String shLinkInsttCdAt;		// 20260422 민간연계기관 기부 여부
    private String shLinkInsttCd;		// 20260422 민간연계기관 코드

    private String shPbancAt;			// 20260430 SMS 수신동의 여부 검색


	private String shKeyword;
	private String shText;

	// <!---콜 수 등록->

	private long callKookmin;
	private long callLov;
	private long callGiver;
	private long registId;
	private long callNhbank;
	private long callPlatform;
	private String callDate;


	// <!-- 기부금 변경신청 관리 -->
	private String shCntrReqmngCode;
	private String lastUpdusrId ;
	private String elctrnPayNo;
	private String shApprove;
	private String shFrstRegistPnttmStart;
	private String shFrstRegistPnttmEnd;
	private String shReqStatusCode;
	private String frstRegisterPttmn;
	private String cntrSn;
	private String reqStatusCode;
	private String reqStatusCodeName;
	private String cntrReqmngCode ;
	private String cntrReqmngCodeName;
	private String frstRegistPnttm;
	private String apprDt;
	private String ntsSttusMssage;

	// 지정기부 추가
    private int prjId;
    private String prjSubject;

    private Boolean isSubmit;

    //민간연계기관 추가
    private String detail;
    private String linkInsttCd;
}
