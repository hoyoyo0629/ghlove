package saleson.shop.donation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class SeoulBugaRequestDto {
	private String	comReqDt; 		 		//요청일자
	private String	comReqTm; 		 		//요청일시
	private String	systemCd; 		 		//인터페이스 구분코드 01
	private String	jijacheCd; 		 		//지자체코드
	private String	siguCd; 		 		//시구코드
	private String	semokCd; 		 		//세목코드
	private String	taxYm; 			 		//과세년월
	private String	taxGubun; 		 		//과세구분
	private String	buseoCd;	 			//부서코드
	private String	taxNo; 	 				//과세번호
	private String	sidoCd; 		 		//시도코드
	private String	napId; 			 		//납세자ID
	private String	napNm; 			 		//납세자명
	private String	napGubun; 		 		//납세자구분
	private Long	taxAmt; 		 		//과세금액
	private Long	sise; 			 		//시세(병기항목아닌경우 본세)
	private Long	guse;		 			//구세(병기세목 해당시 분배된 구세) default 0 
	private Long	gukse;		 			//국세(병기세목 해당시 분배된 국세) default 0 
	private Long	gigum;		 			//기금(병기세목 해당시 분배된 기금) default 0  
	private Long	siseIja;	 			//시세이자(병기항목아닌경우 본세 연부이자) default 0  
	private Long	guseIja;	 			//구세이자(병기세목 해당시 분배된 연부이자) default 0 
	private Long	gukseIja;  				//국세이자(병기세목 해당시 분배된 연부이자) default 0 
	private Long	gigumIja;	 			//기금이자(병기세목 해당시 분배된 연부이자) default 0 
	private Long	siseGasanAmt; 			//시세가산금(병기항목아닌경우 본세 가산금) default 0 
	private Long	guseGasamAmt; 			//구세가산금(병기세목 해당시 분배된 가산금) default 0 
	private Long	gukseGasanAmt; 			//국세가산금(병기세목 해당시 분배된 가산금) default 0 
	private Long	gigumGasanAmt; 			//기금가산금(병기세목 해당시 분배된 가산금) default 0 
	private String	napMobilNo; 			//납세자휴대폰
	private String	napTelNo;	 			//납세자전화
	private String	napEmail;	 			//납세자이메일
	private String	resideStatus; 	 		//거주상태 10:거주자 11:국외이주신고자 15:직권조치중 40:말소자 41:사망말소 45:현지이민자 47:국외이주자 49:국적이탈자 99:기타
	private String	mulGubun;	 			//물건구분 03:기타
	private String	mulNm;		 			//물건명 해당건물명(지번,차량번호 등)
	private String	mulOcrSiguCd; 			//물건OCR시구코드 (세외수입시스템에서 자동채번)
	private String	mulBdongriCd;	 		//물건법정동리코드 (세외수입시스템에서 자동채번)
	private String	mulSpcCd;	 			//물건특수지코드 1:일반번지 2:산번지 3:가번지 4:구번지 5:구획정리 6:하천번지 7:블록 9:무번지
	private String	mulBon; 	 			//물번본번
	private String	mulBu;		 			//물건부번
	private String	mulTong;	 			//물건통
	private String	mulBan;	 				//물건반
	private String	mulAptNm;	 			//물건아파트명
	private String	mulDong;	 			//물건동
	private String	mulHosu;	 			//물건호
	private String	mulZipCd;	 			//물건우편번호
	private String	mulZipAddr; 			//물건우편번호주소
	private String	mulDtlAddr; 			//물건상세주소
	private String	hdongCd;	 			//행정동코드
	private String	bookNo; 		 		//대장번호, 원천 시스템의 대장번호(유일 key 값) , 중복체크 → 채번필요함
	private String	hangmok1;	 			//항목1
	private String	hangmok2;	 			//항목2
	private String	hangmok3;	 			//항목3
	private String	hangmok4;	 			//항목4
	private String	hangmok5;	 			//항목5
	private String	hangmok6;				//항목6
	private String	gasanRateGubun;			//가산율구분 (세외수입시스템에서 자동채번)
	private Integer	specialRate; 			//특별이율 (세외수입시스템에서 자동채번)
	private String	specialRateApplySayu;	//특별이율적용사유 (세외수입시스템에서 자동채번)
	private String	bigo;					//비고
	private String	ocrSiguCd; 				//OCR시구코드 (세외수입시스템에서 자동채번)
	private String	ocrBuseoCd;				//OCR부서코드 (세외수입시스템에서 자동채번)
	private String	etc1;					//기타1
	private String	lastWorkId;				//최종작업일자ID
	private String	lastWorkDate;			//최종작업날짜
	private Long	vatAmt; 				//부가가치세금액 ( 부가가치세대상 세목인 경우 ) default 0
	private String	gasanAmtSkipGubun; 		//가산금면제여부 (세외수입시스템에서 자동채번)
	private String	sysGubun; 				//가산금면제여부 시스템 고유번호 LVHT  (임시코드, 별도요청 없으면 수정없이 사용)
	private String	napDzipCd;				//도로명우편번호
	private String	napDzipAddr;			//도로명우편주소
	private String	napDdtlAddr;			//도로명상세주소
	private String	napDrefAddr;			//도로명참고항목
	private String	etcCm1;					//
	private String	etcCm2;					//
	private String	etcCm3;					//
	private String	etcCm4;					//
	private String	etcCm5;					//
	private String	napBldBon;				//
	private String	napBldBu;				//
	private String	napDoroCd;				//
	private String	napUndYn; 				//
	private String	napbuYmd;				//

}
