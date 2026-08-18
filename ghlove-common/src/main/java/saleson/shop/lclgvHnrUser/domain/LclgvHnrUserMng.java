package saleson.shop.lclgvHnrUser.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import saleson.shop.designateddonation.domain.PrjImage;
import saleson.shop.designateddonation.domain.PrjImageExplain;

@Data
@NoArgsConstructor
@Getter
@Setter
public class LclgvHnrUserMng{

	// 지자체 코드
	private String lclgvCd;

	// 상위 지자체 코드
	private String upperLocgovCode;

	// 상위 지자체 코드 명
	private String upperLocgovNm;

	// 지자체 코드 이름
	private String lclgvCdNm;

	private int rowNumber;

	// 골드 등급 기부 금액
	private int gldGrdDntnAmt;

	// 실버 등급 기부 금액
	private int slvrGrdDntnAmt;

	// 브론즈 등급 기부 금액
	private int brnzGrdDntnAmt;

	// 지자체 명예 사용자 혜택
	private String hnrUserRwrd;

	// 대표 이미지명
	private String rprsImgNm;

	// 명예 사용자 선정 구분 코드
	private String hnrUserSlctnSeCd;

	// 명예 사용자 선정 구분 코드명
	private String hnrUserSlctnSeCdNm;

	// 사용여부
	private String useYn;

	// 이미지경로
	private String imgPath;

	// 최초수정자 ID
	private long frstRgtrId;

	// 최초등록일
	private Timestamp frstRegDt;

	// 마지막수정자 ID
	private long lastRgtrId;

	// 아이디
	private long prjId;

	// 이미지 순서
	private int imgSeq;

	// 이미지 설명
	private String imgExpln;

	// 상품 이미지 설명 접근성
	private List<PrjImageExplain> prjImageExplain = new ArrayList<>();

	private List<PrjImage> prjImages = new ArrayList<>();

	// 명예사용자 설정 제목
	private String hnrUserStngTtl;

	//사용자명
	private String userName;

	//열람일자
	private String viewYm;

	//열람횟수
	private int viewCnt;



}
