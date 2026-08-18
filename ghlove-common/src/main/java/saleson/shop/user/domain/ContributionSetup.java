package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ContributionSetup {
	private String stdrYear;			// 기준 년도
	private String locgovCode;			// 지자체 코드
	private Integer lmtAmt;				// 한도 금액
	private Double pointRate;			// 포인트 비율
	private Integer pointValidPd;		// 포인트 유효 기간
	private Long frstRegisterId;		// 최초 등록자 ID
	private String frstRegistPnttm;		// 최초 등록 시점
	private Long lastUpdusrId;			// 최종 수정자 ID
	private String lastUpdtPnttm;		// 최종 수정 시점
	private String lastUpdusrName;		// 최종 수정자 성명
}
