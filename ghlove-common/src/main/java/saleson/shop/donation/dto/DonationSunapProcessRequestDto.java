package saleson.shop.donation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DonationSunapProcessRequestDto {
	private String cntrSn;
	
	private String domain;			//도메인
	private String jijacheCd;		//지자체코드
	private String elctrnPayNo;		//enapbuNo
	private String userCntrPoint;	//사용포인트
	private String taxAmt;			//기부금
	private String presentType;		//답례품여부
}
