package saleson.shop.orderagency.domain;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderAgencyLoginConfirmInfo {

	// 세일즈온 세션 아이디
	private String userSessionId;
	
	// 토큰 값을 db 암호화 한 값(normal)
	private String validAccessCd;
	
	// 로그인한 관리자 아이디
	private Long managerId;
	
	// 기부자 전화번호
	private String cntrbtrMobile;
	
	// 기부자 아이디
	private Long cntrbtrId;
	
	// 로그인 일자
	private Timestamp frstRegDt;
	
	// 암호화키
	private String privateKey;
	
}
