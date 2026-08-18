package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SecedeCntr {
	private String cntrSn;			// 기부 일련번호
	private Long userId;			// 사용자 ID
	private String psitnLocgovCode;	// 기부 지자체 코드
	private String cntrLocgovCode;	// 소속 지자체 코드
	private Integer cntrBlcePoint;	// 기부 잔액 포인트
	private String useSeCode;		// 사용 구분 코드(1사용, 2소멸,3탈퇴)
	private String useCn;			// 사용 내용
	private Long loginUserId;		// 로그인 사용자 ID
}
