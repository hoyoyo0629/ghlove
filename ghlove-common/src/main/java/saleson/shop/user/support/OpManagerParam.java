package saleson.shop.user.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpManagerParam extends SearchParam{
	
	private String loginId;
	private String email;
	private String password;
	private String phoneNumber;
	
	private String leaveCode;		// 탈퇴코드
    private String leaveReason;
}
