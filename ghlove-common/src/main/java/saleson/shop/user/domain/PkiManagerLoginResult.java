package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PkiManagerLoginResult {
	private String code;		// 결과값
	private Long userId;		// 회원 ID
	private String mberDn;		// 회원 DN
	private String mberFinDn;	// 회원 FINDN
	private String statusCode;	// 회원 상태코드
	private String mberCi;		// 회원 Ci
	private String password;
	private String type;        // 공동인지 금융인지 구분값

}
