package saleson.shop.user.domain;

import com.onlinepowers.framework.security.DataEncryptor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralCustomer {
	private Long userId;					// 회원 ID
	private String loginId;					// 로그인 ID
	private String userName;				// 이름
	private String email;					// 이메일
	private String createdDate;				// 등록일
	private String loginDate;				// 최종로그인날짜
	private String sbscrbSeCode;			// 가입구분코드
	private String sbscrbSeNm;				// 가입구분명
	private String post;					// 우편주소
	private String address;					// 주소
	private String addressDetail;			// 상세 주소
	private String receiveEmail;			// 이메일 수신여부 (0: 수신, 1:비수신)
	private String receiveSms;				// SMS 수신여부 (0: 수신, 1:비수신)
	private String phoneNumber;				// 휴대폰번호
	private String gender;					// 성별 (0: 남, 1:여)
	private String birthday;				// 생년월일
	private String userKey;					// 디지털원패스로그인키
	private Long statusCode;				// 상태코드(1: 가입대기, 2:차단, 3:탈퇴, 4:휴면계정, 9:정상) 
	private Integer totalCntrAmt;			// 기부누적액
	private Integer totalCntrBlcePoint;		// 포인트잔액
	private String mberCi;					// 회원 CI
	
	/**
	 * 컬럼 암호화
	 * @param encryptor
	 */
	public void encrypt(DataEncryptor encryptor) {
		encryptor.encrypt(this);
	}

	/**
	 * 컬럼 복호화
	 * @param encryptor
	 * @param needMasking
	 */
	public void decrypt(DataEncryptor encryptor, boolean needMasking) {
		encryptor.decrypt(this, needMasking);
	}
}
