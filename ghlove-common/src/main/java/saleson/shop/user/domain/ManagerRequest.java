package saleson.shop.user.domain;

import com.onlinepowers.framework.security.DataEncryptor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ManagerRequest {

	private Long userId;			// 회원 고유번호
	private Integer reqstSn;		// 신청 일련번호
	private String loginId;			// 로그인 ID
	private String locgovCode;		// 지자체 코드
	private String reqstSeCode;		// 신청 구분 코드
	private String psitnCode;		// 소속코드-은행
	private String psitnNm;			// 소속 명
	private String psitnDeptNm;		// 소속부서
	private String ofcpsNm;			// 직위 명
	private String cttpc;			// 연락처
	private String confmSttusCode;	// 승인상태코드
	private String rejectResn;		// 거절사유
	private Long frstRegisterId;	// 최초 등록자 ID
	private String frstRegistPnttm;	// 최초 등록 시점
	private Long lastUpdusrId;		// 최종 수정자 ID
	private String lastUpdtPnttm;	// 최종 수정 시점
	private String password;		// 비밀번호

	/* 회원 정보 */
	private String userName;		// 이름
	private String email;			// 이메일
	private String phoneNumber;		// 휴대폰 번호
	private String birthday;		// 생년월일
	private String receiveEmail;	// 수신여부 (0:수신, 1:비수신)

	/* 공통코드 & 공통코드명 */
	private String confmSttusNm;	// 승인상태명
	private String reqstSeNm;		// 신청 구분명
	private String locgovNm;		// 지자체명
	private String upperlocgovNm;	// 광역시지자체코드
	private String upperLocgovCode;	// 광역시지자체명
	private String bankNm;			// 은행명

	private String updIdRole;		// 승인자 권한

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
