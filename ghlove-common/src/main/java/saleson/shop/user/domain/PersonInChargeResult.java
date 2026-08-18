package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonInChargeResult {
	private String code;		// 결과코드 
	private String isLogout;	// 로그아웃 여부 (Y/N, 'Y' 인 경우 회원 탈퇴 후 강제 로그아웃 처리)
}