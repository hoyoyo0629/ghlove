package saleson.shop.user.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <pre>
 * author    : csh
 * since     : 2023. 3. 20.
 * comment   : 금융인증서 관련 정보
 * </pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class FinancResultInfo {

	private long userId;
	private String loginId;
	private String password;
    private String userName;
	private String mberCi;
    private String mberDi;
    private String mberDn;
    private String mberFinDn;
}
