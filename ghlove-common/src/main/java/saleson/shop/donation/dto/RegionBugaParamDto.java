package saleson.shop.donation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RegionBugaParamDto {
	private String frstPctAmt;			//기부금액
	private String pyrNo;				//납부자번호
	private String pyrNm;				//납부자이름
	private String roadNmCd;			//도로명코드
	private String bmno;				//건물본번
	private String bsno;				//건물부번
	private String zip;					//우편번호
	private String dongCd;				//행정동코드
	private String roadNmDaddr;			//상세주소
	private String glNm;				//물건지명
	private String selectedRegionCd;	//기부지자체
	private String userRegionCd;		//소속지자체
	private String cntrLocgovCode;		//기부지자체
	private String psitnLocgovCode;		//소속지자체
	private String presentType;			//답례품여부
	private String foreignStatusCode;	//내/외국인 구분코드 - 0: 내국인, 1: 등록외국인, 2: 재외국민, 3: 외국국적동포
	private Long prjId;				//지정기부 프로젝트 아이디
	private Long dsgnDntnBizId;				//지정기부 프로젝트 아이디
	private String cntrPathCode;		//기부경로코드
}