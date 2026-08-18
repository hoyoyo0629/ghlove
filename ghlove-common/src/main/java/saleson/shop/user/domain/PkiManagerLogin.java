package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PkiManagerLogin {
	private String loginId;		// 사용자 ID
	private String phoneNumber;	// 휴대폰번호
	private String birthday;	// 생년월일
	private Long userId;		// 회원 ID
	private String mberDn;		// 회원 DN
	private String mberFinDn;	// 회원 FINDN(DN)
	private String mberCi;		// 회원 Ci
	private String telNumber;		// 오프라인 담당자 지점 연락처 -- 안씀
	private String offEmpId;	// 오프라인 담당자 개인번호
	private String charge;	// 지자체 담당자 오프라인 담당자 구분값
	private String type;        // 공동인지 금융인지 구분값
}
