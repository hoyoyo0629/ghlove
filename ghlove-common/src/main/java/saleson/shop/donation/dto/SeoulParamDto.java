package saleson.shop.donation.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SeoulParamDto {
	private String	systemCd; 		 		//인터페이스 구분코드
	private String	jijacheCd; 		 		//지자체코드
	private String	siguCd; 		 		//시구코드
	private String	semokCd; 		 		//세목코드
	private String	taxYm; 			 		//과세년월
	private String	taxGubun; 		 		//과세구분
	private String	sidoCd; 		 		//시도코드
	private String	napId; 			 		//납세자ID
	private String	napNm; 			 		//납세자명
	private String	napGubun; 		 		//납세자구분
	private Long	taxAmt; 		 		//과세금액
	private Long	sise; 			 		//시세(병기항목아닌경우 본세)
	private String	resideStatus; 	 		//거주상태 10:거주자 11:국외이주신고자 15:직권조치중 40:말소자 41:사망말소 45:현지이민자 47:국외이주자 49:국적이탈자 99:기타
	private String	mulGubun;	 			//물건구분 03:기타
	private String	mulNm;		 			//물건명 해당건물명(지번,차량번호 등)
	private String	sysGubun; 				//가산금면제여부 시스템 고유번호 LVHT  (임시코드, 별도요청 없으면 수정없이 사용)

	private String 	userRegionCd;			//거주지자체
	private String 	presentType;			//답례품여부
	private String 	foreignStatusCode;		//내·외국인구분
	private Long	prjId;					//지정기부
	private Long dsgnDntnBizId;				//지정기부 프로젝트 아이디

	private String 	cntrSn;					//기부일련번호
	private String 	enapbuNo;				//전자납부번호
	private String 	elctrnPayNo;
	private String	bookNo;					// 화면에서 mng → bookNo

}
