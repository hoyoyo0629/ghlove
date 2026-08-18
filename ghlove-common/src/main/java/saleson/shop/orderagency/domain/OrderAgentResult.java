package saleson.shop.orderagency.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderAgentResult {
	
	// 공개키(암호화키)
	private String publicKey;
	
	// 결과 코드
	private String rstCd;
	
	// 결과 메시지
	private String rstMsg;
	
	// 로그인 토큰
	private String authToken;
	
}
