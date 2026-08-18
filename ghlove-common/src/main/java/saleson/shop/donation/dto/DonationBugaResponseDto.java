package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
public class DonationBugaResponseDto {

	private String sgbCd;
	private String linkTrgtCd;
	private String linkMngKey;
	private String linkRstCd;
	private String linkRstMsg;
	
	private String elctrnPayNo;
	private String cntrSn;
	
	//지정기부 응답
	private String resultCode; 
	
	//서울시 세외수입 응답
	private String errorCode; // 0이 아니면 ERROR
	private String errorMsg;   
	private String insertKey;   
	private String insertAk;   
	private String resultCnt;   
	private String etcCm1;   
	private String etcCm2; 
	private String mngNo; //이택스 전문관리 대장번호
	

}
