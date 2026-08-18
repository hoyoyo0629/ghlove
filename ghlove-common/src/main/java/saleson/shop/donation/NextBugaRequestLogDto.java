package saleson.shop.donation;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NextBugaRequestLogDto extends SearchParam{

	private static final long serialVersionUID = 1L;
	private String lvyYmd;    //부과일
	private String epayNo;		//전자납부번호 linkRstMsg에서
	private String bugaStatusCd;	// 응답코드
	private String linkRstCd;	//연계결과코드

	private String srchStartLogDate;				// 부과일 시작
	private String srchEndLogDate;				// 부과일 끝
	private Integer itemsPerPageTemp;			// 임시 목록수

	private String srchlinkRstYn;	// 연계성공여부
	private String srchpyrNm;		// 납부자명

}

