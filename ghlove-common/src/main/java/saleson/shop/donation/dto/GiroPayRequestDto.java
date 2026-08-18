package saleson.shop.donation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GiroPayRequestDto {
	private String domain;			//도메인
	private String jijacheCd;		//지자체코드
	private String enapbuNo;		//전자납부번호
	private String userCntrPoint;	//기부포인트
	private String taxAmt;			//기부금
	private String presentType;		//답례품여부
	private String userAgent;		//
	
	private String useInsttCode;    // 분류코드 
	private String giroNo;          // 지로번호
	private String host;            // 사용자접속URL
	
}