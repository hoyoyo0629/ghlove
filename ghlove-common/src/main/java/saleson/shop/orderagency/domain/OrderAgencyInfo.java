package saleson.shop.orderagency.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderAgencyInfo {

	// 주문대행 수행 관리자 아이디
	String loginId;
	
	// 주문대행 수행 관리자 비밀번호
	String password;
	
	// 주문대행 신청 기부자 전화번호
	String cntrbtrMobile;
	
	// 주문대행 신청 기부자 ci값
	String mberCi;
	
}
