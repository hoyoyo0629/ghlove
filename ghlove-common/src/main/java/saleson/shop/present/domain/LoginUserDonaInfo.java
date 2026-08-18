package saleson.shop.present.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDonaInfo {
    private String userDonaKey;			// 기부 기본키(지역별 등등)
    private String userDonaPoint;		// 답례금
    private String userDonaRemainPoint;	// 잔액
    private String userDonaZoneCode;	// 행안부에서 지정한 지역코드 ZoneCode sheet 를 참조하면 된다.
    private String userDonaDate;		// 기부한 날짜시간
}
