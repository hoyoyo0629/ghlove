package saleson.shop.catalog.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CatalogContentMngParam extends SearchParam {

	private static final long serialVersionUID = -6088982746106552398L;

	// 소식지 내용 아이디
	private long catalogContentId;

	// 소식지 발간년도
	private int catalogYear;

	// 소식지 발간호수
	private int catalogNo;

	// 카운트
	private int rowNumber;

	// 중복체크
	private boolean isDuplicate;
	private int dupCnt;

	// 대민화면 표시 여부
	private String displayYn;

	// 배너 핫소식 표시 여부
	private String bannerYn;

	// 로그인된 아이디
	private long userId;

	// 상위 지자체 코드
	private String upperLocgovCode;

	// 지자체 코드
	private String locgovCode;

	// 호출화면
	private String display;

	// 서치키워드
	private String searchKeyword;



	public String getCatalogYearStr() {
		return String.valueOf(catalogYear);
	}

	public String getCatalogNoStr() {
		return String.format("%02d", catalogNo);
	}

}
