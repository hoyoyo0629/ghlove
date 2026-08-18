package saleson.shop.givepointexpiration.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GivePointExpirationMail {
	private Long userId;
	private String userName;
	private String pointEndDe;
	private Integer point;
	private String email;
	private String loginId;
	private String statusCode;
}
