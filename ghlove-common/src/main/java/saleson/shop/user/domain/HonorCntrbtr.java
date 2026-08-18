package saleson.shop.user.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HonorCntrbtr {
	
	private String stdrYear;					// 년도
	private String locgovCode;					// 지자체 코드
	private String locgovNm;					// 지자체명
	private String upperLocgovCode;				// 상위 지자체
	private String upperLocgovNm;				// 상위 지자체명
	private String userId;						// 유저ID
	private String honorCntrbtrLevelCode;		// 명예 기부자 레벨 코드
	private Long frstRegisterId;				// 등록자 ID
	private String label;						// 레벨 명
	private int levelCnt;						// 레벨 건수
	
	private int standardAmt;					// 기준 금액
	
}
