package saleson.shop.user.domain;

import java.util.List;

import com.onlinepowers.framework.security.DataEncryptor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SleepUser {
	private Long userId;					// 회원 ID
	private String loginId;					// 로그인 ID
	private String loginDate;				// 마지막 로그인 날짜
	private String userName;				// 사용자 이름
	private String address;					// 주소
	private String addressDetail;			// 상세 주소
	private Integer totalCntrAmt;			// 기부누적액
	private Integer totalCntrBlcePoint;		// 포인트잔액
	private List<Long> userIdList;			// 회원 ID 목록
	
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
