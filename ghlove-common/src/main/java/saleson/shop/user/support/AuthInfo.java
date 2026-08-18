package saleson.shop.user.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthInfo {

	private long userId;
	private String mberCi;
    private String mberDi;
    private String userName;
    private String loginId;
    private String birthday;
    private String gender;
    private boolean underAge;
}
