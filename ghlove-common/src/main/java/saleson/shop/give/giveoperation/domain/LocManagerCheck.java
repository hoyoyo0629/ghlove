package saleson.shop.give.giveoperation.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import saleson.api.common.enumerated.UserAdminRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocManagerCheck {
	
	private UserAdminRole adminRole;
	private String locgovCode;
	private String locgovNm;
	
	@Builder.Default
	private String returnUrl = "/opmanager";
	
	@Builder.Default
	private String message = "지자체 등록 후 이용할 수 있습니다.";
	
	@Builder.Default
	private boolean isPass = true;
	
	public boolean getIsLoc() {
		return UserAdminRole.LOC.equals(adminRole);
	}
	
}
