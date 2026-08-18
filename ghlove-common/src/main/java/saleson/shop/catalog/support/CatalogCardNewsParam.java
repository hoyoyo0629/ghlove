package saleson.shop.catalog.support;

import com.onlinepowers.framework.web.domain.SearchParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CatalogCardNewsParam extends SearchParam {

	private static final long serialVersionUID = 4097024713358173537L;
	
	// 지정기부 공지사항 아이디
	private long cardNewsId;
	
	// 지정기부 아이디	
	private long catalogYear;
	
	// 삭제 여부
	private String deleteYn;

	// 제목 검색어
	private String searchKeyword;

	// 검색 시작일
	private String startDate;

	// 검색 종료일	
	private String endDate;
	
	
}
