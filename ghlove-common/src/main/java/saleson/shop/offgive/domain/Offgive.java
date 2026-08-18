package saleson.shop.offgive.domain;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Offgive extends SearchParam {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	// 조회조건
	private String shKeyword;	// 접수번호/기부 지자체/이름/전자납부번호/지점명
	private String shText;
	private String shFrstRegistPnttmStart;	// 신청일
	private String shFrstRegistPnttmEnd;	// 신청일
	private String shCntrAmtStart;			// 금액
	private String shCntrAmtEnd;			// 금액
	private String shCntrSttusCode;		// 종류
	private String shRceptBankCode;		// 소속코드
	private String shRceptBankNm;		// 지점
	private String shRceptBankCodeNm;		// 소속명


	// 목록
	private String cntrSn;
	private String upperLocgovCode;
	private String upperLocgovNm;
	private String locgovCode;
	private String locgovNm;
	private String sttemntPayDe;	// 수납일
	private String userName; 		// 이름
	private String cntrAmt;			// 기부금액
	private String elctrnPayNo;		// 전자납부번호
	private String rceptBankCode; 	// 접수은행코드
	private String rceptBankCodeNm; // 접수은행명
	private String rceptBankNm; 	// 접수지점
	private String frstRegistPnttm; // 신청일
	private String cntrSttusCode; 	// 상태 (신규/취소)
	private String cntrPathCode;

	// 상세
	private String linkMngKey;
//	private String frstRegistPnttm; // 신청일
	private String loginId;
//	private String userName;
	private String birthday;
	private String phoneNumber;
	private String phoneNumber1;
	private String phoneNumber2;
	private String phoneNumber3;
	private String infoAgreAt;	// 행정정보 공동이용 동의
	private String post;
	private String address;
	private String addressDetail;
//	private String upperLocgovCode;
//    private String upperLocgovNm;
//    private String locgovCode;
//    private String locgovNm;
//    private String cntrAmt;	// 기부금액
    private String cntrAmtYearTotal; // 올해 기부 누적액
//    private String rceptBankNm; // 접수지점
//    private String elctrnPayNo;	// 전자납부번호
    private String cntrBlcePoint; //잔액포인트
    private String cntrUsePoint; //사용포인트
    private int reqStatusCount; //변경신청
    private int reqId; //


    // 등록
    private String psitnLocgovCode;	// 소속지자체코드
    private String psitnLocgovUpperCode;	// 소속지자체코드(시도)
    private String locgovUpperCode;	// 기부 지자체(시도)
    private String rtnpsntReqstCode;	// 답례품신청코드
    private String mberCi;	// ci
    private String mberDi;	// di
    private String gender;	// 성별 (0;남, 1;여)
    private String userId;	// 회원id

    // 실명인증
    private String id;
	private String srvNo;
	private String reqNum;
	private String jumin1;
	private String jumin2;
	private String name;
	private String retUrl;
	private String reqInfo;
	private String ok_url;

	private String cntrPoint;

	// 지정기부 아이디
	private long prjId;
	// 지정기부 사업 명
	private String prjSubject;

	// 농협, 복지센터 구분 조회용
	private String role;

	// 오프라인 대표답례품 주문정보
	private String orderItems;
	private String rcvUserName;
	private String rcvPhoneNumber1;
	private String rcvPhoneNumber2;
	private String rcvPhoneNumber3;
	private String rcvPost;
	private String rcvAddress;
	private String rcvAddressDetail;

	private String options;
}
