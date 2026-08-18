package saleson.shop.donation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NextSunapResponseLogDto {

	private String sgbCd;    //행정표준코드관리시스템의 자치단체코드
	private String sgbNm;    //자치단체코드의 명
	private String linkMngKey;    //연계대상 기관에서 관리하는 유일키
	private String taxnNo;    //과세자료의 유일한 번호
	private String untyTaxnNo;    //과세자료(과세번호)의 통합관리번호
	private String dptCd;    //행정표준코드관리시스템의 부서코드
	private String dptNm;    //행정표준코드관리시스템의 부서코드의 명
	private String spclFisBizCd;    //
	private String spclFisBizNm;    //
	private String fyr;    //부과연도
	private String actSeCd;    //국세:16, 시도세:31, 시군구세:41. 특별회계시도세:51, 특별회계시군구세:61
	private String actSeNm;    //국세:16, 시도세:31, 시군구세:41. 특별회계시도세:51, 특별회계시군구세:61
	private String rprsTxmCd;    //세목자릿수 검토중
	private String rprsTxmNm;    //세입과목명
	private String operItemCd;    //세목자릿수 검토중
	private String operItemNm;    //세입과목명
	private String lvyNo;    //
	private String itmNo;    //제도에 따른 부과자료 분납순번 01 ~ 89 일부납부 신청에 따른 부과자료 순번 90 ~ 99
	private String epayNo;    //전자납부번호
	private String rcvmtNo;    //수납순번
	private String rcvmtSeCd;    //01: 완납, 02: 이중수납, 03: 분납, 04: 과납, 05 : 오납, 06: 일부수납
	private String rcvmtSeNm;    //수납구분코드명(일부납부, 완납 등)
	private String rcvmtYmd;    //수납일자
	private String actYmd;    //회계일자
	private String tsfYmd;    //이체일자
	private String rcvmtPctAmt;    //수납본세
	private String rcvmtAdtnAmt;    //수납가산금
	private String rcvmtIntrAmt;    //수납이자
	private String bankNm;    //
	private String rcvmtTyCd;    //01:OCR수납, 02:수기수납, 03:계좌이체, 04:세입정정, 05:배당 등
	private String rcvmtTy;    //납부매체정보(계좌이체, 신용카드결제, 가상계좌수납 등)
	private String rsveItem1;    //
	private String rsveItem2;    //예비항목2
	private String rsveItem3;    //예비항목3
	private String rsveItem4;    //예비항목4
	private String rsveItem5;    //예비항목5
	private String frstRegistPnttm; //생성일자


}

