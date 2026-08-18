package saleson.shop.give.givestate.domain;

import com.onlinepowers.framework.web.domain.SearchParam;
import lombok.ToString;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ToString
public class GiveStateTest extends SearchParam {

	private String cntrSn             ;
	private String cntrDe             ;
	private String userId             ;
	private String psitnLocgovCode    ;
	private String cntrLocgovCode     ;
	private String cntrAmt            ;
	private String cntrPoint          ;
	private String cntrBlcePoint      ;
	private String pointEndDe         ;
	private String sttemntPayDe       ;
	private String payValidDe         ;
	private String cntrPathCode       ;
	private String cntrSttusCode      ;
	private String cntrbtrOpinionCn   ;
	private String setleMthCode       ;
	private String cntrUsePurpsCode   ;
	private String elctrnPayNo        ;
	private String seoulTrgetAt       ;
	private String rceptBankCode      ;
	private String rceptBankNm        ;
	private String rcepterNm          ;
	private String rtnpsntReqstCode   ;
	private String rtnpsntReqstAt     ;
	private String infoAgreAt         ;
	private String frstRegisterId     ;
	private String frstRegistPnttm    ;
	private String lastUpdusrId       ;
	private String lastUpdtPnttm      ;
	private String deleteAt           ;
	private String ntsSttusMssage     ;

	private String userName;
	private String loginId;
	private String shWdr;
	private String psitnLocgovCodeName;
	private String cntrLocgovCodeName;
	private String rceptBankCodeNm;

	private String shCntrDeStart;
	private String shCntrDeEnd;
	private String shKeyword;
	private String shText;

	//추가
	private String userKey;

	//수납확인을 위한 변수
	private String mngNo;
	private String enapbuNo;
	private String jijacheCd;

	//2023-03-28 기부금변경신청관리 추가
    private String cntrReqmngCode ;
    private String cntrReqmngCodeName;
    private String discription ;
    private String locgovCode ;
    private String locgovCodeName;
    private Long reqId;
    private String apprDt;
    private String cancleDt;
    private String reqStatusCode;
    private String reqStatusCodeName;
    private String cntrUsePoint;
    private String shSttemntPayDe;

    //2026-07-21 기부금변경신청관리 추가
    private String taxSysCancelDe;				// 세외수입 시스템 취소일
    private String relatedDocDptNm;				// 관련문서 생산부서명
    private String relatedDocNum;				// 관련문서 문서번호
    private String relatedDocDe;				// 관련문서 시행일

    // 지정기부 추가
    private int prjId;
    private String prjSubject;

    private int dsgnDntnBizId;

    //민간연계기관 추가
    private String detail;
    private String linkInsttCd;
}
