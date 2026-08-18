package saleson.shop.donation.support;

import lombok.Data;

@Data
public class NextGnrParam {
	
	private String processDeptCd;	//처리부서코드
	private String sysCd;	//시스템코드    	
	private String locgovCode;
	
	/** g_cntr */
	private String userId;
	private String elctrnPayNo;
	
	
	/** 부과정보연계 파라미터*/
	private String sgbCd;    //자치단체코드
	private String linkTrgtCd;    //연계대상코드
	private String linkMngKey;    //연계관리키
	private String dptCd;    //부서코드
	private String spclFisBizCd;    //특별회계사업코드
	private String fyr;    //회계연도
	private String actSeCd;    //회계구분코드
	private String rprsTxmCd;    //대표세입과목코드
	private String operItemCd;    //운영항목코드
	private String lvyYmd;    //부과일자
	private String frstPctAmt;    //최초본세금액
	private String frstPidYmd;    //최초납기일자
	private String lvySeCd;    //부과구분코드
	private String untySeCd;    //통합구분코드
	private String ntntaxAmt;    //국세분금액
	private String prvtxAmt;    //시도세금액
	private String curprcAmt;    //시군구세금액
	private String vatAmt;    //부가가치세금액
	private String pyrSeCd;    //납부자구분코드
	private String pyrNo;    //납부자번호
	private String pyrNm;    //납부자명
	private String rprsPyrNo;    //대표납부자번호
	private String rprsPyrNm;    //대표납부자명
	private String cnpc;    //연락처
	private String cnpcSeCd;    //연락처구분코드
	private String pyrEmlAddr;    //납부자이메일주소
	private String pyrSttCd;    //납부자상태코드
	private String lotnoRoadAddrSeCd;    //지번도로주소구분코드
	private String zip;    //우편번호
	private String roadNmCd;    //도로명코드
	private String addrUdgdYn;    //지하여부
	private String bmno;    //건물본번
	private String bsno;    //건물부번
	private String stdgCd;    //법정동코드
	private String dongCd;    //행정동코드
	private String addrMtnYn;    //산구분코드
	private String mno;    //본번
	private String sno;    //부번
	private String roadNmAddr;    //도로명주소
	private String roadNmDaddr;    //도로명상세주소
	private String roadNmAlAddr;    //도로명전체주소
	private String lotnoAddr;    //지번주소
	private String lotnoDaddr;    //지번상세주소
	private String lotnoAlAddr;    //지번전체주소
	private String lvyTrgtSeCd;    //부과대상구분코드
	private String glNm;    //물건지명
	private String glLotnoRoadAddrSeCd;    //물건지지번도로주소구분코드
	private String glZip;    //물건지우편번호
	private String glRoadNmCd;    //물건지도로명코드
	private String glAddrUdgdYn;    //물건지주소지하여부
	private String glBmno;    //물건지건물본번
	private String glBsno;    //물건지건물부번
	private String glStdgCd;    //물건지법정동코드
	private String glDongCd;    //물건지행정동코드
	private String glMtnYn;    //물건지산구분코드
	private String glMno;    //물건지본번
	private String glSno;    //물건지부번
	private String glRoadNmAddr;    //물건지도로명기본주소
	private String glRoadNmDaddr;    //물건지도로명상세주소
	private String glRoadNmAlAddr;    //물건지도로명전체주소
	private String glLotnoAddr;    //물건지지번기본주소
	private String glLotnoDaddr;    //물건지지번상세주소
	private String glLotnoAlAddr;    //물건지지번전체주소
	private String mngItemCn1;    //관리1항목
	private String mngItemCn2;    //관리2항목
	private String mngItemCn3;    //관리3항목
	private String mngItemCn4;    //관리4항목
	private String mngItemCn5;    //관리5항목
	private String mngItemCn6;    //관리6항목
	private String rmCn;    //비고내용
	private String rsveItem1;    //예비항목1
	private String rsveItem2;    //예비항목2
	private String rsveItem3;    //예비항목3
	private String rsveItem4;    //예비항목4
	private String rsveItem5;    //예비항목5

	
	
	/** 응답코드 */
	private String linkRstCd;
	private String linkRstMsg;



}

