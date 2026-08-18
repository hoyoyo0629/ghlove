package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralCustomerPoint {
	private String pointType;			// 포인트 타입 (OCC: 발생, USE: 사용)
	private String cntrDe;				// 발생일
	private String cntrLocgovCode;		// 기부 지자체 코드
	private String cntrLocgovNm;		// 기부 지자체명
	private String cntrUpperLocgovCode;	// 기부 상위 지자체 코드
	private String cntrUpperLocgovNm;	// 기부 상위 지자체명
	private Integer cntrAmt;			// 기부금액
	private Integer cntrPoint;			// 발생포인트
	private Integer cntrUsePoint;		// 사용포인트
	private Integer cntrBlcePoint;		// 포인트작액
	private String orderCode;			// 답례품 주문 번호
}
