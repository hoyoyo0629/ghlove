package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SecedeUser {
	private Long userId;			// 사용자 ID
	private String loginId;			// 로그인 ID
	private String leaveDate;		// 탈퇴일자
	private String leaveReason;		// 탈퇴사유
	private String leaveCode;		// 탈퇴코드
	private String leaveCodeLabel;	// 탈퇴코드라벨
	private String leaveUserId;		// 강제탈퇴 ID
	private String leaveUserName;	// 강제탈퇴 사용자명
	private String roleName;		// 권한명
}
