package saleson.shop.give.giveoperation.domain;

import lombok.Data;

@Data
public class GiveOperation {
	
	private String cntrYear;			// 기부년도
	private String upperLocgovCode;		// 상위 지자체 코드
	private String upperLocgovNm;		// 상위 지자체 명
	private String locgovCode;			// 지자체 코드
	private String locgovNm;			// 지자체 명
	private String cntrAmt;				// 기부금액
	private String expndtrAmt;			// 사용금액
	private String balanceAmt;			// 잔액
	
}
