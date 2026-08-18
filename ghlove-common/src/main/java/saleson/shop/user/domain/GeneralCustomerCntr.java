package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralCustomerCntr {
	private String cntrDe;					// 기부 일자
	private String cntrLocgovCode;			// 기부 지자체 코드
	private String cntrUpperLocgovCode;		// 기부 지자체명
	private String cntrLocgovNm;			// 기부 상위 지자체 코드
	private String cntrUpperLocgovNm;		// 기부 상위 지자체명
	private Integer cntrAmt;				// 기부 금액
	private Integer cntrPoint;				// 기부 포인트
	private String sttemntPayDe;			// 신고 납부 일자
	private String elctrnPayNo;				// 전자 납부 번호
	private String cntrPathCode;			// 기부 경로 코드
	private String cntrPathNm;				// 기부 경로명
	private String linkInsttNm;				// 기부 민간연계기관명
}
