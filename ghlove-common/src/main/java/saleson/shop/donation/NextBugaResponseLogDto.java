package saleson.shop.donation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NextBugaResponseLogDto {

	private String sgbCd;    //행정표준코드관리시스템의 자치단체코드
	private String linkTrgtCd;    //세외수입에서 부여한 시스템코드(연계 확정시 코드부여)
	private String linkMngKey;    //연계대상 기관에서 관리하는 유일키
	private String dptCd;    //행정표준코드관리시스템의 부서코드
	private String spclFisBizCd;    //특별회계 세목코드를 사용한 경우 특별회계사업코드 필요(회계구분이 '51','61' 인경우에만 해당)
	private String fyr;    //부과연도
	private String actSeCd;    //국세:16, 시도세:31, 시군구세:41. 특별회계시도세:51, 특별회계시군구세:61
	private String rprsTxmCd;    //
	private String operItemCd;    //
	private String lvyYmd;    //
	private String frstPctAmt;    //국세+시도세+시군구세 합계금액
	private String frstPidYmd;    //
	private String lvySeCd;    //01:정기분 02:수시분
	private String untySeCd;    //01:통합 02: 미통합
	private String ntntaxAmt;    //통합구분이 "01"이면 국세, 시도세, 시군구세, 부가가치세의 금액이 있을 경우 각 부과자료가 생성되며, 각 부과자료를 통합한 통합부과자료가 생성
	private String prvtxAmt;    //
	private String curprcAmt;    //
	private String vatAmt;    //
	private String pyrSeCd;    //01:개인 02:법인 03:단체 05:외국인
	private String pyrNo;    //- 빼고 숫자만
	private String pyrNm;    //숫자, 특수문자, 기호 등 제외
	private String rprsPyrNo;    //법인의 경우 대표 납부자번호 필수
	private String rprsPyrNm;    //법인의 경우 대표 납부자명 필수
	private String cnpc;    //- 빼고 숫자만
	private String cnpcSeCd;    //01: 전화번호, 02:휴대전화
	private String pyrEmlAddr;    //
	private String pyrSttCd;    //10:거주자 , 11:국외이주신고자, 15:직권조치중 , 40:말소자, 41:사망말소, 43:거주불명등록자, 45:현지이민자, 47:국외이주자, 49:국적이탈자, 99:기타
	private String lotnoRoadAddrSeCd;    //01:지번주소, 02:도로명주소
	private String zip;    //
	private String roadNmCd;    //도로명주소인경우 필수
	private String addrUdgdYn;    //Y:지하, N:지상
	private String bmno;    //도로명주소인경우 필수
	private String bsno;    //도로명주소인경우 필수
	private String stdgCd;    //도로명주소인경우 필수
	private String dongCd;    //
	private String addrMtnYn;    //01:일반번지, 02:산번지
	private String mno;    //번지. 지번주소인 경우 필수
	private String sno;    //호
	private String spclDg;    //상세주소 동(영문명 수정)
	private String spclHo;    //상세주소 호(영문명 수정)
	private String spclAddr;    //상세주소(영문명 수정)
	private String roadNmAddr;    //도로명 주소를 텍스트 형태로 관리하는 시스템일 경우
	private String roadNmDaddr;    //도로명 주소를 텍스트 형태로 관리하는 시스템일 경우
	private String roadNmAlAddr;    //도로명 주소를 텍스트 형태로 관리하는 시스템일 경우
	private String lotnoAddr;    //지번주소를 텍스트로 관리하는 시스템일 경우
	private String lotnoDaddr;    //지번주소를 텍스트로 관리하는 시스템일 경우
	private String lotnoAlAddr;    //지번주소를 텍스트로 관리하는 시스템일 경우
	private String lvyTrgtSeCd;    //부과대상물건에 대한 코드 00 : 없음	01 : 차량	02 : 토지·건물	03 : 토지	04 : 건물	05 : 시설물	06 : 하천	07 : 도로	08 : 공유수면	09 : 자원	10 : 동·식물(미생물)	11 : 선박	12 : 증지(수수료)	13 : 폐기물	14 : 의료	15 : 자금	16 : 교육	17 : 자재	18 : 공산품	19 : 무형자산	20 : 소송명	91 : 위반사항	99 : 기타
	private String glNm;    //부과대상구분이 차량인경우 물건지명은 차량번호만 (숫자, 특수문자, 기호 등 제외)
	private String glLotnoRoadAddrSeCd;    //01:지번주소, 02:도로명주소
	private String glZip;    //
	private String glRoadNmCd;    //도로명주소인경우 필수
	private String glAddrUdgdYn;    //Y:지하, N:지상
	private String glBmno;    //도로명주소인경우 필수
	private String glBsno;    //도로명주소인경우 필수
	private String glStdgCd;    //
	private String glDongCd;    //
	private String glMtnYn;    //01:일반번지,02:산번지
	private String glMno;    //번지
	private String glSno;    //호
	private String glSpclDG;    //상세주소 동(영문명 수정)
	private String glSpclHo;    //상세주소 호(영문명 수정)
	private String glSpclAddr;    //상세주소(영문명 수정)
	private String glRoadNmAddr;    //도로명 주소를 텍스트 형태로 관리하는 시스템일 경우
	private String glRoadNmDaddr;    //도로명 주소를 텍스트 형태로 관리하는 시스템일 경우
	private String glRoadNmAlAddr;    //도로명 주소를 텍스트 형태로 관리하는 시스템일 경우
	private String glLotnoAddr;    //지번주소를 텍스트로 관리하는 시스템일 경우
	private String glLotnoDaddr;    //지번주소를 텍스트로 관리하는 시스템일 경우
	private String glLotnoAlAddr;    //지번주소를 텍스트로 관리하는 시스템일 경우
	private String mngItemCn1;    //부과처리시 필요한 부과근거 1(법적부과근거)
	private String mngItemCn2;    //부과처리시 필요한 부과근거 2
	private String mngItemCn3;    //부과처리시 필요한 부과근거 3
	private String mngItemCn4;    //부과처리시 필요한 부과근거 4
	private String mngItemCn5;    //부과처리시 필요한 부과근거 5
	private String mngItemCn6;    //부과처리시 필요한 부과근거 6
	private String rmCn;    //
	private String rsveItem1;    //
	private String rsveItem2;    //
	private String rsveItem3;    //
	private String rsveItem4;    //
	private String rsveItem5;    //
	private String bugaStatusCd; // 연계상태
	private String linkRstCd;	//연계결과코드
	private String linkRstMsg;	//연계결과메세지
	private String frstRegistPnttm; //생성일자
	private String upperLocgovNm; //지자체 시도
	private String locgovNm; //지자체 시군구
	private String epayNo; // 전자납부번호

}

