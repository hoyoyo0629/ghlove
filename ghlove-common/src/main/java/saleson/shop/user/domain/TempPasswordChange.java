package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TempPasswordChange {
	private String loginId;			// 로그인 ID
	private String password;		// 패스워드
	private String currentPassword;	// 현재 패스워드
}
