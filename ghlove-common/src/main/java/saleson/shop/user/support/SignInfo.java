package saleson.shop.user.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignInfo {

	private long userId;
    private String loginId;
    private String birthday;
    private String mberDn;
    private String mberFinDn;
    private String userName;
    private String phoneNumber;

}
