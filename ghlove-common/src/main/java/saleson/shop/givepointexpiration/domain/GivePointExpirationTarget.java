package saleson.shop.givepointexpiration.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GivePointExpirationTarget {
	
	private String cntrSn;
	private String cntrBlcePoint;
	private long userId;
	private String psitnLocgovCode;
	private String cntrLocgovCode;
	private String useCn = "사용기한 만료로 자동 소멸"; //소멸
	private String useSeCode = "2"; //소멸
	
}
