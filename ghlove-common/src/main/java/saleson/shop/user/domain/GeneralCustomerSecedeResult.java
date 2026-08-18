package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralCustomerSecedeResult {
	private String code;		// 결과코드 (FAIL: 실패, SUCC: 성공, ERR_ALR_SECEDE: 이미 탈퇴 완료, ERR_ONE_PASS: 원패스 회원)
	private String isLogout;	// 로그아웃 여부 (Y/N, 'Y' 인 경우 회원 탈퇴 후 강제 로그아웃 처리)
}
