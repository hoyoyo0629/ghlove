package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ManagerRequestResult {
	private String code;		// 결과코드 (SUCC: 성공, REQ: 관리자 신청중, REQ_NOT: 관리자 신청안됨, ERR_MNG_PASS: 관리자 패스워드 오류, ERR_USER_NOT: 일반회원 정보 존재하지 않음,
								// ERR_USER_PASS: 일반회원 패스워드 오류, ERR: 오류, PASS_CHANGE_* (C/I/F): 비밀번호 변경, ERR_ROCK: 계정잠김)
	private Long userId;		// 회원 ID
	private String statusCode;	// 상태코드 (9: 정상, 3: 탈퇴, 4: 휴면계정)
}
