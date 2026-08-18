package saleson.shop.user.domain;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonInCharge {
	private String locgovCode;			// 지자체코드
	private String locgovNm;			// 지자체명
	private String upperLocgovCode;		// 상위지자체코드
	private String upperLocgovNm;		// 상위지자체명
	private String authority;			// 권한 ID
	private String loginId;				// 로그인 ID
	private String userName;			// 이름
	private String phoneNumber;			// 휴대폰번호
	private String createdDate;			// 등록일
	private Long statusCode;			// 상태코드(1: 가입대기, 2:차단, 3:탈퇴, 4:휴면계정, 5:휴면대기 6:탈퇴대기, 9:정상)
	private String denyDate;			// 차단일
	private Long userId;				// 회원 ID
	private String bankCode;			// 은행코드
	private String empId;				// 농협직원 ID(개인번호)
	private String psitnNm;				// 소속명
	private String psitnDeptNm;			// 소속부서
	private String ofcpsNm;				// 직위명
	private String bankNm;				// 은행명
	private String password;			// 비밀번호
	private List<String> userIdList;	// 회원 ID 목록
	private String passwordExpiredDate;	// 패스워드 유효일자
	private String telNumber;	// off 담당자의 지점연락처 안씀
	private String email; 				// email

	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(PersonInChargeEncryptor encryptor) {
		encryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(PersonInChargeEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}
}
