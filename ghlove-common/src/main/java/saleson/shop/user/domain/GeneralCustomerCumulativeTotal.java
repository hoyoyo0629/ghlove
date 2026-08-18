package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralCustomerCumulativeTotal {
	private Integer totalCntrAmt;			// 누적합계 - 기부금액
	private Integer totalCntrPoint;			// 누적합계 - 발생포인트
	private Integer totalCntrBlcePoint;		// 누적합계 - 사용포인트
	private Integer totalCntrUsePoint;		// 누적합계 - 포인트잔액
}
