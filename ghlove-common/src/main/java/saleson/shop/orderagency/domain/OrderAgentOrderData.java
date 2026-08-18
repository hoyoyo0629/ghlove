package saleson.shop.orderagency.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderAgentOrderData {
	
	// 공개키(암호화키)
	private String orderCode;
	
	// 결과 코드
	private long userId;
	
	// 결과 메시지
	private long managerId;
	
	
}
