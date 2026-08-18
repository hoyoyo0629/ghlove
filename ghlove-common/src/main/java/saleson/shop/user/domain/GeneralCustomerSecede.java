package saleson.shop.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeneralCustomerSecede {
	private Long userId;			// 사용자 ID
	private String leaveReason;		// 탈퇴사유
	private String leaveCode;		// 탈퇴코드
	private Long leaveUserId;		// 강제탈퇴 ID
}
