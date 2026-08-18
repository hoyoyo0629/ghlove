package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class GiroPayResponseDto {
	private String isMobile;	//모바일여부
	private String sortCode;	//분류코드
	private String giroNo;		//지로번호
	private String elecNo;		//전자납부번호
	private String birthDate;	//생년월일
	private String payerName;	//이름
	private String callDt;		//현재일시
	private String successRU;	//성공URL
	private String failRU;		//실패URL
	private String data;		//ENC_KEY
	private String popUrl;		//팝업URL
	private String errorMsg;	//에러메시지
}